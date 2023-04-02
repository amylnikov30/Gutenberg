import org.apache.spark.*;
import org.apache.spark.api.java.*;
import org.apache.spark.mllib.rdd.RDDFunctions;
import org.apache.spark.rdd.RDD;
import scala.Tuple2;
import scala.reflect.ClassTag;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class CS1003P4
{
    public static void main(String[] args) throws FileNotFoundException
    {
//        Logger.getLogger("org").setLevel(Level.OFF);
//        Logger.getLogger("akka").setLevel(Level.OFF);

        final String DATA_DIR = args[0];
        final String SEARCH_TERM = args[1];
        final float JACCARD_THRESHOLD = Float.parseFloat(args[2]);

        SearchEngine.init();

        SearchEngine.search(SEARCH_TERM, DATA_DIR, JACCARD_THRESHOLD).forEach(System.out::println);

        SearchEngine.close();

//        final int n = 5;
//        final String SEARCH_TERM = "this a file";
//        final float JACCARD_THRESHOLD = 0.3f;
//
//        final SparkConf conf = new SparkConf();
//        conf.setAppName("CS1003P4");
//        conf.setMaster("local[*]");
////
//        final JavaSparkContext ctx = new JavaSparkContext(conf);
//        ctx.setLogLevel("OFF");
//
//        final String content = new Scanner(new File("sample.txt")).useDelimiter("\\Z").next();
////        List<String> refined = List.of(content.replaceAll("[^a-zA-Z0-9]", " ").toLowerCase().split("[ \\s\\t\\n\\r]"));
//
//        final JavaRDD<String> data = ctx.textFile("sample.txt").flatMap(e -> List.of(e.replaceAll("[^a-zA-Z0-9]", " ").toLowerCase().split("[ \\s\\t\\n\\r]")).iterator());
//
//        final ClassTag<String> STRING_CLASSTAG = scala.reflect.ClassTag$.MODULE$.apply(String.class);
//
////        SlidingRDD<String[]> sliding = new SlidingRDD<>(data.rdd(), 5, 1, STRINGARR_CLASSTAG);
//
//        RDDFunctions<String> functions = new RDDFunctions<>(data.rdd(), STRING_CLASSTAG);
//
//        // Sliding window
//        RDD<Object> slidingRdd = functions.sliding(n);
//
//        JavaRDD<String[]> mainWorkingRdd = slidingRdd.toJavaRDD().map(e -> (String[])e);
//
//        JavaPairRDD<String[], Float> jaccardSimilarity = mainWorkingRdd.mapToPair(e -> new Tuple2<>(e, getJaccardSimilarity(e, SEARCH_TERM.split(" "))));
//        jaccardSimilarity
//                .filter(e -> e._2 >= JACCARD_THRESHOLD)
//                .foreach(e -> System.out.println(String.join(" ", e._1)));
//
//        System.out.println("----------Original------------");
//
//        jaccardSimilarity.foreach(e -> System.out.println(List.of(e._1()) + ": " + e._2()));
//
//        ctx.close();
    }

    private static float getJaccardSimilarity(String[] a, String[] b)
    {
        int intersection = 0;

        for (String s : a)
        {
            for (String value : b)
            {
                if (s.equals(value))
                {
                    intersection++;
                    break;
                }
            }
        }

        final int union = a.length + b.length - intersection;

        return (float)intersection / union;
    }
}