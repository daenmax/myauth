package cn.myauthx.api.main.controller;

import cn.myauthx.api.base.annotation.DataDecrypt;
import cn.myauthx.api.base.annotation.SignValidated;
import cn.myauthx.api.base.annotation.SoftValidated;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.util.IpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

/**
 * 软件使用API接口
 * @author DaenMax
 */
@Slf4j
@RestController
@RequestMapping("/soft")
public class SoftApiController {

    /**
     * 检测连接
     * @param request
     * @return
     */
    @GetMapping("/connect")
    public Result conn(HttpServletRequest request){
        JSONObject retJson = new JSONObject(true);
        retJson.put("ip", IpUtil.getIpAddr(request));
        retJson.put("ua",request.getHeader("user-agent"));
        return Result.ok("连接服务器成功",retJson);
    }
    /**
     * 初始化软件
     * @param request
     * @return
     */
    @SoftValidated
    @DataDecrypt
    @SignValidated
    @PostMapping("/init")
    public Result init(HttpServletRequest request) {
        //不管有没有加密和解密，取提交的JSON都要通过下面这行去取
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = (Soft) request.getAttribute("obj_soft");
        System.out.println("软件->" + soft.toString());
        System.out.println("执行->" + jsonObject.toJSONString());
        JSONObject retJson = new JSONObject(true);
        retJson.put("Name",soft.getName());
        retJson.put("Status",soft.getStatus());
        retJson.put("Type",soft.getType());
        retJson.put("BatchSoft",soft.getBatchSoft());
        retJson.put("MultipleLogin",soft.getMultipleLogin());
        retJson.put("HeartTime",soft.getHeartTime());
        return Result.ok("初始化成功",retJson);
    }

    @DataDecrypt
    @PostMapping("/checkUpdate")
    public Result checkUpdate(HttpServletRequest request){
        return Result.ok("OK");
    }
}
