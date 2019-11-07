package space.harbour.lesson4;

import java.io.Serializable;

public class Person implements Serializable {
  static final long serialVersionUID = 42L;

  public String name = "";
  public String surname = "";
  public int age = 0;
  public String email = "";
  public String address = "";

  public Person() {}
  public Person(String name, String surname, int age, String email, String address) {
    this.name = name;
    this.surname = surname;
    this.age = age;
    this.email = email;
    this.address = address;
  }
}
