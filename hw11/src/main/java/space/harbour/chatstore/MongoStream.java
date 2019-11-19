package space.harbour.chatstore;

import java.util.LinkedList;
import java.util.List;

import com.mongodb.client.MongoCollection;

public class MongoStream implements ChatStreamer {
  private final MongoCollection<Message> collection;
  private final List<Message> cache = new LinkedList<>();

  public MongoStream(MongoCollection<Message> collection) {
    this.collection = collection;
  }

  @Override
  public synchronized void putMessage(Message message) {
    collection.insertOne(message);
    cache.add(message);
  }

  @Override
  public synchronized List<Message> getHistory() {
    if (cache.size() > 0) return cache;
    collection.find().iterator().forEachRemaining(m -> cache.add(m));
    return cache;
  }
}
