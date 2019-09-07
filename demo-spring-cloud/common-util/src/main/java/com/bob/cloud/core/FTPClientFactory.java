package com.bob.cloud.core;

import com.bob.cloud.data.FTPConfigData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.io.IOException;

/**
 * @author bobo
 * @create 2019-08-25 15:08
 */
@Slf4j
public class FTPClientFactory extends BasePooledObjectFactory<FTPClient> {
    private FTPConfigData configData;

    public FTPClientFactory(FTPConfigData ftpConfigData) {
        this.configData = ftpConfigData;
    }

    /**
     * 创建FtpClient对象
     */
    @Override
    public FTPClient create() {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding(configData.getEncoding());
        ftpClient.setDataTimeout(configData.getDataTimeout());
        ftpClient.setConnectTimeout(configData.getConnectTimeout());
        ftpClient.setControlKeepAliveTimeout(configData.getKeepAliveTimeout());
        try {
            ftpClient.connect(configData.getHostname(), configData.getPort());
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                ftpClient.disconnect();
                log.warn("ftpServer refused connection,replyCode:{}", replyCode);
                return null;
            }

            if (!ftpClient.login(configData.getUsername(), configData.getPassword())) {
                log.warn("ftpClient login failed... username is {}; password: {}", configData.getUsername(), configData.getPassword());
            }

            ftpClient.setBufferSize(configData.getBufferSize());
            ftpClient.setFileType(configData.getTransferFileType());
            if (configData.isPassiveMode()) {
                ftpClient.enterLocalPassiveMode();//被动模式。
            }

        } catch (IOException e) {
            log.error("create ftp connection failed...", e.getMessage());
        }
        return ftpClient;
    }

    /**
     * 用PooledObject封装对象放入池中
     */
    @Override
    public PooledObject<FTPClient> wrap(FTPClient ftpClient) {
        return new DefaultPooledObject<>(ftpClient);
    }

    /**
     * 销毁FtpClient对象
     */
    @Override
    public void destroyObject(PooledObject<FTPClient> ftpPooled) {
        if (ftpPooled == null) {
            return;
        }
        FTPClient ftpClient = ftpPooled.getObject();
        try {
            ftpClient.logout();
        } catch (IOException io) {
            log.error("ftp client logout failed...{}", io.getMessage());
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
            } catch (IOException io) {
                log.error("close ftp client failed...{}", io.getMessage());
            }
        }
    }

    /**
     * 验证FtpClient对象
     */
    @Override
    public boolean validateObject(PooledObject<FTPClient> ftpPooled) {
        try {
            FTPClient ftpClient = ftpPooled.getObject();
            return ftpClient.sendNoOp();
        } catch (IOException e) {
            log.error("failed to validate client: {}", e.getMessage());
        }
        return false;
    }
}
