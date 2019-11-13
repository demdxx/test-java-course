package space.harbour.hw4;

import javax.json.*;

import space.harbour.hw4.Jsonable;
import space.harbour.hw4.JsonBuilder;

public class Writer implements Jsonable, JsonValue {
  String name;
  String type;

  public Writer() {
  }

  public Writer(String name, String type) {
    this.name = name;
    this.type = type;
  }

  public Writer(JsonObject obj) {
    this.fromJsonObject(obj);
  }

  @Override
  public ValueType getValueType() {
    return ValueType.OBJECT;
  }

  @Override
  public JsonObject toJsonObject() {
    return JsonBuilder.create().
      add("Name", name).add("Type", type).build();
  }

  @Override
  public void fromJsonObject(JsonObject obj) {
    if (obj== null) return;
    try { name = obj.getString("Name"); } finally {}
    try { type = obj.getString("Type"); } finally {}
  }
}