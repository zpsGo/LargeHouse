package zps;

import java.lang.reflect.Proxy;

/**
 * @author: zps
 * 这个类主要是代理客户端向服务器发起远程调用
 **/
public class RpcClientProxy {
    public <T> T clientProxy(final Class<T> interfaceCls, final String host, final int port) {

        //创建代理对象，没有调用的方法都会去调用RemoerInvocationhandler的代理方法
        return (T) Proxy.newProxyInstance(interfaceCls.getClassLoader(),
                new Class<?>[]{interfaceCls}, new RemoteInvocationHandler(host, port));
    }
}
