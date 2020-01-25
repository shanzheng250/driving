package com.fh.framework.receiver;

import com.fh.framework.message.ErrorMessage;
import com.fh.framework.packet.Packet;
import com.fh.framework.receiver.BaseMessageHandler;
import com.fh.framework.session.Connection;
import com.fh.framework.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @ClassName:ErrorMessageHandler
 * @Description: 接收到错误信息返回码
 * @Author: shanzheng
 * @Date: 2019/12/19 14:11
 * @Version:1.0
 **/
public class ErrorMessageHandler extends BaseMessageHandler<ErrorMessage> {

    private static final Logger logger = LoggerFactory.getLogger(ErrorMessageHandler.class);


    @Override
    public ErrorMessage decode(Packet packet, Connection connection) {
        return new ErrorMessage(packet,connection);
    }

    @Override
    public void handle(ErrorMessage message) {
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
            logger.error("接收错误信息，原因是{}",message.reason);
        }
    }

}
