package com.fh.framework.client;

import com.fh.framework.bootstrap.ExchangeContainer;
import com.fh.framework.handle.WebSocketClientChannelHandler;
import com.fh.framework.listener.Listener;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @ClassName:aClient
 * @Description: websocket client处理
 * @Author: shanzheng
 * @Date: 2019/12/26 20:00
 * @Version:1.0
 **/
public class WebSocketClient extends BizNettyClient {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketClient.class);

    public WebSocketClient(int port, String host, ExchangeContainer container) {
        super(port, host, container);
        handler = new WebSocketClientChannelHandler(container);
    }

    /* ws客户端处理对象 */
    private WebSocketClientChannelHandler handler;

    @Override
    public ChannelHandler getChannelHandler() {
        return handler;
    }


    @Override
    public Channel connect(Listener listener) {

        Channel channel = null;
        try {
            HttpHeaders httpHeaders = new DefaultHttpHeaders();
            //进行握手
            WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(getUri(), WebSocketVersion.V13, null, true,httpHeaders);
            channel = super.connect(listener);
            WebSocketClientChannelHandler handler = (WebSocketClientChannelHandler)channel.pipeline().get("hookedHandler");
            handler.setHandshaker(handshaker);
            handshaker.handshake(channel);
            //阻塞等待是否握手成功
            handler.handshakeFuture().sync();
        } catch (Exception e) {
            logger.error("connect error..cause:{}",e.getMessage());
        }

        return channel;
    }


    @Override
    protected void initPipeline(ChannelPipeline pipeline) {
        pipeline.addLast(new HttpClientCodec());
        pipeline.addLast(new HttpObjectAggregator(1024*1024*1024));
        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
        pipeline.addLast("hookedHandler", getChannelHandler());
    }

    /**
     * 创建uri
     * @return
     */
    private URI getUri() throws URISyntaxException {
         return new URI("ws://"+getHost()+":"+getPort()+"/");
    }

}
