package cn.myauthx.api.main.controller.web;

import cn.myauthx.api.base.annotation.AdminLogin;
import cn.myauthx.api.base.annotation.OpenApi;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Js;
import cn.myauthx.api.main.entity.MyPage;
import cn.myauthx.api.main.service.IJsService;
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
public class JsController {
    @Resource
    private IJsService jsService;

    /**
     * 获取函数列表
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("getJsList")
    public Result getJsList(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Js js = jsonObject.toJavaObject(Js.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        if (CheckUtils.isObjectEmpty(js) || CheckUtils.isObjectEmpty(myPage)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(myPage.getPageIndex()) || CheckUtils.isObjectEmpty(myPage.getPageSize())) {
            return Result.error("页码和尺寸参数不能为空");
        }
        if (CheckUtils.isObjectEmpty(js.getFromSoftId())) {
            return Result.error("fromSoftId参数不能为空");
        }
        return jsService.getJsList(js, myPage);
    }

    /**
     * 查询函数，根据id
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("getJs")
    public Result getJs(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Js js = jsonObject.toJavaObject(Js.class);
        if (CheckUtils.isObjectEmpty(js)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(js.getId())) {
            return Result.error("id不能为空");
        }
        return jsService.getJs(js);
    }

    /**
     * 修改软件
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("updJs")
    public Result updJs(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Js js = jsonObject.toJavaObject(Js.class);
        if (CheckUtils.isObjectEmpty(js)) {
            return Result.error("参数错误");
        }
        js.setAddTime(null);
        if (CheckUtils.isObjectEmpty(js.getId())) {
            return Result.error("id不能为空");
        }
        if (CheckUtils.isObjectEmpty(js.getFromSoftId())) {
            return Result.error("fromSoftId参数不能为空");
        }
        if (CheckUtils.isObjectEmpty(js.getJsFun()) && CheckUtils.isObjectEmpty(js.getJsContent())
                && CheckUtils.isObjectEmpty(js.getStatus()) && CheckUtils.isObjectEmpty(js.getRemark())) {
            return Result.error("参数不能全部为空");
        }
        return jsService.updJs(js);
    }

    /**
     * 添加版本
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("addJs")
    public Result addJs(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Js js = jsonObject.toJavaObject(Js.class);
        if (CheckUtils.isObjectEmpty(js)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(js.getFromSoftId())) {
            return Result.error("fromSoftId参数不能为空");
        }
        if (CheckUtils.isObjectEmpty(js.getJsFun()) || CheckUtils.isObjectEmpty(js.getJsContent()) || CheckUtils.isObjectEmpty(js.getStatus())) {
            return Result.error("参数不全");
        }
        return jsService.addJs(js);
    }

    /**
     * 删除函数
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("delJs")
    public Result delJs(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Js js = jsonObject.toJavaObject(Js.class);
        if (CheckUtils.isObjectEmpty(js)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(js.getId())) {
            return Result.error("id不能为空");
        }
        return jsService.delJs(js);
    }

    /**
     * 运行函数
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("runJs")
    public Result runJs(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Js js = jsonObject.toJavaObject(Js.class);
        if (CheckUtils.isObjectEmpty(js)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(js.getId())) {
            return Result.error("id不能为空");
        }
        String c1 = jsonObject.getJSONObject("data").getString("c1");
        String c2 = jsonObject.getJSONObject("data").getString("c2");
        String c3 = jsonObject.getJSONObject("data").getString("c3");
        String c4 = jsonObject.getJSONObject("data").getString("c4");
        String c5 = jsonObject.getJSONObject("data").getString("c5");
        String c6 = jsonObject.getJSONObject("data").getString("c6");
        String c7 = jsonObject.getJSONObject("data").getString("c7");
        String c8 = jsonObject.getJSONObject("data").getString("c8");
        String c9 = jsonObject.getJSONObject("data").getString("c9");
        String c10 = jsonObject.getJSONObject("data").getString("c10");
        return jsService.runJsWeb(js, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10);
    }
}
