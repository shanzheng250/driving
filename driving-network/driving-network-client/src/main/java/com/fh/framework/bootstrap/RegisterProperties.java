package com.fh.framework.bootstrap;

import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName:RegisterProperties
 * @Description: 注册实体bean
 * @Author: shanzheng
 * @Date: 2019/12/23 9:04
 * @Version:1.0
 **/
public class RegisterProperties {
    /* 系统编号 */
    private String sysid;

    /* 地区编码 */
    private String areaCode;

    /* 用户id */
    private String userCode;

    public RegisterProperties(String sysid, String areaCode, String userCode) {
        this.sysid = sysid;
        this.areaCode = areaCode;
        this.userCode = userCode;
    }

    public String getId()  {
        StringBuilder sb = new StringBuilder();
        sb.append(sysid);

        if (!StringUtils.isBlank(areaCode)){
            sb.append("_").append(areaCode);
        }

        if (!StringUtils.isBlank(userCode)){
            sb.append("_").append(userCode);
        }
        return sb.toString();
    }

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
