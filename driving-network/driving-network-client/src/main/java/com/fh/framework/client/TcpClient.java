package com.fh.framework.client;

import com.fh.framework.bootstrap.ExchangeContainer;
import com.fh.framework.handle.TcpClientChannelHandler;
import io.netty.channel.ChannelHandler;

/**
 * @ClassName:TcpClient
 * @Description: tcp链接
 * @Author: shanzheng
 * @Date: 2019/12/20 8:58
 * @Version:1.0
 **/
public class TcpClient extends BizNettyClient{

    private final TcpClientChannelHandler handler;

    public TcpClient(int port, String host, ExchangeContainer container) {
        super(port, host, container);
        handler = new TcpClientChannelHandler(container);
    }


    @Override
    public ChannelHandler getChannelHandler() {
        return handler;
    }
}
