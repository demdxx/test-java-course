package space.harbour.hw10;

import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import space.harbour.models.Movie;
import space.harbour.storage.Client;

public class StorageTests {
  private static final String MONGO_HOST = "127.0.0.1";
  private static final Integer MONGO_PORT = 27017;
  private static final String MONGO_DB = "test";

  private Client client = null;

  @Before
  public void setUp() throws Exception {
    client = new Client(MONGO_HOST, MONGO_PORT, MONGO_DB);
  }

  @After
  public void tearDown() throws Exception {
    client.close();
  }
  
  @Test
  public void insertOne() {
    var movies = client.getMovies();
    try {
      movies.insertOne(Main.randMovie());
    } catch (Exception e) {
      Assert.fail(e.toString());
    }
  }
  
  @Test
  public void insertMany() {
    var movies = client.getMovies();
    try {
      movies.insertMany(Main.randMovie(), Main.randMovie(), Main.randMovie());
    } catch (Exception e) {
      Assert.fail(e.toString());
    }
  }
  
  @Test
  public void removeOne() {
    var movies = client.getMovies();
    try {
      movies.truncate();
      movies.insertOne(Main.randMovie());
      Movie movie = movies.find().first();
      Assert.assertNotNull(movie);
      movies.removeOne(new Document("_id", movie.getId()));
      movie = movies.find().first();
      Assert.assertNull(movie);
    } catch (Exception e) {
      Assert.fail(e.toString());
    }
  }
}