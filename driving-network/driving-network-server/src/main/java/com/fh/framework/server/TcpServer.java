
package com.fh.framework.server;


import com.fh.framework.codec.Command;
import com.fh.framework.handle.TcpServerChannelHandler;
import com.fh.framework.listener.Listener;
import com.fh.framework.receiver.MessageReceiver;
import com.fh.framework.receiver.ServerExchangeMessageHandler;
import com.fh.framework.receiver.UserBindMessageHandler;
import io.netty.channel.ChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName:TcpServer
 * @Description: tcp服务端
 * @Author: shanzheng
 * @Date: 2019/12/17 16:38
 * @Version:1.0
 **/
public final class TcpServer extends NettyServer {

    private static final Logger logger = LoggerFactory.getLogger(TcpServer.class);

    /* 可以处理的模式类 */
    private MessageReceiver receiver = new MessageReceiver();

    private TcpServerChannelHandler channelHandler = new TcpServerChannelHandler(receiver);

    public TcpServer(int port) {
        super(port);
    }

    public TcpServer(int port, String host) {
        super(port, host);
    }

    @Override
    public void init() {
        super.init();
        receiver.register(Command.USERBIND,UserBindMessageHandler::new);
        receiver.register(Command.PUSH,ServerExchangeMessageHandler::new);
    }


    @Override
    public void start(Listener listener) {
        super.start(listener);
    }

    @Override
    public void stop(Listener listener) {
        super.stop(listener);
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return channelHandler;
    }
}
