package com.fh.framework.constant;

/**
 * @ClassName:StatusEnum
 * @Description: TODO
 * @Author: shanzheng
 * @Date: 2019/12/10 13:56
 * @Version:1.0
 **/
public enum StatusEnum {
    SUCCESS("200","成功"),

    ERROR_SYSTEM("500","系统内部处理异常"),

    ERROR_PARAM("585","参数校验异常"),

    ERROR_AUTH("401","鉴权失败"),

    ERROR_TOKEN_EXPRIE("401","TOKEN过期"),

    ERROR_REQUSET_METHOD("586","请求方法错误"),

    ERROR_EXIST("1001","已存在"),

    EROOR_NOT_EXIST("1002","不存在"),

    DISPATCH_ERROR("3001","分发异常"),

    EROOR_OTHER("9999","其他异常");


    private String status;
    private String msg;
    /**
     * @param status
     * @param msg
     */
    private StatusEnum(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }
    public String getMsg() {
        return msg;
    }
}
