package space.harbour.storage;

import java.io.Closeable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import space.harbour.models.Movie;
import space.harbour.storage.collections.Movies;

public class Client implements Closeable {
  /**
   * Logger object of client
   */
  private final Logger logger;

  /**
   * Client connect to mongodb
   */
  private final MongoClient client;

  /**
   * Database mongodb link
   */
  private final MongoDatabase database;

  /**
   * Client constructor initialise one database connection
   * 
   * @param hostname
   * @param port
   * @param databaseName
   * @param logger
   */
  public Client(String hostname, Integer port, String databaseName, Logger l) {
    client = new MongoClient(hostname, port);
    database = client.getDatabase(databaseName);
    if (l == null) {
      l = Logger.getLogger("org.mongodb.driver");
      l.setLevel(Level.ALL);
    }
    logger = l;
  }

  /**
   * Client constructor initialise one database connection
   * 
   * @param hostname
   * @param port
   * @param databaseName
   */
  public Client(String hostname, Integer port, String databaseName) {
    this(hostname, port, databaseName, null);
  }

  /**
   * Get movies collection object
   * 
   * @return Movies
   */
  public Movies getMovies() {
    logger.info("get movies collection");
    return new Movies(database.withCodecRegistry(Movie.registry()).getCollection("movies", Movie.class));
  }

  /**
   * Overriding of Closeable interface methods
   * 
   * @throws IOException
   */
  @Override
  public void close() throws IOException {
    logger.info("close connection");
    client.close();
  }
}
