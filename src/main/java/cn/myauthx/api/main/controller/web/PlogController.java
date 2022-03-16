package cn.myauthx.api.main.controller.web;

import cn.myauthx.api.base.annotation.AdminLogin;
import cn.myauthx.api.base.annotation.NoEncryptNoSign;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.base.vo.MyPage;
import cn.myauthx.api.main.entity.Plog;
import cn.myauthx.api.main.service.IPlogService;
import cn.myauthx.api.util.CheckUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
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
public class PlogController {
    @Resource
    private IPlogService plogService;

    /**
     * 获取日志列表
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("getPlogList")
    public Result getPlogList(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Plog plog = jsonObject.toJavaObject(Plog.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        if (CheckUtils.isObjectEmpty(plog) || CheckUtils.isObjectEmpty(myPage)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(myPage.getPageIndex()) || CheckUtils.isObjectEmpty(myPage.getPageSize())) {
            return Result.error("页码和尺寸参数不能为空");
        }
        return plogService.getPlogList(plog, myPage);
    }

    /**
     * 删除数据
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("delPlog")
    public Result delPlog(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Plog plog = jsonObject.toJavaObject(Plog.class);
        if (CheckUtils.isObjectEmpty(plog)) {
            plog = new Plog();
        }
        return plogService.delPlog(plog);
    }


}
