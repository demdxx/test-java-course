package space.harbour.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import space.harbour.chatstore.ChatStreamer;
import space.harbour.chatstore.Message;

public class Server implements Consumer<Message> {
  private final int port;
  private ExecutorService executor = Executors.newFixedThreadPool(5);
  private final List<ChatHandler> clients = new LinkedList<>();
  private final ChatStreamer streamer;
  private int idEnum = 0;

  public Server(int port, ChatStreamer streamer) {
    this.port = port;
    this.streamer = streamer;
  }

  private String nextId() {
    return String.format("client.%d", idEnum++);
  }

  public void listen() throws IOException {
    var server = new ServerSocket(port);
    System.out.printf("-> RUN Listener:%d\n", port);
    while (true) {
      try {
        final Socket socket = server.accept();
        final BotHandler chatClient = new BotHandler(nextId(), socket, streamer);
        chatClient.consume(this);
        registerClient(chatClient);
        executor.execute(() -> {
          try {
            chatClient.run();
            socket.close();
          } catch (IOException e) {
            e.printStackTrace();
          } finally {
            removeClient(chatClient);
          }
        });
      } catch (Exception e) {
        e.printStackTrace();
        break;
      }
    }
    server.close();
  }

  private synchronized void registerClient(BotHandler chatClient) {
    clients.add(chatClient);
  }

  private synchronized void removeClient(BotHandler chatClient) {
    clients.remove(chatClient);
  }

  @Override
  public synchronized void accept(Message msg) {
    streamer.putMessage(msg);
    for (var client: clients) {
      System.out.printf("%s => %s.%s\n", client.getChatID(), msg.getChatId(), msg);
      if (client.getChatID().equals(msg.getChatId())) {
        continue;
      }
      try {
        client.sendMessage(msg);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}