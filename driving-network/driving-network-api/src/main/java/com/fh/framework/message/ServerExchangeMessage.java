package com.fh.framework.message;

import com.fh.framework.packet.Packet;
import com.fh.framework.session.Connection;

import java.util.Map;

/**
 * @ClassName:ServerExchangeMessage
 * @Description: 服务器端消息
 * @Author: shanzheng
 * @Date: 2019/12/23 11:15
 * @Version:1.0
 **/
public class ServerExchangeMessage extends ExchangeMessage {


    /* 策略名称 */
    private String strategyKey;

    /* body */
    private Map<String,Object> body;

    public ServerExchangeMessage(Packet packet, Connection connection) {
        super(packet, connection);
    }

    public ServerExchangeMessage() {
    }


    @Override
    protected void decodeJsonBody0(Map<String, Object> body) {
        super.decodeJsonBody0(body);
        this.strategyKey = (String) body.get("strategy");
        this.body = body;
    }

    public String getBizId() {
        return bizId;
    }

    public String getStrategyKey() {
        return strategyKey;
    }

    public Map<String, Object> getBody() {
        return body;
    }
}
