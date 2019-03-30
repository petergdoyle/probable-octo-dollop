/*
 */
package challenge.java.spark;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;

/**
 *
 * @author peter
 */
public class CountOccurrences {

    public static void main(String[] args) {

        String filename = "/Users/peter/vagrant/phdata_challenge/data/log_data.txt";
        if (args[0] != null && args[0].length() > 0) {
            filename = args[0];
        }
        SparkSession spark = SparkSession.builder().appName("CountOccurrences").getOrCreate();
        spark.sparkContext().setLogLevel("ERROR");
        Dataset<String> logData = spark.read().textFile(filename).cache();

        long numAs = logData.filter(s -> s.contains("a")).count();
        long numBs = logData.filter(s -> s.contains("b")).count();

        System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs);

        spark.stop();
    }
}
