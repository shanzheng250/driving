package com.fh.framework.bootstrap;

import com.alibaba.fastjson.JSONObject;
import com.fh.framework.client.NettyClient;
import com.fh.framework.codec.Command;
import com.fh.framework.constant.NetworkEnum;
import com.fh.framework.factory.NetworkClientFactory;
import com.fh.framework.listener.Listener;
import com.fh.framework.message.ExchangeMessage;
import com.fh.framework.packet.JsonPacket;
import com.fh.framework.packet.Packet;
import com.fh.framework.session.Connection;
import com.fh.framework.utils.JsonsUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName:ClientStarter
 * @Description: 客户端启动
 * @Author: shanzheng
 * @Date: 2019/12/20 17:14
 * @Version:1.0
 **/
public class ClientStarter {

    private static final Logger logger = LoggerFactory.getLogger(ClientStarter.class);

    /*绑定端口*/
    private int port;

    /* 绑定ip */
    private String host;

    /* 策略对象 */
    private ExchangeInitializer exchangeInitializer;

    /* 链接对象 */
    private Map<InetSocketAddress,Channel> connections = new ConcurrentHashMap<>();

    /* 客户端对象 */
    private NettyClient client;

    /* 生成network对象 */
    private NetworkEnum networkEnum = NetworkEnum.TCP;

    /* 注册对象 */
    RegisterProperties properties;

    public ClientStarter(int port, String host) {
        this.port = port;
        this.host = host;
    }

    /**
     * 初始化
     * @param networkEnum
     * @return
     */
    public ClientStarter init(NetworkEnum networkEnum){
        this.networkEnum = networkEnum;
        return this;
    }

    /**
     * 注册消息推送策略
     * @param exchangeInitializer
     * @return
     */
    public ClientStarter registerExchange(ExchangeInitializer exchangeInitializer){
        if (exchangeInitializer == null) {
            throw new NullPointerException("childHandler");
        } else {
            this.exchangeInitializer = exchangeInitializer;
            NetworkClientFactory factory = new NetworkClientFactory(port,host,exchangeInitializer);
            client = factory.getClient(networkEnum);
            return this;
        }
    }

    /**
     * 注册信息
     * @param properties
     * @return
     */
    public ClientStarter registerClientProperties(RegisterProperties properties){
        this.properties = properties;

        Packet packet = new JsonPacket(Command.USERBIND);

        packet.setBody(JsonsUtils.toJson(properties));

        send(packet);

        return this;
    }



    /**
     * 发送消息
     * @param packet
     */
    public void send(Packet packet){
        try {
            send(packet,null,null,null);
        } catch (InterruptedException e) {
            logger.info("启动工作链接线程失败..cause:{}",e.getMessage());
        }
    }

    /**
     * 发送消息
     * @param packet
     * @param listener
     */
    public void send(Packet packet, ChannelFutureListener listener){
        try {
            send(packet,null,null,listener);
        } catch (InterruptedException e) {
            logger.info("启动工作链接线程失败..cause:{}",e.getMessage());
        }
    }

    /**
     * 点对点发送消息
     * @param packet  数据包
     * @param strategy 消息模块功能名称
     * @param bizId   发送对方的业务绑定id
     * @param listener 监听器
     */
    public void sendP2P(Packet packet, String strategy,String bizId, ChannelFutureListener listener){
        try {
            send(packet,strategy,bizId,listener);
        } catch (InterruptedException e) {
            logger.info("启动工作链接线程失败..cause:{}",e.getMessage());
        }
    }

    /**
     * 广播通知
     * @param packet
     * @param strategy
     * @param listener
     */
    public void broadcast(Packet packet, String strategy,ChannelFutureListener listener){
        try {
            send(packet,strategy,"*",listener);
        } catch (InterruptedException e) {
            logger.info("启动工作链接线程失败..cause:{}",e.getMessage());
        }
    }

    /**
     * 发送消息
     * @param packet 数据包
     * @param message 消息策略/功能模块
     * @param bizId  发送对方的标识
     * @param listener
     */
    private void send(Packet packet, String message, String bizId, ChannelFutureListener listener) throws InterruptedException {
        // ip 地址
        InetSocketAddress inetSocketAddress = new InetSocketAddress(host,port);

        Channel channel = connections.get(inetSocketAddress);

        if (channel == null || !channel.isOpen()){
            // 获取远程连接future
            client.createNioClient(new Listener() {
                @Override
                public void onSuccess(Object... args) {
                    logger.info("create client success...server ip:{},port:{}",host,port);
                }

                @Override
                public void onFailure(Throwable cause) {
                    logger.info("create client error...server ip:{},port:{},cause:{}",host,port,cause.getMessage());
                }
            });

            channel = client.connect(new Listener() {
                @Override
                public void onSuccess(Object... args) {
                    logger.info("connect remote server success...server ip:{},port:{}",host,port);
                }

                @Override
                public void onFailure(Throwable cause) {
                    logger.info("connect remote server error...server ip:{},port:{},cause:{}",host,port,cause.getMessage());
                }
            });
            connections.put(inetSocketAddress,channel);
        }
        // 设置为标准链接
        Connection connection = new Connection(channel,networkEnum);

        // 增强packet
        enhancePacket(packet,message,bizId);

        connection.send(packet,listener);
    }

    /**
     * 增强数据包
     * @param packet
     * @param message
     * @param bizId
     */
    private void enhancePacket(Packet packet, String message, String bizId){
        // 若数据包类型是json
        if (packet.flag == Packet.FLAG_JSON_BODY){

            JSONObject json = packet.getBody();
            json.put("from_bizId",properties.getId());

            if (message != null){
                json.put("strategy",message);
            }

            if (!StringUtils.isBlank(bizId)){
                json.put("bizId",bizId);
            }
            packet.setBody(json.toJSONString());

        } else {

            JSONObject json = JSONObject.parseObject(new String((byte[]) packet.getBody()));
            json.put("from_bizId",properties.getId());

            if (message != null){
                json.put("strategy",message);
            }

            if (!StringUtils.isBlank(bizId)){
                json.put("bizId",bizId);
            }
            packet.setBody(json.toJSONString().getBytes());
        }



    }
}
