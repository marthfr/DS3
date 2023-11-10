package main.server;

import java.net.Socket;
import java.util.Map;

public interface MessageHandler {
  public Map<String, Integer> getPortMapVals();
  public Map<String, Socket> getSocketMapVals();

  void handleMessage(String msg, String memberNum);
}
