package com.bob.cloud.task;

import com.bob.cloud.core.FTPBaseClient;
import com.bob.cloud.core.FTPClientTemplate;
import com.bob.cloud.core.FTPStaticClient;
import com.bob.cloud.data.FTPConfigData;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

/**
 * @author bobo
 * @create 2019-08-25 16:01
 */
@Component
public class PoolTask {

    @Resource
    private FTPConfigData configData;

    @Resource
    private FTPClientTemplate ftpClientTemplate;

    @Resource
    private FTPBaseClient baseClient;

    @Resource
    private FTPStaticClient staticClient;

    @Scheduled(fixedRate = 130 * 1000,initialDelay = 60 * 60 * 1000)
    public void poolTest(){
        File fileDir = new File(configData.getLocalPath());
        File[] files = fileDir.listFiles();
        if (files.length>0){
            for (File file : files) {
                ftpClientTemplate.uploadFile("/",file);
            }
        }
    }

    @Scheduled(fixedRate = 125 * 1000,initialDelay = 60 * 60 * 1000)
    public void baseTest(){
        File fileDir = new File(configData.getLocalPath());
        File[] files = fileDir.listFiles();
        if (files.length>0){
            FTPClient client = baseClient.getClient();
            for (File file : files) {
                baseClient.sendFileToFTPServer(client,configData.getFtpPath(),file);
            }
        }
    }

    @Scheduled(fixedRate = 2 * 1000)
    public void staticTest(){
        File fileDir = new File(configData.getLocalPath());
        File[] files = fileDir.listFiles();
        if (files.length>0){
            FTPClient client = staticClient.getClient();
            for (File file : files) {
                staticClient.sendFileToFTPServer(client,configData.getFtpPath(),file);
            }
        }
    }
}