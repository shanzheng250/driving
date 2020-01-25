package com.fh.framework.message;

import io.netty.channel.ChannelFutureListener;

/**
 * @ClassName:IMessage
 * @Description: TODO
 * @Author: shanzheng
 * @Date: 2019/12/18 15:19
 * @Version:1.0
 **/
public interface IMessage {
    /**
     * 数据包解码
     * @param packet
     */
    void decodeBodyJson();

    /**
     * 消息发送
     * @param listener
     */
    void send(ChannelFutureListener listener);
}
