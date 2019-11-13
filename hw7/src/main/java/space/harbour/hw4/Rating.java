package space.harbour.hw4;

import javax.json.*;

import space.harbour.hw4.Jsonable;
import space.harbour.hw4.JsonBuilder;

public class Rating implements Jsonable, JsonValue {
  String source;
  String value;
  Integer votes;

  public Rating() {
  }

  public Rating(String source, String value, Integer votes) {
    this.source = source;
    this.value = value;
    this.votes = votes;
  }

  public Rating(JsonObject obj) {
    this.fromJsonObject(obj);
  }

  @Override
  public ValueType getValueType() {
    return ValueType.OBJECT;
  }

  @Override
  public JsonObject toJsonObject() {
    return JsonBuilder.create().
      add("Source", source).
      add("Value", value).
      add("Votes", votes).
      build();
  }

  @Override
  public void fromJsonObject(JsonObject obj) {
    if (obj== null) return;
    try { source = obj.getString("Source"); } finally {}
    try { value = obj.getString("Value"); } finally {}
    try { votes = obj.getInt("Votes"); } finally {}
  }
}