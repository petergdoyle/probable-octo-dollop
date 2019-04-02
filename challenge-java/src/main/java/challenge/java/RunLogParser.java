/*
 */
package challenge.java;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author peter
 */
public class RunLogParser {

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
            LogParser lp = LogParserBuilder.createLogParser(input.nextLine());
            String ip = lp.getIp();
            String subnet = lp.getSubNet();
            String bytecount = lp.getBytecount();
            String timestamp = lp.getTimestampAsString();
            String browser = lp.getBrowser();
            String response = lp.getResponse();
            String request = lp.getRequest();
            int lastIndexOf = timestamp.lastIndexOf(":");
            int seconds = Integer.valueOf(lp.getTimestamp().getSeconds());
            String timeframe;
            int increment = 15;
            int max = 60;
            int iteration = 0;
            if (max % increment != 0) {
                throw new Exception("increment must be able to divide evenly within max");
            }
            timeframe = null;
            while (timeframe == null) {
                for (int lo = 0; lo <= seconds; lo += increment) {
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
