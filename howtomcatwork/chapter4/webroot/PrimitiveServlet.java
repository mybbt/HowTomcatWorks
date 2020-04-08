import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

public class PrimitiveServlet implements Servlet {
    public PrimitiveServlet() {
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("init");
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("from service");
        PrintWriter printWriter = servletResponse.getWriter();
        printWriter.println(("HTTP/1.1 PrimitiveServlet" + "\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n"));
        printWriter.println("Hello,World");
        printWriter.println("Hello,Servlet");
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public String getServletInfo() {
        return null;
    }

}
