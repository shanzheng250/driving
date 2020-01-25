package com.fh.framework.packet;

import com.fh.framework.codec.Command;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;

/**
 * @ClassName:Packet
 * @Description: 消息包
 * @Author: shanzheng
 * @Date: 2019/12/16 10:17
 * @Version:1.0
 **/
public class Packet {
    /* 命令 */
    public byte cmd;

    /* 0:byte 1:json */
    public byte flag = 0;

    /* sessionid */
    transient public byte [] sessionId;

    /* 请求内容 */
    transient private byte [] body;

    /* json BODY */
    public static final byte FLAG_JSON_BODY = 1;

    /* header长度 */
    public static final int HEADER_LEN = 2;

    /* 心跳长度 */
    public static final byte HB_PACKET_BYTE = -33;
    public static final Packet HB_PACKET = new Packet(Command.HEARTBEAT);

    public Packet(byte cmd) {
        this.cmd = cmd;
    }

    public Packet(Command cmd) {
        this.cmd = cmd.cmd;
    }

    public Packet(byte cmd, byte[] sessionId) {
        this.cmd = cmd;
        this.sessionId = sessionId;
    }

    /**
     * 解码  headerLength(1) || cmd(1)  || flag(1) || sessionId(?) || bodyLength(1) || body(?)
     * @param in
     * @return
     */
    public static Packet decodePacket(ByteBuf in){
        // 可读索引
        int readIndex = in.readableBytes();
        // 头长度
        int headerLength = in.readInt();
        // 读取头信息
        byte cmd = in.readByte();

        Packet packet = new Packet(cmd);

        // 心跳不需要解码内容
        if (packet.cmd == Command.HEARTBEAT.cmd) {
            return packet;
        }
        packet.flag = in.readByte();

        if (headerLength > 2){
            in.readBytes(packet.sessionId = new byte[headerLength-2]);
        }
        // body长度
        int bodyLength = in.readInt();
        // 包内容不完整
        if (readIndex < headerLength + bodyLength ){
            return null;
        }
        if (bodyLength > 0) {
            in.readBytes(packet.body = new byte[bodyLength]);
        }

        // 解码时若是json解析为jsonPacket
        if (packet.flag == FLAG_JSON_BODY){
            JsonPacket jsonPacket = new JsonPacket(packet.cmd);
            jsonPacket.setBody(new String((byte[]) packet.getBody()));
            return jsonPacket;
        }

        return packet;
    }

    /**
     * 编码
     * @param packet
     * @param out
     */
    public static void encodePacket(Packet packet, ByteBuf out) {
        if (packet.cmd == Command.HEARTBEAT.cmd) {
            out.writeByte(Packet.HB_PACKET_BYTE);
        } else {
            out.writeInt(packet.getHeaderLength());
            out.writeByte(packet.cmd);
            out.writeByte(packet.flag);
            if (packet.getSessionIdLength() > 0){
                out.writeBytes(packet.sessionId);
            }
            out.writeInt(packet.getBodyLength());
            if (packet.getBodyLength() > 0) {
                out.writeBytes(packet.body);
            }
        }
        packet.body = null;
    }

    public <T> T getBody() {
        return (T) body;
    }

    public <T> void setBody(T body) {
        this.body = (byte[]) body;
    }

    /**
     * 请求头长度
     * @return
     */
    private int getHeaderLength(){
        return HEADER_LEN + getSessionIdLength();
    }

    /**
     * 内容长度
     * @return
     */
    private int getBodyLength() {
        return body == null ? 0 : body.length;
    }

    /**
     * sessionId长度
     * @return
     */
    private int getSessionIdLength(){
        return sessionId == null ? 0 : sessionId.length;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "cmd=" + cmd +
                ", flag=" + flag +
                ", sessionId=" + Arrays.toString(sessionId) +
                ", body=" + Arrays.toString(body) +
                '}';
    }
}
