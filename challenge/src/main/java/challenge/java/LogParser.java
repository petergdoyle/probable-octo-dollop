/*
 */
package challenge.java;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author peter
 */
public class LogParser implements Serializable {

    private static final String SAMPLE
            = "200.4.93.122 - - [25/May/2015:23:11:15 +0000] \"GET / HTTP/1.0\" 200 3557 \"-\" \"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; {1C69E7AA-C14E-200E-5A77-8EAB2D667A07})\"\n"
            + "155.156.150.253 - - [25/May/2015:23:11:15 +0000] \"GET / HTTP/1.0\" 200 3557 \"-\" \"Opera/9.00 (Windows NT 5.1; U; en)\"\n"
            + "155.157.99.22 - - [25/May/2015:23:11:15 +0000] \"GET / HTTP/1.0\" 200 3557 \"-\" \"Mozilla/5.0 (Windows; U; Windows NT 5.0; ja-JP; rv:1.7.12) Gecko/20050919 Firefox/1.0.7\"\n"
            + "155.156.103.181 - - [25/May/2015:23:11:15 +0000] \"GET / HTTP/1.0\" 200 3557 \"-\" \"Opera/9.00 (Windows NT 5.1; U; en)\"\n"
            + "155.156.140.104 - - [25/May/2015:23:11:15 +0000] \"GET / HTTP/1.0\" 200 3557 \"-\" \"Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.8.1.8) Gecko/20071004 Iceweasel/2.0.0.8 (Debian-2.0.0.6+2.0.0.8-Oetch1)\"";

    private final static String REGEX_PATTERN = "^(\\S+) (\\S+) (\\S+) "
            + "\\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\S+)"
            + " (\\S+)\\s*(\\S+)?\\s*\" (\\d{3}) (\\S+)";
    public static final int NUM_FIELDS = 9;
    public static final Pattern PATTERN;
    private final static SimpleDateFormat apacheLogFileTimestampFormat = new SimpleDateFormat("dd/MMM/yyyy:hh:mm:ss Z");

    static {
        PATTERN = Pattern.compile(REGEX_PATTERN, Pattern.MULTILINE);
    }

    public static void main(String args[]) {
        System.out.println(REGEX_PATTERN);
        Matcher matcher = PATTERN.matcher(SAMPLE);
        if (!matcher.matches() || NUM_FIELDS != matcher.groupCount()) {
            throw new RuntimeException("Bad Input");
        }
        matcher.group(1);
    }

    private final String text;
    private String ip = "";
    private String timestamp = "";
    private String bytecount = "";
    private String request = "";
    private String response = "";
    private String referer = "";
    private String browser = "";
    private boolean error = false;

    public LogParser(final String text) {
        this.text = text;
        Matcher matcher = LogParser.PATTERN.matcher(text);
        if (!matcher.matches() && NUM_FIELDS != matcher.groupCount()) {
            throw new RuntimeException("Bad Input");
        }
        ip = matcher.group(1);
        timestamp = matcher.group(4);
        request = matcher.group(5);
        response = matcher.group(6);
        bytecount = matcher.group(7);
        if (!matcher.group(8).equals("-")) {
            referer = matcher.group(8);
        } else {
            referer = "";
        }
        browser = matcher.group(9);
        System.out.println("ip: " + ip + " subnet:" + getSubNet());
    }

    public String getText() {
        return text;
    }

    public String getIp() {
        return ip;
    }

    public String getSubNet() {
        if (ip.length() == 0) {
            return "";
        }
        String[] parts = ip.split("\\.");
        return parts[0].concat(".").concat(parts[1]).concat(".*.*");
    }

    public String getTimestampAsString() {
        return timestamp;
    }

    public Timestamp getTimestamp() throws ParseException {
        if (timestamp.length() == 0) {
            return new Timestamp(GregorianCalendar.getInstance().getTimeInMillis());
        }
        Date parsedDate = apacheLogFileTimestampFormat.parse(timestamp);
        return new java.sql.Timestamp(parsedDate.getTime());
    }

    public String getBytecount() {
        return bytecount;
    }

    public String getRequest() {
        return request;
    }

    public String getResponse() {
        return response;
    }

    public String getReferer() {
        return referer;
    }

    public String getBrowser() {
        return browser;
    }

    @Override
    public String toString() {
        return "LogParser{" + "ip=" + ip + ", timestamp=" + timestamp + ", bytecount=" + bytecount + ", request=" + request + ", response=" + response + ", referer=" + referer + ", browser=" + browser + '}';
    }

}
