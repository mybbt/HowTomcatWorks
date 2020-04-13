package ex06.pyrmont.core;

import org.apache.catalina.*;

import javax.servlet.http.HttpServletRequest;

public class SimpleContextMapper implements Mapper {

    private SimpleContext context = null;
    String protocol = null;

    @Override
    public Container getContainer() {
        return this.context;
    }

    @Override
    public void setContainer(Container container) {
        if (!(container instanceof SimpleContext))
            throw new IllegalArgumentException ("Illegal type of container");
        this.context = (SimpleContext)container;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public void setProtocol(String s) {
        this.protocol = s;
    }

    @Override
    public Container map(Request request, boolean b) {
        String contextPath = ((HttpServletRequest)request.getRequest()).getContextPath();
//        System.out.println("contextPath: " + contextPath);
        String requestURI = ((HttpRequest)request).getDecodedRequestURI();
//        System.out.println("requestURI: " + requestURI);
        String relativeURI = requestURI.substring(contextPath.length());
//        System.out.println("relativeURI: " + relativeURI);
        Wrapper wrapper = null;
        String servletPath = relativeURI;
        String pathInfo = null;
        String name = context.findServletMapping(relativeURI);
//        System.out.println("name: " + name);
        if (name!=null)
            wrapper = (Wrapper)context.findChild(name);
        return wrapper;
    }
}
