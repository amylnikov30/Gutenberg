import org.apache.spark.*;
import org.apache.spark.api.java.*;

public class CS1003P4
{
    public static void main(String[] args)
    {
        final SparkConf conf = new SparkConf();
        conf.setAppName("CS1003P4");
        conf.setMaster("local[3]");
//
        final JavaSparkContext ctx = new JavaSparkContext(conf);
        final JavaRDD<String> distFile = ctx.textFile("sample.txt");

        distFile.foreach(System.out::println);
    }
}