package com.fh.framework.factory;

import com.fh.framework.constant.NetworkEnum;
import com.fh.framework.server.NettyServer;
import com.fh.framework.server.TcpServer;
import com.fh.framework.server.WebSocketServer;

/**
 * @ClassName:NetworkServerFactory
 * @Description:  服务工厂
 * @Author: shanzheng
 * @Date: 2019/12/25 14:52
 * @Version:1.0
 **/
public class NetworkServerFactory {

    /*绑定端口*/
    private int port;

    public NetworkServerFactory(int port) {
        this.port = port;
    }

    /**
     * 获取客户端
     * @param networkEnum
     * @return
     */
    public NettyServer getServer(NetworkEnum networkEnum){

        if (networkEnum == NetworkEnum.TCP){
            return new TcpServer(port);
        }

        if (networkEnum == NetworkEnum.WEBSOCKET){
            return new WebSocketServer(port);
        }
        return null;
    }

}
