package space.harbour.lesson4;

import java.lang.System;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.json.JSONObject;

import space.harbour.lesson4.Person;

public class Main {
  public static void main(String[] args) {
    System.out.println(args);

    var person = new Person();
    Scanner scanner = new Scanner(System.in);

    System.out.println("Enter username");
    person.name = scanner.nextLine();

    System.out.println("Enter surname");
    person.surname = scanner.nextLine();

    System.out.println("How old are you?");
    person.age = scanner.nextInt();
    scanner.nextLine();

    System.out.println("Enter your email");
    person.email = scanner.nextLine();

    System.out.println("Enter your address");
    person.address = scanner.nextLine();

    printPerson(person);

    System.out.println("Enter filename for write");
    storeIntoFile(scanner.nextLine(), person);

    System.out.println("Enter filename for read");
    person = loadFromFile(scanner.nextLine());
    printPerson(person);

    scanner.close();
  }

  private static void storeIntoFile(String filename, Person obj) {
    try {
      var stream = new FileOutputStream(filename);

      JSONObject p = new JSONObject();
      p.put("name", obj.name);
      p.put("surname", obj.surname);
      p.put("age", obj.age);
      p.put("email", obj.email);
      p.put("address", obj.address);

      stream.write(p.toString().getBytes("utf-8"));
      stream.close();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  private static Person loadFromFile(String filename) {
    Person obj = null;
    try {
      var stream = new FileInputStream(filename);
      JSONObject p = new JSONObject(new String(stream.readAllBytes()));
      obj = new Person(p.getString("name"), p.getString("surname"), p.getInt("age"), p.getString("email"),
          p.getString("address"));
      stream.close();
    } catch (Exception e) {
      System.out.println(e);
    }
    return obj;
  }

  private static void printPerson(Person p) {
    System.out.println("============================");
    System.out.printf("Name: %s\n", p.name);
    System.out.printf("Surname: %s\n", p.surname);
    System.out.printf("Age: %d\n", p.age);
    System.out.printf("Email: %s\n", p.email);
    System.out.printf("Address: %s\n", p.address);
    System.out.println("============================");
  }
}