package space.harbour.hw7;

import org.junit.Assert;
import org.junit.Test;

import space.harbour.hw4.Movie;

public class FiltersTests {
  @Test
  public void lengthTest() {
    var movies = Main.generateMovies(100);
    var list = movies.stream()
      .sorted(Main.movieComporatorLength())
      .toArray(Movie[]::new);
    var lng = -1;

    for (var m: list) {
      if (lng > m.getLength()) {
        Assert.fail("length ordering");
      }
    }
  }

  @Test
  public void yearTest() {
    var movies = Main.generateMovies(100);
    var list = movies.stream()
      .sorted(Main.movieComporatorYear())
      .toArray(Movie[]::new);
    var year = -1;

    for (var m: list) {
      if (year > m.getYear()) {
        Assert.fail("year ordering");
      }
    }
  }

  @Test
  public void filterGenreTest() {
    var movies = Main.generateMovies(100);
    var list = movies.stream()
      .sorted(Main.movieComporatorYear())
      .filter(Main.movieFilterGenre("Comedy"))
      .toArray(Movie[]::new);

    for (var m: list) {
      Assert.assertEquals("genre filter", "Comedy", m.getGenre());
    }
  }

  @Test
  public void filterDirectorTest() {
    var movies = Main.generateMovies(100);
    var list = movies.stream()
      .sorted(Main.movieComporatorYear())
      .filter(Main.movieFilterDirector("Richard Fleischer"))
      .toArray(Movie[]::new);

    for (var m: list) {
      Assert.assertEquals("genre filter", "Richard Fleischer", m.getDirector().toString());
    }
  }
}
