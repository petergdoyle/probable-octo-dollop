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

    private final static String REGEX_PATTERN = "^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\d+) \"([^\"]+)\" \"([^\"]+)\"";
    public static final int NUM_FIELDS = 9;
    public static final Pattern PATTERN;
    private final static SimpleDateFormat apacheLogFileTimestampFormat = new SimpleDateFormat("dd/MMM/yyyy:hh:mm:ss Z");

    static {
        PATTERN = Pattern.compile(REGEX_PATTERN);
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
        try {
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

        } catch (Exception ex) {
            error = true;
        }
        System.out.println("ip: "+ip+" subnet:"+getSubNet());
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
