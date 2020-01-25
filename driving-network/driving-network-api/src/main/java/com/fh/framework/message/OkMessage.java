package com.fh.framework.message;

import com.fh.framework.codec.Command;
import com.fh.framework.packet.Packet;
import com.fh.framework.session.Connection;

import java.util.Map;

/**
 * @ClassName:OkMessage
 * @Description: TODO
 * @Author: shanzheng
 * @Date: 2019/12/25 9:21
 * @Version:1.0
 **/
public class OkMessage extends BaseMessage {

    /* 默认成功cmd */
    public byte cmd = Command.OK.cmd;

    /* 返回数据 */
    public String data;

    public OkMessage(Packet packet, Connection connection) {
        super(packet, connection);
    }

    @Override
    protected void decodeJsonBody0(Map<String, Object> body) {
        super.decodeJsonBody0(body);
        data = (String) body.get("data");
    }

    public byte getCmd() {
        return cmd;
    }

    public void setCmd(byte cmd) {
        this.cmd = cmd;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    @Override
    public String toString() {
        return "OkMessage{" +
                "cmd=" + cmd +
                ", data='" + data + '\'' +
                ", bizId='" + bizId + '\'' +
                '}';
    }
}
