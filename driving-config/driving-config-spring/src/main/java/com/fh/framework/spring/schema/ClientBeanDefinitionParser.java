package com.fh.framework.spring.schema;

import com.fh.framework.config.ClientConfig;
import com.fh.framework.config.RegisterConfig;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Iterator;
import java.util.List;

/**
 * @ClassName:DrivingBeanDefinitionParser
 * @Description: 解析注册的xml节点
 * @Author: shanzheng
 * @Date: 2019/12/30 15:25
 * @Version:1.0
 **/
public class ClientBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {

    private static final String DEFAULT_NETTYPE = "tcp";

    @Override
    protected Class<?> getBeanClass(Element element) {
        return ClientConfig.class;
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

        builder.addPropertyValue("parameter", this.parseChildSequences(element));
    }


    private ManagedMap parseChildSequences(Element element) {
        NodeList nodeList = element.getChildNodes();
        if (nodeList != null && nodeList.getLength() > 0) {
            ManagedMap parameters = null;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node instanceof Element) {
                    if ("strategy".equals(node.getNodeName())
                            || "strategy".equals(node.getLocalName())) {
                        if (parameters == null) {
                            parameters = new ManagedMap();
                        }
                        String message = ((Element) node).getAttribute("key");
                        String messagehandle = ((Element) node).getAttribute("ref");
                        parameters.put(message, new TypedStringValue(messagehandle, String.class));
                    }
                }
            }
            return parameters;
        }
        return null;
    }


}



