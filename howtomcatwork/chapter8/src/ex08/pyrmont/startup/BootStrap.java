package ex08.pyrmont.startup;

import org.apache.catalina.*;
import org.apache.catalina.connector.http.HttpConnector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.loader.WebappClassLoader;
import org.apache.catalina.loader.WebappLoader;
import org.apache.naming.resources.ProxyDirContext;
import ex08.pyrmont.core.SimpleContextConfig;
import ex08.pyrmont.core.SimpleWrapper;


public class BootStrap {
    public static void main(String[] args) {

        System.setProperty("catalina.base", System.getProperty("user.dir"));
        Connector connector = new HttpConnector();
        Wrapper wrapper1 = new SimpleWrapper();
        wrapper1.setName("Primitive");
        wrapper1.setServletClass("PrimitiveServlet");
        Wrapper wrapper2 = new SimpleWrapper();
        wrapper2.setName("Modern");
        wrapper2.setServletClass("ModernServlet");

        Context context = new StandardContext();

        context.setPath("/myApp");
        context.setDocBase("myApp");

        context.addChild(wrapper1);
        context.addChild(wrapper2);

        context.addServletMapping("/Primitive", "Primitive");
        context.addServletMapping("/Modern", "Modern");

        LifecycleListener listener = new SimpleContextConfig();
        ((Lifecycle) context).addLifecycleListener(listener);

        Loader loader = new WebappLoader();
        context.setLoader(loader);

        connector.setContainer(context);
        try {
            connector.initialize();
            ((Lifecycle) connector).start();
            ((Lifecycle) context).start();

            WebappClassLoader classLoader = (WebappClassLoader) loader.getClassLoader();
            System.out.println(classLoader.getJarPath());
            System.out.println("Resources' docBase: " + ((ProxyDirContext) classLoader.getResources()).getDocBase());
            String[] repositories = classLoader.findRepositories();
            for (int i = 0; i < repositories.length; i++) {
                System.out.println("repository: " + repositories[i]);
            }

            System.in.read();
            ((Lifecycle) context).stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
