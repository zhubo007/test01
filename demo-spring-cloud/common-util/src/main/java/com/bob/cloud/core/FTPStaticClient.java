package com.bob.cloud.core;

import com.bob.cloud.data.FTPConfigData;
import com.csvreader.CsvReader;
import lombok.Getter;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author bobo
 * 始终保持一个可用的链接，使用本客户端，不建议手动关闭
 */
@Component
public class FTPStaticClient {

    private Logger logger = LoggerFactory.getLogger(FTPStaticClient.class);

    @Resource
    private FTPConfigData configData;

    @Getter
    private String localChartSet = "GBK";

    public FTPClient getClient() {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(configData.getHostname(), configData.getPort());
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                ftpClient.disconnect();
                logger.warn("ftpServer refused connection,replyCode:{}", replyCode);
                return null;
            }
            if (!ftpClient.login(configData.getUsername(), configData.getPassword())) {
                logger.warn("ftpClient login failed... username is {}; password: {}", configData.getUsername(), configData.getPassword());
                return null;
            }
            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8","ON"))){
                localChartSet = "UTF-8";
            }
            ftpClient.setBufferSize(configData.getBufferSize());
            ftpClient.setFileType(configData.getTransferFileType());
            ftpClient.setControlEncoding(localChartSet);
            if (configData.isPassiveMode()) {
                ftpClient.enterLocalPassiveMode();//被动模式。
            }
        } catch (IOException e) {
            logger.error("create ftp connection failed...", e);
        }
        return ftpClient;
    }
    /**
     * 销毁连接
     */
    public void destroyObject(FTPClient client) {
        try {
            client.logout();
        } catch (IOException e) {
            logger.warn(e.getMessage());
        } finally {
            try {
                if (client.isConnected()) {
                    client.disconnect();
                }
            } catch (IOException io) {
                logger.error("destroyObject close ftp client failed...{}", io);
            }
        }
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
    /**
     * 上传文件到FTP，同名文件有同名文件不上传
     * @param remotePath    服务器的位置
     * @param file  要传输的文件
     * @return  是否成功
     */
    public boolean sendFileToFTPServer(FTPClient client,String remotePath, File file) {
        boolean isSuccess = false;
        if (this.validateObject(client)){
            try (FileInputStream inputStream = new FileInputStream(file)){
                client.changeWorkingDirectory(remotePath);
                String fileName = file.getName();
                isSuccess = client.storeFile(new String(fileName.getBytes(localChartSet), StandardCharsets.ISO_8859_1), inputStream);
                logger.info("{} has been send to FTP Server successful",fileName);
            }catch (IOException e){
                logger.error("{}, error={}",FTPStaticClient.class,e.getMessage());
            }
        }
        return isSuccess;
    }

    /**
     * 下载FTP文件到本地
     * @param ftpFileName  FTP的文件名
     * @param localFileName 需要存储在本地的文件地址，带文件名
     * @param ftpPath   FTP Server目录位置
     */
    public void saveFTPFile2Local(FTPClient client, String ftpPath, String ftpFileName,String localFileName){
        File file = new File(localFileName);
        try (FileOutputStream outputStream = new FileOutputStream(file)){
            client.changeWorkingDirectory(ftpPath);
            client.retrieveFile(ftpFileName,outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取FTP上文件的文件流，并处理，以CSV为例
     * @param ftpFileName FTP文件名称
     */
    public List<Map<String, Object>> handleFTPFileStream(FTPClient client, String ftpFileName){
        String[] strArray = ftpFileName.split("\\.");
        String suffixName = strArray[strArray.length - 1];// 获取文件后缀名，没有后缀的，获取到的是文件名
        List<Map<String, Object>> list = new ArrayList<>();
        if ("csv".equals(suffixName)) {
            try (InputStream inputStream = client.retrieveFileStream(ftpFileName)){
                CsvReader csvReader = new CsvReader(inputStream, ',', Charset.forName("utf-8"));
                List<String> titleData = CSVUtil.readCsvTitleData(csvReader);
                list = CSVUtil.readCsvData(csvReader,titleData);
            } catch (IOException e) {
                logger.error("File Stream handle failed, error = {}",e.getCause());
            }finally {
                try {
                    client.completePendingCommand();// 在多次获取输入流时，一定要在关闭输入流后执行此操作，否则会在第一次获取成功后获取到空的输入流
                } catch (IOException e) {
                    logger.error("completePendingCommand failed error = {}",e.getCause());
                }
            }
        }
        return list;
    }
}