package space.harbour.bank;

import java.util.function.Consumer;

import space.harbour.atm.ATM;
import space.harbour.logging.AbstractLogger;

public class Branch implements Consumer<ATM> {
  /**
   * Logger which used to the logs issue. Is optional
   */
  private AbstractLogger logger;

  /**
   * ATM template for setup
   */
  private ATM template;

  public Branch(AbstractLogger logger, ATM template) {
    this.logger = logger;
    this.template = template;
  }

  public ATM newATM(String address) {
    var atm = this.template.clone(address);
    atm.subscribe(this);
    return atm;
  }

  @Override
  public void accept(ATM item) {
    logger.DEBUG("accept");
    item.refill(template);
  }
}
