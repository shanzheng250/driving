package com.fh.framework.handle;

import com.fh.framework.bootstrap.ExchangeContainer;
import com.fh.framework.packet.Packet;
import com.fh.framework.session.Connection;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName:TcpClientChannelHandler
 * @Description: 客户端链接
 * @Author: shanzheng
 * @Date: 2019/12/20 9:08
 * @Version:1.0
 **/

public class TcpClientChannelHandler extends SimpleChannelInboundHandler<Packet> {

    private static final Logger logger = LoggerFactory.getLogger(TcpClientChannelHandler.class);

    /* 提供的策略功能 */
    private ExchangeContainer container;

    public TcpClientChannelHandler(ExchangeContainer container) {
        this.container = container;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
        // 获取服务器返回客户端信息
        Connection connection = new Connection(ctx.channel());
        // 消息处理
        BaseClientChannelHandler.handleMessage(connection,packet,container);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("caught an ex, channel={}", ctx.channel(), cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("connect an channel, channel={}", ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("client disconnect channel={}", ctx.channel());
    }
}
