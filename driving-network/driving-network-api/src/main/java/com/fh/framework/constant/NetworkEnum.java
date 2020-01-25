package com.fh.framework.constant;

import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName:NetworkEnum
 * @Description: 服务
 * @Author: shanzheng
 * @Date: 2019/12/25 13:42
 * @Version:1.0
 **/
public enum NetworkEnum {

    TCP,

    WEBSOCKET;

    /**
     * 功能描述
     * @param:
     * @return:
     * @date: 2019/10/15 14:01
     */
    public static NetworkEnum getNetworkType(String type){
        // 没有默认为tcp
        if (StringUtils.isBlank(type)){
            return TCP;
        }

        for(NetworkEnum e :values()){
            if(StringUtils.equalsIgnoreCase(e.toString(), type)){
                return e;
            }
        }
        throw new RuntimeException("该network type【"+type+"】找不到对应的初始化对象！");
    }


}
