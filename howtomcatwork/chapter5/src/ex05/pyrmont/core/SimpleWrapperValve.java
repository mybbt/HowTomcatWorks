package ex05.pyrmont.core;

import org.apache.catalina.*;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SimpleWrapperValve implements Valve, Contained {

    private Container container;

    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public void invoke(Request request, Response response, ValveContext valveContext) throws IOException, ServletException {

        SimpleWrapper wrapper = (SimpleWrapper) getContainer();
        ServletRequest sreq = request.getRequest();
        ServletResponse sres = response.getResponse();
        Servlet servlet = null;
        HttpServletRequest hreq = null;
        HttpServletResponse hres = null;

        if (sreq instanceof HttpServletRequest)
            hreq = (HttpServletRequest) sreq;
        if (sres instanceof HttpServletResponse)
            hres = (HttpServletResponse) sres;

        try {
            servlet = wrapper.allocate();
            if (hres != null&& hreq != null){
                servlet.service(hreq,hres);
            }
            else
                servlet.service(sreq,sres);
        } catch (ServletException e) {
            e.printStackTrace();
        }

    }
}