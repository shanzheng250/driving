package com.fh.framework.codec;

import com.fh.framework.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @ClassName:PacketEncodeHandle
 * @Description: 编码请求数据包
 * @Author: shanzheng
 * @Date: 2019/12/17 10:36
 * @Version:1.0
 **/
public class PacketEncode extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) throws Exception {
        Packet.encodePacket(packet,byteBuf);
    }
}
