package cn.myauthx.api.main.controller.web;


import cn.myauthx.api.base.annotation.AdminLogin;
import cn.myauthx.api.base.annotation.OpenApi;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Alog;
import cn.myauthx.api.main.entity.MyPage;
import cn.myauthx.api.main.entity.Plog;
import cn.myauthx.api.main.service.IAlogService;
import cn.myauthx.api.main.service.IPlogService;
import cn.myauthx.api.util.CheckUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author DaenMax
 * @since 2022-03-10
 */
@RestController
@RequestMapping("/web")
public class AlogController {
    @Resource
    private IAlogService alogService;

    /**
     * 获取日志列表
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("getAlogList")
    public Result getAlogList(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Alog alog = jsonObject.toJavaObject(Alog.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        if (CheckUtils.isObjectEmpty(alog) || CheckUtils.isObjectEmpty(myPage)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(myPage.getPageIndex()) || CheckUtils.isObjectEmpty(myPage.getPageSize())) {
            return Result.error("页码和尺寸参数不能为空");
        }
        return alogService.getAlogList(alog, myPage);
    }

    /**
     * 删除数据
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("delAlog")
    public Result delAlog(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Alog alog = jsonObject.toJavaObject(Alog.class);
        if (CheckUtils.isObjectEmpty(alog)) {
            alog = new Alog();
        }
        return alogService.delAlog(alog);
    }
}