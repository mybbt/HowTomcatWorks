1.  Bootstrap1所需类: core包的SimpleLoader,SimplePipeline,SimpleWrapper,SimpleWrapperValve,valves包的ClientIPLoggerValve,HeaderLoggerValve,webroot包的ModernServlet

2.  在Bootstrap1的调试中浏览器会自动发送一个favicon.ico图标请求,所以控制台会输出两次请求

3.  HttpProcessor类中的process方法会调用SimpleWrapper类中的invoke方法
- Valve代表要执行的任务,Pipeline用来存放Valve,Wrapper是最小的servlet容器，代表一个可以独立运行的servlet

4.容器包含管道，容器的invoke方法会调用管道的invoke方法,管道中的invoke会调用所有添加到容器中的阀,然后再调用基础阀(类似栈的思想),基础阀载入Servlet类

5. catalina中的jar包版本由4.0.6更换为4.1.31

6. Bootstrap2所需类: 除了Bootstrap1类，其他的类都需要

7. 容器包含管道，容器的invoke方法会调用管道的invoke方法,管道中的invoke会调用所有添加到容器中的阀,然后再调用基础阀(类似栈的思想);在SimpleContext类中设置基础阀，基础阀使用映射器查找子容器,......
