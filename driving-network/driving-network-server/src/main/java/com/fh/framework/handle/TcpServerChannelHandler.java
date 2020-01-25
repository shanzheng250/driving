package com.fh.framework.handle;

import com.fh.framework.packet.Packet;
import com.fh.framework.receiver.MessageReceiver;
import com.fh.framework.session.Connection;
import com.fh.framework.session.SessionContext;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName:ServerChannelHandle
 * @Description: tcp 通道处理器
 * @Author: shanzheng
 * @Date: 2019/12/19 10:26
 * @Version:1.0
 **/
@ChannelHandler.Sharable
public class TcpServerChannelHandler extends SimpleChannelInboundHandler<Packet> {

    private static final Logger logger = LoggerFactory.getLogger(TcpServerChannelHandler.class);

    /* 实际处理类  */
    private MessageReceiver receiver;


    public TcpServerChannelHandler(MessageReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
        Connection connection = new Connection(ctx.channel());
        logger.debug("channelRead conn={}, packet={}", ctx.channel(), packet);
        receiver.onReceive(packet, connection);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("caught an ex, channel={}, cause={}", ctx.channel(), cause);
        ctx.close();
        SessionContext.getSessionInstance().remove(ctx.channel().id().toString());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("client connected conn={}", ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("client disconnected id={}", ctx.channel());
        // 清除
        ctx.close();
        SessionContext.getSessionInstance().remove(ctx.channel().id().toString());
    }
}
