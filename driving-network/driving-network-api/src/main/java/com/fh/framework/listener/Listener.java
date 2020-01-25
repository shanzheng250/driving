package com.fh.framework.listener;

/**
 * @ClassName:Listener
 * @Description: 用来回调处理
 * @Author: shanzheng
 * @Date: 2019/12/17 11:07
 * @Version:1.0
 **/
public interface Listener {

    void onSuccess(Object... args);

    void onFailure(Throwable cause);
}
