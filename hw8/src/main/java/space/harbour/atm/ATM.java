package space.harbour.atm;

import java.security.InvalidParameterException;

import space.harbour.logging.AbstractLogger;

/**
 * ATM simultaion of real bank ATM automat.
 * 
 * @author  Dmitry Ponomarev
 */
public class ATM {
  /**
   * Logger which used to the logs issue. Is optional
   */
  private AbstractLogger logger = null;

  /**
   * CashContainer abstration storage of money
   */
  private CashContainer cashContainer;

  public ATM(AbstractLogger logger, CashContainer cashContainer) throws InvalidParameterException {
    this.logger = logger;
    this.cashContainer = cashContainer;

    if (this.logger == null)
      throw new InvalidParameterException("logger is required parameter");

    if (this.cashContainer == null)
      throw new InvalidParameterException("cashContainer is not setup");
  }

  public int sumOfMoney(Currency currency) {
    return cashContainer.sumOfMoney(currency);
  }

  public CashBlock withdrowMoney(int amount, Currency currency) throws OutOfTheCashException, LackOfMoneyException {
    logger.INFO("Withdrow money %d %s", amount, currency);
    return cashContainer.withdrowMoney(amount, currency);
  }
}
