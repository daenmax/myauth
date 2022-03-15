package cn.myauthx.api.main.controller.web;

import cn.myauthx.api.base.annotation.AdminLogin;
import cn.myauthx.api.base.annotation.OpenApi;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.*;
import cn.myauthx.api.main.service.IAdminService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.IpUtil;
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
public class AdminController {
    @Resource
    private IAdminService adminService;

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
    @AdminLogin(is_super_role = false)
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
    @AdminLogin(is_super_role = false)
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
    @AdminLogin(is_super_role = false)
    @PostMapping("editQQ")
    public Result editQQ(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        String qq = jsonObject.getString("qq");
        Admin admin = (Admin) request.getAttribute("obj_admin");
        if (CheckUtils.isObjectEmpty(qq)) {
            return Result.error("QQ不能为空");
        }
        return adminService.editQQ(qq, admin);
    }

    /**
     * 获取管理员列表
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin(is_admin = true)
    @PostMapping("getAdminList")
    public Result getAdminList(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Admin admin = jsonObject.toJavaObject(Admin.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        if (CheckUtils.isObjectEmpty(admin) || CheckUtils.isObjectEmpty(myPage)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(myPage.getPageIndex()) || CheckUtils.isObjectEmpty(myPage.getPageSize())) {
            return Result.error("页码和尺寸参数不能为空");
        }
        return adminService.getAdminList(admin, myPage);
    }

    /**
     * 修改管理员
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin(is_admin = true)
    @PostMapping("updAdmin")
    public Result updAdmin(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Admin admin = jsonObject.toJavaObject(Admin.class);
        if (CheckUtils.isObjectEmpty(admin)) {
            return Result.error("参数错误");
        }
        admin.setRegTime(null);
        admin.setLastTime(null);
        admin.setLastIp(null);
        admin.setToken(null);
        if (CheckUtils.isObjectEmpty(admin.getId())) {
            return Result.error("id不能为空");
        }
        if ("admin".equals(admin.getUser())) {
            return Result.error("账号不能修改为admin");
        }
        if (CheckUtils.isObjectEmpty(admin.getUser()) && CheckUtils.isObjectEmpty(admin.getPass())
                && CheckUtils.isObjectEmpty(admin.getQq()) && CheckUtils.isObjectEmpty(admin.getStatus())
                && CheckUtils.isObjectEmpty(admin.getRole()) && CheckUtils.isObjectEmpty(admin.getMoney())) {
            return Result.error("参数不能全部为空");
        }
        return adminService.updAdmin(admin);
    }

    /**
     * 查询管理员，根据id
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin(is_admin = true)
    @PostMapping("getAdmin")
    public Result getAdmin(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Admin admin = jsonObject.toJavaObject(Admin.class);
        if (CheckUtils.isObjectEmpty(admin)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(admin.getId())) {
            return Result.error("id不能为空");
        }
        return adminService.getAdmin(admin);
    }

    /**
     * 添加管理员
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin(is_admin = true)
    @PostMapping("addAdmin")
    public Result addAdmin(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Admin admin = jsonObject.toJavaObject(Admin.class);
        if (CheckUtils.isObjectEmpty(admin)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(admin.getUser()) || CheckUtils.isObjectEmpty(admin.getPass()) || CheckUtils.isObjectEmpty(admin.getQq())
                || CheckUtils.isObjectEmpty(admin.getStatus()) || CheckUtils.isObjectEmpty(admin.getRole()) || CheckUtils.isObjectEmpty(admin.getMoney())) {
            return Result.error("参数不全");
        }
        if ("admin".equals(admin.getUser())) {
            return Result.error("账号不能为admin");
        }
        admin.setRegTime(null);
        admin.setLastTime(null);
        admin.setLastIp(null);
        admin.setToken(null);
        return adminService.addAdmin(admin);
    }

    /**
     * 删除管理员
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin(is_admin = true)
    @PostMapping("delAdmin")
    public Result delAdmin(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Admin admin = jsonObject.toJavaObject(Admin.class);
        if (CheckUtils.isObjectEmpty(admin)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(admin.getId())) {
            return Result.error("id不能为空");
        }
        return adminService.delAdmin(admin);
    }

    /**
     * 奖惩管理员
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin(is_admin = true)
    @PostMapping("chaMoney")
    public Result chaMoney(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Admin admin = jsonObject.toJavaObject(Admin.class);
        if (CheckUtils.isObjectEmpty(admin)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(admin.getId()) && CheckUtils.isObjectEmpty(admin.getUser())) {
            return Result.error("id和账号不能都为空");
        }
        if (CheckUtils.isObjectEmpty(admin.getMoney())) {
            return Result.error("变动的金额不能为空");
        }
        Admin myAdmin = (Admin) request.getAttribute("obj_admin");
        return adminService.chaMoney(admin,myAdmin);
    }

    /**
     * 获取我的信息
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin(is_super_role = false)
    @GetMapping("getMyInfo")
    public Result getMyInfo(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Admin admin = (Admin) request.getAttribute("obj_admin");
        return adminService.getMyInfo(admin);
    }
}
