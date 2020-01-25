package com.fh.framework.codec;

/**
 * @ClassName:Command
 * @Description: 消息命令
 * @Author: shanzheng
 * @Date: 2019/12/16 10:18
 * @Version:1.0
 **/
public enum  Command {
    /* 心跳 */
    HEARTBEAT(1),

    /* 用户绑定 */
    USERBIND(2),

    /* 错误 */
    ERROR(10),

    /* 成功 */
    OK(11),

    /* 消息推送 */
    PUSH(15),

    /* 未知 */
    UNKNOWN(-1);


    Command(int cmd) {
        this.cmd = (byte) cmd;
    }

    public final byte cmd;

    /**
     * 功能描述
     * @param:
     * @return:
     * @date: 2019/10/15 14:01
     */
    public static Command toCMD(byte b){
        for(Command e :values()){
            if(b == e.cmd){
                return e;
            }
        }
        return UNKNOWN;
    }

    /**
     * 返回cmd
     * @return
     */
    public byte getCmd() {
        return cmd;
    }
}
