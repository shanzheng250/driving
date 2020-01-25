package com.fh.framework.config;

import com.fh.framework.constant.NetworkEnum;
import com.fh.framework.receiver.ExchangeMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName:ClientConfig
 * @Description: 客户端配置
 * @Author: shanzheng
 * @Date: 2019/12/30 11:19
 * @Version:1.0
 **/
public class ClientConfig extends BaseConfig implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(ClientConfig.class);

    /* 用户自定义的参数 */
    private Map<String,String> parameter;

    private HashMap<String,ExchangeMessageHandler> customizer = new HashMap<>(10);

    /* spring 上下文 */
    private ApplicationContext applicationContext;

    public Map<String, String> getParameter() {
        return parameter;
    }

    public void setParameter(Map<String, String> parameter) {
        this.parameter = parameter;
    }

    /**
     * 验证客户端提供的自定义策略是否存在
     * @return
     */
    public boolean initAndValid(){
        if (parameter == null){
            return true;
        }

        for (Map.Entry<String,String> entry : parameter.entrySet()){

            String id = entry.getValue();

            Object o =  applicationContext.getBean(id);

            if (o == null){

                logger.error("no strategy handle class name [{}] find",id);

                return false;
            }

            if (!(o instanceof ExchangeMessageHandler)){
                logger.error("ref [{}] is not a message handle",o);

                return false;
            }

            customizer.put(entry.getKey(), (ExchangeMessageHandler) o);
        }

        return true;
    }

    public HashMap<String, ExchangeMessageHandler> getCustomizer() {
        return customizer;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
