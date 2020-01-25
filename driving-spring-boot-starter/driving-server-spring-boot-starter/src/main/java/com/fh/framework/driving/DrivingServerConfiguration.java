package com.fh.framework.driving;

import com.fh.framework.constant.NetworkEnum;
import com.fh.framework.factory.NetworkServerFactory;
import com.fh.framework.listener.Listener;
import com.fh.framework.server.NettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName:DrivingClientConfiguration
 * @Description:
 * @Author: shanzheng
 * @Date: 2020/1/3 16:18
 * @Version:1.0
 **/
@Configuration
@EnableConfigurationProperties({DrivingServerProperties.class})
@ConditionalOnProperty(
        value = {"driving.server.enabled"},
        matchIfMissing = true
)
public class DrivingServerConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DrivingServerConfiguration.class);

    private DrivingServerProperties drivingClientProperties;

    // 构造注入
    public DrivingServerConfiguration(DrivingServerProperties drivingClientProperties) {
        this.drivingClientProperties = drivingClientProperties;
    }


    @Bean
    public NettyServer getClientStarter(){

        NetworkServerFactory factory = new NetworkServerFactory(drivingClientProperties.getPort());

        NettyServer server = factory.getServer(NetworkEnum.getNetworkType(drivingClientProperties.getNetType()));

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

        return server;
    }

}
