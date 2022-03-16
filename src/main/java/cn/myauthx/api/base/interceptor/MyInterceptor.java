package cn.myauthx.api.base.interceptor;

import cn.myauthx.api.base.annotation.*;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.*;
import cn.myauthx.api.main.enums.*;
import cn.myauthx.api.main.mapper.AdminMapper;
import cn.myauthx.api.main.mapper.UserMapper;
import cn.myauthx.api.util.*;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class MyInterceptor implements HandlerInterceptor {
    @Resource
    private AdminMapper adminMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisUtil redisUtil;
    @Value("${genKey}")
    private String genKey;

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = IpUtil.getIpAddr(request);
        response.setHeader("Content-Type", "application/json; charset=utf-8");
        //判断请求的方法上是否有注解
        boolean haveAnnotation = handler.getClass().isAssignableFrom(HandlerMethod.class);
        //如果有注解
        if (haveAnnotation) {
            JSONObject jsonObject = new JSONObject(true);
            String reqStr = MyUtils.getRequestPostStr(request);
            if (!CheckUtils.isObjectEmpty(reqStr)) {
                jsonObject = JSONObject.parseObject(reqStr);
            }
            request.setAttribute("json", jsonObject);
            //判断是否是注明了此接口不加密不验签
            if (((HandlerMethod) handler).getMethodAnnotation(NoEncryptNoSign.class) != null) {
                request.setAttribute("NoEncryptNoSign", NoEncryptNoSignEnums.YES.getCode());
            } else {
                request.setAttribute("NoEncryptNoSign", NoEncryptNoSignEnums.NO.getCode());
            }
            //@AdminLogin
            if (((HandlerMethod) handler).getMethodAnnotation(AdminLogin.class) != null) {
                String token = request.getHeader("token");
                if (CheckUtils.isObjectEmpty(token)) {
                    log.info("接收->" + jsonObject.toJSONString());
                    String retStr = Result.error(401, "非法请求").toJsonString();
                    log.info("响应->" + retStr);
                    response.getWriter().write(retStr);
                    return false;
                }
                Admin admin = (Admin) redisUtil.get("admin:" + token);
                if (CheckUtils.isObjectEmpty(admin)) {
                    log.info("接收->" + jsonObject.toJSONString());
                    String retStr = Result.error(402, "token无效，请重新登录").toJsonString();
                    log.info("响应->" + retStr);
                    response.getWriter().write(retStr);
                    return false;
                }
                if (admin.getStatus().equals(AdminEnums.STATUS_DISABLE.getCode())) {
                    log.info("接收->" + jsonObject.toJSONString());
                    String retStr = Result.error(405, "账号已被禁用").toJsonString();
                    log.info("响应->" + retStr);
                    response.getWriter().write(retStr);
                    return false;
                }
                if (admin.getLastTime() + AdminEnums.TOKEN_VALIDITY.getCode() < Integer.parseInt(MyUtils.getTimeStamp())) {
                    log.info("接收->" + jsonObject.toJSONString());
                    String retStr = Result.error(403, "登录状态失效，请重新登录").toJsonString();
                    log.info("响应->" + retStr);
                    response.getWriter().write(retStr);
                    return false;
                }
                AdminLogin methodAnnotation = ((HandlerMethod) handler).getMethodAnnotation(AdminLogin.class);
                boolean is_super_role = methodAnnotation.is_super_role();
                if(is_super_role){
                    Role role = (Role) redisUtil.get("role:" + admin.getRole());
                    if(role.getFromSoftId() != 0){
                        log.info("接收->" + jsonObject.toJSONString());
                        String retStr = Result.error(500, "你没有权限[1001]").toJsonString();
                        log.info("响应->" + retStr);
                        response.getWriter().write(retStr);
                        return false;
                    }
                }
                boolean is_admin = methodAnnotation.is_admin();
                if(is_admin){
                    if (!"admin".equals(admin.getUser())) {
                        log.info("接收->" + jsonObject.toJSONString());
                        String retStr = Result.error(500, "你没有权限[1002]").toJsonString();
                        log.info("响应->" + retStr);
                        response.getWriter().write(retStr);
                        return false;
                    }
                }

                request.setAttribute("obj_admin", admin);
            }


            //@SoftValidated
            if (((HandlerMethod) handler).getMethodAnnotation(SoftValidated.class) != null) {
                String skey = jsonObject.getString("skey");
                if (CheckUtils.isObjectEmpty(skey)) {
                    log.info("接收->" + jsonObject.toJSONString());
                    String retStr = Result.error("缺少skey参数").toJsonString();
                    log.info("响应->" + retStr);
                    response.getWriter().write(retStr);
                    return false;
                }

                Soft soft = (Soft) redisUtil.get("soft:" + skey);
                if (CheckUtils.isObjectEmpty(soft)) {
                    log.info("接收->" + jsonObject.toJSONString());
                    String retStr = Result.error("skey错误").toJsonString();
                    log.info("响应->" + retStr);
                    response.getWriter().write(retStr);
                    return false;
                }
                if (SoftEnums.STATUS_DISABLE.getCode().equals(soft.getStatus())) {
                    log.info("接收->" + jsonObject.toJSONString());
                    String retStr = Result.error("软件已停用").toJsonString();
                    log.info("响应->" + retStr);
                    response.getWriter().write(retStr);
                    return false;
                }
                if (SoftEnums.STATUS_FIX.getCode().equals(soft.getStatus())) {
                    log.info("接收->" + jsonObject.toJSONString());
                    String retStr = Result.error("软件维护中").toJsonString();
                    log.info("响应->" + retStr);
                    response.getWriter().write(retStr);
                    return false;
                }
                request.setAttribute("obj_soft", soft);
            }
            //@VersionValidated
            if (((HandlerMethod) handler).getMethodAnnotation(VersionValidated.class) != null) {
                Soft soft = (Soft) request.getAttribute("obj_soft");
                String vkey = jsonObject.getString("vkey");
                if (CheckUtils.isObjectEmpty(vkey)) {
                    log.info("接收->" + jsonObject.toJSONString());
                    String retStr = Result.error("缺少vkey参数").toJsonString();
                    log.info("响应->" + retStr);
                    response.getWriter().write(retStr);
                    return false;
                }
                Version version = (Version) redisUtil.get("version:" + vkey);
                if (CheckUtils.isObjectEmpty(version)) {
                    log.info("接收->" + jsonObject.toJSONString());
                    String retStr = Result.error("vkey错误").toJsonString();
                    log.info("响应->" + retStr);
                    response.getWriter().write(retStr);
                    return false;
                }
                if (!version.getFromSoftId().equals(soft.getId())) {
                    log.info("接收->" + jsonObject.toJSONString());
                    String retStr = Result.error("vkey与skey不匹配").toJsonString();
                    log.info("响应->" + retStr);
                    response.getWriter().write(retStr);
                    return false;
                }
                if (VersionEnums.STATUS_DISABLE.getCode().equals(version.getStatus())) {
                    log.info("接收->" + jsonObject.toJSONString());
                    String retStr = Result.error("版本已停用").toJsonString();
                    log.info("响应->" + retStr);
                    response.getWriter().write(retStr);
                    return false;
                }
                request.setAttribute("obj_version", version);
            }

            //@DataDecrypt
            if (((HandlerMethod) handler).getMethodAnnotation(DataDecrypt.class) != null) {
                if (CheckUtils.isObjectEmpty(reqStr)) {
                    log.info("接收->" + jsonObject.toJSONString());
                    String retStr = Result.error("获取请求数据失败").toJsonString();
                    log.info("响应->" + retStr);
                    response.getWriter().write(retStr);
                    return false;
                }
                String skey = jsonObject.getString("skey");
                Soft soft = (Soft) redisUtil.get("soft:" + skey);
                if (CheckUtils.isObjectEmpty(soft)) {
                    log.info("接收->" + jsonObject.toJSONString());
                    String retStr = Result.error("skey错误").toJsonString();
                    log.info("响应->" + retStr);
                    response.getWriter().write(retStr);
                    return false;
                }
                request.setAttribute("obj_soft", soft);
                if (soft.getGenStatus() == 1) {
                    String jsonStr = AESUtils.decrypt(jsonObject.getString("data"), soft.getGenKey());
                    if (CheckUtils.isObjectEmpty(jsonStr)) {
                        log.info("接收->" + jsonObject.toJSONString());
                        String retStr = Result.error("数据解密失败").toJsonString();
                        log.info("响应->" + retStr);
                        response.getWriter().write(retStr);
                        return false;
                    }
                    Object object = JSONObject.parseObject(jsonStr);
                    jsonObject.put("data", object);
                }
                request.setAttribute("json", jsonObject);
            }
            log.info("接收->" + jsonObject.toJSONString());
            //@UserLogin
            if (((HandlerMethod) handler).getMethodAnnotation(UserLogin.class) != null) {
                Soft soft = (Soft) request.getAttribute("obj_soft");
                String token = jsonObject.getJSONObject("data").getString("token");
                if (CheckUtils.isObjectEmpty(token)) {
                    log.info("接收->" + jsonObject.toJSONString());
                    String retStr = Result.error("非法请求").toJsonString();
                    log.info("响应->" + retStr);
                    response.getWriter().write(retStr);
                    return false;
                }
                JSONObject jsonObject1 = MyUtils.decUserToken(token, genKey);
                if (CheckUtils.isObjectEmpty(jsonObject1)) {
                    log.info("接收->" + jsonObject.toJSONString());
                    String retStr = Result.error("非法请求").toJsonString();
                    log.info("响应->" + retStr);
                    response.getWriter().write(retStr);
                    return false;
                }
                User user = (User) redisUtil.get("user:" + soft.getId() + ":" + jsonObject1.getString("user"));
                if (CheckUtils.isObjectEmpty(user)) {
                    log.info("接收->" + jsonObject.toJSONString());
                    String retStr = Result.error("用户未登录").toJsonString();
                    log.info("响应->" + retStr);
                    response.getWriter().write(retStr);
                    return false;
                }
                if (!user.getToken().equals(token)) {
                    log.info("接收->" + jsonObject.toJSONString());
                    String retStr = Result.error("账号可能异地登录，请重新登录").toJsonString();
                    log.info("响应->" + retStr);
                    response.getWriter().write(retStr);
                    return false;
                }
                /*if((user.getLastTime() + UserEnums.TOKEN_VALIDITY.getCode()) < Integer.parseInt(MyUtils.getTimeStamp())){
                    log.info("接收->" + jsonObject.toJSONString());
                    String retStr = Result.error("登录失效，请重新登录").toJsonString();
                    log.info("响应->" + retStr);
                    response.getWriter().write(retStr);
                    return false;
                }*/
                request.setAttribute("obj_user", user);
            }
            //@SignValidated
            if (((HandlerMethod) handler).getMethodAnnotation(SignValidated.class) != null) {
                String sign = jsonObject.getString("sign");
                if (CheckUtils.isObjectEmpty(sign)) {
                    String retStr = Result.error("缺少sign参数").toJsonString();
                    log.info("响应->" + retStr);
                    response.getWriter().write(retStr);
                    return false;
                }
                Integer timestamp = jsonObject.getJSONObject("data").getInteger("timestamp");
                if (CheckUtils.isObjectEmpty(timestamp)) {
                    String retStr = Result.error("缺少data.timestamp参数").toJsonString();
                    log.info("响应->" + retStr);
                    response.getWriter().write(retStr);
                    return false;
                }
                Integer diffTime = Math.abs(timestamp - Integer.parseInt(MyUtils.getTimeStamp()));
                if (diffTime > SoftEnums.DIFF_TIME.getCode()) {
                    String retStr = Result.error("sign已过期").toJsonString();
                    log.info("响应->" + retStr);
                    response.getWriter().write(retStr);
                    return false;
                }
                String skey = jsonObject.getString("skey");
                Soft soft = (Soft) redisUtil.get("soft:" + skey);
                if (CheckUtils.isObjectEmpty(soft)) {
                    String retStr = Result.error("skey错误").toJsonString();
                    log.info("响应->" + retStr);
                    response.getWriter().write(retStr);
                    return false;
                }
                request.setAttribute("obj_soft", soft);
                String privateSign = MyUtils.calculateSign(jsonObject.getJSONObject("data"), soft.getGenKey());
                if (!privateSign.equals(sign)) {
                    String retStr = Result.error("sign错误").toJsonString();
                    log.info("响应->" + retStr);
                    response.getWriter().write(retStr);
                    return false;
                }
            }
            //@BanValidated
            if (((HandlerMethod) handler).getMethodAnnotation(BanValidated.class) != null) {
                String skey = jsonObject.getString("skey");
                Soft soft = (Soft) redisUtil.get("soft:" + skey);
                if (CheckUtils.isObjectEmpty(soft)) {
                    String retStr = Result.error("skey错误").toJsonString();
                    log.info("响应->" + retStr);
                    response.getWriter().write(retStr);
                    return false;
                }
                BanValidated methodAnnotation = ((HandlerMethod) handler).getMethodAnnotation(BanValidated.class);
                boolean is_ip = methodAnnotation.is_ip();
                if (is_ip) {
                    Ban ban = (Ban) redisUtil.get("ban:" + ip + "-" + 2 + "-" + soft.getId());
                    if (!CheckUtils.isObjectEmpty(ban)) {
                        if (ban.getToTime() == -1) {
                            String msg = "msg=被封禁" + "&type=ip" + "&value=" + ip + "&toTime=-1&time=" + ban.getAddTime()
                                    + "&why=" + ban.getWhy();
                            String retStr = Result.error(300, msg).toJsonString();
                            log.info("响应->" + retStr);
                            response.getWriter().write(retStr);
                            return false;
                        } else {
                            Integer seconds = ban.getToTime() - Integer.parseInt(MyUtils.getTimeStamp());
                            if (seconds > 0) {
                                String msg = "msg=被封禁" + "&type=ip" + "&value=" + ip + "&toTime=" + ban.getToTime() + "&time=" + ban.getAddTime()
                                        + "&why=" + ban.getWhy();
                                String retStr = Result.error(300, msg).toJsonString();
                                log.info("响应->" + retStr);
                                response.getWriter().write(retStr);
                                return false;
                            }
                        }
                    }
                }
                boolean is_device_code = methodAnnotation.is_device_code();
                if (is_device_code) {
                    String device_code = jsonObject.getJSONObject("data").getString("device_code");
                    String device_info = jsonObject.getJSONObject("data").getString("device_info");
                    if (CheckUtils.isObjectEmpty(device_code)) {
                        String retStr = Result.error("缺少data.device_code参数").toJsonString();
                        log.info("响应->" + retStr);
                        response.getWriter().write(retStr);
                        return false;
                    }
                    if (CheckUtils.isObjectEmpty(device_info)) {
                        String retStr = Result.error("缺少data.device_info参数").toJsonString();
                        log.info("响应->" + retStr);
                        response.getWriter().write(retStr);
                        return false;
                    }
                    Ban ban = (Ban) redisUtil.get("ban:" + device_code + "-" + 1 + "-" + soft.getId());
                    if (!CheckUtils.isObjectEmpty(ban)) {
                        if (ban.getToTime() == -1) {
                            String msg = "msg=被封禁" + "&type=device_code" + "&value=" + device_code + "&toTime=-1&time=" + ban.getAddTime()
                                    + "&why=" + ban.getWhy();
                            String retStr = Result.error(300, msg).toJsonString();
                            log.info("响应->" + retStr);
                            response.getWriter().write(retStr);
                            return false;
                        } else {
                            Integer seconds = ban.getToTime() - Integer.parseInt(MyUtils.getTimeStamp());
                            if (seconds > 0) {
                                String msg = "msg=被封禁" + "&type=device_code" + "&value=" + device_code + "&toTime=" + ban.getToTime() + "&time=" + ban.getAddTime()
                                        + "&why=" + ban.getWhy();
                                String retStr = Result.error(300, msg).toJsonString();
                                log.info("响应->" + retStr);
                                response.getWriter().write(retStr);
                                return false;
                            }
                        }
                    }
                }
                boolean is_user = methodAnnotation.is_user();
                if (is_user) {
                    User user = (User) request.getAttribute("obj_user");
                    if (!CheckUtils.isObjectEmpty(user)) {
                        Ban ban = (Ban) redisUtil.get("ban:" + user.getUser() + "-" + 3 + "-" + soft.getId());
                        if (!CheckUtils.isObjectEmpty(ban)) {
                            if (ban.getToTime() == -1) {
                                String msg = "msg=被封禁" + "&type=user" + "&value=" + user.getUser() + "&toTime=-1&time=" + ban.getAddTime()
                                        + "&why=" + ban.getWhy();
                                String retStr = Result.error(300, msg).toJsonString();
                                log.info("响应->" + retStr);
                                response.getWriter().write(retStr);
                                return false;
                            } else {
                                Integer seconds = ban.getToTime() - Integer.parseInt(MyUtils.getTimeStamp());
                                if (seconds > 0) {
                                    String msg = "msg=被封禁" + "&type=user" + "&value=" + user.getUser() + "&toTime=" + ban.getToTime() + "&time=" + ban.getAddTime()
                                            + "&why=" + ban.getWhy();
                                    String retStr = Result.error(300, msg).toJsonString();
                                    log.info("响应->" + retStr);
                                    response.getWriter().write(retStr);
                                    return false;
                                }
                            }
                        }
                    }
                }
            }

            return true;
        }
        return true;
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     *
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
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (handler instanceof HandlerMethod) {
            //取出在ResponseBodyAdvice中设置的body
            if (!CheckUtils.isObjectEmpty(request.getSession())) {
                Result result = (Result) request.getSession().getAttribute("body");
                if (!CheckUtils.isObjectEmpty(result)) {
                    log.info("响应->" + result.toJsonString());
                    request.getSession().setAttribute("body", null);
                }
            }
        }
    }
}
