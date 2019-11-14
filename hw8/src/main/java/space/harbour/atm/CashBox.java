package space.harbour.atm;

import space.harbour.logging.AbstractLogger;

/**
 * CashBox implements interface CashContainer with methods required by ATM to
 * make it work properly. It contains banknotes and information about
 * Denomination, Currency and count of it.
 * 
 * @author Dmitry Ponomarev
 */
public class CashBox implements CashContainer {
  /**
   * Logger which used to the logs issue. Is optional
   */
  private AbstractLogger logger = null;

  /**
   * Currency of the banknotes
   */
  private Currency currency;

  /**
   * Fixed value of the banknotes whish stored in the CashBox. Every banknote is
   * equal to this value.
   * 
   * For example: if denomination equal to 50 it means that each banknote is equal
   * to 50
   */
  private int denomination = 0;

  /**
   * Count out banknotes in the current CashBox
   */
  private int banknoteCount = 0;

  /**
   * Pointer to the next cashbox
   */
  private CashContainer nextCashBox = null;

  /**
   * Chain type container of banknotes
   * 
   * @param currency
   * @param denomination
   * @param banknoteCount
   */
  public CashBox(Currency currency, int denomination, int banknoteCount, CashContainer nextCashContainer,
      AbstractLogger logger) {
    this.currency = currency;
    this.denomination = denomination;
    this.banknoteCount = banknoteCount;
    this.nextCashBox = nextCashContainer;
    this.logger = logger;
  }

  public int getDenomination() {
    return denomination;
  }

  public Currency getCurrency() {
    return currency;
  }

  /**
   * Summarise all banknotes inside and returns information.
   * 
   * @return sum of money in currency
   */
  @Override
  public int sumOfMoney(Currency currency) {
    return (currency.equals(this.currency) ? denomination * banknoteCount : 0)
        + (nextCashBox == null ? 0 : nextCashBox.sumOfMoney(currency));
  }

  /**
   * topUp the cashbox with the count of backnotes
   * 
   * @param count
   */
  synchronized void topUp(int count) {
    banknoteCount += count;
  }

  /**
   * This is a greedy algorithm which use just brute-force to reach the result.
   * The operation can be rollbacked.
   * 
   * @param amount
   * @param currency
   * @return CashBlock
   */
  public synchronized CashBlock withdrowMoney(int amount, Currency currency) throws LackOfMoneyException {
    // Check if requested money in right currency
    if (!currency.equals(this.currency)) {
      if (nextCashBox == null) {
        throw new LackOfMoneyException(
            String.format("Not anough money for the currency %s in the cashbox: %s", currency, this));
      }
      return nextCashBox.withdrowMoney(amount, currency);
    }

    // Detect minimal amount of current denominations
    int count = amount / denomination;
    if (count > banknoteCount) {
      count = banknoteCount;
    }

    // If anought money in the CashBox
    if (amount == count * denomination) {
      banknoteCount -= count;
      return new CashBlock(this, count, null);
    }

    // Lets check the next box if possible
    if (nextCashBox != null) {
      // Iterate cash withdrowing tries
      for (; count >= 0; count--) {
        // Get the rest of money which need to collect
        var tryAmount = amount - count * getDenomination();

        try {
          var state = nextCashBox.withdrowMoney(tryAmount, currency);
          banknoteCount -= count;
          return new CashBlock(this, count, state);
        } catch (LackOfMoneyException e) {
          if (logger != null)
            logger.DEBUG("Withdrow %d %s try error: %s", tryAmount, currency, e);
        }
      }
    }

    throw new LackOfMoneyException(
        String.format("Not suitable amount money for the currency %s in the cashbox: %s", currency, this));
  }

  @Override
  public String toString() {
    return String.format("[%s] %d", currency, denomination);
  }
}
