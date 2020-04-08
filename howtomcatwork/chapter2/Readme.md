1. HttpServer2类是HttpServer1的改进版

2. ServletProcessor2类是ServletProcessor1的改进版

3. 使用外观模式将Request和Response包装起来，防止调用某些不应该被调用的方法
- 补充：使用了相同的接口实现，其中RequestFacade和ResponseFacade与Request和Response实现了相同的接口，
而在ServletProcessor2这个类中，在调用service方法时传入的RequestFacade和ResponseFacade的实例，
这是因为service方法的参数也是ServletRequest和ServletResponse的实例，而ServletRequest和ServletResponse就是RequestFacade，ResponseFacade，Request，Response共同实现的接口，方法概览如下：
- public void service(ServletRequest servletRequest, ServletResponse servletResponse)

4. 关于第二章有些小问题，在Response中需要在输出HTML文件时先手动输出响应头，在提供的示例代码中没有，第一章也是这种情况
- 使用的浏览器是Chrome
