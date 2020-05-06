package ex16.pyrmont.demo;

import java.io.IOException;

public class ShutdownHookDemo {
    public static void main(String[] args) {
        ShutdownHookDemo shutdownHookDemo = new ShutdownHookDemo();
        shutdownHookDemo.start();
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        System.out.println("Demo");
        ShutdownHook shutdownHook = new ShutdownHook();
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    class ShutdownHook extends Thread {
        public void run(){
            System.out.println("SHUTTING DOWN ");
        }
    }
}
