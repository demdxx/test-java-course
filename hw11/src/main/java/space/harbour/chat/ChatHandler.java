package space.harbour.chat;

import java.io.IOException;

import space.harbour.chatstore.Message;

public interface ChatHandler {
  String getChatID();
  String getName();

  void sendMessage(Message message) throws IOException;
}