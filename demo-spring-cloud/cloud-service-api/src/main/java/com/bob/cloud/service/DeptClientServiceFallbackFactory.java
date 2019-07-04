package com.bob.cloud.service;

import com.bob.cloud.entity.Dept;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author bobo
 * @create 2019-07-01 14:21
 * 调用异常，或者Provider被意外关闭，则会进入当前处理类，而不会去请求服务提供方
 */
@Component
public class DeptClientServiceFallbackFactory implements FallbackFactory<DeptClientService> {
    @Override
    public DeptClientService create(Throwable throwable) {
        return new DeptClientService() {
            @Override
            public boolean addDept(Dept dept) {
                return false;
            }

            @Override
            public Dept findById(Long id) {
                return new Dept().setDeptno(id)
                        .setDname("该ID"+id+"没有对应的信息,Consumer 客户端提供的降级信息，此刻Provider不可用，或者调用异常")
                        .setDb_source("no this database in mysql");
            }

            @Override
            public List<Dept> findAll() {
                return null;
            }
        };
    }
}
