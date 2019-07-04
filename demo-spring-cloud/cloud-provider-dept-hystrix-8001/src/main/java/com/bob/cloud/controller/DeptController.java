package com.bob.cloud.controller;

import com.bob.cloud.entity.Dept;
import com.bob.cloud.service.DeptService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
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
    @HystrixCommand(fallbackMethod = "processHystrix_Get")
    public Dept list(@PathVariable("id") Long id){
        Dept dept = deptService.get(id);
        if (null == dept){
            throw new RuntimeException("");
        }
        return deptService.get(id);
    }

    @GetMapping(path = "/list")
    public List<Dept> list(){
        return deptService.list();
    }

    public Dept processHystrix_Get(@PathVariable("id") Long id){
        return new Dept().setDeptno(id)
                .setDname("该ID"+id+"没有对应的信息,null-->@HystrixCommand")
                .setDb_source("no this database in mysql");
    }
}
