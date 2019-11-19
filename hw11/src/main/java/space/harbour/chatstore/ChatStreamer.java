package space.harbour.chatstore;

import java.util.List;

public interface ChatStreamer {
  void putMessage(Message message);
  List<Message> getHistory();
}
