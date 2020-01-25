package com.fh.framework.spring.factory;

import com.fh.framework.bootstrap.ClientStarter;
import com.fh.framework.bootstrap.ExchangeContainer;
import com.fh.framework.bootstrap.ExchangeInitializer;
import com.fh.framework.bootstrap.RegisterProperties;
import com.fh.framework.config.ClientConfig;
import com.fh.framework.config.RegisterConfig;
import com.fh.framework.constant.NetworkEnum;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @ClassName:DrivingClientFactory
 * @Description: 客户端工厂类
 * @Author: shanzheng
 * @Date: 2019/12/31 9:15
 * @Version:1.0
 **/
public class DrivingClientFactory implements InitializingBean, FactoryBean<ClientStarter>,ApplicationContextAware {

    /* spring上下文 */
    private transient ApplicationContext applicationContext;

    /* 注册信息 */
    private RegisterConfig registerConfig;

    /* 客户端信息 */
    private ClientConfig clientConfig;


    /**
     * 进行对象获取
     * @return
     * @throws Exception
     */
    public ClientStarter getObject() throws Exception {

        ClientStarter clientStarter = new ClientStarter(clientConfig.getPort(),clientConfig.getHost());

        clientStarter
                .init(NetworkEnum.getNetworkType(clientConfig.getNetType()))
                .registerExchange(new ExchangeInitializer() {
                    @Override
                    protected ExchangeContainer initExchange(ExchangeContainer container) {
                        container.addStrategy(clientConfig.getCustomizer());
                        return container;
                    }
                })
                .registerClientProperties(new RegisterProperties(registerConfig.getSysid(),registerConfig.getAreaCode(),registerConfig.getUserCode()));


        return clientStarter;
    }


    /**
     * 设置和初始化属性
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {

        registerConfig =  applicationContext.getBean(RegisterConfig.class);

        if (registerConfig == null){
            registerConfig = RegisterConfig.getDefaultConfig();
        }

        clientConfig =  applicationContext.getBean(ClientConfig.class);

        if (clientConfig == null){
            throw new IllegalStateException("no client configs: " + clientConfig);
        }

        // 属性校验
        clientConfig.initAndValid();
    }


    public Class<?> getObjectType() {
        return ClientStarter.class;
    }

    public boolean isSingleton() {
        return false;
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
