package cn.myauthx.api.main.controller;

import cn.myauthx.api.base.annotation.NoEncryptNoSign;
import cn.myauthx.api.base.vo.MyPage;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.entity.User;
import cn.myauthx.api.main.enums.SoftEnums;
import cn.myauthx.api.main.service.IConfigService;
import cn.myauthx.api.main.service.ISoftService;
import cn.myauthx.api.main.service.IUserService;
import cn.myauthx.api.main.service.IVersionService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.IpUtil;
import cn.myauthx.api.util.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 公开API，不需要任何鉴权
 *
 * @author DaenMax
 */
@Slf4j
@RestController
@RequestMapping("/web")
public class WebApiController {
    @Resource
    private IVersionService versionService;
    @Resource
    private IConfigService configService;
    @Resource
    private ISoftService softService;
    @Resource
    private IUserService userService;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 检查服务状态
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
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
    @NoEncryptNoSign
    @GetMapping("/getWebInfo")
    public Result getWebInfo(HttpServletRequest request) {
        return configService.getWebInfo();
    }


    /**
     * 获取更新日志
     *
     * @param skey
     * @return
     */
    @NoEncryptNoSign
    @GetMapping("getUpdateLog")
    public Result getUpdateLog(String skey) {
        if (CheckUtils.isObjectEmpty(skey)) {
            return Result.error("skey不能为空");
        }
        return versionService.getUpdateLog(skey);
    }

    /**
     * 获取软件列表_全部_简要_自助用
     *
     * @param name
     * @return
     */
    @NoEncryptNoSign
    @GetMapping("getSoftListSimple")
    public Result getSoftListSimple(String name) {
        return softService.getSoftListSimple(name);
    }

    /**
     * 自助注册账号
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @PostMapping("selfRegister")
    public Result selfRegister(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        User user = jsonObject.toJavaObject(User.class);
        Soft softC = jsonObject.toJavaObject(Soft.class);
        if (CheckUtils.isObjectEmpty(user)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(softC)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(softC.getSkey())) {
            return Result.error("skey不能为空");
        }
        Soft soft = (Soft) redisUtil.get("soft:" + softC.getSkey());
        if (CheckUtils.isObjectEmpty(soft)) {
            return Result.error("skey错误");
        }
        if (soft.getRegister().equals(SoftEnums.REGISTER_DISABLE.getCode())) {
            return Result.error("当前不允许注册新用户");
        }
        if (soft.getStatus().equals(SoftEnums.STATUS_FIX.getCode())) {
            return Result.error("软件维护中");
        }
        if (soft.getStatus().equals(SoftEnums.STATUS_DISABLE.getCode())) {
            return Result.error("软件已停用");
        }
        if (CheckUtils.isObjectEmpty(user.getUser())) {
            return Result.error("账号不能为空");
        }
        String ip = IpUtil.getIpAddr(request);
        user.setLastIp(ip);
        return userService.register(user, soft);
    }

    @NoEncryptNoSign
    @PostMapping("selfChangeUser")
    public Result selfChangeUser(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        String user = jsonObject.getString("user");
        String newUser = jsonObject.getString("newUser");
        String pass = jsonObject.getString("pass");
        String skey = jsonObject.getString("skey");
        String ckey = jsonObject.getString("ckey");
        if (CheckUtils.isObjectEmpty(user)) {
            return Result.error("原账号不能为空");
        }
        if (CheckUtils.isObjectEmpty(newUser)) {
            return Result.error("新账号不能为空");
        }
        if (CheckUtils.isObjectEmpty(skey)) {
            return Result.error("skey不能为空");
        }
        if (CheckUtils.isObjectEmpty(ckey) && CheckUtils.isObjectEmpty(pass)) {
            return Result.error("当前密码和卡密不能都为空");
        }
        Soft soft = (Soft) redisUtil.get("soft:" + skey);
        if (CheckUtils.isObjectEmpty(soft)) {
            return Result.error("skey错误");
        }
        if (soft.getStatus().equals(SoftEnums.STATUS_FIX.getCode())) {
            return Result.error("软件维护中");
        }
        if (soft.getStatus().equals(SoftEnums.STATUS_DISABLE.getCode())) {
            return Result.error("软件已停用");
        }
        return userService.selfChangeUser(user, newUser, pass, soft.getId(), ckey);
    }

}
