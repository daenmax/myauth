package cn.myauthx.api.main.controller;

import cn.myauthx.api.base.annotation.NoEncryptNoSign;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.service.IConfigService;
import cn.myauthx.api.main.service.IVersionService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.IpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 公开API，不需要任何鉴权
 *
 * @author DaenMax
 */
@Slf4j
@RestController
@RequestMapping("/web")
public class WebApiController {
    @Resource
    private IVersionService versionService;
    @Resource
    private IConfigService configService;

    /**
     * 检查服务状态
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @GetMapping("/connect")
    public Result conn(HttpServletRequest request) {
        JSONObject retJson = new JSONObject(true);
        retJson.put("ip", IpUtil.getIpAddr(request));
        retJson.put("ua", request.getHeader("user-agent"));
        return Result.ok("服务器正常", retJson);
    }

    /**
     * 获取web信息
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @GetMapping("/getWebInfo")
    public Result getWebInfo(HttpServletRequest request) {
        return configService.getWebInfo();
    }


    /**
     * 获取更新日志
     *
     * @param skey
     * @return
     */
    @NoEncryptNoSign
    @GetMapping("getUpdateLog")
    public Result getUpdateLog(String skey) {
        if (CheckUtils.isObjectEmpty(skey)) {
            return Result.error("skey不能为空");
        }
        return versionService.getUpdateLog(skey);
    }

}
