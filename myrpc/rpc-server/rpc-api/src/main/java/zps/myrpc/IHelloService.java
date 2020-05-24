package zps.myrpc;

/**
 * @author: zps
 **/
public interface IHelloService {

    String sayHello(String content);

    String saveUser(User user);

}
