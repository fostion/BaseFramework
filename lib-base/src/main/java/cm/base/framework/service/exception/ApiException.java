package cm.base.framework.service.exception;

/**
 * 接口错误异常
 */

public class ApiException extends RuntimeException {

    int code;
    String msg;

    public ApiException(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

}
