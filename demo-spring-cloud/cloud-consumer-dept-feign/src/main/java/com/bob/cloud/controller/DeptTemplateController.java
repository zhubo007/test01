package com.bob.cloud.controller;

import com.bob.cloud.entity.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author bobo
 * @create 2019-06-29 20:29
 */
@RestController
@RequestMapping("/template/dept/")
public class DeptTemplateController {

    private static final String REST_URL_PREFIX = "http://CLOUD-DEPT";

    @Autowired
    private RestTemplate restTemplate;
    /**
     *
     * (url,requestMap,ResponseBean.class)这三个参数分别代表Rest请求地址，请求参数，Http相应转换被转换成的对象类型
     *
     */
    @PostMapping(path = "add")
    public boolean add(@RequestBody Dept dept) {
        return restTemplate.postForObject(REST_URL_PREFIX+"dept/add",dept,Boolean.class);
    }

    @GetMapping(path = "get/{id}")
    public Dept get(@PathVariable("id") Long id){
        return restTemplate.getForObject(REST_URL_PREFIX+"/dept/get/"+id,Dept.class);
    }

    @GetMapping(path = "list")
    public List<Dept> list(){
        return restTemplate.getForObject(REST_URL_PREFIX+"/dept/list",List.class);
    }

    @GetMapping(path = "discovery")
    public Object discovery(){
        return restTemplate.getForObject(REST_URL_PREFIX+"/dept/discovery",Object.class);
    }
}
