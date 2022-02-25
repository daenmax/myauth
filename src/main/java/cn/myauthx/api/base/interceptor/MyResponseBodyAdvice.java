package cn.myauthx.api.base.interceptor;


import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.enums.OpenApiEnums;
import cn.myauthx.api.util.MyUtils;
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
        //形如：/myauth/soft/connect
        String requestPath = request.getURI().getPath();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession httpSession = httpServletRequest.getSession(true);
        //放到缓存里，以便于可以在HandlerInterceptor拦截里取出并打印出返回结果
        httpSession.setAttribute("body", body);
        Integer open = Integer.parseInt(String.valueOf(httpServletRequest.getAttribute("open")));
        //如果不是开放API，那么返回结果需要加签，具体要不要加密在于软件的genStatus，在这里实现全局拦截返回结果并修改后放行
        if (OpenApiEnums.NO.getCode().equals(open)) {
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
