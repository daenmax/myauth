package cn.myauthx.api.main.controller;

import cn.myauthx.api.base.annotation.AdminLogin;
import cn.myauthx.api.base.annotation.OpenApi;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.MyPage;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.service.IAdminService;
import cn.myauthx.api.main.service.ISoftService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.IpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
    private ISoftService softService;

    /**
     * 检测连接
     * @param request
     * @return
     */
    @OpenApi
    @GetMapping("/connect")
    public Result conn(HttpServletRequest request){
        JSONObject retJson = new JSONObject(true);
        retJson.put("ip", IpUtil.getIpAddr(request));
        retJson.put("ua",request.getHeader("user-agent"));
        return Result.ok("服务器正常",retJson);
    }
    /**
     * 登录
     * @param request
     * @return
     */
    @OpenApi
    @PostMapping("login")
    public Result login(HttpServletRequest request){
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        String user = jsonObject.getString("user");
        String pass = jsonObject.getString("pass");
        if(CheckUtils.isObjectEmpty(user) || CheckUtils.isObjectEmpty(pass)){
            return Result.error("参数错误");
        }
        String ip = IpUtil.getIpAddr(request);
        return adminService.login(user,pass,ip);
    }

    /**
     * 检查登录token有效性
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("checkLogin")
    public Result checkLogin(HttpServletRequest request){
        return Result.ok("token正常");
    }

    /**
     * 获取软件列表
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("getSoftList")
    public Result getSoftList(HttpServletRequest request){
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = jsonObject.toJavaObject(Soft.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        if(CheckUtils.isObjectEmpty(myPage.getPageIndex()) || CheckUtils.isObjectEmpty(myPage.getPageSize())){
            return Result.error("页码和尺寸参数不能为空");
        }
        return softService.getSoftList(soft,myPage);
    }

    /**
     * 添加软件
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("addSoft")
    public Result addSoft(HttpServletRequest request){
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = jsonObject.toJavaObject(Soft.class);
        if(CheckUtils.isObjectEmpty(soft)){
            return Result.error("参数错误");
        }
        if(CheckUtils.isObjectEmpty(soft.getName()) || CheckUtils.isObjectEmpty(soft.getStatus()) || CheckUtils.isObjectEmpty(soft.getType())
                || CheckUtils.isObjectEmpty(soft.getGenKey()) || CheckUtils.isObjectEmpty(soft.getGenStatus()) || CheckUtils.isObjectEmpty(soft.getBindDeviceCode())
                || CheckUtils.isObjectEmpty(soft.getMultipleLogin()) || CheckUtils.isObjectEmpty(soft.getHeartTime()) || CheckUtils.isObjectEmpty(soft.getRegister())){
            return Result.error("参数不全");
        }
        return softService.addSoft(soft);
    }

    /**
     * 修改软件
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("updSoft")
    public Result updSoft(HttpServletRequest request){
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = jsonObject.toJavaObject(Soft.class);
        if(CheckUtils.isObjectEmpty(soft)){
            return Result.error("参数错误");
        }
        soft.setSkey(null);
        if(CheckUtils.isObjectEmpty(soft.getId()) && CheckUtils.isObjectEmpty(soft.getName()) && CheckUtils.isObjectEmpty(soft.getStatus())
                && CheckUtils.isObjectEmpty(soft.getType()) && CheckUtils.isObjectEmpty(soft.getGenKey()) && CheckUtils.isObjectEmpty(soft.getGenStatus())
                && CheckUtils.isObjectEmpty(soft.getBindDeviceCode()) && CheckUtils.isObjectEmpty(soft.getMultipleLogin()) && CheckUtils.isObjectEmpty(soft.getHeartTime())
                && CheckUtils.isObjectEmpty(soft.getRegister())){
            return Result.error("参数不能全部为空");
        }
        return softService.updSoft(soft);
    }

    /**
     * 删除软件，会同步删除版本、卡密、用户、事件、数据、封禁、JS、回复、日志
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("delSoft")
    public Result delSoft(HttpServletRequest request){
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = jsonObject.toJavaObject(Soft.class);
        if(CheckUtils.isObjectEmpty(soft)){
            return Result.error("参数错误");
        }
        if(CheckUtils.isObjectEmpty(soft.getId())){
            return Result.error("id不能为空");
        }
        return softService.delSoft(soft);
    }

    /**
     * 查询软件，根据id或者skey
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("getSoft")
    public Result getSoft(HttpServletRequest request){
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = jsonObject.toJavaObject(Soft.class);
        if(CheckUtils.isObjectEmpty(soft)){
            return Result.error("参数错误");
        }
        if(CheckUtils.isObjectEmpty(soft.getId()) && CheckUtils.isObjectEmpty(soft.getSkey())){
            return Result.error("id和skey不能全部为空");
        }
        return softService.getSoft(soft);
    }

}
