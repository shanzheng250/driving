package com.fh.framework.server;

import com.fh.framework.codec.PacketDecode;
import com.fh.framework.codec.PacketEncode;
import com.fh.framework.constant.NumberContant;
import com.fh.framework.constant.ServerStateEnum;
import com.fh.framework.exception.ServiceException;
import com.fh.framework.listener.Listener;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @ClassName:NettyServer
 * @Description: 基础服务端
 * @Author: shanzheng
 * @Date: 2019/12/17 10:50
 * @Version:1.0
 **/
public abstract class NettyServer implements Server {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    /* 工作线程 */
    private EventLoopGroup workerGroup;

    /* boss线程 */
    private EventLoopGroup bossGroup;

    /*绑定端口*/
    private int port;

    /* 绑定ip */
    private String host;

    /* 初始化 */
    private AtomicReference<ServerStateEnum> serverState = new AtomicReference<>(ServerStateEnum.Created);

    public NettyServer(int port) {
        this.port = port;
        this.host = null;
    }

    public NettyServer(int port, String host) {
        this.port = port;
        this.host = host;
    }

    @Override
    public void init(){
        if (!serverState.compareAndSet(ServerStateEnum.Created,ServerStateEnum.Initialized)){
            throw new ServiceException("Server already init");
        }
    }

    @Override
    public void start(Listener listener) {
        if (!serverState.compareAndSet(ServerStateEnum.Initialized,ServerStateEnum.Starting)){
            throw new ServiceException("Server not init");
        }
        createNioServer(listener);
    }

    @Override
    public void stop(Listener listener) {
        if (!serverState.compareAndSet(ServerStateEnum.Started, ServerStateEnum.Shutdown)) {
            if (listener != null) listener.onFailure(new ServiceException("server was already shutdown."));
            logger.error("{} was already shutdown.", this.getClass().getSimpleName());
            return;
        }
        logger.info("try shutdown {}...", this.getClass().getSimpleName());
        if (bossGroup != null) bossGroup.shutdownGracefully().syncUninterruptibly();//要先关闭接收连接的main reactor
        if (workerGroup != null) workerGroup.shutdownGracefully().syncUninterruptibly();//再关闭处理业务的sub reactor
        logger.info("{} shutdown success.", this.getClass().getSimpleName());
        if (listener != null) {
            listener.onSuccess(port);
        }
    }

    /**
     * 这里默认使用的NIO的I/O 模型
     * 之后要想扩展可以在调用地方进行判断
     * @param listener
     */
    private void createNioServer(Listener listener) {
        EventLoopGroup bossGroup = getBossGroup();
        EventLoopGroup workerGroup = getWorkerGroup();

        if (bossGroup == null) {
            NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(getBossThreadNum());
            nioEventLoopGroup.setIoRatio(100);
            bossGroup = nioEventLoopGroup;
        }

        if (workerGroup == null) {
            NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(getWorkThreadNum());
            nioEventLoopGroup.setIoRatio(getIoRate());
            workerGroup = nioEventLoopGroup;
        }
        createServer(listener, bossGroup, workerGroup);
    }

    /**
     * 开启netty服务端口
     * @param listener
     * @param bossGroup
     * @param workerGroup
     */
    private void createServer(Listener listener, EventLoopGroup bossGroup, EventLoopGroup workerGroup){
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 设置对象
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // 默认使用内存池
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childHandler(new ChannelInitializer<Channel>() { // (4)
                        @Override
                        public void initChannel(Channel ch) throws Exception {//每连上一个链接调用一次
                            initPipeline(ch.pipeline());
                        }
                    });
            // 绑定端口
            InetSocketAddress address = StringUtils.isBlank(host) ? new InetSocketAddress(port) : new InetSocketAddress(host,port);
            serverBootstrap.bind(address).addListener(future -> {
                if (future.isSuccess()) {
                    serverState.set(ServerStateEnum.Started);
                    logger.info("server start success on:{}", port);
                    if (listener != null) listener.onSuccess(port);
                } else {
                    logger.error("server start failure on:{}", port, future.cause());
                    if (listener != null) listener.onFailure(future.cause());
                }
            });
        } catch (Exception e) {
            logger.error("server start failure on:{}", port, e.getCause());
            if (listener != null) listener.onFailure(e.getCause());
        }
    }

    /**
     * c初始化
     * @param pipeline
     */
    public void initPipeline(ChannelPipeline pipeline) {
        pipeline.addLast("decoder", new PacketDecode());
        pipeline.addLast("encoder", new PacketEncode());
        pipeline.addLast("handler", getChannelHandler());
    }

    abstract ChannelHandler getChannelHandler();

    private int getIoRate() {
        return NumberContant.NUMBER_50;
    }

    protected int getBossThreadNum() {
        return 1;
    }

    protected int getWorkThreadNum() {
        return 0;
    }

    public EventLoopGroup getWorkerGroup() {
        return workerGroup;
    }

    public EventLoopGroup getBossGroup() {
        return bossGroup;
    }
}
