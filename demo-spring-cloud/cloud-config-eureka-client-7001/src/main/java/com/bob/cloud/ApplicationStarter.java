package com.bob.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author bobo
 * @create 2019-06-29 20:22
 */
@SpringBootApplication
@EnableEurekaServer
public class ApplicationStarter {

    public static void main(String[] args){
        SpringApplication.run(ApplicationStarter.class,args);
    }
}
