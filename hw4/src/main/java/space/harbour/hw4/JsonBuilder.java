package space.harbour.hw4;

import javax.json.*;
import java.util.List;

public class JsonBuilder {
  public static JsonBuilder create() {
    return new JsonBuilder(Json.createObjectBuilder());
  }

  public static JsonArray toJsonArray(List<String> data) {
    if (data == null)
      return null;
    var factory = Json.createBuilderFactory(null);
    var list = factory.createArrayBuilder();
    for (var it : data) {
      list.add(it);
    }
    return list.build();
  }

  public static <T extends Jsonable> JsonArray toJObjectArray(List<T> data) {
    if (data == null)
      return null;
    var factory = Json.createBuilderFactory(null);
    var list = factory.createArrayBuilder();
    for (var it : data) {
      list.add(it.toJsonObject());
    }
    return list.build();
  }

  public static List<String> toStringArray(JsonArray list) {
      if (list == null) return null;
      return list.getValuesAs(JsonString::getString);
  }

  public static <T extends JsonValue> List<T> toListArray(JsonArray list, Class<T> clazz) {
      if (list == null) return null;
      return list.getValuesAs(clazz);
  }

  JsonObjectBuilder builder;

  JsonBuilder(JsonObjectBuilder builder) {
    this.builder = builder;
  }

  public JsonBuilder add(String key, JsonValue value) {
    if (value == null)
      this.builder.add(key, JsonValue.NULL);
    else
      this.builder.add(key, value);
    return this;
  }

  public JsonBuilder add(String key, String value) {
    if (value == null)
      this.builder.add(key, JsonValue.NULL);
    else
      this.builder.add(key, value);
    return this;
  }

  public JsonBuilder add(String key, Integer value) {
    if (value == null)
      this.builder.add(key, JsonValue.NULL);
    else
      this.builder.add(key, value);
    return this;
  }

  public JsonBuilder add(String key, List<String> value) {
    if (value == null)
      this.builder.add(key, JsonValue.NULL);
    else
      this.builder.add(key, toJsonArray(value));
    return this;
  }

  public <T extends Jsonable> JsonBuilder addJObjectArray(String key, List<T> value) {
    if (value == null)
      this.builder.add(key, JsonValue.NULL);
    else
      this.builder.add(key, toJObjectArray(value));
    return this;
  }

  public JsonObject build() {
    return this.builder.build();
  }
}