package com.fh.framework.message;

import com.alibaba.fastjson.JSONObject;
import com.fh.framework.packet.Packet;
import com.fh.framework.session.Connection;
import io.netty.channel.ChannelFutureListener;

import java.util.Map;

/**
 * @ClassName:BaseMessage
 * @Description: 基础消息类
 * @Author: shanzheng
 * @Date: 2019/12/18 15:50
 * @Version:1.0
 **/
public class BaseMessage implements IMessage {

    public Packet packet;

    public Connection connection;

    /* 目标链接bizId */
    public String bizId;

    // 来源链接bizId
    public String fromBizId;


    public BaseMessage() {
    }

    public BaseMessage(Packet packet, Connection connection) {
        this.packet = packet;
        this.connection = connection;
    }

    @Override
    public void decodeBodyJson() {

        JSONObject body = null;

        if (packet.flag == Packet.FLAG_JSON_BODY){

            body = packet.getBody();

        }else {

            body =  JSONObject.parseObject(new String((byte[]) packet.getBody()));
        }

        // 将json转为map
        Map<String,Object> mapBody =  JSONObject.parseObject(body.toJSONString(),Map.class);

        decodeJsonBody0(mapBody);
    }

    @Override
    public void send(ChannelFutureListener listener) {
        connection.send(packet, listener);
    }


    protected void decodeJsonBody0(Map<String,Object> body) {

        if(body.get("bizId") != null){
            this.bizId = (String) body.get("bizId");
        }

        if(body.get("fromBizId") != null){
            this.fromBizId = (String) body.get("fromBizId");
        }
    }
}
