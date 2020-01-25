package com.fh.framework.receiver;

import com.fh.framework.packet.Packet;
import com.fh.framework.session.Connection;

/**
 * @ClassName:MessageHandle
 * @Description: 具体消息处理方法
 * @Author: shanzheng
 * @Date: 2019/12/18 16:33
 * @Version:1.0
 **/
public interface IMessageHandler {

    void handle(Packet packet, Connection connection);

}
