package com.bob.cloud;

import com.bob.config.MyselfRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

/**
 * @author bobo
 * @create 2019-06-29 20:22
 */
@SpringBootApplication
@EnableEurekaClient
//在启动该微服务的时候就能去加载我们的自定义Ribbon配置类，从而是配置生效
@RibbonClient(name = "CLOUD-DEPT",configuration = MyselfRule.class)   //自定义的负载均衡规则
public class ApplicationStarter {

    public static void main(String[] args){
        SpringApplication.run(ApplicationStarter.class,args);
    }
}
