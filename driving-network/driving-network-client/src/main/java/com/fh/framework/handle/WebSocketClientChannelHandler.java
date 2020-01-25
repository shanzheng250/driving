package com.fh.framework.handle;

import com.fh.framework.bootstrap.ExchangeContainer;
import com.fh.framework.codec.PacketDecode;
import com.fh.framework.constant.NetworkEnum;
import com.fh.framework.packet.Packet;
import com.fh.framework.session.Connection;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName:TcpClientChannelHandler
 * @Description: 客户端链接
 * @Author: shanzheng
 * @Date: 2019/12/20 9:08
 * @Version:1.0
 **/

public class WebSocketClientChannelHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketClientChannelHandler.class);

    public WebSocketClientChannelHandler(ExchangeContainer container) {
        this.container = container;
    }

    /* 提供的策略功能 */
    private ExchangeContainer container;

    /* 处理ws握手 */
    private WebSocketClientHandshaker handshaker;

    /* 是否成功握手 */
    private ChannelPromise handshakeFuture;


    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.debug("channelRead0  " + this.handshaker.isHandshakeComplete());
        Channel ch = ctx.channel();
        FullHttpResponse response;
        if (!this.handshaker.isHandshakeComplete()) {
            try {
                response = (FullHttpResponse) msg;
                //握手协议返回，设置结束握手
                this.handshaker.finishHandshake(ch, response);
                //设置成功
                this.handshakeFuture.setSuccess();
                logger.info("WebSocket Client connected! response headers[sec-websocket-extensions]:{}" + response.headers());
            } catch (WebSocketHandshakeException var7) {
                FullHttpResponse res = (FullHttpResponse) msg;
                String errorMsg = String.format("WebSocket Client failed to connect,status:%s,reason:%s", res.status(), res.content().toString(CharsetUtil.UTF_8));
                logger.error(errorMsg);
                this.handshakeFuture.setFailure(new Exception(errorMsg));
            }
        } else if (msg instanceof FullHttpResponse) {
            response = (FullHttpResponse) msg;
            String errorMsg = "Unexpected FullHttpResponse (getStatus=" + response.status() + ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')';
            logger.error(errorMsg);
            throw new IllegalStateException(errorMsg);
        } else {
            WebSocketFrame frame = (WebSocketFrame) msg;
            if (frame instanceof TextWebSocketFrame) {
                String text = ((TextWebSocketFrame) frame).text();
                Packet packet = PacketDecode.decodeFrame(text);
                Connection connection = new Connection(ctx.channel(),NetworkEnum.WEBSOCKET);
                logger.debug("channelRead conn={}, packet={}", ctx.channel(), packet);
                BaseClientChannelHandler.handleMessage(connection,packet,container);
            } else {
                String message = "unsupported frame type: " + frame.getClass().getName();
                throw new UnsupportedOperationException(message);
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("\n\t⌜⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓\n" +
                "\t├ [Mock 建立连接]\n" +
                "\t⌞⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("client disconnect channel={}", ctx.channel());
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        this.handshakeFuture = ctx.newPromise();
    }

    /**
     * 返回阻塞状态
     * @return
     */
    public ChannelFuture handshakeFuture() {
        return this.handshakeFuture;
    }

    public WebSocketClientHandshaker getHandshaker() {
        return handshaker;
    }

    public void setHandshaker(WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    public ChannelPromise getHandshakeFuture() {
        return handshakeFuture;
    }

    public void setHandshakeFuture(ChannelPromise handshakeFuture) {
        this.handshakeFuture = handshakeFuture;
    }
}
