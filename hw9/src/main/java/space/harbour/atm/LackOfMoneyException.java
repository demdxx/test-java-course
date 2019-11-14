package space.harbour.atm;

public class LackOfMoneyException extends Exception {
  /**
   * serialVersionUID generated constant
   */
  private static final long serialVersionUID = 1508752871730404990L;

  public LackOfMoneyException(String errorMessage) {
    super(errorMessage);
  }
}
