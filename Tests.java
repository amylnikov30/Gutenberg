import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.List;
import java.util.Set;

/**
 * Test class for all vital methods in the program.
 */
public class Tests
{
    private static JavaSparkContext ctx;

    /**
     * Initialises the Test class.
     */
    public static void init()
    {
        ctx = SearchEngine.init();
    }

    /**
     * Tests {@code JaccardEngine::getNgrams}.
     * @return Whether the method works properly.
     */
    public static boolean ngrams()
    {
        return
            Set.of("co", "oc", "oa").equals(JaccardEngine.getNgrams("cocoa", 2)) &&
            Set.of("te", "es", "st", "ti", "in", "ng").equals(JaccardEngine.getNgrams("testing", 2)) &&
            Set.of("coc", "oco", "coa").equals(JaccardEngine.getNgrams("cocoa", 3));
    }

    /**
     * Tests {@code JaccardEngine::getJaccardIndex}.
     * @return Whether the method works properly.
     */
    public static boolean jaccardIndex()
    {
        return
            toleranceEquals(0.33333f, JaccardEngine.getJaccardIndex(Set.of(1, 2), Set.of(2, 3))) &&
            toleranceEquals(0.5f, JaccardEngine.getJaccardIndex(Set.of(1), Set.of(1, 2))) &&
            toleranceEquals(1f, JaccardEngine.getJaccardIndex(Set.of(1), Set.of(1)));
    }

    /**
     * Tests {@code SearchEngine::getWindowFrames}.
     * @return Whether the method works properly.
     */
    public static boolean windowFrames()
    {
        final JavaRDD<String> test1 = ctx.parallelize(List.of("this is a file".split(" ")));
        final JavaRDD<String> test2 = ctx.parallelize(List.of("1 2 3 4 5 6 7".split(" ")));
        final JavaRDD<String> test3 = ctx.parallelize(List.of("1 2 3 4 5 6 7".split(" ")));

        return
            List.of("this is", "is a", "a file").equals(SearchEngine.getWindowFrames(test1, 2).collect()) &&
            List.of("1 2 3 4 5", "2 3 4 5 6", "3 4 5 6 7").equals(SearchEngine.getWindowFrames(test2, 5).collect()) &&
            List.of("1 2 3 4 5 6 7").equals(SearchEngine.getWindowFrames(test3, 7).collect());
    }
    
    /**
     * Tests {@code SearchEngine::search}.
     * @return Whether the method works properly.
     */
    public static boolean search()
    {
        final List<String> result1 = SearchEngine.search("hide the christmas tree carefully", "Tests/data", 0.75f);
        final List<String> result2 = SearchEngine.search("either the well was very deep or she fell very slowly", "Tests/data", 1f);
        final List<String> result3 = SearchEngine.search("jane looked at elizabeth", "Tests/data", 0.75f);

        return 
            List.of("hide the christmas tree carefully", "the christmas tree carefully helen").equals(result1) &&
            List.of("either the well was very deep or she fell very slowly").equals(result2) &&
            List.of("jane and elizabeth looked", "and elizabeth looked at", "jane looked at elizabeth").equals(result3);
    }

    /**
     * Test all methods in the program.
     * @return
     */
    public static boolean all()
    {
        return ngrams() && jaccardIndex() && windowFrames() && search();
    }

    /**
     * Close the Test class.
     */
    public static void close()
    {
        SearchEngine.close();
    }

    /**
     * Method for checking whether two floats are equal with a tolerance. 
     * @param a
     * @param b
     * @return Whether two given floats are equal.
     */
    public static boolean toleranceEquals(float a, float b)
    {
        return toleranceEquals(a, b, 0.00001f);
    }

    /**
     * The same as {@code toleranceEquals(float a, float b)} but with a specified tolerance.
     * @param a
     * @param b
     * @param tolerance
     * @return Whether two given floats are equal.
     */
    public static boolean toleranceEquals(float a, float b, float tolerance)
    {
        return Math.abs(a - b) <= tolerance;
    }
}