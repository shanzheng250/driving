package com.fh.framework.session;

import com.fh.framework.constant.NetworkEnum;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName:BizConnection
 * @Description: 业务的连接
 * @Author: shanzheng
 * @Date: 2019/12/19 9:13
 * @Version:1.0
 **/
public class BizConnection extends Connection{

    /* 系统编号 */
    private String sysid;

    /* 地区编码 */
    private String areaCode;

    /* 用户id */
    private String userCode;

    public BizConnection(Channel channel) {
        super(channel);
    }

    public BizConnection(Channel channel, NetworkEnum networkEnum) {
        super(channel, networkEnum);
    }

    @Override
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

    public BizConnection setSysid(String sysid) {
        this.sysid = sysid;
        return this;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public BizConnection setAreaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public String getUserCode() {
        return userCode;
    }

    public BizConnection setUserCode(String userCode) {
        this.userCode = userCode;
        return this;
    }
}
