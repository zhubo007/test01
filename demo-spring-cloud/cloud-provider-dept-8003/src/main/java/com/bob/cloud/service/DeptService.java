package com.bob.cloud.service;

import com.bob.cloud.entity.Dept;

import java.util.List;

/**
 * @author bobo
 * @create 2019-06-29 13:27
 */
public interface DeptService {

    boolean add(Dept dept);

    Dept get(Long id);

    List<Dept> list();
}
