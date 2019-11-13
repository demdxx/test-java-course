package space.harbour.atm;

public class Currency {
  /**
   * Human readable name of currency
   */
  private String name;

  /**
   * Currency code like EUR, USD, JPA, etc.
   */
  private String code;

  public Currency(String name, String code) {
    this.name = name;
    this.code = code;
  }

  @Override
  public boolean equals(Object obj) {
    return code.equals(((Currency)obj).getCode());
  }

  @Override
  public String toString() {
    return String.format("[%s] %s", code, name);
  }

  public String getName() {
    return name;
  }

  public String getCode() {
    return code;
  }
}
