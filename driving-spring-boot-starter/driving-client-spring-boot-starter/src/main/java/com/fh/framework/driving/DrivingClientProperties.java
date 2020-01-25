package com.fh.framework.driving;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName:DrivingClientProperties
 * @Description: TODO
 * @Author: shanzheng
 * @Date: 2020/1/3 16:26
 * @Version:1.0
 **/
@ConfigurationProperties(
        prefix = "driving.client"
)
public class DrivingClientProperties {

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
