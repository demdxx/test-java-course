package space.harbour.hw8;

import java.util.Random;

import space.harbour.atm.ATM;
import space.harbour.atm.CashBox;
import space.harbour.atm.CashContainer;
import space.harbour.atm.Currency;
import space.harbour.atm.LackOfMoneyException;
import space.harbour.atm.OutOfTheCashException;
import space.harbour.logging.StdLogger;
import space.harbour.logging.AbstractLogger;;

public class Main {
  public static final int[] dinominations = new int[] { 5, 10, 20, 25, 50, 100 };

  public static void main(String[] args) {
    CashContainer cashContainer = null;
    var debugLogger = new StdLogger(AbstractLogger.LogLevel.DEBUG);
    var logger = new StdLogger(AbstractLogger.LogLevel.INFO, debugLogger);
    var rand = new Random();
    var USD = new Currency("USA Dollar", "USD");
    var YEN = new Currency("Japan YEN", "JPA");

    // Init cash boxes
    for (var d : dinominations) {
      cashContainer = new CashBox(USD, d, rand.nextInt(10), cashContainer, logger);
      cashContainer = new CashBox(YEN, d, rand.nextInt(10), cashContainer, logger);
    }

    var atm = new ATM(logger, cashContainer);
    System.out.printf("%s amount %s\n", USD, atm.sumOfMoney(USD));
    System.out.printf("%s amount %s\n", YEN, atm.sumOfMoney(YEN));

    try {
      var state = atm.withdrowMoney(115, USD);
      System.out.println(state.toStringByDinomination());
    } catch (OutOfTheCashException | LackOfMoneyException e) {
      e.printStackTrace();
    }
  }
}