package space.harbour.chat;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ChatClient implements Runnable {
  private final Socket client;
  private final String username;
  private final DataInputStream streamIn;
  private final DataOutputStream streamOut;
  private final BufferedReader console;

  // The only one posibility which I fount how to work with keyboard
  // @link https://www.thehelper.net/threads/java-detect-key-pressed.145555/
  // The other way is console interfaces
  public ChatClient(String username, String hostname, int port) throws UnknownHostException, IOException {
    this.username = username;
    this.client = new Socket(hostname, port);
    this.streamIn = new DataInputStream(new BufferedInputStream(client.getInputStream()));
    this.streamOut = new DataOutputStream(client.getOutputStream());
    this.console = new BufferedReader(new InputStreamReader(System.in), 1024 * 64);
  }

  // private Thread runInputReader() {
  // var inputThread = new Thread(() -> {
  // String line = null;
  // try {
  // while (true) {
  // System.out.print("> ");
  // line = console.readLine();
  // if (line == null) break;
  // if (line.length() > 0) {
  // streamOut.writeUTF(line);
  // streamOut.flush();
  // }
  // }
  // } catch (IOException e) {
  // e.printStackTrace();
  // }
  // });
  // inputThread.start();
  // return inputThread;
  // }

  private void printLine(String line) {
    // System.out.print("\033[s\r\n\033[1A");
    // System.out.println(line);
    // System.out.print("\033[u\033[1B");
    System.out.println(line);
  }

  private synchronized void printData(List<String> data) throws IOException {
    // Read next line from the chat
    for (var l: data) {
      if (l.length() == 0) continue;
      if (l.startsWith("USERNAME:")) {
        printLine(String.format("My name is: %s", username));
        streamOut.writeUTF(username);
        streamOut.flush();
      } else {
        printLine(l);
      }
    }
    data.clear();
  }

  @Override
  public void run() {
    Thread inputThread = null;
    List<String> data = new LinkedList<>();
    try (streamIn; streamOut; console) {
      // inputThread = runInputReader();

      // Read all data from the server
      inputThread = new Thread(() -> {
        String line = null;
        try {
          while (true) {
            line = streamIn.readUTF();
            if (line == null) break;
            if (line.length() == 0)  continue;
            synchronized (ChatClient.this) {
              data.add(line);
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
      inputThread.start();

      Thread.sleep(500);
      printData(data);
      Thread.sleep(500);

      while (true) {
        printData(data);

        System.out.print("> ");
        String line = console.readLine();
        if (line == null)
          break;
        if (line.length() > 0) {
          streamOut.writeUTF(line);
          streamOut.flush();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (inputThread != null)
        inputThread.interrupt();
    }
  }

  public static void main(String[] args) {
    try {
      var r = new Random();
      var username = "u" + String.valueOf(r.nextInt(10));
      if (args.length > 0)
        username = args[0];
      System.out.println("RUN Chat Client");
      var chat = new ChatClient(username, "127.0.0.1", 8080);
      chat.run();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}