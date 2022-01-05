package cn.myauthx.api.base.interceptor;

import cn.myauthx.api.base.annotation.Admin;
import cn.myauthx.api.base.vo.Result;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyInterceptor implements HandlerInterceptor {
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
            Admin admin = ((HandlerMethod)handler).getMethodAnnotation(Admin.class);
            //判断是否有admin注解
            if(admin != null){
                String id = request.getParameter("id");
                if("1".equals(id)){
                    return true;
                }else{
                    response.getWriter().write(Result.error("你没有权限").toJsonString());
                    return false;
                }
            }
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
