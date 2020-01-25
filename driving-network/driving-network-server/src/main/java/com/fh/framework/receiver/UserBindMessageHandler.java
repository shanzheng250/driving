package com.fh.framework.receiver;

import com.alibaba.fastjson.JSONObject;
import com.fh.framework.codec.Command;
import com.fh.framework.message.OkMessage;
import com.fh.framework.message.UserBindMessage;
import com.fh.framework.packet.JsonPacket;
import com.fh.framework.packet.Packet;
import com.fh.framework.session.BizConnection;
import com.fh.framework.session.Connection;
import com.fh.framework.session.SessionContext;
import com.fh.framework.utils.JsonsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName:UserBindMessageHandler
 * @Description: 链接处理
 * @Author: shanzheng
 * @Date: 2019/12/19 10:32
 * @Version:1.0
 **/
public class UserBindMessageHandler extends BaseMessageHandler<UserBindMessage> {

    private static final Logger logger = LoggerFactory.getLogger(UserBindMessageHandler.class);


    @Override
    public UserBindMessage decode(Packet packet, Connection connection) {
        return new UserBindMessage(packet,connection);
    }

    @Override
    public void handle(UserBindMessage message) {
        // 转换为业务标准链接
        BizConnection connection = new BizConnection(message.connection.getChannel(),message.connection.getNetworkEnum());;
        try {
            connection.setAreaCode(message.getAreaCode())
                    .setSysid(message.getSysid())
                    .setUserCode(message.getUserCode());

            SessionContext.getSessionInstance().put(connection);
            // 回写消息
            Packet packet = new JsonPacket(Command.OK);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data","bind user success");
            packet.setBody(jsonObject.toJSONString());
            connection.send(packet);

        } catch (Exception e) {
            logger.error("bind user error",e);
            Packet packet = new JsonPacket(Command.ERROR);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("reason","bind user error");
            packet.setBody(jsonObject.toJSONString());
            connection.send(packet);
        }

    }
}
