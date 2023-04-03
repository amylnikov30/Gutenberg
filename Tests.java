import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.List;
import java.util.Set;

public class Tests
{
    public static boolean ngrams()
    {
        return
            Set.of("co", "oc", "oa").equals(JaccardEngine.getNgrams("cocoa", 2)) &&
            Set.of("te", "es", "st", "ti", "in", "ng").equals(JaccardEngine.getNgrams("testing", 2));
    }

    public static boolean jaccardIndex()
    {
        return
            toleranceEquals(0.33333f, JaccardEngine.getJaccardIndex(Set.of(1, 2), Set.of(2, 3))) &&
            toleranceEquals(0.5f, JaccardEngine.getJaccardIndex(Set.of(1), Set.of(1, 2)));
    }

    public static boolean windowFrames()
    {
        Logger.getLogger("org").setLevel(Level.OFF);
        Logger.getLogger("akka").setLevel(Level.OFF);
        Logger.getRootLogger().setLevel(Level.OFF);

        final SparkConf conf = new SparkConf()
                .setAppName("CS1003P4")
                .setMaster("local[*]");

        final JavaSparkContext ctx = new JavaSparkContext(conf);
        ctx.setLogLevel("OFF");

        final JavaRDD<String> test1 = ctx.parallelize(List.of("this is a file".split(" ")));
        final JavaRDD<String> test2 = ctx.parallelize(List.of("1 2 3 4 5 6 7".split(" ")));

        return
            List.of("this is", "is a", "a file").equals(SearchEngine.getWindowFrames(test1, 2).collect()) &&
            List.of("1 2 3 4 5", "2 3 4 5 6", "3 4 5 6 7").equals(SearchEngine.getWindowFrames(test2, 5).collect());
    }

    public static boolean search()
    {
        return false;
    }

    public static boolean all()
    {
        return ngrams() && jaccardIndex() && windowFrames();// && search();
    }

    private static boolean toleranceEquals(float a, float b)
    {
        return toleranceEquals(a, b, 0.00001f);
    }

    private static boolean toleranceEquals(float a, float b, float tolerance)
    {
        return Math.abs(a - b) <= tolerance;
    }
}