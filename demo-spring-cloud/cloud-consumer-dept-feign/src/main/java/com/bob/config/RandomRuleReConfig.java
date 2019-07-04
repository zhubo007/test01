package com.bob.config;


import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author bobo
 * @create 2019-06-30 23:52
 * 轮询，每台机器5次
 */
public class RandomRuleReConfig extends AbstractLoadBalancerRule {
    //total=0 ,当total=5以后，我们指针才能往下走
    //index=0,当前对外提供服务的服务器地址
    //total需要重新置为零，但是已经达到过一个5次，我们的index=1
    private AtomicInteger total = new AtomicInteger(0);//总共被调用的次数，目前要求每台被调用5次
    private AtomicInteger currentIndex = new AtomicInteger(0);//当前提供服务的机器号

    /**
     * Randomly choose from all living servers
     */
//    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "RCN_REDUNDANT_NULLCHECK_OF_NULL_VALUE")
    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        }
        Server server = null;

        while (server == null) {
            if (Thread.interrupted()) {
                return null;
            }
            List<Server> upList = lb.getReachableServers();
            List<Server> allList = lb.getAllServers();

            int serverCount = allList.size();
            if (serverCount == 0) {
                /*
                 * No servers. End regardless of pass, because subsequent passes
                 * only get more restrictive.
                 */
                return null;
            }

//            int index = chooseRandomInt(serverCount);
//            server = upList.get(index);
            if (total.intValue()<5){
                server = upList.get(currentIndex.intValue());
                total.incrementAndGet();
            }else {
                total.set(0);
                currentIndex.incrementAndGet();
                if (currentIndex.intValue()>=upList.size()){
                    currentIndex.set(0);
                }
            }

            if (server == null) {
                /*
                 * The only time this should happen is if the server list were
                 * somehow trimmed. This is a transient condition. Retry after
                 * yielding.
                 */
                Thread.yield();
                continue;
            }

            if (server.isAlive()) {
                return (server);
            }

            // Shouldn't actually happen.. but must be transient or a bug.
            server = null;
            Thread.yield();
        }

        return server;

    }

    protected int chooseRandomInt(int serverCount) {
        return ThreadLocalRandom.current().nextInt(serverCount);
    }

    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        // TODO Auto-generated method stub

    }
}