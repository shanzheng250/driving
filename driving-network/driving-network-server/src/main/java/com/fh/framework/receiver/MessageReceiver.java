package com.fh.framework.receiver;

import com.fh.framework.codec.Command;
import com.fh.framework.constant.StatusEnum;
import com.fh.framework.message.ErrorMessage;
import com.fh.framework.packet.Packet;
import com.fh.framework.session.Connection;
import com.fh.framework.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @ClassName:MessageReceiver
 * @Description: 用来保存packet和处理器之间的关系
 * @Author: shanzheng
 * @Date: 2019/12/19 10:46
 * @Version:1.0
 **/
public final class MessageReceiver {

    private static final Logger logger = LoggerFactory.getLogger(MessageReceiver.class);

    private final Map<Byte, IMessageHandler> handlers = new HashMap<>();

    /**
     * 分发处理
     * @param packet
     * @param connection
     */
    public void onReceive(Packet packet, Connection connection) {
        IMessageHandler handler = handlers.get(packet.cmd);
        if (handler != null) {
            Long startTime = CommonUtils.getCurTime();
            try {
                handler.handle(packet, connection);
            } catch (Exception e) {
                logger.error("dispatch message ex, packet={}, connect={}, body={}", packet, connection, packet.getBody(), e);
                new ErrorMessage(packet, connection)
                        .setErrorCode(StatusEnum.DISPATCH_ERROR)
                        .send(null);
            } finally {
                logger.info("dispatch packet处理完成耗时为:{}ms",CommonUtils.getCurTime() - startTime);
            }
        } else {
            logger.info("dispatch message failure, cmd={} unsupported, packet={}, connect={}, body={}" , Command.toCMD(packet.cmd), packet, connection);
            new ErrorMessage(packet, connection)
                    .setErrorCode(StatusEnum.DISPATCH_ERROR)
                    .setReason("no command")
                    .send(null);
        }
    }

    /**
     * 注册到分发器上
     * @param command
     * @param handler
     */
    public void register(Command command, IMessageHandler handler) {
        handlers.put(command.cmd, handler);
    }

    public void register(Command command, Supplier<IMessageHandler> handlerSupplier) {
        if (!handlers.containsKey(command.cmd)) {
            register(command, handlerSupplier.get());
        }
    }

    public IMessageHandler unRegister(Command command) {
        return handlers.remove(command.cmd);
    }

}
