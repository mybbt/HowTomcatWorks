package ex01.pyrmont;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
        // System.getProperty(String key) 用来获取由特定键指定的属性值
    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";


    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    private boolean shutdown = false;


    public static void main(String[] args) {
//        System.out.println(WEB_ROOT);
        HttpServer server = new HttpServer();
        server.await();
    }

    public void await() {
        ServerSocket serverSocket = null;
        int port = 8080;
        try {
          //第一个参数是请求端口，第二个参数是监听数量上限，第三个是绑定服务器
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
//            System.out.println(serverSocket.toString());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        Socket socket = null;
        InputStream input = null;
        OutputStream output = null;
        while (!shutdown) {
            try {
                socket = serverSocket.accept();
                input = socket.getInputStream();
                output = socket.getOutputStream();
//                System.out.println("socket.toString(): "+socket.toString());
                Request request = new Request(input);
                request.parse();


                Response response = new Response(output);
                response.setRequest(request);
                response.sendStaticResource();

                socket.close();

                shutdown = request.getUri().equals(SHUTDOWN_COMMAND);


            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }
}
