package com.fh.framework.message;

import com.fh.framework.packet.Packet;
import com.fh.framework.session.Connection;

import java.util.Map;

/**
 * @ClassName:ConnMessage
 * @Description: 收到消息处理
 * @Author: shanzheng
 * @Date: 2019/12/20 10:35
 * @Version:1.0
 **/
public class ExchangeMessage extends BaseMessage {

    public ExchangeMessage() {
    }

    public ExchangeMessage(Packet packet, Connection connection) {
        super(packet, connection);
    }


    @Override
    protected void decodeJsonBody0(Map<String, Object> body) {
        super.decodeJsonBody0(body);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeMessage that = (ExchangeMessage) o;
        return that.fromBizId.equals(fromBizId);
    }
}
