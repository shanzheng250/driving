
package com.fh.framework.packet;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fh.framework.codec.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;


public final class JsonPacket extends Packet {

    private static final Logger logger = LoggerFactory.getLogger(JsonPacket.class);

    private JSONObject jsonBody;

    public JsonPacket(byte cmd) {
        super(cmd);
        this.addFlag(FLAG_JSON_BODY);
    }

    public JsonPacket(Command cmd) {
        super(cmd);
        this.addFlag(FLAG_JSON_BODY);
    }

    @Override
    public JSONObject getBody() {
        return jsonBody;
    }

    /**
     * 默认传值为string
     * @param body
     * @param <T>
     */
    @Override
    public <T> void setBody(T body) {

        try {
            jsonBody = JSON.parseObject((String) body);
        } catch (Exception e) {
            logger.info("set body error,cause transform json error,body:{}",body);
        }

        if (jsonBody == null){
            return;
        }
        super.setBody(jsonBody.toJSONString().getBytes());
    }



    public void addFlag(byte flag) {
        this.flag = flag;
    }

//    public Object toFrame(Channel channel) {
//        byte[] json = Json.JSON.toJson(this).getBytes(Constants.UTF_8);
//        return new TextWebSocketFrame(Unpooled.wrappedBuffer(json));
//    }


    public String toJsonStr() {
        JSONObject object = new JSONObject();
        object.put("body",jsonBody);
        object.put("cmd",cmd);
        object.put("flag",flag);
        return object.toJSONString();
    }


    @Override
    public String toString() {
        return "JsonPacket{" +
                "jsonBody=" + jsonBody +
                ", cmd=" + cmd +
                ", flag=" + flag +
                ", sessionId=" + Arrays.toString(sessionId) +
                '}';
    }
}
