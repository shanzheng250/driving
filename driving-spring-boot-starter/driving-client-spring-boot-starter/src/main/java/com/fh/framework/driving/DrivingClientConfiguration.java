package com.fh.framework.driving;

import com.fh.framework.bootstrap.ClientStarter;
import com.fh.framework.bootstrap.ExchangeContainer;
import com.fh.framework.bootstrap.ExchangeInitializer;
import com.fh.framework.bootstrap.RegisterProperties;
import com.fh.framework.constant.NetworkEnum;
import com.fh.framework.receiver.ExchangeMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName:DrivingClientConfiguration
 * @Description:
 * @Author: shanzheng
 * @Date: 2020/1/3 16:18
 * @Version:1.0
 **/
@Configuration
@EnableConfigurationProperties({RegistryProperties.class,DrivingClientProperties.class})
@ConditionalOnProperty(
        value = {"driving.client.enabled"},
        matchIfMissing = true
)
public class DrivingClientConfiguration {

    private RegistryProperties registryProperties;

    private DrivingClientProperties drivingClientProperties;

    // 构造注入
    public DrivingClientConfiguration(RegistryProperties registryProperties, DrivingClientProperties drivingClientProperties) {
        this.registryProperties = registryProperties;
        this.drivingClientProperties = drivingClientProperties;
    }


    @Autowired(
            required = false
    )
    private List<ExchangeMessageHandler> exchangeMessageHandlers = Collections.emptyList();


    @Bean
    public ClientStarter getClientStarter(){

        ClientStarter clientStarter = new ClientStarter(drivingClientProperties.getPort(),drivingClientProperties.getHost());

        HashMap<String,ExchangeMessageHandler> strategys  = getCustomizer();

        clientStarter
                .init(NetworkEnum.getNetworkType(drivingClientProperties.getNetType()))
                .registerExchange(new ExchangeInitializer() {
                    @Override
                    protected ExchangeContainer initExchange(ExchangeContainer container) {

                        if (!strategys.isEmpty()){
                            container.addStrategy(strategys);
                        }

                        return container;
                    }
                })
                .registerClientProperties(new RegisterProperties(registryProperties.getSysid(),
                        registryProperties.getAreaCode(),
                        registryProperties.getUserCode()));

        return clientStarter;
    }


    /**
     * 处理定制化的需求
     * @return
     */
    private HashMap<String,ExchangeMessageHandler> getCustomizer(){

        HashMap<String,ExchangeMessageHandler> messageHandlerHashMap = new HashMap<>();

        if (!this.exchangeMessageHandlers.isEmpty()){
            exchangeMessageHandlers.forEach(exchangeMessageHandler -> {
                messageHandlerHashMap.put(exchangeMessageHandler.aliasName,exchangeMessageHandler);
            });
       }
       return messageHandlerHashMap;
    }


}
