
package com.fh.framework.client;

import com.fh.framework.codec.PacketDecode;
import com.fh.framework.codec.PacketEncode;
import com.fh.framework.listener.Listener;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.channels.spi.SelectorProvider;

/**
 * @ClassName:NettyServer
 * @Description: 基础客户端
 * @Author: shanzheng
 * @Date: 2019/12/19 10:50
 * @Version:1.0
 **/
public abstract class NettyClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private EventLoopGroup workerGroup;

    protected Bootstrap bootstrap;

    /*绑定端口*/
    private int port;

    /* 绑定ip */
    private String host;


    public NettyClient(int port, String host) {
        this.port = port;
        this.host = host;
    }

    /**
     * 创建客户端
     * @param listener
     */
    public void createNioClient(Listener listener) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(getWorkThreadNum());
        workerGroup.setIoRatio(getIoRate());
        createClient(listener, workerGroup, getChannelFactory());
    }

    /**
     * 创建客户端
     * @param listener
     * @param workerGroup
     * @param channelFactory
     */
    private void createClient(Listener listener, EventLoopGroup workerGroup, ChannelFactory<? extends Channel> channelFactory) {
        this.workerGroup = workerGroup;
        this.bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)//
//                .option(ChannelOption.SO_REUSEADDR, true)//
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)//
                .channelFactory(channelFactory);
        bootstrap.handler(new ChannelInitializer<Channel>() { // (4)
            @Override
            public void initChannel(Channel ch) throws Exception {
                initPipeline(ch.pipeline());
            }
        });
        initOptions(bootstrap);
        listener.onSuccess();
    }

    protected void doStop(Listener listener) throws Throwable {
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        logger.error("netty client [{}] stopped.", this.getClass().getSimpleName());
        listener.onSuccess();
    }

    /**
     * 链接
     * @return
     */
    public Channel connect() throws InterruptedException {
        return bootstrap.connect(new InetSocketAddress(host, port)).sync().channel();
    }

    /**
     * 链接
     * @param listener
     * @return
     */
    public Channel connect(Listener listener) throws InterruptedException {
        return bootstrap.connect(new InetSocketAddress(host, port)).addListener(f -> {
            if (f.isSuccess()) {
                if (listener != null) listener.onSuccess(port);
                logger.info("start netty client success, host={}, port={}", host, port);
            } else {
                if (listener != null) listener.onFailure(f.cause());
                logger.error("start netty client failure, host={}, port={},cause:{}", host, port, f.cause());
            }
        }).sync().channel();
    }


    /**
     * 初始化pipeline
     * @param pipeline
     */
    protected void initPipeline(ChannelPipeline pipeline) {
        pipeline.addLast("decoder", getDecoder());
        pipeline.addLast("encoder", getEncoder());
        pipeline.addLast("handler", getChannelHandler());
    }

    protected ChannelHandler getDecoder() {
        return  new PacketDecode();
    }

    protected ChannelHandler getEncoder() {
        return new PacketEncode();
    }

    protected int getIoRate() {
        return 50;
    }

    protected int getWorkThreadNum() {
        return 1;
    }

    public abstract ChannelHandler getChannelHandler();


    protected void initOptions(Bootstrap b) {
        b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 4000);
        b.option(ChannelOption.TCP_NODELAY, true);
    }

    public ChannelFactory<? extends Channel> getChannelFactory() {
        return NioSocketChannel::new;
    }

    public SelectorProvider getSelectorProvider() {
        return SelectorProvider.provider();
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    @Override
    public String toString() {
        return "NettyClient{" +
                ", name=" + this.getClass().getSimpleName() +
                '}';
    }
}
