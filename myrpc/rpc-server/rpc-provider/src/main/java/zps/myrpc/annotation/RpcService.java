package zps.myrpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;
/**
 * @author: zps
 **/

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {

    Class<?> value(); //拿到服务的接口

    /**
     * 版本号
     */
    String version() default "";

}
