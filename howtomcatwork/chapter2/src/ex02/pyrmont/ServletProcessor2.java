package ex02.pyrmont;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

public class ServletProcessor2 {

    public void process(Request request,Response response){
        String uri = request.getUri();
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);
        URLClassLoader loader = null;
        try {

            //URL可以用于http,file,等等，并且可以序列化，实现了序列化的接口
            URL[] urls = new URL[1];

            // Opens a connection to the object referenced by the argument(打开与参数引用对象的连接).URLStreamHandler是一个抽象类
            URLStreamHandler streamHandler = null;
            File classpath = new File(Constants.WEB_ROOT);

            //第一个参数是协议，"file"表示是个文件路径，URL加载的是个文件(新知识，一直以为URL只是用来指代 HTTP链接)
            String repository = (new URL("file",null,classpath.getCanonicalPath() + File.separator)).toString();

            //第三个参数之所以不直接写null,是因为new URL(null,repository,null)可能与另一个URL的构造函数冲突，所以指定类型，非常细节......
            urls[0] = new URL(null,repository,streamHandler);

            //URLClassLoader用来载入文件或者目录，若是以'/'结尾则都假定是目录，否则是默认打开JAR文件;repository中以'/'结尾，则是代表一个文件目录
            loader = new URLClassLoader(urls);

        }catch (IOException e){
            System.out.println(e.toString());
        }
        Class myClass = null;
        try {
            //类加载器，加载 Servlet类
            myClass = loader.loadClass(servletName);

        } catch (ClassNotFoundException e) {
            System.out.println(e.toString());
        }
        Servlet servlet = null;
        RequestFacade requestFacade = new RequestFacade(request);
        ResponseFacade responseFacade = new ResponseFacade(response);
        try {
            //创建 Servlet类的实例
            servlet = (Servlet)myClass.newInstance();
            //调用Servlet的 service方法
            servlet.service((ServletRequest)requestFacade,(ServletResponse)responseFacade);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println(e.toString());
        } catch (ServletException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}
