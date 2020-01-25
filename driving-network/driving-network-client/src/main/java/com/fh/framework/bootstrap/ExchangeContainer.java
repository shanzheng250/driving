package com.fh.framework.bootstrap;

import com.fh.framework.message.ExchangeMessage;
import com.fh.framework.receiver.ExchangeMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @ClassName:ExchangeContainer
 * @Description: 消息推送接收的策略配置容器，对外提供的能力
 * @Author: shanzheng
 * @Date: 2019/12/20 17:22
 * @Version:1.0
 **/
public final class ExchangeContainer {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeContainer.class);


    /* 默认策略 */
    private HashMap<String,ExchangeMessageHandler> strategy = new HashMap<>();

    /**
     * 新增策略
     * @param handler
     */
    public void addStrategy(ExchangeMessageHandler handler){
        strategy.put(handler.aliasName,handler);
    }

    /**
     * 新增策略
     * @param strategys
     */
    public void addStrategy(HashMap<String,ExchangeMessageHandler> strategys){
        strategy.putAll(strategys);
    }

    /**
     * 新增策略
     * @param strategyKey
     * @param handler
     */
    public void addStrategy(String strategyKey,ExchangeMessageHandler handler){
        strategy.put(strategyKey,handler);
    }


    /**
     * 该客户端是否有处理消息名为key的能力
     * @param strategyKey
     * @return
     */
    public boolean containsStrategy(String strategyKey){
        return strategy.containsKey(strategyKey);
    }

    /**
     * 获取消息处理器
     * @return
     */
    public ExchangeMessageHandler getHandleByStrategy(String strategyKey){
        return strategy.getOrDefault(strategyKey, new ExchangeMessageHandler() {
            @Override
            protected void handle(ExchangeMessage message) {
                logger.info("do noting cause strategyKey is {},no handle to deal",strategyKey);
            }
        });
    }
}
