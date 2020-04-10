package ex05.pyrmont.startup;

import ex05.pyrmont.core.SimpleLoader;
import ex05.pyrmont.core.SimpleWrapper;
import ex05.pyrmont.valves.ClientIPLoggerValve;
import ex05.pyrmont.valves.HeaderLoggerValve;
import org.apache.catalina.*;
import org.apache.catalina.connector.http.HttpConnector;

import java.io.IOException;

public class Bootstrap1 {
    public static void main(String[] args){
        HttpConnector connector = new HttpConnector();
        // 最小的容器,仅表示一个独立的Servlet类;主要用来初始化对类进行实例化所需的参数，同时其无参构造函数设置基础阀为SimpleWrapperValve,
        // 而SimpleWrapperValve则用来执行初始化servlet，并调用service方法的任务
        Wrapper wrapper = new SimpleWrapper();

        wrapper.setServletClass("ModernServlet");
        //获取类加载器
        Loader loader = new SimpleLoader();

        Valve valve1 = new HeaderLoggerValve();
        Valve valve2 = new ClientIPLoggerValve();

        //在容器中设置加载器
        wrapper.setLoader(loader);
        ((Pipeline) wrapper).addValve(valve1);
        ((Pipeline) wrapper).addValve(valve2);

        connector.setContainer(wrapper);

        try {
            connector.initialize();
            connector.start();

            System.in.read();
        } catch (LifecycleException | IOException e) {
            e.printStackTrace();
        }
    }
}
