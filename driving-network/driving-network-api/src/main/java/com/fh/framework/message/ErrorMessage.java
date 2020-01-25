package com.fh.framework.message;

import com.fh.framework.codec.Command;
import com.fh.framework.constant.StatusEnum;
import com.fh.framework.packet.Packet;
import com.fh.framework.session.Connection;

import java.util.Map;

/**
 * @ClassName:ErrorMessage
 * @Description: 错误信息解码和构造
 * @Author: shanzheng
 * @Date: 2019/12/19 11:00
 * @Version:1.0
 **/
public class ErrorMessage extends BaseMessage {

    /* 默认错误cmd */
    public byte cmd = Command.ERROR.cmd;

    /* 错误编码 */
    public String code;

    /* 错误原因 */
    public String reason;

    /* 返回数据 */
    public String data;

    public ErrorMessage(Packet packet, Connection connection) {
        super(packet, connection);
    }

    @Override
    protected void decodeJsonBody0(Map<String, Object> body) {
        super.decodeJsonBody0(body);
        code = (String) body.get("code");
        reason = (String) body.get("reason");
        data = (String) body.get("data");
    }

    public ErrorMessage setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public ErrorMessage setData(String data) {
        this.data = data;
        return this;
    }

    public ErrorMessage setErrorCode(StatusEnum code) {
        this.code = code.getStatus();
        this.reason = code.getMsg();
        return this;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "cmd=" + cmd +
                ", code='" + code + '\'' +
                ", reason='" + reason + '\'' +
                ", data='" + data + '\'' +
                ", bizId='" + bizId + '\'' +
                ", packet=" + packet +
                ", connection=" + connection +
                '}';
    }
}
