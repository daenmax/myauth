package cn.myauthx.api.main.controller;


import cn.myauthx.api.base.annotation.NoEncryptNoSign;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.service.StatisService;
import cn.myauthx.api.util.CheckUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 开放API，需要开放接口key
 *
 * @author DaenMax
 */
@Slf4j
@RestController
@RequestMapping("/web")
public class OpenApiController {
    @Resource
    private StatisService statisService;
    /**
     * 获取在线人数
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @PostMapping("/getOnlineUserCount")
    public Result getOnlineUserCount(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = jsonObject.toJavaObject(Soft.class);
        if (CheckUtils.isObjectEmpty(soft)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(soft.getId()) && CheckUtils.isObjectEmpty(soft.getSkey())) {
            return Result.error("id和skey不能全部为空");
        }
        return statisService.getOnlineUserCount(soft);
    }
}
