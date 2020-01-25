package com.fh.framework.codec;

import com.alibaba.fastjson.JSONObject;
import com.fh.framework.packet.JsonPacket;
import com.fh.framework.packet.Packet;
import com.fh.framework.utils.JsonsUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @ClassName:PacketDecodeHandle
 * @Description: 解码处理器
 * @Author: shanzheng
 * @Date: 2019/12/16 14:34
 * @Version:1.0
 **/
public class PacketDecode extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        decodeHeartbeat(in, out);
        decodeFrames(in, out);
    }

    /**
     * 批量解码
     * @param in
     * @param list
     * @return
     */
    private void decodeFrames(ByteBuf in, List<Object> list){
        if (in.readableBytes() >= Packet.HEADER_LEN){
            // 标记读取位置
            in.markReaderIndex();

            Packet packet = Packet.decodePacket(in);

            if (packet != null){
                list.add(packet);
            }else {
                // 重置上次位置
                in.resetReaderIndex();
            }
        }
    }

    /**
     * 针对ws的传值解析，默认传值为json
     * @param frame
     * @return
     * @throws Exception
     */
    public static Packet decodeFrame(String frame) throws Exception {
        if (frame == null) return null;

        // 这里默认必须传值为json字符串
        JSONObject jsonObject = JSONObject.parseObject(frame);

        byte flag = jsonObject.getByte("flag");
        byte cmd = jsonObject.getByte("cmd");
        String body = jsonObject.getString("body");
        // todo 这边省略了sessionid的解析
        Packet packet;
        if (flag == Packet.FLAG_JSON_BODY){
            packet = new JsonPacket(cmd);
            packet.setBody(body);
        } else {
            // byte body数组的解析
            packet = JsonsUtils.fromJson(frame, Packet.class);
        }
        return packet;
    }

    /**
     * 心跳包
     * @param in
     * @param out
     */
    private void decodeHeartbeat(ByteBuf in, List<Object> out) {
        while (in.isReadable()) {
            if (in.readByte() == Packet.HB_PACKET_BYTE) {
                out.add(Packet.HB_PACKET);
            } else {
                in.readerIndex(in.readerIndex() - 1);
                break;
            }
        }
    }
}
