package com.bob.cloud.controller;

import com.bob.cloud.entity.Dept;
import com.bob.cloud.service.DeptClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author bobo
 * @create 2019-06-29 20:29
 */
@RestController
@RequestMapping("/consumer/dept/")
public class DeptConsumerController {


    @Autowired
    private DeptClientService service;
    /**
     *
     * (url,requestMap,ResponseBean.class)这三个参数分别代表Rest请求地址，请求参数，Http相应转换被转换成的对象类型
     *
     */
    @PostMapping(path = "add")
    public boolean add(@RequestBody Dept dept) {
        return this.service.addDept(dept);
    }

    @GetMapping(path = "get/{id}")
    public Dept get(@PathVariable("id") Long id){
        return this.service.findById(id);
    }

    @GetMapping(path = "list")
    public List<Dept> list(){
        return this.service.findAll();
    }

}
