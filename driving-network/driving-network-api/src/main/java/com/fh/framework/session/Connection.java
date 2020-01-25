package com.fh.framework.session;

import com.fh.framework.constant.NetworkEnum;
import com.fh.framework.packet.JsonPacket;
import com.fh.framework.packet.Packet;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName:Connection
 * @Description: 包装的channel对象
 * @Author: shanzheng
 * @Date: 2019/12/18 16:44
 * @Version:1.0
 **/
public class Connection {

    private static final Logger LOGGER = LoggerFactory.getLogger(Connection.class);

    /* 父容器 */
    private SessionContext context = SessionContext.getSessionInstance();

    /* netty链接 */
    private Channel channel;

    private NetworkEnum networkEnum = NetworkEnum.TCP;

    public Connection(Channel channel) {
        this.channel = channel;
        if (channel == null){
            LOGGER.error("Channel is null");
        }
    }

    public Connection(Channel channel, NetworkEnum networkEnum) {
        this.channel = channel;
        this.networkEnum = networkEnum;
    }

    /**
     * 加入上下文
     */
    public void addSessionContext() {
        context.put(this);
    }

    /**
     * 删除上下文内容
     */
    public void removeSessionContext(){
        context.remove(this);
    }

    /**
     * 获取存储上下文的id
     * @return
     */
    public String getId(){
        return channel.id().asLongText();
    }

    /**
     * 发送数据包
     * @param packet
     * @return
     */
    public ChannelFuture send(Packet packet) {
        return send(packet, null);
    }

    /**
     * 带监听器的数据包
     * @param packet
     * @param listener
     * @return
     */
    public ChannelFuture send(Packet packet, final ChannelFutureListener listener) {
        if (channel.isActive()) {

            ChannelFuture future;

            if (networkEnum == NetworkEnum.TCP){
                // http 传值数据包进行编解码
                future = channel.writeAndFlush(packet);
            }else {
                //ws 只传string类型
                JsonPacket jsonPacket = (JsonPacket) packet;
                future = channel.writeAndFlush(new TextWebSocketFrame(jsonPacket.toJsonStr()));
            }

            if (listener != null) {
                future.addListener(listener);
            }

            if (channel.isWritable()) {
                return future;
            }

            //抛异常
            return channel.newPromise().setFailure(new RuntimeException("send data too busy"));

        } else {
            /*if (listener != null) {
                channel.newPromise()
                        .addListener(listener)
                        .setFailure(new RuntimeException("connection is disconnected"));
            }*/
            return this.close();
        }
    }

    /**
     * 关闭链接
     * @return
     */
    public ChannelFuture close() {
        try {
            return this.channel.close().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取链接
     * @return
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * 是否是该channel
     * @param id
     * @return
     */
    public boolean isChannel(String id){
        return channel.id().equals(id);
    }

    public NetworkEnum getNetworkEnum() {
        return networkEnum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection that = (Connection) o;
        return channel.id().equals(that.channel.id());
    }

    @Override
    public int hashCode() {
        return channel.id().hashCode();
    }
}
