package space.harbour.hw4;

import javax.json.*;

import space.harbour.hw4.Jsonable;
import space.harbour.hw4.JsonBuilder;

public class Director implements Jsonable {
  String name;

  public Director() {
  }

  public Director(String name) {
    this.name = name;
  }

  public Director(JsonObject obj) {
    this.fromJsonObject(obj);
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public JsonObject toJsonObject() {
    return JsonBuilder.create().
      add("Name", name).build();
  }

  @Override
  public void fromJsonObject(JsonObject obj) {
    if (obj== null) return;
    name = obj.getString("Name");
  }
}