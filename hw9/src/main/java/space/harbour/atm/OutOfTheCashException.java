package space.harbour.atm;

public class OutOfTheCashException extends Exception {
  /**
   * serialVersionUID generated constant
   */
  private static final long serialVersionUID = 1708752871730404990L;

  public OutOfTheCashException(String errorMessage) {
    super(errorMessage);
  }
}
