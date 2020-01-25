package com.fh.framework.spring.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @ClassName:DrivingNameSpaceHandle
 * @Description: 注册spring解析xml
 * @Author: shanzheng
 * @Date: 2019/12/30 16:09
 * @Version:1.0
 **/
public class DrivingNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {
        registerBeanDefinitionParser("registry",new RegisterBeanDefinitionParser());
        registerBeanDefinitionParser("server",new ServerBeanDefinitionParser());
        registerBeanDefinitionParser("client",new ClientBeanDefinitionParser());
    }
}
