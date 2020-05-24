package zps;

import zps.myrpc.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author: zps
 * 代理类
 **/
public class RemoteInvocationHandler implements InvocationHandler {

    private String host;
    private int port;

    public RemoteInvocationHandler(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        //请求会进入到这里
        System.out.println("come in");
        //请求数据的包装
        RpcRequest rpcRequest=new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParams(args);
//        rpcRequest.setVersion("v2.0");
        //远程通信
        RpcNetTransport netTransport=new RpcNetTransport(host,port);
        Object result=netTransport.send(rpcRequest);

        return result;
    }

}
