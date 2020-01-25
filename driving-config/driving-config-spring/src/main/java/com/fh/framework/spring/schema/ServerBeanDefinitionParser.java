package com.fh.framework.spring.schema;

import com.fh.framework.config.ClientConfig;
import com.fh.framework.config.ServerConfig;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @ClassName:DrivingBeanDefinitionParser
 * @Description: 解析注册的xml节点
 * @Author: shanzheng
 * @Date: 2019/12/30 15:25
 * @Version:1.0
 **/
public class ServerBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {


    private static final String DEFAULT_NETTYPE = "tcp";

    @Override
    protected Class<?> getBeanClass(Element element) {
        return ServerConfig.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        builder.setLazyInit(false);
        String host = element.getAttribute("host");
        String port = element.getAttribute("port");
        String netType = element.getAttribute("type");

        if (StringUtils.hasText(host)) {
            builder.addPropertyValue("host", host);
        }

        if (StringUtils.hasText(port)) {
            builder.addPropertyValue("port", port);
        }

        if (StringUtils.hasText(netType)) {
            builder.addPropertyValue("netType", netType);
        }else {
            builder.addPropertyValue("netType", DEFAULT_NETTYPE);
        }

    }

}



