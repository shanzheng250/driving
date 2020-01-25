package com.fh.framework.handle;

import com.alibaba.fastjson.JSONObject;
import com.fh.framework.bootstrap.ExchangeContainer;
import com.fh.framework.codec.Command;
import com.fh.framework.message.ErrorMessage;
import com.fh.framework.message.OkMessage;
import com.fh.framework.packet.Packet;
import com.fh.framework.session.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName:BaseClientChannelHandler
 * @Description: 客户端消息处理
 * @Author: shanzheng
 * @Date: 2019/12/20 11:04
 * @Version:1.0
 **/
public class BaseClientChannelHandler {

    private static final Logger logger = LoggerFactory.getLogger(BaseClientChannelHandler.class);

    private BaseClientChannelHandler() {
    }

    /**
     * 处理消息
     * @param connection
     * @param packet
     * @throws Exception
     */
    public static void handleMessage(Connection connection, Packet packet, ExchangeContainer container){
        // 获取服务器返回客户端信息
        Command command = Command.toCMD(packet.cmd);
        switch (command){
            case PUSH:
                push(packet,connection,container);
                break;
            case ERROR:
                ErrorMessage message = new ErrorMessage(packet, connection);
                message.decodeBodyJson();
                logger.error("receive error code,receive an error packet=" + message.toString());
                break;
            case OK:
                OkMessage okMessage = new OkMessage(packet, connection);
                okMessage.decodeBodyJson();
                logger.info("receive ok code,receive an ok packet=" + okMessage.toString());
                break;
            case UNKNOWN:
                logger.info("receive unknown code,receive an ok packet=" + packet);
                break;
        }
    }

    /**
     * 消息处理
     * @param packet
     * @param connection
     */
    private static void push(Packet packet, Connection connection, ExchangeContainer container){
        logger.debug("receive package={}, chanel={}", packet, connection.getChannel());

        JSONObject body = null;

        if (packet.flag == Packet.FLAG_JSON_BODY){

            body = packet.getBody();

        }else {

            body =  JSONObject.parseObject(new String((byte[]) packet.getBody()));
        }

        if (body == null){
            logger.error("no body...packet is {}",packet);
            return;
        }
        // 调用策略处理类
        container.getHandleByStrategy((String) body.get("strategy")).handle(packet,connection);
    }
}
