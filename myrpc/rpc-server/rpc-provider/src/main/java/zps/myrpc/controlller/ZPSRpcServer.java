package zps.myrpc.controlller;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;
import zps.myrpc.annotation.RpcService;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: zps
 **/
public class ZPSRpcServer implements ApplicationContextAware , InitializingBean {

    //保存暴露服务的类
    private Map<String,Object> handlerMap=new HashMap();

    //缓存池，作用是减少I/O阻塞带来的性能问题
    ExecutorService executorService = Executors.newCachedThreadPool();
    private int port;

    public ZPSRpcServer(int port) {
        this.port = port;
    }

    /**
     * 继承了InitializingBean,他主要是在进行依赖注入之后，进行的操作，这里直接就是
     * 为了创建处理的socket
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while (true){  //不断接收请求
                Socket socket = serverSocket.accept();
                System.out.println("有请求连接成功！！！");
                //扔给线程池进行处理
                executorService.execute(new ProcessorHandler(socket , handlerMap));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {  //关闭连接
            if(serverSocket != null){
                serverSocket.close();
            }
        }
    }

    /**
     * 该方法主要是从容器中获取所有的需要对外暴露的bean
     * @param applicationContext : Spring bean容器
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //从容器中获取所有被RpcService标志的对象信息
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);

        if(!serviceBeanMap.isEmpty()){
            for(Object serviceBean : serviceBeanMap.values()){
                //获取类上的注解类对象
                RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);
                String serviceName = rpcService.value().getName(); //拿到接口的定义
                String version = rpcService.version();   //拿到版本号
                if(!StringUtils.isEmpty(version)){
                    serviceName += "-" + version;
                }
                handlerMap.put(serviceName , serviceBean);
            }
        }
    }
}
