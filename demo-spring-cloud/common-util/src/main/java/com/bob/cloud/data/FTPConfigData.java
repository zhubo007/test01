package com.bob.cloud.data;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.net.ftp.FTP;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author bobo
 * @create 2019-08-25 14:49
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "ftp.client")
public class FTPConfigData {
    private String hostname;
    private String username;
    private String password;
    private Integer port;

    /**
     * 编码
     */
    private String encoding = "UTF-8";

    /**
     * 被动模式
     */
    private boolean passiveMode = false;

    /**
     * 连接超时时间(秒)
     */
    private Integer connectTimeout;

    /**
     * 传输超时时间(秒)
     */
    private Integer dataTimeout;
    /**
     * 缓冲大小
     */
    private Integer bufferSize = 4096;

    /**
     * 设置keepAlive
     * 单位:秒  0禁用
     * Zero (or less) disables
     */
    private Integer keepAliveTimeout = 0;

    /**
     * 传输文件类型
     * in theory this should not be necessary as servers should default to ASCII
     * but they don't all do so - see NET-500
     */
    private Integer transferFileType = FTP.ASCII_FILE_TYPE;

    private String localPath;

    private String ftpPath;
}
