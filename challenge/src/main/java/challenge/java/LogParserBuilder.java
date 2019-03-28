/*
 */
package challenge.java;

public class LogParserBuilder {

    private LogParserBuilder() {
    }

    public static LogParser createLogParser(final String text) throws Exception {
        return new LogParser(text);
    }

}
