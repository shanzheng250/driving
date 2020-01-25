package com.fh.framework.bootstrap;

/**
 * @ClassName:ExchangeInitializer
 * @Description: 注册信息处理类
 * @Author: shanzheng
 * @Date: 2019/12/20 17:10
 * @Version:1.0
 **/
public abstract class ExchangeInitializer {

    public ExchangeInitializer() {
    }

    protected abstract ExchangeContainer initExchange(ExchangeContainer var1);

    /**
     * 获取使用者定制化的策略容器
     * @return
     * @throws Exception
     */
    public ExchangeContainer getCustomize(){
        return initExchange(new ExchangeContainer());
    }
}
