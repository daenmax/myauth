package cn.myauthx.api.main.controller;


import cn.myauthx.api.base.annotation.NoEncryptNoSign;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.service.IConfigService;
import cn.myauthx.api.main.service.StatisService;
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
    @Resource
    private IConfigService configService;
    /**
     * 获取在线人数
     *
     * @param skey
     * @return
     */
    @NoEncryptNoSign
    @GetMapping("/getOnlineUserCount")
    public Result getOnlineUserCount(String skey,String apikey) {
        if (CheckUtils.isObjectEmpty(apikey)) {
            return Result.error("apikey不能为空");
        }
        if (CheckUtils.isObjectEmpty(skey)) {
            return Result.error("skey不能为空");
        }
        Integer apiKeyIsOk = configService.apiKeyIsOk(apikey);
        if(apiKeyIsOk.equals(-1)){
            return Result.error("系统未设置apikey，无法使用开发接口");
        }
        if(apiKeyIsOk.equals(0)){
            return Result.error("apikey不正确");
        }
        return statisService.getOnlineUserCount(skey);
    }

    /**
     * 获取用户总数
     *
     * @param skey
     * @return
     */
    @NoEncryptNoSign
    @GetMapping("/getUserCount")
    public Result getUserCount(String skey,String apikey) {
        if (CheckUtils.isObjectEmpty(apikey)) {
            return Result.error("apikey不能为空");
        }
        if (CheckUtils.isObjectEmpty(skey)) {
            return Result.error("skey不能为空");
        }
        Integer apiKeyIsOk = configService.apiKeyIsOk(apikey);
        if(apiKeyIsOk.equals(-1)){
            return Result.error("系统未设置apikey，无法使用开发接口");
        }
        if(apiKeyIsOk.equals(0)){
            return Result.error("apikey不正确");
        }
        return statisService.getUserCount(skey);
    }
}
