package com.fh.framework.config;

/**
 * @ClassName:BaseConfig
 * @Description: 基础配置
 * @Author: shanzheng
 * @Date: 2019/12/30 11:19
 * @Version:1.0
 **/
public class BaseConfig {

    /* 服务ip */
    private String host;

    /* 服务端口 */
    private int port;

    /* 服务类型 */
    private String netType;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }
}
