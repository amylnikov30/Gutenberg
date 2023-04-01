import org.apache.spark.*;
import org.apache.spark.api.java.*;
import org.apache.spark.mllib.rdd.RDDFunctions;
import org.apache.spark.rdd.RDD;
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

        final int n = 5;

        final SparkConf conf = new SparkConf();
        conf.setAppName("CS1003P4");
        conf.setMaster("local[*]");
//
        final JavaSparkContext ctx = new JavaSparkContext(conf);
        ctx.setLogLevel("OFF");

        final String content = new Scanner(new File("sample.txt")).useDelimiter("\\Z").next();
        List<String> refined = List.of(content.replaceAll("[^a-zA-Z0-9]", " ").toLowerCase().split("[ \\s\\t\\n\\r]"));

        final JavaRDD<String> data = ctx.parallelize(refined);

        final ClassTag<String> STRING_CLASSTAG = scala.reflect.ClassTag$.MODULE$.apply(String.class);

//        SlidingRDD<String[]> sliding = new SlidingRDD<>(data.rdd(), 5, 1, STRINGARR_CLASSTAG);

        RDDFunctions<String> functions = new RDDFunctions<>(data.rdd(), STRING_CLASSTAG);

        // Sliding window
        RDD<Object> slidingRdd = functions.sliding(5);
        slidingRdd.toJavaRDD().foreach(e -> System.out.println(List.of(((String[])e))));

//        RDDFunctions<String> functions = new RDDFunctions<>(data.rdd(), scala.reflect.ClassManifestFactory.fromClass(String.class));
//        JavaRDD<String> slidingRdd = functions.sliding(5, 1).toJavaRDD().map(e -> (String)e);
//
//        slidingRdd.foreach(e -> System.out.println(e));

//        String[] window = new String[n];
//        AtomicInteger i = new AtomicInteger(0);
//        data.foreach(e ->
//        {
//            if (i.get() == n-1) i.set(0);
//            window[i.get()] = e;
//            i.set(i.get() + 1);
//
//            System.out.println(List.of(window));
//        });


    }
}