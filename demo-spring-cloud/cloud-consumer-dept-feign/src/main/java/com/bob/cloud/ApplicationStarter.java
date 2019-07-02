package com.bob.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author bobo
 * @create 2019-06-29 20:22
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.bob.cloud"})
@ComponentScan("com.bob.cloud")
public class ApplicationStarter {

    public static void main(String[] args){
        SpringApplication.run(ApplicationStarter.class,args);
    }
}
