package space.harbour.chat;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.concurrent.SubmissionPublisher;

import space.harbour.chatstore.ChatStreamer;
import space.harbour.chatstore.Message;

public class BotHandler extends SubmissionPublisher<Message> implements ChatHandler, Runnable {
  private final String id;
  private final ChatStreamer streamer;
  private String username;
  private final DataInputStream streamIn;
  private final DataOutputStream streamOut;

  public BotHandler(String id, Socket client, ChatStreamer streamer) throws IOException {
    super();
    this.id = id;
    this.streamer = streamer;
    this.streamIn = new DataInputStream(new BufferedInputStream(client.getInputStream()));
    this.streamOut = new DataOutputStream(client.getOutputStream());
  }

  @Override
  public String getChatID() {
    return id;
  }

  @Override
  public String getName() {
    return username;
  }

  @Override
  public void sendMessage(Message message) throws IOException {
    if (username == null) return;
    this.streamOut.writeUTF(message.toString());
    this.streamOut.flush();
  }

  // Send all previous messages
  private void sendHistory() throws IOException {
    var history = streamer.getHistory();
    for (var msg: history) {
      if (!msg.getChatId().equals(id)) {
        sendMessage(msg);
      }
    }
  }

  @Override
  public void run() {
    try (this.streamIn; this.streamOut) {
      var exit = false;

      while (true) {
        this.streamOut.writeUTF("USERNAME:");
        this.streamOut.flush();
        try { username = this.streamIn.readUTF(); } catch (NoSuchElementException e) {}
        if (username == null) {
          exit = true;
          break;
        }
        username = username.strip();
        var cmd = username.toLowerCase();
        if (cmd.equals("quit") || cmd.equals("/q")) {
          exit = true;
          break;
        }
        if (username.length() != 0) {
          break;
        }
      }

      if (!exit) {
        System.out.printf("New member joined: %s\n", username);
        this.streamOut.writeUTF(String.format("GREETINGS %s", username));
        sendHistory();
        submit(new Message(id, username));
      } else {
        this.streamOut.writeUTF("BUY");
      }
      this.streamOut.flush();

      while (!exit) {
        String message = null;
        try { message = this.streamIn.readUTF(); } catch (NoSuchElementException e) {}
        if (message == null) {
          break;
        }
        message = message.strip();
        if (message.length() > 0) {
          System.out.printf("%s> %s\n", username, message);
        }
        if (message.toLowerCase().equals("/quit")) {
          submit(new Message(id, username, "left the chat"));
          break;
        }
        if (message.length() != 0) {
          submit(new Message(id, username, message));
        }
      }
      this.streamOut.writeUTF("BUY");
    } catch (Exception e) {
      submit(new Message(id, username, "left the chat"));
      // e.printStackTrace();
    }
  }
}