package com.bob.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RetryRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author bobo
 * @create 2019-06-30 17:52
 * 自定义的规则不能与启动类在同一目录下
 */
@Configuration
public class MyselfRule  {

    @Bean
    public IRule myRule(){
//        return new RoundRobinRule();//默认的轮询
//        return new RandomRule();//达到的目的，用我们重新选择的随机算法代替默认的轮询算法
        return new RandomRuleReConfig();
    }

    /**
     * 模板也定义为负载均衡
     * @return
     */
    @Bean(name = "restTemplate")
    @LoadBalanced   //Spring Cloud Ribbon是基于Netflix Ribbon实现的一套客户端  负载均衡的工具
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
