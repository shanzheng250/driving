package com.fh.framework.server;

import com.fh.framework.codec.Command;
import com.fh.framework.handle.WebSocketServerChannelHandler;
import com.fh.framework.receiver.MessageReceiver;
import com.fh.framework.receiver.ServerExchangeMessageHandler;
import com.fh.framework.receiver.UserBindMessageHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName:WebSokcetServer
 * @Description: websocket服务端
 * @Author: shanzheng
 * @Date: 2019/12/17 16:38
 * @Version:1.0
 **/
public class WebSocketServer extends NettyServer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    public WebSocketServer(int port) {
        super(port);
    }

    public WebSocketServer(int port, String host) {
        super(port, host);
    }

    /* 可以处理的模式类 */
    private MessageReceiver receiver = new MessageReceiver();

    /* hanle处理类 */
    private WebSocketServerChannelHandler serverChannelHandler = new WebSocketServerChannelHandler(receiver);

    @Override
    public void init() {
        super.init();
        // 用户链接绑定
        receiver.register(Command.USERBIND,UserBindMessageHandler::new);
        // 业务消息
        receiver.register(Command.PUSH,ServerExchangeMessageHandler::new);
    }

    @Override
    public void initPipeline(ChannelPipeline pipeline) {
        //websocket协议本身是基于http协议的，所以这边也要使用http解编码器
        pipeline.addLast(new HttpServerCodec());
        //以块的方式来写的处理器
        pipeline.addLast(new ChunkedWriteHandler());
        //netty是基于分段请求的，HttpObjectAggregator的作用是将请求分段再聚合,参数是聚合字节的最大长度
        pipeline.addLast(new HttpObjectAggregator(1024*1024*1024));
        //ws://server:port/context_path
        //ws://localhost:9999/ws
        //参数指的是contex_path
        pipeline.addLast(new WebSocketServerProtocolHandler("/",null,true,65535));
        //websocket定义了传递数据的6中frame类型
        pipeline.addLast(getChannelHandler());
    }

    @Override
    ChannelHandler getChannelHandler() {
        return serverChannelHandler;
    }
}
