/*
 */
package challenge.java;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author peter
 */
public class RunLogParser {

    private static final String SAMPLE
            = "200.4.93.122 - - [25/May/2015:23:11:15 +0000] \"GET / HTTP/1.0\" 200 3557 \"-\" \"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; {1C69E7AA-C14E-200E-5A77-8EAB2D667A07})\"\n"
            + "155.156.150.253 - - [25/May/2015:23:11:15 +0000] \"GET / HTTP/1.0\" 200 3557 \"-\" \"Opera/9.00 (Windows NT 5.1; U; en)\"\n"
            + "155.157.99.22 - - [25/May/2015:23:11:15 +0000] \"GET / HTTP/1.0\" 200 3557 \"-\" \"Mozilla/5.0 (Windows; U; Windows NT 5.0; ja-JP; rv:1.7.12) Gecko/20050919 Firefox/1.0.7\"\n"
            + "155.156.103.181 - - [25/May/2015:23:11:15 +0000] \"GET / HTTP/1.0\" 200 3557 \"-\" \"Opera/9.00 (Windows NT 5.1; U; en)\"\n"
            + "155.156.140.104 - - [25/May/2015:23:11:15 +0000] \"GET / HTTP/1.0\" 200 3557 \"-\" \"Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.8.1.8) Gecko/20071004 Iceweasel/2.0.0.8 (Debian-2.0.0.6+2.0.0.8-Oetch1)\"";

    public static void main(String[] args) throws Exception {

        String filename = "/Users/peter/vagrant/phdata_challenge/data/log_data.txt";
        if (args[0] != null && args[0].length() > 0) {
            filename = args[0];
        }
        File file = new File(filename);
        HashMap<String, Integer> ipCountMap = new HashMap<>();
        Scanner input = new Scanner(file);
        int recordCount = 0;
        while (input.hasNextLine()) {
            recordCount++;
            LogParser lp = new LogParser((String) input.nextLine());
            String ip = lp.getIp();
            String[] parts = ip.split("\\.");
            String subnet = parts[0]
                    .concat(".").concat(parts[1]).concat(".*.*");
            String bytecount = lp.getBytecount();
            String timestamp = lp.getTimestamp();
            String browser = lp.getBrowser();
            String response = lp.getResponse();
            String request = lp.getRequest();
            int lastIndexOf = timestamp.lastIndexOf(":");
            int second = Integer.valueOf(timestamp.substring(lastIndexOf + 1, lastIndexOf + 3));
            String timeframe;
            int increment = 15;
            int max = 60;
            int iteration = 0;
            if (max % increment != 0) {
                throw new Exception("increment must be able to divide evenly within max");
            }
            timeframe = null;
            while (timeframe == null) {
                for (int lo = 0; lo <= second; lo += increment) {
                    iteration++;
                    timeframe = String.format("%02d-%02d", lo, increment * iteration);
                }
            }

            String distribution_by_response_code = response;
            String distribution_by_ip = ip;
            String distribution_by_timeframe = timeframe;
            String distribution_by_subnet_by_timeframe = timeframe.concat(",").concat(subnet);
            String distribution_by_timeframe_by_subnet = subnet.concat(",").concat(timeframe);
            String distribution_by_subnet_by_response_by_timeframe = subnet
                    .concat(",")
                    .concat(response)
                    .concat(",")
                    .concat(timeframe);
            String key = distribution_by_timeframe_by_subnet;
            if (ipCountMap.containsKey(key)) {
                ipCountMap.put(key, ipCountMap.get(key) + 1);
            } else {
                ipCountMap.put(key, 1);
            }
        }
        System.out.println("record count: " + recordCount);
        ipCountMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()) // use this sort to order by the count 
                //                                .sorted(Map.Entry.<String, Integer>comparingByKey()) // use this sort to order by the timeinterval
                //                .limit(100)
                .forEach((item) -> System.out.println(item.getKey().concat(",").concat(item.getValue().toString())));

    }

}
