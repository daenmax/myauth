package cn.myauthx.api.main.controller;

import cn.myauthx.api.base.annotation.GenAndSign;
import cn.myauthx.api.base.vo.Result;


import cn.myauthx.api.main.entity.Soft;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 软件使用API接口
 * @author DaenMax
 */
@RestController
@RequestMapping("/soft")
public class SoftApiController {
    /**
     * 初始化软件
     * @param request
     * @return
     */
    @GenAndSign
    @PostMapping("/init")
    public Result init(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = (Soft) request.getAttribute("obj_soft");
        System.out.println(soft.toString());
        System.out.println("执行->"+jsonObject.toJSONString());
        JSONObject retJson = new JSONObject();
        retJson.put("Status",soft.getStatus());
        retJson.put("Type",soft.getType());
        retJson.put("BatchSoft",soft.getBatchSoft());
        retJson.put("MultipleLogin",soft.getMultipleLogin());
        retJson.put("HeartTime",soft.getHeartTime());
        return Result.ok("初始化成功",retJson);
    }
}
