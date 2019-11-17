package space.harbour.hw10;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import space.harbour.models.Movie;
import space.harbour.storage.Client;

public class Main {
  private static final String MONGO_HOST = "127.0.0.1";
  private static final Integer MONGO_PORT = 27017;
  private static final String MONGO_DB = "test";

  private static final String[] directors = new String[]{
    "Christopher Nolan",
    "Roman Polanski",
    "Richard Fleischer",
    "Blake Edwards",
    "Sidney Lumet",
    "Terry Gilliam",
    "Steven Spielberg",
  };

  private static final String[] titles = new String[]{
    "Chacun sa nuit",
    "2001: A Space Odyssey",
    "20,000 Leagues Under the Sea",
    "24 7: Twenty Four Seven",
    "Twin Falls Idaho",
    "Three Kingdoms: Resurrection of the Dragon",
    "3 Men and a Baby",
  };

  private static final String[] genre = new String[]{
    "Comedy",
    "Action",
    "Drama",
    "Musical",
  };

  private static final String[] actors = new String[]{
    "bald from brazzers",
    "Harry Potter",
  };

  public static Movie randMovie() {
    Random r = new Random();
    return new Movie(
      titles[r.nextInt(titles.length)],
      directors[r.nextInt(directors.length)],
      actors[r.nextInt(actors.length)],
      genre[r.nextInt(genre.length)],
      r.nextInt(100) + 1900,
      r.nextInt(100)
    );
  }

  public static List<Movie> generateMovies(int count) {
    List<Movie> result = new ArrayList<Movie>();
    for (int i = 0; i < count; i++) {
      result.add(randMovie());
    }
    return result;
  }

  public static void main(String[] args) {
    // var p = System.getProperties();
    try (var client = new Client(MONGO_HOST, MONGO_PORT, MONGO_DB)) {
      var movies = client.getMovies();
      movies.insertMany(generateMovies(100));
      for (var movie: movies.find()) { 
        System.out.println(movie);
      }
      movies.truncate();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
