package cn.myauthx.api.base.exception;

import cn.myauthx.api.base.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 * @author DaenMax
 */
@RestControllerAdvice
@Slf4j
public class MyExceptionHandle {

    /**
     * 处理自定义异常
     * @param e AbpException
     * @return Result
     */
    @ExceptionHandler(MyException.class)
    public Result<?> abpException(MyException e) {
        //log.error(e.getMessage(), e);
        log.info(e.getMessage());
        return Result.error(e.getMessage());
    }



    /**
     * NotBlank异常处理
     * @param e MethodArgumentNotValidException
     * @return Result
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> argumentException(MethodArgumentNotValidException e){
        log.error(e.getMessage(), e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return Result.error("操作失败，" + e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * 入参格式错误导致的序列化失败
     * @param e HttpMessageNotReadableException
     * @return Result
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> jsonParseException(HttpMessageNotReadableException e) {
        log.warn("参数格式错误", e);
        return Result.error("参数格式错误");
    }

    /**
     * 请求参数不全
     * @param e MissingServletRequestParameterException
     * @return Result
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<?> missingRequestParam(MissingServletRequestParameterException e) {
        log.warn("请求参数不全", e);
        return Result.error("参数不全，请检查后重试");
    }

    /**
     * 以上未处理的异常
     * @param e Exception
     * @return Result
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e){
        log.error("未处理的异常信息：", e);
        return Result.error("操作失败，请联系管理员");
    }

}
