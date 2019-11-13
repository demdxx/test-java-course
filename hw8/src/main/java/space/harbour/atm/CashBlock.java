package space.harbour.atm;

public class CashBlock {
  /**
   * The original cashbox where was taken the baknotes
   * 
   * @param source
   */
  private CashBox source;

  /**
   * Count of banknotes taken from the one of cashbox
   * 
   * @param count
   */
  private int count;

  /**
   * Next block of money in different denomination
   */
  private CashBlock nextBlock;

  CashBlock(CashBox source, int count, CashBlock nextBlock) {
    this.source = source;
    this.count = count;
    this.nextBlock = nextBlock;
  }

  public int getCount() {
    return count;
  }

  public CashBox getSource() {
    return source;
  }

  public int getSum() {
    return count * source.getDenomination() + (nextBlock != null ? nextBlock.getSum() : 0);
  }

  public synchronized void rollback() {
    source.topUp(count);
    count = 0;
  }

  @Override
  public String toString() {
    return String.format("%d %s", getSum(), source.getCurrency().getCode());
  }

  public String toStringByDinomination() {
    var buff = new StringBuilder();
    buff.append(String.format("%d:%d %s", count, source.getDenomination(), source.getCurrency().getCode()));
    if (nextBlock != null) {
      buff.append(", ");
      buff.append(nextBlock.toStringByDinomination());
    }
    return buff.toString();
  }
}
