/*
 */
package challenge.java.spark;

import challenge.java.LogParser;
import challenge.java.LogParserBuilder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.examples.streaming.StreamingExamples;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import scala.Tuple2;
import scala.Tuple3;

/**
 *
 * @author peter
 */
public class KafkaSubnetCount {

    private final static String REGEX_PATTERN = "^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\d+) \"([^\"]+)\" \"([^\"]+)\"";

    private final static Pattern PATTERN;
    private final static SimpleDateFormat apacheLogFileTimestampFormat = new SimpleDateFormat("dd/MMM/yyyy:hh:mm:ss Z");

    static {
        PATTERN = Pattern.compile(REGEX_PATTERN);
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 5) {
            System.err.println("Usage: KafkaWordCount <brokers> <groupId> <topics>\n"
                    + "  <brokers> is a list of one or more Kafka brokers\n"
                    + "  <groupId> is a consumer group name to consume from topics\\n"
                    + "  <topics> is a list of one or more kafka topics to consume from\n"
                    + "  <batchsize> is a the size of the streaming batch window in seconds\\n\n"
                    + "  <threshold> is a the size of the threshold to break within the batch window\\n\n");

            System.exit(1);
        }

        StreamingExamples.setStreamingLogLevels();

        String brokers = args[0];
        String groupId = args[1];
        String topics = args[2];
        Integer batchSize = Integer.valueOf(args[3]);
        Integer threshold = Integer.valueOf(args[4]);

        // Create context with a 2 seconds batch interval
        SparkConf sparkConf = new SparkConf().setAppName("KafkaWordCount");
        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(batchSize));

        Set<String> topicsSet = new HashSet<>(Arrays.asList(topics.split(",")));
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        kafkaParams.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        kafkaParams.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaParams.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // Create direct kafka stream with brokers and topics
        JavaInputDStream<ConsumerRecord<String, String>> stream = KafkaUtils.createDirectStream(
                jssc,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topicsSet, kafkaParams));

        JavaDStream<String> msgs = stream.map(ConsumerRecord::value);
        msgs.foreachRDD(new VoidFunction<JavaRDD<String>>() {
            @Override
            public void call(JavaRDD<String> f) throws Exception {
                f.foreach((String s) -> {
                    
                try {
                    Matcher matcher = PATTERN.matcher(s);
                    String ip = matcher.group(1);
                    String subnet = "";
                    if (ip != null && ip.length() > 0) {
                        String[] parts = ip.split("\\.");
                        subnet = parts[0].concat(".").concat(parts[1]).concat(".*.*");
                    }
                    System.out.println("INFO:  ip=" + ip + " subnet=" + subnet);
                } catch (Exception ex) {
                    System.out.println("ERROR:  bad input, cannot parse  " + f);
                }
            });
        }
        });
            

//        JavaDStream<Tuple2<String, String>> parsedMsgs = msgs.map(m -> {
//            String ip = "error";
//            String subnet = "error";
//            try {
//                Matcher matcher = PATTERN.matcher(m);
//                ip = matcher.group(1);
//                if (ip != null && ip.length() > 0) {
//                    String[] parts = ip.split("\\.");
//                    subnet = parts[0].concat(".").concat(parts[1]).concat(".*.*");
//                }
//            } catch (Exception ex) {
//                System.out.println("DEBUG:  bad input, cannot parse  " + m);
//            }
//            return new Tuple2<>(subnet, ip);
//        });
//        parsedMsgs.print();
//
//        JavaPairDStream<String, Integer> subnetCounts = parsedMsgs
//                .mapToPair(s -> new Tuple2<>(s._1, 1))
//                .reduceByKey((i1, i2) -> i1 + i2)
//                .filter((t) -> t._2 > threshold);
//        subnetCounts.print();
//        msgs.foreachRDD((JavaRDD<String> rdd) -> {
//            if (!rdd.isEmpty()) {
//
//            }
//        });
//
        JavaPairDStream<String, Integer> subnetCounts = msgs
                .mapToPair(s -> new Tuple2<>(s, 1))
                .reduceByKey((i1, i2) -> i1 + i2)
                .filter((t) -> t._2 > threshold);

        subnetCounts.print();
        System.out.println("----------");

        // Start the computation
        jssc.start();

        jssc.awaitTermination();
    }
}
