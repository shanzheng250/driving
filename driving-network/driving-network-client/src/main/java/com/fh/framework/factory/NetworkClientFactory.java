package com.fh.framework.factory;

import com.fh.framework.bootstrap.ExchangeInitializer;
import com.fh.framework.client.NettyClient;
import com.fh.framework.client.TcpClient;
import com.fh.framework.client.WebSocketClient;
import com.fh.framework.constant.NetworkEnum;

/**
 * @ClassName:ClientFactory
 * @Description: 客户端工厂
 * @Author: shanzheng
 * @Date: 2019/12/25 13:41
 * @Version:1.0
 **/
public class NetworkClientFactory {

    /*绑定端口*/
    private int port;

    /* 绑定ip */
    private String host;

    /* 策略 */
    private ExchangeInitializer exchangeInitializer;

    public NetworkClientFactory(int port, String host, ExchangeInitializer exchangeInitializer) {
        this.port = port;
        this.host = host;
        this.exchangeInitializer = exchangeInitializer;
    }

    /**
     * 获取客户端
     * @param networkEnum
     * @return
     */
    public NettyClient getClient(NetworkEnum networkEnum){

        if (networkEnum == NetworkEnum.TCP){
            return new TcpClient(port, host, exchangeInitializer.getCustomize());
        }

        if (networkEnum == NetworkEnum.WEBSOCKET){
            return new WebSocketClient(port, host, exchangeInitializer.getCustomize());
        }

        return null;
    }


}
