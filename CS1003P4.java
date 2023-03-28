import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.*;
import org.apache.spark.api.java.*;

public class CS1003P4
{
    public static void main(String[] args)
    {
//        Logger.getLogger("org").setLevel(Level.OFF);
//        Logger.getLogger("akka").setLevel(Level.OFF);

        final SparkConf conf = new SparkConf();
        conf.setAppName("CS1003P4");
        conf.setMaster("local[*]");
//
        final JavaSparkContext ctx = new JavaSparkContext(conf);
        ctx.setLogLevel("OFF");

        final JavaRDD<String> distFile = ctx.textFile("sample.txt");

        distFile.foreach(e -> System.out.println(e));
    }
}