package cn.myauthx.api.main.controller.web;


import cn.myauthx.api.base.annotation.AdminLogin;
import cn.myauthx.api.base.annotation.OpenApi;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.MyPage;
import cn.myauthx.api.main.entity.Role;
import cn.myauthx.api.main.service.IRoleService;
import cn.myauthx.api.util.CheckUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author DaenMax
 * @since 2022-03-09
 */
@RestController
@RequestMapping("/web")
public class RoleController {
    @Resource
    private IRoleService roleService;

    /**
     * 获取角色列表
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("getRoleList")
    public Result getRoleList(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Role role = jsonObject.toJavaObject(Role.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        if (CheckUtils.isObjectEmpty(role) || CheckUtils.isObjectEmpty(myPage)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(myPage.getPageIndex()) || CheckUtils.isObjectEmpty(myPage.getPageSize())) {
            return Result.error("页码和尺寸参数不能为空");
        }
        return roleService.getRoleList(role, myPage);
    }

    /**
     * 获取角色列表_全部_简要
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("getRoleListEx")
    public Result getRoleListEx(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Role role = jsonObject.toJavaObject(Role.class);
        if (CheckUtils.isObjectEmpty(role)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(role.getFromSoftId())) {
            return Result.error("fromSoftId参数不能为空");
        }
        return roleService.getRoleListEx(role);
    }

    /**
     * 查询角色，根据id
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("getRole")
    public Result getRole(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Role role = jsonObject.toJavaObject(Role.class);
        if (CheckUtils.isObjectEmpty(role)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(role.getId())) {
            return Result.error("id不能为空");
        }
        return roleService.getRole(role);
    }

    /**
     * 修改角色
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("updRole")
    public Result updRole(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Role role = jsonObject.toJavaObject(Role.class);
        JSONArray jsonArray = jsonObject.getJSONArray("meunList");
        if (!CheckUtils.isObjectEmpty(jsonArray)) {
            List<String> list = (List<String>) JSONArray.toJavaObject(jsonArray, List.class);
            role.setMeunList(list);
        }
        if (CheckUtils.isObjectEmpty(role)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(role.getId())) {
            return Result.error("id不能为空");
        }
        if (CheckUtils.isObjectEmpty(role.getName()) && CheckUtils.isObjectEmpty(role.getFromSoftId())
                && CheckUtils.isObjectEmpty(role.getDiscount())) {
            return Result.error("参数不能全部为空");
        }
        return roleService.updRole(role);
    }

    /**
     * 添加角色
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("addRole")
    public Result addRole(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Role role = jsonObject.toJavaObject(Role.class);
        JSONArray jsonArray = jsonObject.getJSONArray("meunList");
        if (!CheckUtils.isObjectEmpty(jsonArray)) {
            List<String> list = (List<String>) JSONArray.toJavaObject(jsonArray, List.class);
            role.setMeunList(list);
        }
        if (CheckUtils.isObjectEmpty(role)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(role.getName()) || CheckUtils.isObjectEmpty(role.getFromSoftId())
                || CheckUtils.isObjectEmpty(role.getDiscount())) {
            return Result.error("参数不全");
        }
        return roleService.addRole(role);
    }

    /**
     * 删除角色
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("delRole")
    public Result delRole(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Role role = jsonObject.toJavaObject(Role.class);
        if (CheckUtils.isObjectEmpty(role)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(role.getId())) {
            return Result.error("id不能为空");
        }
        return roleService.delRole(role);
    }
}
