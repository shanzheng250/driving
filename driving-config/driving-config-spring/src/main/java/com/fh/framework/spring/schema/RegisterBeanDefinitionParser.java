package com.fh.framework.spring.schema;

import com.fh.framework.config.RegisterConfig;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import static com.fh.framework.config.DefaultConfig.*;

/**
 * @ClassName:DrivingBeanDefinitionParser
 * @Description: 解析注册的xml节点
 * @Author: shanzheng
 * @Date: 2019/12/30 15:25
 * @Version:1.0
 **/
public class RegisterBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {



    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        builder.setLazyInit(false);
        String sysid = element.getAttribute("sysid");
        String areaCode = element.getAttribute("areacode");
        String userCode = element.getAttribute("usercode");

        if (StringUtils.hasText(sysid)) {
            builder.addPropertyValue("sysid", sysid);
        }else {
            builder.addPropertyValue("sysid", DEFAULT_SYSID);
        }

        if (StringUtils.hasText(areaCode)) {
            builder.addPropertyValue("areaCode", areaCode);
        }else {
            builder.addPropertyValue("areaCode", DEFAULT_AREACODE);
        }

        if (StringUtils.hasText(userCode)) {
            builder.addPropertyValue("userCode", userCode);
        }else {
            builder.addPropertyValue("userCode", DEFAULT_USERCODE);
        }
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        return RegisterConfig.class;
    }
}



