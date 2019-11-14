package space.harbour.logging;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StdLogger extends AbstractLogger {
  private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

  public StdLogger(LogLevel level) {
    super(level);
  }

  public StdLogger(LogLevel level, String timeFormat) {
    super(level);
    format = new SimpleDateFormat(timeFormat);
  }

  public StdLogger(LogLevel level, AbstractLogger nextLogger) {
    super(level, nextLogger);
  }

  public StdLogger(LogLevel level, AbstractLogger nextLogger, String timeFormat) {
    super(level, nextLogger);
    format = new SimpleDateFormat(timeFormat);
  }

  @Override
  protected void logMessage(LogLevel level, String message) {
    System.out.printf("%s [%s] - %s\n", format.format(new Date()), level, message);
  }
}
