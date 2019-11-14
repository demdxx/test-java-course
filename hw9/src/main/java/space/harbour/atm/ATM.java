package space.harbour.atm;

import java.util.concurrent.SubmissionPublisher;
import java.util.function.Consumer;
import java.security.InvalidParameterException;

import space.harbour.logging.AbstractLogger;

/**
 * ATM simultaion of real bank ATM automat.
 * 
 * @author Dmitry Ponomarev
 */
public class ATM {
  /**
   * Address of the ATM
   */
  public String address = "";

  /**
   * Logger which used to the logs issue. Is optional
   */
  private AbstractLogger logger = null;

  /**
   * CashContainer abstration storage of money
   */
  private CashContainer cashContainer;

  /**
   * Subscribe processor
   */
  private SubmissionPublisher<ATM> publisher = new SubmissionPublisher<>();

  public ATM(AbstractLogger logger, CashContainer cashContainer) throws InvalidParameterException {
    this(logger, cashContainer, "");
  }

  public ATM(AbstractLogger logger, CashContainer cashContainer, String address) throws InvalidParameterException {
    this.address = address;
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
    if (sumOfMoney(currency) == 0) {
      publisher.submit(this);
      throw new OutOfTheCashException("ATM is empty");
    }
    CashBlock block;
    try {
      block = cashContainer.withdrowMoney(amount, currency);
    } catch (LackOfMoneyException e) {
      publisher.submit(this);
      throw e;
    }
    if (sumOfMoney(currency) == 0) {
      System.out.println(currency);
      publisher.submit(this);
      publisher.close();
    }
    return block;
  }

  public ATM clone(String address) {
    return new ATM(logger, cashContainer.clone(), address);
  }

  public void refill(ATM template) {
    logger.INFO("Refill ATM: %s", this);
    cashContainer.refill(template.cashContainer);
  }

  @Override
  public String toString() {
    return address;
  }

  public void subscribe(Consumer<? super ATM> consumer) {
    publisher.consume(consumer);
  }
}
