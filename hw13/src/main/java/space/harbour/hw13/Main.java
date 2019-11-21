package space.harbour.hw13;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import space.harbour.models.Movie;
import spark.Spark;

public class Main {
  private static final String MONGO_HOST = "127.0.0.1";
  private static final Integer MONGO_PORT = 27017;
  private static final String MONGO_DB = "test";

  public static void main(String[] args) {
    System.out.println("run webserver");

    Spark.staticFileLocation("public");

    var client = new MongoClient(MONGO_HOST, MONGO_PORT);
    var database = client.getDatabase(MONGO_DB);
    new MovieServer(getMovies(database));
  }

  public static MongoCollection<Movie> getMovies(MongoDatabase database) {
    var db = database.withCodecRegistry(Movie.registry());
    return db.getCollection("movies", Movie.class);
  }
}