package com.bob.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author bobo
 * @create 2019-06-29 13:08
 */
@SpringBootApplication
@EnableEurekaClient //本服务启动后自动注册金eureka服务中
@EnableDiscoveryClient //服务发现
public class ApplicationStarter {

    public static void main(String[] args){
        SpringApplication.run(ApplicationStarter.class,args);
    }

}
