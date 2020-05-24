package zps.myrpc.controlller;

import org.springframework.util.StringUtils;
import zps.myrpc.RpcRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;
/**
 * 该类是主要处理请求的类，在这里，会进行反射创建类，并进行
 * 方法的调用
 *
 * @author: zps
 **/

public class ProcessorHandler implements Runnable {

    private Socket socket;
    private Map<String, Object> handlerMap;

    public ProcessorHandler(Socket socket, Map<String, Object> handlerMap) {
        this.socket = socket;
        this.handlerMap = handlerMap;
    }

    @Override
    public void run() {
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            //获取输入流中的类名，方法名，参数
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();

            //通过反射调用本地服务
            Object result = invoke(rpcRequest);

            //接下来就是把结果写回
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {  //关闭流
            if(objectInputStream != null){
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(objectOutputStream != null){
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private Object invoke(RpcRequest rpcRequest) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        String servcieName = rpcRequest.getClassName(); //雷鸣
        String version = rpcRequest.getVersion();  //版本号
//        if (!StringUtils.isEmpty(version)) {
//            servcieName += "-" + "v1.0";
//        }
        servcieName += "-" + "v1.0";
        System.out.println(servcieName);
        //获取处理请求的对象
        Object service = handlerMap.get(servcieName);

        if (service == null) {
            throw new RuntimeException("servce not found :" + servcieName);
        }

        Object[] args = rpcRequest.getParams(); //获取请求的参数
        Method method = null;
        //下面主要是对有请求参数和没有请求参数的处理
        if (args != null) {
            //填充请求参数类型,主要是为了下面根据方法名，请求参数类型获取
            Class<?>[] types = new Class[args.length];
            for (int i = 0; i < args.length; ++i) {
                types[i] = args[i].getClass();
            }
            Class clazz = Class.forName(rpcRequest.getClassName());
            method = clazz.getMethod(rpcRequest.getMethodName()  ,types);
        } else {
            Class clazz = Class.forName(rpcRequest.getClassName()); //跟去请求的类进行加载
            method = clazz.getMethod(rpcRequest.getMethodName()); //sayHello, saveUser找到这个类中的方法
        }
        //通过反射直接调用方法
        Object result = method.invoke(service, args);
        return result;
    }
}
