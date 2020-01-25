package com.fh.framework.server;

import com.fh.framework.listener.Listener;

/**
 * @ClassName:Server
 * @Description: 服务接口
 * @Author: shanzheng
 * @Date: 2019/12/17 11:11
 * @Version:1.0
 **/
public interface Server {

    void init();

    void start(Listener listener);

    void stop(Listener listener);

}
