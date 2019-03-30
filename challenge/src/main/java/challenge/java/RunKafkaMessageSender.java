/*

 */
package challenge.java;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;

/**
 *
 * @author peter
 */
public class RunKafkaMessageSender {

    public static void main(String[] args) throws Exception {
        
        //create a kafka producer 
        InputStream resourceAsStream = RunKafkaMessageSender.class.getClassLoader().getResourceAsStream ("resources/kafka-0.10.2.1-producer.properties");
        Properties kafkaProducerProperties = new Properties();
        kafkaProducerProperties.load(resourceAsStream);
        KafkaMessageSender kafkaMessageSender = new KafkaMessageSender(kafkaProducerProperties);
        
        // get input data from file 
        String filename = "/vagrant/data/log_data.txt";
        if (args[0] != null && args[0].length() > 0) {
            filename = args[0];
        }
        File file = new File(filename);
        HashMap<String, Integer> ipCountMap = new HashMap<>();
        Scanner input = new Scanner(file);
        int recordCount = 0;
        // scan through the file and create new kafka messag from only what is needed 
        while (input.hasNextLine()) {
            recordCount++;
            LogParser lp = LogParserBuilder.createLogParser(input.nextLine());
            String msg = lp.getSubNet().concat(",").concat(lp.getTimestamp().toString());
            kafkaMessageSender.send(msg);
        }
    }
}
