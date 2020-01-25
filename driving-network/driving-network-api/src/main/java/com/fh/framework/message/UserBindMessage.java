package com.fh.framework.message;

import com.fh.framework.packet.Packet;
import com.fh.framework.session.Connection;

import java.util.Map;

/**
 * @ClassName:UserBindMessage
 * @Description: 用户服务注册,业务强相关，绑定到这边统一的传递格式字段
 * @Author: shanzheng
 * @Date: 2019/12/18 15:57
 * @Version:1.0
 **/
public class UserBindMessage extends BaseMessage{

    /* 系统编号 */
    private String sysid;

    /* 地区编码 */
    private String areaCode;

    /* 用户id */
    private String userCode;

    /* 模块名称 */
    private String module;

    public UserBindMessage(Packet packet, Connection connection) {
        super(packet, connection);
    }


    @Override
    protected void decodeJsonBody0(Map<String, Object> body) {
        sysid = (String) body.get("sysid");
        areaCode = (String) body.get("areaCode");
        userCode = (String) body.get("userCode");
        module = (String) body.get("module");
    }

    public String getSysid() {
        return sysid;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public String getModule() {
        return module;
    }
}
