/*
 */
package challenge.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author peter
 */
public class LogRegex {

    public static void main(String[] args) throws FileNotFoundException {

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
            String subnet = parts[0].concat(".").concat(parts[1]).concat(".*.*");
            String timestamp = matcher.group(4);
            String logtime = "";
            try {
                Date parsedDt = apacheLogFileTimestampFormat.parse(timestamp);
                logtime = new Timestamp(parsedDt.getTime()).toString();
            } catch (ParseException ex) {
                Logger.getLogger(LogRegex.class.getName()).log(Level.SEVERE, null, ex);
            }
            String method = matcher.group(5);
            String response = matcher.group(6);
            String output = ip.concat(",")
                    .concat(",")
                    .concat(logtime)
                    .concat(",")
                    .concat(method)
                    .concat(",")
                    .concat(response)
                    .concat(subnet);
            System.out.println(output);
        }
    }
}
