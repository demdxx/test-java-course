package space.harbour.hw4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import space.harbour.hw4.Movie;

public class MovieTests {
  static final String TARGET_FILE = "BladeRunner.json";

  Movie movie;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
    movie = new Movie();
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void loadDataTests() {
    assertNotNull("load JSON movie", movie.readFromJsonFile(TARGET_FILE));
    assertNotNull("Autors must be present in test file", movie.actors);
    assertNotNull("Writers must be present in test file", movie.writers);
    assertNotNull("Ratings must be present in test file", movie.ratings);
    assertEquals("Director", "Ridley Scott", movie.director.name);
  }
}
