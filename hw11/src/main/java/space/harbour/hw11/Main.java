package space.harbour.hw11;

import java.io.IOException;

import com.mongodb.MongoClient;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import space.harbour.chat.Server;
import space.harbour.chatstore.Message;
import space.harbour.chatstore.MongoStream;

// docker run -it --rm -p 27017:27017 mongo
public class Main {
  private static final String MONGO_HOST = "127.0.0.1";
  private static final Integer MONGO_PORT = 27017;
  private static final String MONGO_DB = "test";
  private static final String MONGO_COLLECTION = "test";
  
  public static CodecRegistry registry() {
    PojoCodecProvider codecProvider = PojoCodecProvider.builder().automatic(true).build();
    return fromRegistries(MongoClient.getDefaultCodecRegistry(), fromProviders(codecProvider));
  }

  public static void main(String[] args) {
    try (var client = new MongoClient(MONGO_HOST, MONGO_PORT)) {
      var database = client.getDatabase(MONGO_DB);
      var server = new Server(8080, new MongoStream(
        database.withCodecRegistry(registry()).
          getCollection(MONGO_COLLECTION, Message.class)
      ));
      try {
        server.listen();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
