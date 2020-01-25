package com.fh.framework.handle;

import com.fh.framework.codec.PacketDecode;
import com.fh.framework.constant.NetworkEnum;
import com.fh.framework.packet.Packet;
import com.fh.framework.receiver.MessageReceiver;
import com.fh.framework.session.Connection;
import com.fh.framework.session.SessionContext;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ChannelHandler.Sharable
public class WebSocketServerChannelHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServerChannelHandler.class);

    private final MessageReceiver receiver;

    public WebSocketServerChannelHandler(MessageReceiver receiver) {
        this.receiver = receiver;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object frame) throws Exception {
        logger.debug("<-------------channelRead----------->");
        if (frame instanceof TextWebSocketFrame) {
            String text = ((TextWebSocketFrame) frame).text();
            Packet packet = PacketDecode.decodeFrame(text);
            Connection connection = new Connection(ctx.channel(),NetworkEnum.WEBSOCKET);
            logger.debug("channelRead conn={}, packet={}", ctx.channel(), packet);
            receiver.onReceive(packet, connection);
        } else {
            String message = "unsupported frame type: " + frame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
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
        SessionContext.getSessionInstance().remove(ctx.channel().id().toString());
    }
}