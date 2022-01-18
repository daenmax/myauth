package cn.myauthx.api.base.interceptor;

import cn.myauthx.api.base.annotation.*;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Admin;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.entity.Version;
import cn.myauthx.api.main.enums.AdminEnums;
import cn.myauthx.api.main.enums.OpenEnums;
import cn.myauthx.api.main.enums.SoftEnums;
import cn.myauthx.api.main.enums.VersionEnums;
import cn.myauthx.api.main.mapper.SoftMapper;
import cn.myauthx.api.util.AESUtils;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.MyUtils;
import cn.myauthx.api.util.RedisUtil;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
public class MyInterceptor implements HandlerInterceptor {
    @Autowired
    private SoftMapper softMapper;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Content-Type","application/json; charset=utf-8");
        //判断请求的方法上是否有注解
        boolean haveAnnotation = handler.getClass().isAssignableFrom(HandlerMethod.class);
        //如果有注解
        if (haveAnnotation){
            JSONObject jsonObject = new JSONObject(true);
            String reqStr = MyUtils.getRequestPostStr(request);
            if(!CheckUtils.isObjectEmpty(reqStr)){
                jsonObject = JSONObject.parseObject(reqStr);
            }
            request.setAttribute("json",jsonObject);

            if(((HandlerMethod)handler).getMethodAnnotation(OpenApi.class) != null){
                request.setAttribute("open", OpenEnums.YES.getCode());
            }else{
                request.setAttribute("open",OpenEnums.NO.getCode());
            }
            //@AdminAuth
            if(((HandlerMethod)handler).getMethodAnnotation(AdminAuth.class) != null){
                String token = request.getHeader("token");
                if(CheckUtils.isObjectEmpty(token)){
                    response.getWriter().write(Result.error("非法请求").toJsonString());
                    return false;
                }
                Admin admin = (Admin) redisUtil.get("admin_" + token);
                if(CheckUtils.isObjectEmpty(admin)){
                    response.getWriter().write(Result.error("非法请求").toJsonString());
                    return false;
                }
                if(admin.getLastTime() + AdminEnums.TOKEN_VALIDITY.getCode() < Integer.parseInt(MyUtils.getTimeStamp())){
                    response.getWriter().write(Result.error("登录失效，请重新登录").toJsonString());
                    return false;
                }
            }

            //@SoftValidated
            if(((HandlerMethod)handler).getMethodAnnotation(SoftValidated.class) != null){
                String skey = jsonObject.getString("skey");
                if(CheckUtils.isObjectEmpty(skey)){
                    response.getWriter().write(Result.error("缺少skey参数").toJsonString());
                    return false;
                }

                Soft soft = (Soft) redisUtil.get("soft_" + skey);
                if(CheckUtils.isObjectEmpty(soft)){
                    response.getWriter().write(Result.error("skey错误").toJsonString());
                    return false;
                }
                if(SoftEnums.STATUS_DISABLE.getCode().equals(soft.getStatus())){
                    response.getWriter().write(Result.error("软件已停用").toJsonString());
                    return false;
                }
                if(SoftEnums.STATUS_FIX.getCode().equals(soft.getStatus())){
                    response.getWriter().write(Result.error("软件维护中").toJsonString());
                    return false;
                }
                request.setAttribute("obj_soft",soft);
            }
            //@VersionValidated
            if(((HandlerMethod)handler).getMethodAnnotation(VersionValidated.class) != null){
                String vkey = jsonObject.getString("vkey");
                if(CheckUtils.isObjectEmpty(vkey)){
                    response.getWriter().write(Result.error("缺少vkey参数").toJsonString());
                    return false;
                }

                Version version = (Version) redisUtil.get("version_" + vkey);
                if(CheckUtils.isObjectEmpty(version)){
                    response.getWriter().write(Result.error("vkey错误").toJsonString());
                    return false;
                }
                if(VersionEnums.STATUS_DISABLE.getCode().equals(version.getStatus())){
                    response.getWriter().write(Result.error("版本已停用").toJsonString());
                    return false;
                }
                request.setAttribute("obj_version",version);
            }

            //@DataDecrypt
            if(((HandlerMethod)handler).getMethodAnnotation(DataDecrypt.class) != null){
                if(CheckUtils.isObjectEmpty(reqStr)){
                    response.getWriter().write(Result.error("获取请求数据失败").toJsonString());
                    return false;
                }
                String skey = jsonObject.getString("skey");
                Soft soft = (Soft) redisUtil.get("soft_" + skey);
                if(CheckUtils.isObjectEmpty(soft)){
                    response.getWriter().write(Result.error("skey错误").toJsonString());
                    return false;
                }
                request.setAttribute("obj_soft",soft);
                if(soft.getGenStatus() == 1){
                    String jsonStr = AESUtils.decrypt(jsonObject.getString("data"),soft.getGenKey());
                    if(CheckUtils.isObjectEmpty(jsonStr)){
                        response.getWriter().write(Result.error("数据解密失败").toJsonString());
                        return false;
                    }
                    Object object = JSONObject.parseObject(jsonStr);
                    jsonObject.put("data",object);
                }
                request.setAttribute("json",jsonObject);
            }
            log.info("接收->" + jsonObject.toJSONString());
            //@SignValidated
            if(((HandlerMethod)handler).getMethodAnnotation(SignValidated.class) != null){
                String sign = jsonObject.getString("sign");
                if(CheckUtils.isObjectEmpty(sign)){
                    response.getWriter().write(Result.error("缺少sign参数").toJsonString());
                    return false;
                }
                String skey = jsonObject.getString("skey");
                Soft soft = (Soft) redisUtil.get("soft_" + skey);
                if(CheckUtils.isObjectEmpty(soft)){
                    response.getWriter().write(Result.error("skey错误").toJsonString());
                    return false;
                }
                request.setAttribute("obj_soft",soft);
                String privateSign = MyUtils.calculateSign(jsonObject.getJSONObject("data"),soft.getGenKey());
                if(!privateSign.equals(sign)){
                    response.getWriter().write(Result.error("sign错误").toJsonString());
                    return false;
                }
            }

            return true;
        }
        return true;
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if(handler instanceof HandlerMethod){
            //取出在ResponseBodyAdvice中设置的body
            Result result = (Result) request.getSession().getAttribute("body");
            log.info("响应->" + result.toJsonString());
        }

    }
}
