package com.bob.cloud.service;

import com.bob.cloud.entity.Dept;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author bobo
 * @create 2019-07-01 1:30
 * cloud-dept
 */
//@FeignClient(value = "CLOUD-DEPT")
@FeignClient(value = "CLOUD-DEPT",fallbackFactory = DeptClientServiceFallbackFactory.class)
public interface DeptClientService {

    @PostMapping(path = "/dept/add")
    boolean addDept(@RequestBody Dept dept);

    @GetMapping(path = "/dept/get/{id}")
    Dept findById(@PathVariable("id") Long id);

    @GetMapping(path = "/dept/list")
    List<Dept> findAll();
}
