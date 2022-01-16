package cn.myauthx.api.main.controller;

import cn.myauthx.api.base.annotation.AdminAuth;
import cn.myauthx.api.base.annotation.Open;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.MyPage;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.service.IAdminService;
import cn.myauthx.api.main.service.ISoftService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.IpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 前端web使用的API接口
 * @author DaenMax
 */
@Slf4j
@RestController
@RequestMapping("/web")
public class WebApiController {
    @Resource
    private IAdminService adminService;
    @Resource
    private ISoftService iSoftService;

    @Open
    @PostMapping("login")
    public Result login(@RequestBody Map<String,Object> map, HttpServletRequest request){
        Object user = map.get("user");
        Object pass = map.get("pass");
        if(CheckUtils.isObjectEmpty(user) || CheckUtils.isObjectEmpty(pass)){
            return Result.error("参数错误");
        }
        String ip = IpUtil.getIpAddr(request);
        return adminService.login(user.toString(),pass.toString(),ip);
    }

    @Open
    @AdminAuth
    @PostMapping("getSoftList")
    public Result getSoftList(HttpServletRequest request){
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = jsonObject.toJavaObject(Soft.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        return iSoftService.getSoftList(soft,myPage);
    }
}
