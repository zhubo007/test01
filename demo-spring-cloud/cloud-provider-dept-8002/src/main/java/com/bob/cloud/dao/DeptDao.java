package com.bob.cloud.dao;

import com.bob.cloud.entity.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author bobo
 * @create 2019-06-29 13:09
 */
@Mapper
@Repository
public interface DeptDao{

    boolean addDept(Dept dept);

    Dept findById(Long id);

    List<Dept> findAll();
}
