package ex04.pyrmont.start;

import ex04.pyrmont.core.SimpleContainer;
import org.apache.catalina.connector.http.HttpConnector;

public class Bootstrap {
    public static void main(String[] args) {
        byte[] a = new byte[2048];
        HttpConnector connector = new HttpConnector();
        SimpleContainer container = new SimpleContainer();
        connector.setContainer(container);
        try {
            //这里会从套接字工厂返回一个serverSocket
            connector.initialize();
            connector.start();
            // read()本来是用来读取键盘输入，在这里用来停止web服务器;
            // 当输入字符时，会停止Bootstrap类，从而达到停止web服务器的目的
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
