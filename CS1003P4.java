import org.apache.spark.*;
import org.apache.spark.api.java.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

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

        String[] window = new String[n];
        AtomicInteger i = new AtomicInteger(0);
        data.foreach(e ->
        {
            if (i.get() == n-1) i.set(0);
            window[i.get()] = e;
            i.set(i.get() + 1);

            System.out.println(List.of(window));
        });
    }
}