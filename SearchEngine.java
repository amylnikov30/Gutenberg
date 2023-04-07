import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.rdd.RDDFunctions;
import org.apache.spark.rdd.RDD;
import scala.Tuple2;
import scala.reflect.ClassTag;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

public class SearchEngine
{
    private static final ClassTag STRING_CLASSTAG = scala.reflect.ClassTag$.MODULE$.apply(String.class);

    private static JavaSparkContext ctx;

    /**
     * Initializes the Spark context.
     */
    public static JavaSparkContext init()
    {
        Logger.getLogger("org").setLevel(Level.OFF);
        Logger.getLogger("akka").setLevel(Level.OFF);
        Logger.getRootLogger().setLevel(Level.OFF);

        final SparkConf conf = new SparkConf()
                .setAppName("CS1003P4")
                .setMaster("local[*]");

        ctx = new JavaSparkContext(conf);
        ctx.setLogLevel("OFF");

        return ctx;
    }

    /**
     * Searches for a given search term in a given path to data.
     * @param searchTerm The search term to be searched for.
     * @param pathToData The path to the data to be searched.
     * @param jaccardThreshold The Jaccard similarity threshold.
     * @return A list of matched phrases the same length as the search term.
     */
    public static List<String> search(String searchTerm, String pathToData, float jaccardThreshold)
    {
        // TODO: Check data cleaning (this is one of the window frames: so much support is thisif the).
        JavaRDD<String> data;
        if (!System.getProperty("os.name").toLowerCase().startsWith("windows"))
        {
            data = ctx.wholeTextFiles(pathToData).values()
                        .flatMap(e -> List.of(e.split("[ \\s\\t\\n\\r]")).iterator())
                        .flatMap(e -> List.of(e.replaceAll("[^a-zA-Z0-9]", "").toLowerCase()).iterator())
                        .filter(e -> !e.isBlank());
        }
        else
        {
            // Windows solution:
            data = ctx.parallelize(List.of(""));
            // Can only read relative paths.
            File dataDir = new File(pathToData);
            for (File file : dataDir.listFiles())
            {
                if (file.isFile())
                {
                    final JavaRDD<String> singleTextFile = ctx.textFile(file.getAbsolutePath())
                            .flatMap(e -> List.of(e.replaceAll("[^a-zA-Z0-9]", " ").toLowerCase()).iterator())
                            .flatMap(e -> List.of(e.split("[\\s\\t\\n\\r]")).iterator())
                            .filter(e -> !e.isEmpty());
                    data = data.union(singleTextFile);
                }
            }
        }

        final Set<String> searchTermBigrams = JaccardEngine.getNgrams(searchTerm, 2);


        //TODO: Don't use JavaPairRDD, instead calculate Jaccard index in filter.
        return getWindowFrames(data, searchTerm.trim().split(" ").length)
//                .mapToPair(e ->
//                        new Tuple2<>(e, JaccardEngine.getJaccardIndex(
//                                JaccardEngine.getNgrams(e, 2), searchTermBigrams))
                .filter(e -> JaccardEngine.getJaccardIndex(JaccardEngine.getNgrams(e, 2), searchTermBigrams) >= jaccardThreshold).collect();
    }

    /**
     * Returns a {@code JavaRDD} where each element is a single frame of the sliding window of size {@code n}.
     * @param rawData Raw data to be split into frames.
     * @param size Size of the sliding window.
     * @return {@code JavaRDD} of strings where each element is a phrase which represents a single frame of the
     * sliding window.
     */
    public static JavaRDD<String> getWindowFrames(JavaRDD<String> rawData, int size)
    {
        final RDDFunctions<String> functions = new RDDFunctions<>(rawData.rdd(), STRING_CLASSTAG);

        // Sliding window
        final RDD<Object> slidingRdd = functions.sliding(size);

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