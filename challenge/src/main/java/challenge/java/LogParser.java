/*
 */
package challenge.java;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author peter
 */
public class LogParser {

    private final static String REGEX_PATTERN = "^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\d+) \"([^\"]+)\" \"([^\"]+)\"";
    private final static int NUM_FIELDS = 9;
    private final static Pattern PATTERN;

    static {
        PATTERN = Pattern.compile(REGEX_PATTERN);
    }

    private final String text;
    private final String ip;
    private final String timestamp;
    private final String bytecount;
    private final String request;
    private final String response;
    private final String referer;
    private final String browser;

    public LogParser(final String text) throws Exception {
        this.text = text;
        Matcher matcher = LogParser.PATTERN.matcher(this.text);
        if (!matcher.matches() || NUM_FIELDS != matcher.groupCount()) {
            throw new Exception("Bad Input");
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

    }

    public String getText() {
        return text;
    }

    public String getIp() {
        return ip;
    }

    public String getTimestamp() {
        return timestamp;
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
