package com.fh.framework.client;

import com.fh.framework.bootstrap.ExchangeContainer;

/**
 * @ClassName:BizNettyClient
 * @Description: 暴漏处理推送消息接口
 * @Author: shanzheng
 * @Date: 2019/12/20 13:55
 * @Version:1.0
 **/
public abstract class BizNettyClient extends NettyClient {

    private ExchangeContainer container;

    public BizNettyClient(int port, String host, ExchangeContainer container) {
        super(port, host);
        this.container = container;
    }

}
