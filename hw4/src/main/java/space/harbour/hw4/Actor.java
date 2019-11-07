package space.harbour.hw4;

import javax.json.*;

import space.harbour.hw4.Jsonable;
import space.harbour.hw4.JsonBuilder;

public class Actor implements Jsonable, JsonValue {
  String name;
  String as;

  public Actor() {
  }

  public Actor(String name, String as) {
    this.name = name;
    this.as = as;
  }

  public Actor(JsonObject obj) {
    this.fromJsonObject(obj);
  }

  @Override
  public ValueType getValueType() {
    return ValueType.OBJECT;
  }

  @Override
  public JsonObject toJsonObject() {
    return JsonBuilder.create().
      add("Name", name).add("As", as).build();
  }

  @Override
  public void fromJsonObject(JsonObject obj) {
    if (obj== null) return;
    try { name = obj.getString("Name"); } finally {}
    try { as = obj.getString("As"); } finally {}
  }
}