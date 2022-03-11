package cn.myauthx.api.main.controller.web;

import cn.myauthx.api.base.annotation.AdminLogin;
import cn.myauthx.api.base.annotation.OpenApi;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Admin;
import cn.myauthx.api.main.entity.Menu;
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
     * 获取权限菜单
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin(is_super_admin = false)
    @GetMapping("getMenuList")
    public Result getMenuList(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Admin admin = (Admin) request.getAttribute("obj_admin");
        return menuService.getMenuList(admin);
    }

    /**
     * 获取菜单列表
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin()
    @GetMapping("getMenuListAll")
    public Result getMenuListAll(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        return menuService.getMenuListAll();
    }

    /**
     * 查询菜单，根据id
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("getMenu")
    public Result getMenu(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Menu menu = jsonObject.toJavaObject(Menu.class);
        if (CheckUtils.isObjectEmpty(menu)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(menu.getId())) {
            return Result.error("id不能为空");
        }
        return menuService.getMenu(menu);
    }

    /**
     * 修改菜单
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("updMenu")
    public Result updMenu(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Menu menu = jsonObject.toJavaObject(Menu.class);
        if (CheckUtils.isObjectEmpty(menu)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(menu.getId())) {
            return Result.error("id不能为空");
        }
        if (CheckUtils.isObjectEmpty(menu.getParentId()) && CheckUtils.isObjectEmpty(menu.getLevel())
                && CheckUtils.isObjectEmpty(menu.getSort()) && CheckUtils.isObjectEmpty(menu.getType())
                && CheckUtils.isObjectEmpty(menu.getPath()) && CheckUtils.isObjectEmpty(menu.getTitle())
                && CheckUtils.isObjectEmpty(menu.getIcon())) {
            return Result.error("参数不能全部为空");
        }
        return menuService.updMenu(menu);
    }

    /**
     * 添加菜单
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("addMenu")
    public Result addMenu(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Menu menu = jsonObject.toJavaObject(Menu.class);
        if (CheckUtils.isObjectEmpty(menu)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(menu.getLevel()) || CheckUtils.isObjectEmpty(menu.getSort())
                || CheckUtils.isObjectEmpty(menu.getType()) || CheckUtils.isObjectEmpty(menu.getTitle())) {
            return Result.error("参数不全");
        }
        return menuService.addMenu(menu);
    }

    /**
     * 删除菜单
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("delMenu")
    public Result delMenu(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Menu menu = jsonObject.toJavaObject(Menu.class);
        if (CheckUtils.isObjectEmpty(menu)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(menu.getId())) {
            return Result.error("id不能为空");
        }
        return menuService.delMenu(menu);
    }

}
