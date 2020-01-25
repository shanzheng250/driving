package com.fh.framework.receiver;

import com.fh.framework.message.ExchangeMessage;
import com.fh.framework.message.ServerExchangeMessage;
import com.fh.framework.packet.Packet;
import com.fh.framework.session.Connection;
import com.fh.framework.session.SessionContext;
import com.fh.framework.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @ClassName:ServerExchangeMessageHandler
 * @Description: 服务器端接收消息交换并处理转发
 * @Author: shanzheng
 * @Date: 2019/12/23 11:11
 * @Version:1.0
 **/
public class ServerExchangeMessageHandler extends ExchangeMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerExchangeMessageHandler.class);


    @Override
    protected ExchangeMessage decode(Packet packet, Connection connection) {
        return new ServerExchangeMessage(packet,connection);
    }

    @Override
    protected void handle(ExchangeMessage message) {
        ServerExchangeMessage serverExchangeMessage = (ServerExchangeMessage) message;

        // 获取要发送的标识
        String bizId = serverExchangeMessage.getBizId();

        // 获取匹配的链接,排除自身的id
        List<Connection> connections = getMatchConnections(bizId,message.connection.getId());

        if (CommonUtils.isEmptyList(connections)){
            logger.error("no match connection...bizId is :{}",bizId);
            return;
        }

        // 批量发送信息
        connections.forEach(connection -> connection.send(message.packet)
        );
    }



}
