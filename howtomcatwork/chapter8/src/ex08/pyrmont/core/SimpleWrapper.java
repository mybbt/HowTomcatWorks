package ex08.pyrmont.core;

import org.apache.catalina.*;
import org.apache.catalina.util.LifecycleSupport;

import javax.naming.directory.DirContext;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class SimpleWrapper implements Wrapper, Pipeline, Lifecycle {

    private Servlet instance = null;
    private String servletClass;
    private Loader loader;
    private String name;
    protected LifecycleSupport lifecycle = new LifecycleSupport(this);
    private SimplePipeline pipeline = new SimplePipeline(this);
    protected Container parent = null;
    protected boolean started = false;

    public SimpleWrapper() {
        pipeline.setBasic(new SimpleWrapperValve());
    }

    public Servlet allocate() throws ServletException {

        if (instance == null) {
            try {
                instance = loadServlet();
            } catch (ServletException e) {
                throw e;
            } catch (Throwable e) {
                throw new ServletException("Cannot allocate a servlet instance");
            }
        }
        return instance;
    }

    public Servlet loadServlet() throws ServletException {
        if (instance != null)
            return instance;

        Servlet servlet = null;
        String actualClass = servletClass;
        if (actualClass == null) {
            throw new ServletException("servlet class has not been secified");
        }
        System.out.println("TEST TEST");
        Loader loader = getLoader();
        if (loader == null) {
            throw new ServletException("No Loader");
        }

        ClassLoader classLoader = loader.getClassLoader();
        Class cla = null;

        try {
            if (classLoader != null) {
                cla = classLoader.loadClass(actualClass);
            }
        } catch (ClassNotFoundException e) {
            throw new ServletException("Servlet class not found");
        }

        try {
            servlet = (Servlet) cla.newInstance();
        } catch (Throwable e) {
            throw new ServletException("Failed to instantiate servlet");
        }

        try {
            System.out.println(servlet.toString());
            servlet.init(null);
        } catch (Throwable f) {
            throw new ServletException("Failed initialize servlet.");
        }

        return servlet;
    }

    public Loader getLoader() {
        if (loader != null)
            return loader;
        if (parent != null)
            return parent.getLoader();
        return null;
    }

    public void addLifecycleListener(LifecycleListener lifecycleListener) {

    }

    public LifecycleListener[] findLifecycleListeners() {
        return new LifecycleListener[0];
    }

    public void removeLifecycleListener(LifecycleListener lifecycleListener) {

    }

    public void start() throws LifecycleException {
        System.out.println("Starting Wrapper " + name);

        if (started)
            throw new LifecycleException("Wrapper already started");

        lifecycle.fireLifecycleEvent(BEFORE_START_EVENT, null);
        started = true;

        if ((loader != null) && (loader instanceof Lifecycle))
            ((Lifecycle) loader).start();

        if (pipeline instanceof Lifecycle)
            ((Lifecycle) pipeline).start();

        lifecycle.fireLifecycleEvent(START_EVENT, null);

        lifecycle.fireLifecycleEvent(AFTER_START_EVENT, null);
    }

    public void stop() throws LifecycleException {
        System.out.println("Stopping wrapper " + name);
        try {
            instance.destroy();
        } catch (Throwable t) {
            t.printStackTrace();
        }

        instance = null;
        if (!started)
            throw new LifecycleException("Wrapper " + name + " not started");

        lifecycle.fireLifecycleEvent(BEFORE_STOP_EVENT, null);
        lifecycle.fireLifecycleEvent(STOP_EVENT, null);
        started = false;

        if (pipeline instanceof Lifecycle) {
            ((Lifecycle) pipeline).stop();
        }

        if ((loader != null) && (loader instanceof Lifecycle)) {
            ((Lifecycle) loader).stop();
        }

        lifecycle.fireLifecycleEvent(AFTER_STOP_EVENT, null);
    }

    public Valve getBasic() {
        return pipeline.getBasic();
    }

    public void setBasic(Valve valve) {
        pipeline.setBasic(valve);
    }

    public synchronized void addValve(Valve valve) {
        pipeline.addValve(valve);
    }

    public Valve[] getValves() {
        return pipeline.getValves();
    }

    public void removeValve(Valve valve) {
        pipeline.removeValve(valve);
    }

    public long getAvailable() {
        return 0;
    }

    public void setAvailable(long l) {

    }

    public String getJspFile() {
        return null;
    }

    public void setJspFile(String s) {

    }

    public int getLoadOnStartup() {
        return 0;
    }

    public void setLoadOnStartup(int i) {

    }

    public String getRunAs() {
        return null;
    }

    public void setRunAs(String s) {

    }

    public String getServletClass() {
        return null;
    }

    public void setServletClass(String s) {
        this.servletClass = s;
    }

    public boolean isUnavailable() {
        return false;
    }

    public void addInitParameter(String s, String s1) {

    }

    public void addInstanceListener(InstanceListener instanceListener) {

    }

    public void addSecurityReference(String s, String s1) {

    }

    public void deallocate(Servlet servlet) throws ServletException {

    }

    public String findInitParameter(String s) {
        return null;
    }

    public String[] findInitParameters() {
        return new String[0];
    }

    public String findSecurityReference(String s) {
        return null;
    }

    public String[] findSecurityReferences() {
        return new String[0];
    }

    public void load() throws ServletException {

    }

    public void removeInitParameter(String s) {

    }

    public void removeInstanceListener(InstanceListener instanceListener) {

    }

    public void removeSecurityReference(String s) {

    }

    public void unavailable(UnavailableException e) {

    }

    public void unload() throws ServletException {

    }

    public String getInfo() {
        return null;
    }

    public void setLoader(Loader loader) {
        this.loader = loader;
    }

    public Logger getLogger() {
        return null;
    }

    public void setLogger(Logger logger) {

    }

    public Manager getManager() {
        return null;
    }

    public void setManager(Manager manager) {

    }

    public Cluster getCluster() {
        return null;
    }

    public void setCluster(Cluster cluster) {

    }

    public String getName() {
        return name;
    }

    public void setName(String s) {
        this.name = s;
    }

    public Container getParent() {
        return parent;
    }

    public void setParent(Container container) {
        this.parent = container;
    }

    public ClassLoader getParentClassLoader() {
        return null;
    }

    public void setParentClassLoader(ClassLoader classLoader) {

    }

    public Realm getRealm() {
        return null;
    }

    public void setRealm(Realm realm) {

    }

    public DirContext getResources() {
        return null;
    }

    public void setResources(DirContext dirContext) {

    }

    public void addChild(Container container) {

    }

    public void addContainerListener(ContainerListener containerListener) {

    }

    public void addMapper(Mapper mapper) {

    }

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {

    }

    public Container findChild(String s) {
        return null;
    }

    public Container[] findChildren() {
        return new Container[0];
    }

    public ContainerListener[] findContainerListeners() {
        return new ContainerListener[0];
    }

    public Mapper findMapper(String s) {
        return null;
    }

    public Mapper[] findMappers() {
        return new Mapper[0];
    }

    public void invoke(Request request, Response response) throws IOException, ServletException {
        pipeline.invoke(request, response);
    }

    public Container map(Request request, boolean b) {
        return null;
    }

    public void removeChild(Container container) {

    }

    public void removeContainerListener(ContainerListener containerListener) {

    }

    public void removeMapper(Mapper mapper) {

    }

    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {

    }
}
