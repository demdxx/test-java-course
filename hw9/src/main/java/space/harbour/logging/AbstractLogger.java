package space.harbour.logging;

public abstract class AbstractLogger {
  public enum LogLevel {
    DEBUG(0), TRACE(1), INFO(2), ERROR(3), PANIC(4);

    private int level;

    LogLevel(int l) {
      this.level = l;
    }

    public int getLevel() {
      return level;
    }
  };

  private LogLevel level = LogLevel.DEBUG;
  private AbstractLogger nextLogger = null;

  public AbstractLogger(LogLevel level) {
    this.level = level;
  }

  public AbstractLogger(LogLevel level, AbstractLogger nextLogger) {
    this.level = level;
    this.nextLogger = nextLogger;
  }

  public void log(LogLevel level, String message, Object... args) {
    if (level.getLevel() >= this.level.getLevel()) {
      logMessage(level, String.format(message, args));
    }
    if (nextLogger != null)
      nextLogger.log(level, message, args);
  }

  protected abstract void logMessage(LogLevel level, String message);

  public void DEBUG(String message, Object ...args) {
    log(LogLevel.DEBUG, message, args);
  }

  public void TRACE(String message, Object ...args) {
    log(LogLevel.TRACE, message, args);
  }

  public void INFO(String message, Object ...args) {
    log(LogLevel.INFO, message, args);
  }

  public void ERROR(String message, Object ...args) {
    log(LogLevel.ERROR, message, args);
  }

  public void PANIC(String message, Object ...args) {
    log(LogLevel.PANIC, message, args);
  }
}
