package ui;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ColorFormatter extends Formatter {

    public static final String RESET  = "\u001B[0m";
    public static final String GREEN  = "\u001B[32m";
    public static final String WHITE  = "\u001B[37m";
    public static final String BLUE   = "\u001B[33m";

    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        String loggerName = record.getLoggerName();

        String color;
        if (loggerName != null) {
            if (loggerName.contains("app")) {
                color = GREEN;
            } else if (loggerName.contains("ui")) {
                color = WHITE;
            } else if (loggerName.contains("service")) {
                color = BLUE;
            } else {
                color = RESET;
            }
        } else {
            color = RESET;
        }

        sb.append(color);
        sb.append("[").append(record.getLevel()).append("] ");
        sb.append(formatMessage(record));
        sb.append(RESET);
        sb.append("\n");
        return sb.toString();
    }
}
