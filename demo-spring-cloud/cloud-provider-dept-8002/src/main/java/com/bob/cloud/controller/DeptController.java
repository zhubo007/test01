package com.bob.cloud.controller;

import com.bob.cloud.entity.Dept;
import com.bob.cloud.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author bobo
 * @create 2019-06-29 13:31
 */
@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    DeptService deptService;

    @Autowired
    private DiscoveryClient client;

    @PostMapping(path = "/add")
    public boolean add(@RequestBody Dept dept){
        return deptService.add(dept);
    }

    @GetMapping(path = "/get/{id}")
    public Dept list(@PathVariable("id") Long id){
        return deptService.get(id);
    }

    @GetMapping(path = "/list")
    public List<Dept> list(){
        return deptService.list();
    }

    @GetMapping(path = "/discovery")
    public Object discovery(){
        List<String> services = client.getServices();
        System.out.println(services);
        List<ServiceInstance> instances = client.getInstances("CLOUD-DEPT");

        instances.forEach(element->
                System.out.println(element.getServiceId()+"\t"+element.getHost()+"\t"
                +element.getPort()+"\t"+element.getUri())
        );
        return this.client;
    }
}
