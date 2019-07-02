package com.bob.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author bobo
 * @create 2019-06-30 17:52
 * 自定义的规则不能与启动类在同一目录下
 */
@Configuration
public class MyselfRule  {

    @Bean
    public IRule myRule(){
        //return new RandomRule();//Ribbon默认是轮询，我自定义为随机
        return new RandomRuleReConfig();
    }
}
