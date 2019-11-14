package space.harbour.atm;

public interface CashContainer {
  /**
   * Summarise all banknotes inside and returns information.
   * @return sum of money in currency
   */
  int sumOfMoney(Currency currency);

  /**
   * Get count of banknots for the currency and denomination
   * 
   * @param currency
   * @param denomination
   * @return count
   */
  int getCountFor(Currency currency, int denomination);

  /**
   * This is a greedy algorithm which use just brute-force to reach the result.
   * The operation can be rollbacked.
   * 
   * @param amount
   * @param currency
   * @return CashBlock
   */
  CashBlock withdrowMoney(int amount, Currency currency) throws LackOfMoneyException;

  /**
   * Clone current container
   * 
   * @return CashContainer
   */
  CashContainer clone();

  /**
   * Refill container from other template container
   */
  void refill(CashContainer template);
}
