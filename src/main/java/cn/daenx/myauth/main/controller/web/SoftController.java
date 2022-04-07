package cn.daenx.myauth.main.controller.web;

import cn.daenx.myauth.util.CheckUtils;
import cn.daenx.myauth.base.annotation.AdminLogin;
import cn.daenx.myauth.base.annotation.NoEncryptNoSign;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.main.entity.Soft;
import cn.daenx.myauth.main.service.ISoftService;
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
public class SoftController {
    @Resource
    private ISoftService softService;

    /**
     * 获取软件列表
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("getSoftList")
    public Result getSoftList(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = jsonObject.toJavaObject(Soft.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        if (CheckUtils.isObjectEmpty(myPage.getPageIndex()) || CheckUtils.isObjectEmpty(myPage.getPageSize())) {
            return Result.error("页码和尺寸参数不能为空");
        }
        return softService.getSoftList(soft, myPage);
    }

    /**
     * 添加软件
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("addSoft")
    public Result addSoft(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = jsonObject.toJavaObject(Soft.class);
        if (CheckUtils.isObjectEmpty(soft)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(soft.getName()) || CheckUtils.isObjectEmpty(soft.getStatus()) || CheckUtils.isObjectEmpty(soft.getType())
                || CheckUtils.isObjectEmpty(soft.getGenKey()) || CheckUtils.isObjectEmpty(soft.getGenStatus()) || CheckUtils.isObjectEmpty(soft.getBindDeviceCode())
                || CheckUtils.isObjectEmpty(soft.getHeartTime()) || CheckUtils.isObjectEmpty(soft.getRegister())) {
            return Result.error("参数不全");
        }
        return softService.addSoft(soft);
    }

    /**
     * 修改软件
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("updSoft")
    public Result updSoft(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = jsonObject.toJavaObject(Soft.class);
        if (CheckUtils.isObjectEmpty(soft)) {
            return Result.error("参数错误");
        }
        soft.setSkey(null);
        soft.setAddTime(null);
        if (CheckUtils.isObjectEmpty(soft.getId())) {
            return Result.error("id不能为空");
        }
        if (CheckUtils.isObjectEmpty(soft.getName()) && CheckUtils.isObjectEmpty(soft.getStatus())
                && CheckUtils.isObjectEmpty(soft.getType()) && CheckUtils.isObjectEmpty(soft.getGenKey()) && CheckUtils.isObjectEmpty(soft.getGenStatus())
                && CheckUtils.isObjectEmpty(soft.getBindDeviceCode()) && CheckUtils.isObjectEmpty(soft.getHeartTime())
                && CheckUtils.isObjectEmpty(soft.getRegister())) {
            return Result.error("参数不能全部为空");
        }
        return softService.updSoft(soft);
    }

    /**
     * 删除软件，会同步删除版本、卡密、用户、事件、数据、封禁、JS、回复、日志
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("delSoft")
    public Result delSoft(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = jsonObject.toJavaObject(Soft.class);
        if (CheckUtils.isObjectEmpty(soft)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(soft.getId())) {
            return Result.error("id不能为空");
        }
        return softService.delSoft(soft);
    }

    /**
     * 查询软件，根据id或者skey
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("getSoft")
    public Result getSoft(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = jsonObject.toJavaObject(Soft.class);
        if (CheckUtils.isObjectEmpty(soft)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(soft.getId()) && CheckUtils.isObjectEmpty(soft.getSkey())) {
            return Result.error("id和skey不能全部为空");
        }
        return softService.getSoft(soft);
    }

    /**
     * 获取软件列表_全部_简要
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("getSoftListEx")
    public Result getSoftListEx(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = jsonObject.toJavaObject(Soft.class);
        return softService.getSoftListEx(soft);
    }
}
