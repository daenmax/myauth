package cn.daenx.myauth.base.exception;

/**
 * 自定义异常处理
 * @author DaenMax
 */
public class MyException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MyException(String message) {
        super(message);
    }
}
