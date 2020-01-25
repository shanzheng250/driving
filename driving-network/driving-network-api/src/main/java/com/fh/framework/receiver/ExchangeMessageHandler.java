package com.fh.framework.receiver;

import com.fh.framework.message.ExchangeMessage;
import com.fh.framework.packet.Packet;
import com.fh.framework.receiver.BaseMessageHandler;
import com.fh.framework.session.Connection;

/**
 * @ClassName:ConnMessageHandler
 * @Description: 信息交换处理
 * @Author: shanzheng
 * @Date: 2019/12/20 11:07
 * @Version:1.0
 **/
public abstract class ExchangeMessageHandler extends BaseMessageHandler<ExchangeMessage> {

    public String aliasName;

    public ExchangeMessageHandler(String aliasName) {
        this.aliasName = aliasName;
    }

    public ExchangeMessageHandler() {
        this.aliasName =  this.getClass().getSimpleName();
    }

    @Override
    protected ExchangeMessage decode(Packet packet, Connection connection) {
        return new ExchangeMessage(packet,connection);
    }


    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
}
