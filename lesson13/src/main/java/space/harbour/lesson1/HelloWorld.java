package space.harbour.lesson1;

import com.google.gson.Gson;

import spark.Spark;

public class HelloWorld {
  public static class msg {
    public String message;

    public msg(String msg) {
      message = msg;
    }
  }

  public static void main(String[] args) {
    System.out.println("run webserver");
    var hson = new Gson();
    Spark.get("/", (req, res) -> { return "HI!"; });
    Spark.get("/json", (req, res) -> new msg("Message!"), hson::toJson);
  }
}