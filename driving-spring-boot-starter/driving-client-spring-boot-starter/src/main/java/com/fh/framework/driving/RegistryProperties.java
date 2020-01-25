package com.fh.framework.driving;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName:RegistryProperties
 * @Description: 注册服务对象
 * @Author: shanzheng
 * @Date: 2020/1/3 16:26
 * @Version:1.0
 **/
@ConfigurationProperties(
        prefix = "driving.registry"
)
public class RegistryProperties {
    /* 系统编号 */
    private String sysid;

    /* 地区编码 */
    private String areaCode;

    /* 用户id */
    private String userCode;

    public String getSysid() {
        return sysid;
    }

    public void setSysid(String sysid) {
        this.sysid = sysid;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}
