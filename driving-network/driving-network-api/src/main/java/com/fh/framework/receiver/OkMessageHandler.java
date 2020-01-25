
package com.fh.framework.receiver;

import com.fh.framework.message.OkMessage;
import com.fh.framework.packet.Packet;
import com.fh.framework.receiver.BaseMessageHandler;
import com.fh.framework.session.Connection;
import com.fh.framework.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 */
public class OkMessageHandler extends BaseMessageHandler<OkMessage> {

    private static final Logger logger = LoggerFactory.getLogger(OkMessageHandler.class);


    @Override
    public OkMessage decode(Packet packet, Connection connection) {
        return new OkMessage(packet, connection);
    }

    @Override
    public void handle(OkMessage message) {
        // 若是链接A 发送给链接B 操作之后的成功状态则进行成功状态转发，否则啥都不做
        if (!StringUtils.isBlank(message.bizId)){

            // 获取匹配的链接,排除自身的id
            List<Connection> connections = getMatchConnections(message.bizId,message.connection.getId());

            if (CommonUtils.isEmptyList(connections)){
                logger.error("no match connection...bizId is :{}",message.bizId);
                return;
            }
            // 批量发送信息
            connections.forEach(connection -> connection.send(message.packet));

        } else {
            // do nothing just record message
            logger.info("success get ok message...");
        }
    }
}
