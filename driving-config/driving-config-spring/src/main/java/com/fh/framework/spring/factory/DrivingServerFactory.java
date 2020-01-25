package com.fh.framework.spring.factory;

import com.fh.framework.config.ServerConfig;
import com.fh.framework.constant.NetworkEnum;
import com.fh.framework.factory.NetworkServerFactory;
import com.fh.framework.listener.Listener;
import com.fh.framework.server.NettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @ClassName:DrivingServerFactory
 * @Description: 容器启动则启动server服务
 * @Author: shanzheng
 * @Date: 2019/12/31 13:58
 * @Version:1.0
 **/
public class DrivingServerFactory implements InitializingBean, ApplicationContextAware,ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(DrivingServerFactory.class);

    private ApplicationContext applicationContext;

    private ServerConfig serverConfig;


    @Override
    public void afterPropertiesSet() throws Exception {
        serverConfig = applicationContext.getBean(ServerConfig.class);

        if (serverConfig == null){
            throw new IllegalStateException("no server configs: " + serverConfig);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        logger.info("driving sever starting");

        NetworkServerFactory factory = new NetworkServerFactory(serverConfig.getPort());

        NettyServer server = factory.getServer(NetworkEnum.getNetworkType(serverConfig.getNetType()));

        server.init();

        server.start(new Listener() {
            @Override
            public void onSuccess(Object... args) {
                logger.info("driving sever start success");
            }

            @Override
            public void onFailure(Throwable cause) {
                logger.error("driving sever start error,cause {}",cause);
            }
        });

    }

}
