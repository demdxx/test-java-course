package space.harbour.hw9;

import org.junit.Assert;
import org.junit.Test;

import space.harbour.logging.StdLogger;
import space.harbour.atm.ATM;
import space.harbour.atm.CashBox;
import space.harbour.atm.CashContainer;
import space.harbour.atm.Currency;
import space.harbour.atm.LackOfMoneyException;
import space.harbour.atm.OutOfTheCashException;
import space.harbour.bank.Branch;
import space.harbour.logging.AbstractLogger;

public class ATMTests {
  private AbstractLogger debugLogger = new StdLogger(AbstractLogger.LogLevel.DEBUG);
  private AbstractLogger logger = new StdLogger(AbstractLogger.LogLevel.INFO, debugLogger);
  private Currency USD = new Currency("USA Dollar", "USD");

  ATM newATM(boolean empty) {
    CashContainer cashContainer = null;
    int[] banknotes = null;

    if (empty) {
      banknotes = new int[] { 5, 0, 10, 0, 20, 0, 25, 0, 50, 0 };
    } else {
      banknotes = new int[] { 5, 1, 10, 5, 20, 7, 25, 10, 50, 30 };
    }

    for (int i = 0; i < banknotes.length / 2; i += 2) {
      cashContainer = new CashBox(USD, banknotes[i], banknotes[i + 1], cashContainer, logger);
    }

    return new ATM(logger, cashContainer);
  }

  @Test
  public void simpleWithdrawTest() {
    var atm = newATM(false);
    try {
      Assert.assertNotNull(atm.withdrowMoney(35, USD));
    } catch (Exception e) {
      Assert.fail(e.toString());
    }
  }

  @Test
  public void lackOfMoneyWithdrawTest() {
    var atm = newATM(false);
    try {
      Assert.assertNull(atm.withdrowMoney(3, USD));
    } catch (LackOfMoneyException e) {
      // Success ...
    } catch (Exception e) {
      Assert.fail(e.toString());
    }
  }

  @Test
  public void lackOfMoneyWithdrawRefill() {
    var branch = new Branch(logger, newATM(false));
    var atm = branch.newATM("Malibu safeguards");
    var total = atm.sumOfMoney(USD);

    try {
      var state = atm.withdrowMoney(total, USD);
      Assert.assertEquals("withdrow", total, state.getSum());
      Thread.sleep(1000);
      Assert.assertEquals("ATM total", total, atm.sumOfMoney(USD));
    } catch (InterruptedException | OutOfTheCashException | LackOfMoneyException e) {
      Assert.fail(e.toString());
    }
  }

  @Test
  public void outOfCashWithdrawTest() {
    var atm = newATM(true);
    try {
      Assert.assertNull(atm.withdrowMoney(100, USD));
    } catch (OutOfTheCashException e) {
      // Success ...
    } catch (Exception e) {
      Assert.fail(e.toString());
    }
  }

  @Test
  public void rollbackWithdrawTest() {
    var atm = newATM(false);
    var total = atm.sumOfMoney(USD);
    try {
      var money = atm.withdrowMoney(100, USD);
      System.out.printf("%d - %d", total, atm.sumOfMoney(USD));
      Assert.assertNotEquals("Available in ATM", total, atm.sumOfMoney(USD));
      money.rollback();
      Assert.assertEquals("rollback fail", total, atm.sumOfMoney(USD));
    } catch (Exception e) {
      Assert.fail(e.toString());
    }
  }
}
