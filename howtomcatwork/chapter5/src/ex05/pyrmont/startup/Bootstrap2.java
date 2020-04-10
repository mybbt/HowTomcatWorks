package ex05.pyrmont.startup;

import ex05.pyrmont.core.SimpleContext;
import ex05.pyrmont.core.SimpleContextMapper;
import ex05.pyrmont.core.SimpleLoader;
import ex05.pyrmont.core.SimpleWrapper;
import ex05.pyrmont.valves.ClientIPLoggerValve;
import ex05.pyrmont.valves.HeaderLoggerValve;
import org.apache.catalina.*;
import org.apache.catalina.connector.http.HttpConnector;

import java.io.IOException;

public class Bootstrap2 {
    public static void main(String[] args){
        HttpConnector httpConnector = new HttpConnector();
        //创建两个最小的Wrapper容器, 给容器设置容器名和容器所执行的Servlet类名
        Wrapper wrapper1 = new SimpleWrapper();
        wrapper1.setName("Primitive");
        wrapper1.setServletClass("PrimitiveServlet");
        Wrapper wrapper2 = new SimpleWrapper();
        wrapper2.setName("Modern");
        wrapper2.setServletClass("ModernServlet");

        //创建可以包含Wrapper的Context容器,将每个子容器Wrapper放入其中，通过Wrapper的名字绑定Wrapper容器
        Context context = new SimpleContext();
        context.addChild(wrapper1);
        context.addChild(wrapper2);


        Valve valve1 = new HeaderLoggerValve();
        Valve valve2 = new ClientIPLoggerValve();

        //加入额外的阀(非基础阀);而阀的invoke方法，由HttpProcessor类中调用
        ((Pipeline) context).addValve(valve1);
        ((Pipeline) context).addValve(valve2);

        //映射器, 在本例中用来查找绑定的Wrapper
        Mapper mapper = new SimpleContextMapper();
        mapper.setProtocol("http");
        context.addMapper(mapper);

        //加载器,这里主要用来初始化 类加载路径
        Loader loader = new SimpleLoader();
        context.setLoader(loader);

        //键值对映射，相当于XML文件中的映射配置，第一个是请求路径,第二个是Servlet名字(Servlet的名字是前面绑定 Wrapper的名字)
        context.addServletMapping("/Primitive","Primitive");
        context.addServletMapping("/Modern","Modern");

        httpConnector.setContainer(context);

        try {
            httpConnector.initialize();
            httpConnector.start();

            System.in.read();

        } catch (LifecycleException | IOException e) {
            e.printStackTrace();
        }
    }
}
