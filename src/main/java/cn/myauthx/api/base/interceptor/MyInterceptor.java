package cn.myauthx.api.base.interceptor;

import cn.myauthx.api.base.annotation.DataDecrypt;
import cn.myauthx.api.base.annotation.Admin;
import cn.myauthx.api.base.annotation.SignValidated;
import cn.myauthx.api.base.annotation.SoftValidated;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.mapper.SoftMapper;
import cn.myauthx.api.util.AESUtils;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.MyUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyInterceptor implements HandlerInterceptor {
    @Autowired
    private SoftMapper softMapper;

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
        response.setHeader("content-type","application/json; charset=utf-8");
        //判断请求的方法上是否有注解
        boolean haveAnnotataion = handler.getClass().isAssignableFrom(HandlerMethod.class);
        //如果有注解，判断是否是MyAnnotation
        if (haveAnnotataion){
            JSONObject jsonObject = new JSONObject(true);
            String reqStr = MyUtils.getRequestPostStr(request);
            if(!CheckUtils.isObjectEmpty(reqStr)){
                jsonObject = JSONObject.parseObject(reqStr);
            }
            request.setAttribute("json",jsonObject);
            //@SoftValidated
            if(((HandlerMethod)handler).getMethodAnnotation(SoftValidated.class) != null){
                String skey = jsonObject.getString("skey");
                if(CheckUtils.isObjectEmpty(skey)){
                    response.getWriter().write(Result.error("缺少skey参数").toJsonString());
                    return false;
                }
                LambdaQueryWrapper<Soft> softLambdaQueryWrapper = new LambdaQueryWrapper<>();
                softLambdaQueryWrapper.eq(Soft::getSkey,skey);
                Soft soft = softMapper.selectOne(softLambdaQueryWrapper);
                if(CheckUtils.isObjectEmpty(soft)){
                    response.getWriter().write(Result.error("skey错误").toJsonString());
                    return false;
                }
                if(soft.getStatus() == 0){
                    response.getWriter().write(Result.error("软件已停用").toJsonString());
                    return false;
                }
                if(soft.getStatus() == 2){
                    response.getWriter().write(Result.error("软件维护中").toJsonString());
                    return false;
                }
                request.setAttribute("obj_soft",soft);
            }

            //@DataDecrypt
            if(((HandlerMethod)handler).getMethodAnnotation(DataDecrypt.class) != null){
                if(CheckUtils.isObjectEmpty(reqStr)){
                    response.getWriter().write(Result.error("获取请求数据失败").toJsonString());
                    return false;
                }
                Soft soft = (Soft) request.getAttribute("obj_soft");
                //如果没有@SoftValidated注解，那么就重新获取soft对象
                if(CheckUtils.isObjectEmpty(soft)){
                    String skey = jsonObject.getString("skey");
                    if(CheckUtils.isObjectEmpty(skey)){
                        response.getWriter().write(Result.error("缺少skey参数").toJsonString());
                        return false;
                    }
                    LambdaQueryWrapper<Soft> softLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    softLambdaQueryWrapper.eq(Soft::getSkey,skey);
                    soft = softMapper.selectOne(softLambdaQueryWrapper);
                    if(CheckUtils.isObjectEmpty(soft)){
                        response.getWriter().write(Result.error("skey错误").toJsonString());
                        return false;
                    }
                    request.setAttribute("obj_soft",soft);
                }
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
            //@SignValidated
            if(((HandlerMethod)handler).getMethodAnnotation(SignValidated.class) != null){
                String sign = jsonObject.getString("sign");
                if(CheckUtils.isObjectEmpty(sign)){
                    response.getWriter().write(Result.error("缺少sign参数").toJsonString());
                    return false;
                }
                Soft soft = (Soft) request.getAttribute("obj_soft");
                //如果没有@SoftValidated注解，那么就重新获取soft对象
                if(CheckUtils.isObjectEmpty(soft)){
                    String skey = jsonObject.getString("skey");
                    if(CheckUtils.isObjectEmpty(skey)){
                        response.getWriter().write(Result.error("缺少skey参数").toJsonString());
                        return false;
                    }
                    LambdaQueryWrapper<Soft> softLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    softLambdaQueryWrapper.eq(Soft::getSkey,skey);
                    soft = softMapper.selectOne(softLambdaQueryWrapper);
                    if(CheckUtils.isObjectEmpty(soft)){
                        response.getWriter().write(Result.error("skey错误").toJsonString());
                        return false;
                    }
                    request.setAttribute("obj_soft",soft);
                }
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
    }
}
