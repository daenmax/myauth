package cn.myauthx.api.main.controller;

import cn.myauthx.api.base.annotation.AdminLogin;
import cn.myauthx.api.base.annotation.OpenApi;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Admin;
import cn.myauthx.api.main.service.IAdminService;
import cn.myauthx.api.main.service.IConfigService;
import cn.myauthx.api.main.service.IVersionService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.IpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 前端web使用的API接口
 *
 * @author DaenMax
 */
@Slf4j
@RestController
@RequestMapping("/web")
public class WebApiController {
    @Resource
    private IAdminService adminService;
    @Resource
    private IVersionService versionService;
    @Resource
    private IConfigService configService;

    /**
     * 检查服务状态
     *
     * @param request
     * @return
     */
    @OpenApi
    @GetMapping("/connect")
    public Result conn(HttpServletRequest request) {
        JSONObject retJson = new JSONObject(true);
        retJson.put("ip", IpUtil.getIpAddr(request));
        retJson.put("ua", request.getHeader("user-agent"));
        return Result.ok("服务器正常", retJson);
    }

    /**
     * 获取web信息
     *
     * @param request
     * @return
     */
    @OpenApi
    @GetMapping("/getWebInfo")
    public Result getWebInfo(HttpServletRequest request) {
        return configService.getWebInfo();
    }

    /**
     * 登录
     *
     * @param request
     * @return
     */
    @OpenApi
    @PostMapping("login")
    public Result login(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        String user = jsonObject.getString("user");
        String pass = jsonObject.getString("pass");
        if (CheckUtils.isObjectEmpty(user) || CheckUtils.isObjectEmpty(pass)) {
            return Result.error("参数错误");
        }
        String ip = IpUtil.getIpAddr(request);
        return adminService.login(user, pass, ip);
    }

    /**
     * 检查登录token有效性
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("checkLogin")
    public Result checkLogin(HttpServletRequest request) {
        return Result.ok("token正常");
    }

    /**
     * 修改密码
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("editPass")
    public Result editPass(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        String nowPass = jsonObject.getString("nowPass");
        String newPass = jsonObject.getString("newPass");
        Admin admin = (Admin) request.getAttribute("obj_admin");
        if (CheckUtils.isObjectEmpty(nowPass) || CheckUtils.isObjectEmpty(newPass)) {
            return Result.error("新密码和旧密码均不能为空");
        }
        return adminService.editPass(nowPass, newPass, admin);
    }

    /**
     * 修改QQ
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("editQQ")
    public Result editQQ(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        String qq = jsonObject.getString("qq");
        Admin admin = (Admin) request.getAttribute("obj_admin");
        if (CheckUtils.isObjectEmpty(qq) ) {
            return Result.error("QQ不能为空");
        }
        return adminService.editQQ(qq, admin);
    }

    /**
     * 获取更新日志
     *
     * @param skey
     * @return
     */
    @OpenApi
    @GetMapping("getUpdateLog")
    public Result getUpdateLog(String skey) {
        if (CheckUtils.isObjectEmpty(skey)) {
            return Result.error("skey不能为空");
        }
        return versionService.getUpdateLog(skey);
    }

}
