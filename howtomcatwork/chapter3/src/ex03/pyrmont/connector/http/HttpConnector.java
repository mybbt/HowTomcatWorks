package ex03.pyrmont.connector.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpConnector implements Runnable {
    boolean stopped;
    private String scheme = "http";

    public String getScheme(){
        return scheme;
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        int port = 8080;
        try {
            serverSocket = new ServerSocket(port,1, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        while (!stopped){
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                continue;
            }
            //这个this代表当前类(当前代码中这个 this代表 HttpConnector的实例化对象)
            HttpProcessor processor = new HttpProcessor(this);
            processor.process(socket);
        }

    }

    public void start(){
        //这个this代表当前实现了Runnable的这个类( this在这个类中代表的是HttpConnetor的实例对象)
        Thread thread = new Thread(this);
        thread.start();
    }
}
