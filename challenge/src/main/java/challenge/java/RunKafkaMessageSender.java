/*

 */
package challenge.java;

import java.io.File;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author peter
 */
public class RunKafkaMessageSender {

    public static void main(String[] args) throws Exception {

        //create a kafka producer 
        InputStream resourceAsStream = RunKafkaMessageSender.class.getClassLoader().getResourceAsStream("resources/kafka-0.10.2.1-producer.properties");
        Properties kafkaProducerProperties = new Properties();
        kafkaProducerProperties.load(resourceAsStream);
        KafkaMessageSender kafkaMessageSender = new KafkaMessageSender(kafkaProducerProperties);

        String REGEX_PATTERN = "^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\d+) \"([^\"]+)\" \"([^\"]+)\"";
        int NUM_FIELDS = 9;
        Pattern PATTERN = Pattern.compile(REGEX_PATTERN);
        SimpleDateFormat apacheLogFileTimestampFormat = new SimpleDateFormat("dd/MMM/yyyy:hh:mm:ss Z");

        String filename = args[0];
        File file = new File(filename);
        Scanner input = new Scanner(file);
        while (input.hasNextLine()) {
            String nextLine = input.nextLine();
            Matcher matcher = PATTERN.matcher(nextLine);
            if (!matcher.matches() || NUM_FIELDS != matcher.groupCount()) {
                throw new RuntimeException("Matches: " + matcher.matches() + " GroupCount" + matcher.groupCount() + " Bad Input " + nextLine);
            }
            String ip = matcher.group(1);
            String[] parts = ip.split("\\.");
            String subnet = parts[0].concat(".").concat(parts[1]);//.concat(".").concat(".*.*");
            String timestamp = matcher.group(4);
            String logtime = "";
            try {
                Date parsedDt = apacheLogFileTimestampFormat.parse(timestamp);
                logtime = new Timestamp(parsedDt.getTime()).toString();
            } catch (ParseException ex) {
                Logger.getLogger(LogRegex.class.getName()).log(Level.SEVERE, null, ex);
            }
            String request = matcher.group(5);
            String[] split = request.split("\\s");
            String method = split[0];
            String uri = split[1];
            String protocol = split[2];
            String response = matcher.group(6);
            String output = 
                    logtime
                    .concat(",")
                    .concat(ip)
                    .concat(",")
                    .concat(subnet)
                    .concat(",")
                    .concat(method)
                    .concat(",")
                    .concat(uri)
                    .concat(",")
                    .concat(protocol)
                    .concat(",")
                    .concat(response);
            System.out.println(output);
            kafkaMessageSender.send(output);
        }
    }
}
