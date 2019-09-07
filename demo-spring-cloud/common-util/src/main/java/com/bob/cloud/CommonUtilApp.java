package com.bob.cloud;

import com.bob.cloud.core.FTPClientFactory;
import com.bob.cloud.core.FTPClientTemplate;
import com.bob.cloud.data.FTPConfigData;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;

/**
 * @author bobo
 * @create 2019-08-25 14:47
 */
@SpringBootApplication
@EnableScheduling
public class CommonUtilApp {

    @Resource
    private FTPConfigData ftpConfigData;

    private Logger logger = LoggerFactory.getLogger(CommonUtilApp.class);

    @Bean
    public FTPClientFactory getFtpClientFactory() {
        return new FTPClientFactory(ftpConfigData);
    }

    @Bean
    public FTPClientTemplate getFtpTemplate() {
        GenericObjectPool<FTPClient> pool = new GenericObjectPool<>(getFtpClientFactory());
        pool.setMinIdle(3);
        pool.setMaxIdle(5);
        pool.setTestOnBorrow(false);
        pool.setTestOnReturn(false);
        pool.setTestWhileIdle(true);
        pool.setTimeBetweenEvictionRunsMillis(130*1000);//间隔多久进行一次检测，检测需要关闭的空闲连接
        pool.setMaxWaitMillis(60000);//在连接池获取一个连接最长的等待时间
        pool.setMinEvictableIdleTimeMillis(300000);//一个连接在池中最小生存的时间
        try {
            pool.preparePool();//初始化FTP连接池
            logger.info("FTP Pool inited successful");
        } catch (Exception e) {
            logger.error("FTP Pool inited failed");
        }
        return new FTPClientTemplate(pool);
    }

    public static void main(String[] args){
        SpringApplication.run(CommonUtilApp.class,args);
    }

}
