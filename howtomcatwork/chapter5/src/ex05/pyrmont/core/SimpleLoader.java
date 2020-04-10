package ex05.pyrmont.core;

import org.apache.catalina.Container;
import org.apache.catalina.DefaultContext;
import org.apache.catalina.Loader;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

public class SimpleLoader implements Loader {

    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";
    ClassLoader classLoader = null;
    Container container = null;

    public SimpleLoader() {
        try {
            URL[] urls = new URL[1];
            URLStreamHandler urlStreamHandler = null;
            File classpath = new File(WEB_ROOT);
            String respository = (new URL("file",null,classpath.getCanonicalPath() + File.separator)).toString();
            urls[0] = new URL(null,respository,urlStreamHandler);
            classLoader = new URLClassLoader(urls);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public DefaultContext getDefaultContext() {
        return null;
    }

    @Override
    public void setDefaultContext(DefaultContext defaultContext) {

    }

    @Override
    public boolean getDelegate() {
        return false;
    }

    @Override
    public void setDelegate(boolean b) {

    }

    @Override
    public String getInfo() {
        return "A simple loader";
    }

    @Override
    public boolean getReloadable() {
        return false;
    }

    @Override
    public void setReloadable(boolean b) {

    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {

    }

    @Override
    public void addRepository(String s) {

    }

    @Override
    public String[] findRepositories() {
        return null;
    }

    @Override
    public boolean modified() {
        return false;
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {

    }
}
