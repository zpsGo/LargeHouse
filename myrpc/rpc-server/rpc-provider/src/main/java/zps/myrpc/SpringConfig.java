package zps.myrpc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import zps.myrpc.controlller.ZPSRpcServer;

/**
 * @author: zps
 **/

@Configuration
@ComponentScan(basePackages = "zps.myrpc.service")
public class SpringConfig {

    @Bean
    public ZPSRpcServer zpsRpcServer(){
        return new ZPSRpcServer(8080);
    }

}
