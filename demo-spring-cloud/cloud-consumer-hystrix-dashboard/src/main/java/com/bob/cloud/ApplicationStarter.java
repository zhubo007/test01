package com.bob.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * @author bobo
 * @create 2019-06-29 20:22
 */
@SpringBootApplication
@EnableHystrixDashboard
public class ApplicationStarter {

    public static void main(String[] args){
        SpringApplication.run(ApplicationStarter.class,args);
    }
}
