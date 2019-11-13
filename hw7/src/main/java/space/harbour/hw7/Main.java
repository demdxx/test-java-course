package space.harbour.hw7;

import java.util.Random;
import java.util.function.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import space.harbour.hw4.Actor;
import space.harbour.hw4.Director;
import space.harbour.hw4.Movie;

public class Main {
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
      new Director(directors[r.nextInt(directors.length)]),
      Arrays.asList(new Actor(actors[r.nextInt(actors.length)])),
      Arrays.asList(genre[r.nextInt(genre.length)]),
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

  public static Comparator<? super Movie> movieComporatorYear() {
    return Comparator.comparingInt(m -> m.getYear());
  }

  public static Comparator<? super Movie> movieComporatorLength() {
    return Comparator.comparingInt(m -> m.getLength());
  }

  public static Comparator<? super Movie> movieComporatorTitle() {
    return (m1, m2) -> {
      return m1.getTitle().compareTo(m2.getTitle());
    };
  }

  public static Predicate<? super Movie> movieFilterDirector(String director) {
    return m -> director.equals(m.getDirector().toString());
  }

  public static Predicate<? super Movie> movieFilterGenre(String genre) {
    return m -> genre.equals(m.getGenre());
  }

  public static Predicate<? super Movie> movieFilterActor(String actor) {
    return m -> actor.equals(m.getActor());
  }

  public static void main(String[] args) {
    var movies = generateMovies(10);
    movies.stream()
      .sorted(movieComporatorYear())
      .filter(movieFilterGenre(genre[0]))
      .forEach(m -> System.out.printf("1> %s\n", m.toString()));
  }
}