package com.bob.cloud.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * @author bobo
 * @create 2019-07-04 1:53
 * Zuul过滤器
 */
@Component
public class CloudZuulFilter extends ZuulFilter {
    /**
     * 类型包含 pre post route error
     * pre 代表在路由代理之前执行
     * route 代表代理的时候执行
     * error 代表出现错的时候执行
     * post 代表在route 或者是 error 执行完成后执行
     * @return 类型
     */
    @Override
    public String filterType() {
        return FilterConstants.ERROR_TYPE;
    }
    /**
     * filter 为链式过滤器，多个filter按顺序执行
     * javaee中根据filter先后配置顺序决定
     * zuul 根据order决定, 由小到大
     * @return
     */
    @Override
    public int filterOrder() {
        return 1;
    }
    /**
     * 是否启用该过滤器
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println("CloudZuulFilter error!");
        return null;
    }
}
