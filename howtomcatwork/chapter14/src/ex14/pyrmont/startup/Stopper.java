package ex14.pyrmont.startup;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Stopper {

  public static void main(String[] args) {
    // StandardServer默认端口是8005,可以在Bootstrap中调用setPort(int port)方法修改
    int port = 8005;
    try {
      Socket socket = new Socket("127.0.0.1", port);
      OutputStream stream = socket.getOutputStream();
      String shutdown = "SHUTDOWN";
      for (int i = 0; i < shutdown.length(); i++)
        stream.write(shutdown.charAt(i));
      stream.flush();
      stream.close();
      socket.close();
      System.out.println("The server was successfully shut down.");
    }
    catch (IOException e) {
      System.out.println("Error. The server has not been started.");
    }
  }
}