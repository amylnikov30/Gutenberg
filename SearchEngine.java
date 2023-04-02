import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.rdd.RDDFunctions;
import org.apache.spark.rdd.RDD;
import scala.Tuple2;
import scala.reflect.ClassTag;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;

public class SearchEngine
{
    private static final ClassTag STRING_CLASSTAG = scala.reflect.ClassTag$.MODULE$.apply(String.class);

    private static JavaSparkContext ctx;

    /**
     * Initializes the Spark context.
     */
    public static void init()
    {
        final SparkConf conf = new SparkConf()
                .setAppName("CS1003P4")
                .setMaster("local[*]");

        ctx = new JavaSparkContext(conf);
    }

    /**
     * Searches for a given search term in a given path to data.
     * @param searchTerm The search term to be searched for.
     * @param pathToData The path to the data to be searched.
     * @param jaccardThreshold The Jaccard similarity threshold.
     * @return
     */
    public static List<String> search(String searchTerm, String pathToData, float jaccardThreshold) throws FileNotFoundException
    {
//        final JavaPairRDD<String, String> files = ctx.wholeTextFiles(pathToData);

        // TODO: Fix this
        // Temporarily reading from disk like this bc I can't get the wholeTextFiles() method to work
        JavaRDD<String> data = ctx.parallelize(List.of(""));
        File dataDir = new File(pathToData);
        for (File file : dataDir.listFiles())
            if (file.isFile())
            {
                final JavaRDD<String> singleTextFile = ctx.textFile(file.getAbsolutePath())
                        .flatMap(e -> List.of(e.split("[ \\s\\t\\n\\r]")).iterator())
                        .flatMap(e -> List.of(e.replaceAll("[^a-zA-Z0-9]", "").toLowerCase()).iterator())
                        .filter(e -> !e.isEmpty());
                data = data.union(singleTextFile);
            }

        final Set<String> searchTermBigrams = JaccardEngine.getNgrams(searchTerm, 2);

        return getWindowFrames(data, searchTerm.trim().split(" ").length)
                .mapToPair(e ->
                        new Tuple2<>(e, JaccardEngine.getJaccardIndex(JaccardEngine.getNgrams(e, 2), searchTermBigrams))
                ).filter(e -> e._2 >= jaccardThreshold).keys().collect();
    }

    /**
     * Returns a JavaRDD where each element is a single frame of the sliding window of size {@code n}.
     * @param rawData Raw data to be split into frames.
     * @param n Size of the sliding window.
     * @return
     */
    private static JavaRDD<String> getWindowFrames(JavaRDD<String> rawData, int n)
    {
        RDDFunctions<String> functions = new RDDFunctions<>(rawData.rdd(), STRING_CLASSTAG);

        // Sliding window
        RDD<Object> slidingRdd = functions.sliding(n);

        return slidingRdd.toJavaRDD().map(e -> String.join(" ", (String[])e));
    }

    /**
     * Closes the Spark context.
     */
    public static void close()
    {
        ctx.close();
    }
}