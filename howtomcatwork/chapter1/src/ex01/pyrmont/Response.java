package ex01.pyrmont;

import java.io.*;

public class Response {

    private static final int BUFFER_SIZE = 2048;
    Request request;
    OutputStream output;

    public Response(OutputStream output) {
        this.output = output;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        OutputStreamWriter ou = null;
        try {
            File file = new File(HttpServer.WEB_ROOT, request.getUri());
//            System.out.println("file: " + file.toString() + "  " + file.exists());
            if (file.exists()) {
                fis = new FileInputStream(file);
                int ch = fis.read(bytes, 0, BUFFER_SIZE);
//                System.out.println("fis: " + fis.toString() + "  " + file.exists() + " ch: "+ ch);


                //这是手动添加的 HTTP响应头
                output.write(("HTTP/1.1 /index.html\r\n" +
                        "Content-Type: text/html\r\n" +
                        "\r\n").getBytes());

                //直接输出bytes到浏览器会报错(无响应)，加入 HTTP响应头后再输出HTML文件就能正确显示在浏览器中
                output.write(bytes);
            } else {
                String erroMessage = "HTTP/1.1 404 File Not Found\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 23\r\n" +
                        "\r\n" +
                        "<h1>File Not Found</h1>";
                output.write(erroMessage.getBytes());
            }
        } catch (Exception e) {
            System.out.println("response: " + e.toString());
        } finally {
            if (fis != null)
                fis.close();
        }
    }
}
