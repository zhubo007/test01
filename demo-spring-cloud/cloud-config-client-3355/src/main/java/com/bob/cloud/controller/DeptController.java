package com.bob.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bobo
 * @create 2019-07-03 21:34
 */
@RestController
@RequestMapping("/dept/")
public class DeptController {
    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${eureka.client.service-url.defaultZone}")
    private String eurekaServers;

    @Value("${server.port}")
    private String port;

    @GetMapping(path = "config")
    public String config(){
        String message = "applicationName: "+applicationName+"\t\t eurekaServers："+
                eurekaServers+"\t\t port:"+port;
        System.out.println("***********messgae="+message);
        return message;
    }
}
