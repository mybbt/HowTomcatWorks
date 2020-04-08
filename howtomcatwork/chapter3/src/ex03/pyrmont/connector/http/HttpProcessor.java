package ex03.pyrmont.connector.http;


import ex03.pyrmont.ServletProcessor;
import ex03.pyrmont.StaticResourceProcessor;
import org.apache.catalina.util.RequestUtil;
import org.apache.catalina.util.StringManager;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class HttpProcessor {

    private HttpConnector connector = null;
    private HttpRequest request;
    private HttpResponse response;
    private HttpRequestLine requestLine = new HttpRequestLine();

    protected String method = null;
    protected String queryString = null;

    public HttpProcessor(HttpConnector connector) {
        this.connector = connector;
    }

    // 参数填写包名，默认是包下面的LocalStrings文件
    protected StringManager sm = StringManager.getManager("ex03.pyrmont.connector.http");

    public void process(Socket socket){
        //继承了InputStream,可以更快捷的处理HTTP请求行
        SocketInputStream input = null;

        OutputStream output = null;

        try {
            input = new SocketInputStream(socket.getInputStream(),2048);
            output = socket.getOutputStream();

            request = new HttpRequest(input);

            response = new HttpResponse(output);
            response.setRequest(request);
            //设置请求头，例如 Content-Type
            response.setHeader("Server","Pyrmont Servlet Container");

            //
            parseRequest(input,output);
            //
            parseHeaders(input);

            //用来处理servlet和静态资源
            if(request.getRequestURI().startsWith("/servlet")){
                ServletProcessor processor = new ServletProcessor();
                processor.process(request,response);
            } else{
                StaticResourceProcessor processor = new StaticResourceProcessor();
                processor.process(request,response);
            }
            socket.close();
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
    }

    private void parseRequest(SocketInputStream input,OutputStream output) throws ServletException, IOException {
        //将input中的请求行放入requestLine中，这里充分阐述了面向对象思想.....
        input.readRequestLine(requestLine);

        String method = new String(requestLine.method,0,requestLine.methodEnd);
        String uri;
        String protocol = new String(requestLine.protocol,0,requestLine.protocolEnd);

        if(method.length() < 1){
            throw new ServletException("Missing HTTP request method ");
        }
        else if (requestLine.uriEnd < 1){
            throw new ServletException("Missing HTTP request URI");
        }

        //获取请求资源
        int question = requestLine.indexOf("?");
        if (question >= 0){
            request.setQueryString(new String(requestLine.uri,question+1,requestLine.uriEnd - question - 1));
            uri = new String(requestLine.uri,0,question);
        }
        else {
            request.setQueryString(null);
            uri = new String(requestLine.uri,0,requestLine.uriEnd);
        }

        if (!uri.startsWith("/")) {
            int pos = uri.indexOf("://");
            // Parsing out protocol and host name
            if (pos != -1) {
                pos = uri.indexOf('/', pos + 3);
                if (pos == -1) {
                    uri = "";
                }
                else {
                    uri = uri.substring(pos);
                }
            }
        }

        // Parse any requested session ID out of the request URI
        String match = ";jsessionid=";
        int semicolon = uri.indexOf(match);
        if (semicolon >= 0) {
            String rest = uri.substring(semicolon + match.length());
            int semicolon2 = rest.indexOf(';');
            if (semicolon2 >= 0) {
                request.setRequestedSessionId(rest.substring(0, semicolon2));
                rest = rest.substring(semicolon2);
            }
            else {
                request.setRequestedSessionId(rest);
                rest = "";
            }
            request.setRequestedSessionURL(true);
            uri = uri.substring(0, semicolon) + rest;
        }
        else {
            request.setRequestedSessionId(null);
            request.setRequestedSessionURL(false);
        }

        // Normalize URI (using String operations at the moment)
        String normalizedUri = normalize(uri);

        // Set the corresponding request properties
        ((HttpRequest) request).setMethod(method);
        request.setProtocol(protocol);
        if (normalizedUri != null) {
            ((HttpRequest) request).setRequestURI(normalizedUri);
        }
        else {
            throw new ServletException("Invalid URI: " + uri + "'");
        }
    }

    private void parseHeaders(SocketInputStream input) throws ServletException,IOException {
        while(true){
            HttpHeader header = new HttpHeader();

            input.readHeader(header);
            if (header.nameEnd == 0){
                if (header.valueEnd == 0){
                    return ;
                }
                else {
                    throw new ServletException(sm.getString("httpProcessor.parseHeaders.colon"));
                }
            }

            String name = new String(header.name,0,header.nameEnd);
            String value = new String(header.value,0,header.valueEnd);
            request.addHeader(name,value);

            if(name.equals("cookie")){
                Cookie cookies[] = RequestUtil.parseCookieHeader(value);

                for (int i = 0; i < cookies.length; i++){
                    if (cookies[i].getName().equals("jsessionid")) {

                        if (!request.isRequestedSessionIdFromCookie()) {

                            request.setRequestedSessionId(cookies[i].getValue());
                            request.setRequestedSessionCookie(true);
                            request.setRequestedSessionURL(false);
                        }
                    }
                    request.addCookie(cookies[i]);
                }
            }
            else if (name.equals("content-length")){
                int n = -1;
                try {
                    n = Integer.parseInt(value);
                }catch (Exception e){
                    throw new ServletException((sm.getString("httpProcessor.parseHeaders.contentLength")));
                }
                request.setContentLength(n);
            }
            else if(name.equals("content-type")){
                request.setContentType(value);
            }
        }
    }

    //尝试修正错误的URI，若没有错误或者修正成功则返回正确的URI，反之则返回null
    protected String normalize(String path) {
        if (path == null)
            return null;
        // Create a place for the normalized path
        String normalized = path;

        // Normalize "/%7E" and "/%7e" at the beginning to "/~"
        if (normalized.startsWith("/%7E") || normalized.startsWith("/%7e"))
            normalized = "/~" + normalized.substring(4);

        // Prevent encoding '%', '/', '.' and '\', which are special reserved
        // characters
        if ((normalized.indexOf("%25") >= 0)
                || (normalized.indexOf("%2F") >= 0)
                || (normalized.indexOf("%2E") >= 0)
                || (normalized.indexOf("%5C") >= 0)
                || (normalized.indexOf("%2f") >= 0)
                || (normalized.indexOf("%2e") >= 0)
                || (normalized.indexOf("%5c") >= 0)) {
            return null;
        }

        if (normalized.equals("/."))
            return "/";

        // Normalize the slashes and add leading slash if necessary
        if (normalized.indexOf('\\') >= 0)
            normalized = normalized.replace('\\', '/');
        if (!normalized.startsWith("/"))
            normalized = "/" + normalized;

        // Resolve occurrences of "//" in the normalized path
        while (true) {
            int index = normalized.indexOf("//");
            if (index < 0)
                break;
            normalized = normalized.substring(0, index) +
                    normalized.substring(index + 1);
        }

        // Resolve occurrences of "/./" in the normalized path
        while (true) {
            int index = normalized.indexOf("/./");
            if (index < 0)
                break;
            normalized = normalized.substring(0, index) +
                    normalized.substring(index + 2);
        }

        // Resolve occurrences of "/../" in the normalized path
        while (true) {
            int index = normalized.indexOf("/../");
            if (index < 0)
                break;
            if (index == 0)
                return (null);  // Trying to go outside our context
            int index2 = normalized.lastIndexOf('/', index - 1);
            normalized = normalized.substring(0, index2) +
                    normalized.substring(index + 3);
        }

        // Declare occurrences of "/..." (three or more dots) to be invalid
        // (on some Windows platforms this walks the directory tree!!!)
        if (normalized.indexOf("/...") >= 0)
            return (null);

        // Return the normalized path that we have completed
        return (normalized);

    }
}
