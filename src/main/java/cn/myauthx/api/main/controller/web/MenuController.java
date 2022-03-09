package cn.myauthx.api.main.controller.web;

import cn.myauthx.api.base.annotation.AdminLogin;
import cn.myauthx.api.base.annotation.OpenApi;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Admin;
import cn.myauthx.api.main.entity.Msg;
import cn.myauthx.api.main.entity.MyPage;
import cn.myauthx.api.main.service.IMenuService;
import cn.myauthx.api.main.service.IMsgService;
import cn.myauthx.api.util.CheckUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class MenuController {
    @Resource
    private IMenuService menuService;

    /**
     * 获取菜单
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin()
    @GetMapping("getMenuList")
    public Result getMenuList(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Admin admin = (Admin) request.getAttribute("obj_admin");
        return menuService.getMenuList(admin);
    }



}
