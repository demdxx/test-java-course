package space.harbour.chatstore;

import java.io.Serializable;

public class Message implements Serializable {
  private static final long serialVersionUID = -5805442012911913570L;

  private String chatId;
  private String username;
  private String message;

  public Message() {
    this(null, null, null);
  }

  public Message(String chatId, String username) {
    this(chatId, username, null);
  }

  public Message(String chatId, String username, String message) {
    this.chatId = chatId;
    this.username = username;
    this.message = message;
  }

  public String getChatId() {
    return chatId;
  }

  public void setChatId(String id) {
    chatId = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String s) {
    username = s;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String s) {
    message = s;
  }

  @Override
  public String toString() {
    if (message == null) {
      return String.format("NEW USER: %s", getUsername());
    }
    return String.format("%s> %s", getUsername(), message);
  }
}
