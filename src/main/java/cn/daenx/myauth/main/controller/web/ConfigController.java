package cn.daenx.myauth.main.controller.web;

import cn.daenx.myauth.base.annotation.AdminLogin;
import cn.daenx.myauth.main.service.IConfigService;
import cn.daenx.myauth.util.CheckUtils;
import cn.daenx.myauth.base.annotation.NoEncryptNoSign;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.Config;
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
public class ConfigController {
    @Resource
    private IConfigService configService;


    /**
     * 修改系统设置
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_admin = true)
    @PostMapping("editConfig")
    public Result editConfig(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Config config = jsonObject.toJavaObject(Config.class);
        if (CheckUtils.isObjectEmpty(config)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(config.getSeoTitle()) && CheckUtils.isObjectEmpty(config.getSeoKeywords()) && CheckUtils.isObjectEmpty(config.getSeoDescription())
                && CheckUtils.isObjectEmpty(config.getDingbotAccessToken()) && CheckUtils.isObjectEmpty(config.getDingbotMsg()) && CheckUtils.isObjectEmpty(config.getOpenApiKey())) {
            return Result.error("参数不能全部为空");
        }
        return configService.editConfig(config);
    }


    /**
     * 获取系统设置
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_admin = true)
    @GetMapping("getConfig")
    public Result getConfig(HttpServletRequest request) {
        return configService.getConfig();
    }


}
