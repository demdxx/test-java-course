package space.harbour.atm;

public interface CashContainer {
  /**
   * Summarise all banknotes inside and returns information.
   * @return sum of money in currency
   */
  int sumOfMoney(Currency currency);

  /**
   * This is a greedy algorithm which use just brute-force to reach the result.
   * The operation can be rollbacked.
   * 
   * @param amount
   * @param currency
   * @return CashBlock
   */
  CashBlock withdrowMoney(int amount, Currency currency) throws LackOfMoneyException;
}
