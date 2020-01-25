package com.fh.framework.receiver;

import com.fh.framework.message.BaseMessage;
import com.fh.framework.packet.Packet;
import com.fh.framework.session.Connection;
import com.fh.framework.session.SessionContext;
import com.fh.framework.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @ClassName:BaseReceiver
 * @Description: 消息的实际处理方法，再packet进行判断后交付实际的处理handle类，进行内容的解码并执行相应的业务
 * @Author: shanzheng
 * @Date: 2019/12/18 15:42
 * @Version:1.0
 **/
public abstract class BaseMessageHandler<T extends BaseMessage>  implements IMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(BaseMessageHandler.class);

    /**
     * 解码数据包，获取相应的message信息
     * @param packet
     * @return
     */
    protected abstract T decode(Packet packet, Connection connection);

    /**
     * 根据发送的数据包内容进行相应的业务处理
     * @param message
     */
    protected abstract void handle(T message);

    @Override
    public void handle(Packet packet, Connection connection) {
        // 当前时间
        Long startTime = CommonUtils.getCurTime();
        //  解码数据包
        T  t = decode(packet,connection);
        // 解码数据字段填充message对象
        t.decodeBodyJson();

        handle(t);

        logger.debug("packet:{}处理完成耗时为:{}ms",packet,CommonUtils.getCurTime() - startTime);
    }


    /**
     * 获取匹配链接
     * @param bizId 匹配id
     * @param exclude 排除id
     * @return
     */
    protected List<Connection> getMatchConnections(String bizId, String exclude){

        // 获取匹配的链接
        SessionContext sessionContext = SessionContext.getSessionInstance();

        List<Connection> connections;

        if (bizId == null || "*".equals(bizId)){

            connections = sessionContext.getAllConnections(exclude);

        }else {

            connections = sessionContext.getConnections(bizId,exclude);
        }
        return connections;
    }
}
