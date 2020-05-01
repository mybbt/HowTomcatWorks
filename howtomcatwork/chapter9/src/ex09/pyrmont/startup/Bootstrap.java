package ex09.pyrmont.startup;

import ex09.pyrmont.core.SimpleContextConfig;
import ex09.pyrmont.core.SimpleWrapper;
import org.apache.catalina.*;
import org.apache.catalina.connector.http.HttpConnector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.loader.WebappLoader;
import org.apache.catalina.session.StandardManager;

public final class Bootstrap {
  public static void main(String[] args) {

    System.setProperty("catalina.base", System.getProperty("user.dir"));
    Connector connector = new HttpConnector();
    Wrapper wrapper1 = new SimpleWrapper();
    wrapper1.setName("Session");
    wrapper1.setServletClass("SessionServlet");

    Wrapper wrapper2 = new SimpleWrapper();
    wrapper2.setName("Modern");
    wrapper2.setServletClass("ModernServlet");

    Wrapper wrapper3 = new SimpleWrapper();
    wrapper3.setName("Primitive");
    wrapper3.setServletClass("PrimitiveServlet");

    Context context = new StandardContext();

    context.setPath("/myApp");
    context.setDocBase("myApp");

    context.addChild(wrapper1);
    context.addChild(wrapper2);
    context.addChild(wrapper3);

    context.addServletMapping("/Session", "Session");
    context.addServletMapping("/Modern", "Modern");
    context.addServletMapping("/Primitive", "Primitive");


    LifecycleListener listener = new SimpleContextConfig();
    ((Lifecycle) context).addLifecycleListener(listener);


    Loader loader = new WebappLoader();

    context.setLoader(loader);

    connector.setContainer(context);

    Manager manager = new StandardManager();
    context.setManager(manager);

    try {
      connector.initialize();
      ((Lifecycle) connector).start();

      ((Lifecycle) context).start();

      System.in.read();
      ((Lifecycle) context).stop();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}