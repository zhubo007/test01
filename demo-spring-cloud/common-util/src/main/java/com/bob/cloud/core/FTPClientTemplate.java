package com.bob.cloud.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObjectInfo;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import sun.net.ftp.FtpClient;

import java.io.*;
import java.util.Set;

/**
 * 目前只支持单个客户端，同一时刻，使用一个客户端会报错
 * @author bobo
 * @create 2019-08-25 15:07
 */
@Slf4j
public class FTPClientTemplate {

    private GenericObjectPool<FTPClient> ftpClientPool;

    private Logger logger = LoggerFactory.getLogger(FTPClientTemplate.class);

    @Autowired
    private FTPBaseClient baseClient;

    public FTPClientTemplate(GenericObjectPool<FTPClient> pool) {
        this.ftpClientPool = pool;
    }

    @Scheduled(fixedRate = 115 * 1000)
    public void heartBeat(){
        try {
            ftpClientPool.clear();
            ftpClientPool.preparePool();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean sendFileToFTPServer(String remotePath, File file) {
        boolean isSuccess = false;
        FTPClient client = null;
        try (FileInputStream inputStream = new FileInputStream(file)){
            client = ftpClientPool.borrowObject();
            client.changeWorkingDirectory(remotePath);
            String fileName = file.getName();
            isSuccess = client.storeFile(new String(fileName.getBytes(baseClient.getLocalChartSet()), "UTF-8"), inputStream);
            logger.info("{} has been send to FTP Server successful",fileName);
        } catch (Exception e) {
            logger.error("{}, error={}",FTPClientTemplate.class,e.getMessage());
        } finally {
            if (null!=client){
                //将对象放回池中
                ftpClientPool.returnObject(client);
            }
        }
        return isSuccess;
    }
    /***
     * 上传Ftp文件
     *
     * @param file 当地文件
     * @param remotePath 上传服务器路径 - 应该以/结束
     * @return true or false
     */
    public boolean uploadFile(String remotePath, File file) {
        FTPClient client = null;
        try(FileInputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream inStream = new BufferedInputStream(fileInputStream)) {
            //从池中获取对象
            client = ftpClientPool.borrowObject();
            if (!validateObject(client)){
                ftpClientPool.invalidateObject(client);
                Set<DefaultPooledObjectInfo> allObjects = ftpClientPool.listAllObjects();
                FTPClientFactory factory = (FTPClientFactory)ftpClientPool.getFactory();
                client = factory.create();
                factory.wrap(client);
            }
            // 改变工作路径
            client.changeWorkingDirectory(remotePath);
            log.info("start upload... {}", file.getName());
            final int retryTimes = 3;
            for (int j = 0; j <= retryTimes; j++) {
                boolean success = client.storeFile(file.getName(), inStream);
                if (success) {
                    log.info("upload file success! {}", file.getName());
                    return true;
                }
                log.warn("upload file failure! try uploading again... {} times", j);
            }
        } catch (FileNotFoundException e) {
            log.error("file not found!{}", file);
        } catch (Exception e) {
            log.error("upload file failure!", e);
        } finally {
            //将对象放回池中
            ftpClientPool.returnObject(client);
        }
        return false;
    }

    /**
     * 下载文件
     *
     * @param remotePath FTP服务器文件目录
     * @param fileName   需要下载的文件名称
     * @param localPath  下载后的文件路径
     * @return true or false
     */
    public boolean downloadFile(String remotePath, String fileName, String localPath) {
        FTPClient ftpClient = null;
        OutputStream outputStream = null;
        try {
            ftpClient = ftpClientPool.borrowObject();
            // 切换FTP目录
            ftpClient.changeWorkingDirectory(remotePath);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (FTPFile file : ftpFiles) {
                if (fileName.equalsIgnoreCase(file.getName())) {
                    File localFile = new File(localPath + File.separator + file.getName());
                    outputStream = new FileOutputStream(localFile);
                    ftpClient.retrieveFile(file.getName(), outputStream);
                }
            }
            ftpClient.logout();
            return true;
        } catch (Exception e) {
            log.error("download file failure!", e);
        } finally {
            if (null!=outputStream){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ftpClientPool.returnObject(ftpClient);
        }
        return false;
    }

    /**
     * 删除文件
     *
     * @param remotePath FTP服务器保存目录
     * @param fileName   要删除的文件名称
     * @return true or false
     */
    public boolean deleteFile(String remotePath, String fileName) {
        FTPClient ftpClient = null;
        try {
            ftpClient = ftpClientPool.borrowObject();
            // 切换FTP目录
            ftpClient.changeWorkingDirectory(remotePath);
            boolean file = ftpClient.deleteFile(fileName);
            log.debug("delete file reply code:{}", file);
            return true;
        } catch (Exception e) {
            log.error("delete file failure!", e);
        } finally {
            ftpClientPool.returnObject(ftpClient);
        }
        return false;
    }

    /**
     * 判断连接是否为有效连接
     * @return  是否有效
     */
    private boolean validateObject(FTPClient client) {
        try {
            return client.sendNoOp();
        } catch (IOException e) {
            logger.warn("failed to validate client: {}", e.getMessage());
        }
        return false;
    }


}
