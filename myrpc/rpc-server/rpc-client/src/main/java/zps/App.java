package zps;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import zps.myrpc.IHelloService;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ApplicationContext context=new
                AnnotationConfigApplicationContext(SpringConfig.class);
        RpcClientProxy rpcProxyClient=context.getBean(RpcClientProxy.class);

        IHelloService iHelloService=rpcProxyClient.clientProxy
                (IHelloService.class,"localhost",8080);
        System.out.println(iHelloService.sayHello("你好，rpc"));
    }
}
