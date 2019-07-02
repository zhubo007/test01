package com.bob.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author bobo
 * @create 2019-06-29 21:34
 */
@SpringBootApplication
@EnableEurekaServer //EureKaServer服务端启动类，接收其他微服务注册进来
public class ApplicationStarter {
    public static void main(String[] args){
        SpringApplication.run(ApplicationStarter.class,args);
    }
}
