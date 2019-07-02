package com.bob.cloud;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

/**
 * @author bobo
 * @create 2019-06-29 13:08
 */
@SpringBootApplication
@EnableEurekaClient //本服务启动后自动注册金eureka服务中
@EnableDiscoveryClient //服务发现
@EnableCircuitBreaker //启用熔断器
public class ApplicationStarter {
    /**
     * 要监测当前模块的使用情况需要注入以下Bean
     * @return
     */
    @Bean
    public ServletRegistrationBean<HystrixMetricsStreamServlet> getServlet(){
        HystrixMetricsStreamServlet hystrixMetricsStreamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean<HystrixMetricsStreamServlet> servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(hystrixMetricsStreamServlet);
        servletRegistrationBean.addUrlMappings("/actuator/hystrix.stream");
        servletRegistrationBean.setName("HystrixMetricsStreamServlet");
        return servletRegistrationBean;
    }

    public static void main(String[] args){
        SpringApplication.run(ApplicationStarter.class,args);
    }

}
