package space.harbour.storage.collections;

import java.util.Arrays;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import space.harbour.models.Movie;

public class Movies {
  private MongoCollection<Movie> collection;

  public Movies(MongoCollection<Movie> collection) {
    this.collection = collection;
  }

  public Movie getOne(Object id) {
    return collection.find(new BasicDBObject("_id", id)).first();
  }

  /**
   * Finds all documents in the collection.
   *
   * @return the find iterable interface
   * @mongodb.driver.manual tutorial/query-documents/ Find
   */
  public FindIterable<Movie> find() {
    return collection.find();
  }

  /**
   * Finds all documents in the collection.
   *
   * @param filter the query filter
   * @return the find iterable interface
   * @mongodb.driver.manual tutorial/query-documents/ Find
   */
  public FindIterable<Movie> find(BasicDBObject filter) {
    return collection.find(filter);
  }

  /**
   * Inserts the provided document. If the document is missing an identifier, the driver should generate one.
   *
   * <p>Note: Supports retryable writes on MongoDB server versions 3.6 or higher when the retryWrites setting is enabled.</p>
   * @param document the document to insert
   * @throws com.mongodb.MongoWriteException        if the write failed due to some specific write exception
   * @throws com.mongodb.MongoWriteConcernException if the write failed due to being unable to fulfil the write concern
   * @throws com.mongodb.MongoCommandException      if the write failed due to a specific command exception
   * @throws com.mongodb.MongoException             if the write failed due some other failure
   */
  public void insertOne(Movie movie) {
    collection.insertOne(movie);
  }

  /**
   * Inserts one or more documents.  A call to this method is equivalent to a call to the {@code bulkWrite} method
   *
   * <p>Note: Supports retryable writes on MongoDB server versions 3.6 or higher when the retryWrites setting is enabled.</p>
   * @param documents the documents to insert
   * @throws com.mongodb.MongoBulkWriteException if there's an exception in the bulk write operation
   * @throws com.mongodb.MongoCommandException   if the write failed due to a specific command exception
   * @throws com.mongodb.MongoException          if the write failed due some other failure
   * @see com.mongodb.client.MongoCollection#bulkWrite
   */
  public void insertMany(Movie ...movies) {
    collection.insertMany(Arrays.asList(movies));
  }

  public void insertMany(List<Movie> movies) {
    collection.insertMany(movies);
  }

  /**
   * Update a single document in the collection according to the specified arguments.
   *
   * <p>Use this method to only update the corresponding fields in the document according to the update operators used in the update
   * document. To replace the entire document with a new document, use the corresponding {@link #replaceOne(Bson, Object)} method.</p>
   *
   * <p>Note: Supports retryable writes on MongoDB server versions 3.6 or higher when the retryWrites setting is enabled.</p>
   * @param filter a document describing the query filter, which may not be null.
   * @param update a document describing the update, which may not be null. The update to apply must include at least one update operator.
   * @return the result of the update one operation
   * @throws com.mongodb.MongoWriteException        if the write failed due to some specific write exception
   * @throws com.mongodb.MongoWriteConcernException if the write failed due to being unable to fulfil the write concern
   * @throws com.mongodb.MongoCommandException      if the write failed due to a specific command exception
   * @throws com.mongodb.MongoException             if the write failed due some other failure
   * @mongodb.driver.manual tutorial/modify-documents/ Updates
   * @mongodb.driver.manual reference/operator/update/ Update Operators
   * @mongodb.driver.manual reference/command/update   Update Command Behaviors
   * @see com.mongodb.client.MongoCollection#replaceOne(Bson, Object)
   */
  public UpdateResult updateOne(ObjectId id, Document doc) {
    return collection.updateOne(new Document("_id", id), doc);
  }

  /**
   * Atomically find a document and remove it.
   *
   * <p>Note: Supports retryable writes on MongoDB server versions 3.6 or higher when the retryWrites setting is enabled.</p>
   * @param filter the query filter to find the document with
   * @return the document that was removed.  If no documents matched the query filter, then null will be returned
   */
  public void removeOne(Bson filter) {
    collection.findOneAndDelete(filter);
  }

  public void remove(Bson filter) {
    collection.deleteMany(filter);
  }

  public void truncate() {
    collection.deleteMany(new Document());
  }
}
