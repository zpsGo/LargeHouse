package zps;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: zps
 **/
@Configuration
public class SpringConfig  {

    @Bean
    public RpcClientProxy rpcClientProxy(){
        return new RpcClientProxy();
    }

}
