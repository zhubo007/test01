package com.bob.cloud;

import com.bob.config.MyselfRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author bobo
 * @create 2019-06-29 20:22
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.bob.cloud"})
@RibbonClient(name = "CLOUD-DEPT",configuration = MyselfRule.class)   //自定义的负载均衡规则
@ComponentScan("com.bob.cloud")
public class ApplicationStarter {

    public static void main(String[] args){
        SpringApplication.run(ApplicationStarter.class,args);
    }
}
