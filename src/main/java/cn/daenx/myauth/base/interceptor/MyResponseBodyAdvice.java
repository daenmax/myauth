package cn.daenx.myauth.base.interceptor;


import cn.daenx.myauth.main.entity.Soft;
import cn.daenx.myauth.util.CheckUtils;
import cn.daenx.myauth.util.MyUtils;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.enums.NoEncryptNoSignEnums;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;

/**
 * 全局拦截返回值
 *
 * @author DaenMax
 */
@ControllerAdvice
public class MyResponseBodyAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        //一般情况下，如果body不是我们返回的Result类型，而是LinkedHashMap类型，说明报错了，例如路由不存在之类的
        if(body.getClass().equals(LinkedHashMap.class)){
            LinkedHashMap linkedHashMap = (LinkedHashMap) body;
            Integer status = (Integer) linkedHashMap.get("status");
            String msg = (String) linkedHashMap.get("error");
            return Result.error(status,msg);
        }
        //形如：/myauth/soft/connect
        String requestPath = request.getURI().getPath();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession httpSession = httpServletRequest.getSession(true);
        //放到缓存里，以便于可以在HandlerInterceptor拦截里取出并打印出返回结果
        httpSession.setAttribute("body", body);
        Integer NoEncryptNoSign = 0;
        if(CheckUtils.isObjectEmpty(httpServletRequest.getAttribute("NoEncryptNoSign"))){
            NoEncryptNoSign = 0;
        }else{
            NoEncryptNoSign = Integer.parseInt(String.valueOf(httpServletRequest.getAttribute("NoEncryptNoSign")));
        }
        //如果不是开放API，那么返回结果需要加签，具体要不要加密在于软件的genStatus，在这里实现全局拦截返回结果并修改后放行
        if (NoEncryptNoSignEnums.NO.getCode().equals(NoEncryptNoSign)) {
            Soft soft = (Soft) httpServletRequest.getAttribute("obj_soft");
            Result result = (Result) body;
            JSONObject retJson = (JSONObject) result.getResult();
            if (retJson != null) {
                body = MyUtils.calculateSignReturn(result.getMsg(), retJson, soft.getGenStatus(), soft.getGenKey());
            }
        }
        return body;
    }
}
