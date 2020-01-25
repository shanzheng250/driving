package com.fh.framework.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName:SessionContext
 * @Description: 回话信息保存
 * @Author: shanzheng
 * @Date: 2019/12/18 16:55
 * @Version:1.0
 **/
public class SessionContext {

    private static final Logger logger = LoggerFactory.getLogger(SessionContext.class);

    /* 保存回话内容 */
    private Map<String,Connection> context = new ConcurrentHashMap<>();


    private SessionContext() {
    }

    /**
     * 获取实例
     * @return
     */
    public static SessionContext getSessionInstance(){
        return SessionInstance.sessionContext;
    }

    /**
     * 新增链接
     * @param connection
     */
    public void put(Connection connection){
        context.put(connection.getId(),connection);
    }

    /**
     * 清除链接
     * @param connection
     */
    public void remove(Connection connection){
        context.remove(connection.getId());
    }

    /**
     * 清除链接
     * @param channelId
     */
    public void remove(String channelId){
        context.entrySet().forEach(entry->{
            if (entry.getValue().isChannel(channelId)){
                remove(entry.getValue());
            }
        });
    }

    /**
     * 获取链接
     * @param id
     * @return
     */
    public Connection getConnection(String id){
        return context.get(id);
    }

    /**
     * 模糊匹配
     * @param match
     * @return
     */
    public List<Connection> getConnections(String match, String exclude) {

        List<Connection> connections = new ArrayList<>();

        context.entrySet().forEach(entry -> {
            if (entry.getKey().matches(match) && !entry.getValue().getChannel().id().equals(exclude)){
                connections.add(entry.getValue());
            }
        });

        return connections;
    }

    /**
     * 所有的业务链接
     * @return
     */
    public List<Connection> getAllConnections(String exclude){
        List<Connection> connections = new ArrayList<>();

        context.entrySet().forEach(entry -> {
            // 排除掉相应的链接
            if (!entry.getValue().getChannel().id().asLongText().equals(exclude)){
                connections.add(entry.getValue());
            }

        });
        return connections;
    }

    /**
     * 单例静态内部类
     */
    private static class SessionInstance{

        private static SessionContext sessionContext = new SessionContext();

    }
}
