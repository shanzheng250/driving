package com.fh.framework.config;
import static com.fh.framework.config.DefaultConfig.*;
/**
 * @ClassName:RegisterConfig
 * @Description: 注册配置
 * @Author: shanzheng
 * @Date: 2019/12/30 11:18
 * @Version:1.0
 **/
public class RegisterConfig {

    /* 系统标识 */
    private String sysid;

    /* 地区编码 */
    private String areaCode;

    /* 用户编码 */
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

    /**
     * 默认初始化对象
     * @return
     */
    public static RegisterConfig getDefaultConfig(){
        RegisterConfig registerConfig = new RegisterConfig();
        registerConfig.setSysid(DEFAULT_SYSID);
        registerConfig.setAreaCode(DEFAULT_AREACODE);
        registerConfig.setUserCode(DEFAULT_USERCODE);
        return registerConfig;
    }
}
