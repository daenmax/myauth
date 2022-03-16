package cn.myauthx.api.main.controller.web;

import cn.myauthx.api.base.annotation.AdminLogin;
import cn.myauthx.api.base.annotation.NoEncryptNoSign;
import cn.myauthx.api.base.vo.MyPage;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.*;
import cn.myauthx.api.main.service.IBanService;
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
public class BanController {
    @Resource
    private IBanService banService;

    /**
     * 获取封禁列表
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("getBanList")
    public Result getBanList(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Ban ban = jsonObject.toJavaObject(Ban.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        if (CheckUtils.isObjectEmpty(ban) || CheckUtils.isObjectEmpty(myPage)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(myPage.getPageIndex()) || CheckUtils.isObjectEmpty(myPage.getPageSize())) {
            return Result.error("页码和尺寸参数不能为空");
        }
        if (CheckUtils.isObjectEmpty(ban.getFromSoftId())) {
            return Result.error("fromSoftId参数不能为空");
        }
        return banService.getBanList(ban, myPage);
    }

    /**
     * 查询封禁，根据id
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("getBan")
    public Result getBan(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Ban ban = jsonObject.toJavaObject(Ban.class);
        if (CheckUtils.isObjectEmpty(ban)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(ban.getId())) {
            return Result.error("id不能为空");
        }
        return banService.getBan(ban);
    }

    /**
     * 修改封禁
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("updBan")
    public Result updBan(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Ban ban = jsonObject.toJavaObject(Ban.class);
        if (CheckUtils.isObjectEmpty(ban)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(ban.getId())) {
            return Result.error("id不能为空");
        }
        ban.setAddTime(null);
        ban.setFromSoftId(null);
        if (CheckUtils.isObjectEmpty(ban.getValue()) && CheckUtils.isObjectEmpty(ban.getToTime())
                && CheckUtils.isObjectEmpty(ban.getWhy()) && CheckUtils.isObjectEmpty(ban.getType())) {
            return Result.error("参数不能全部为空");
        }
        return banService.updBan(ban);
    }

    /**
     * 添加封禁
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("addBan")
    public Result addBan(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Ban ban = jsonObject.toJavaObject(Ban.class);
        if (CheckUtils.isObjectEmpty(ban)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(ban.getFromSoftId())) {
            return Result.error("fromSoftId参数不能为空");
        }
        if (CheckUtils.isObjectEmpty(ban.getToTime()) || ban.getToTime() == 0) {
            ban.setToTime(-1);
        }
        if (CheckUtils.isObjectEmpty(ban.getValue()) || CheckUtils.isObjectEmpty(ban.getWhy()) || CheckUtils.isObjectEmpty(ban.getType())) {
            return Result.error("参数不全");
        }
        return banService.addBan(ban);
    }

    /**
     * 删除封禁（支持批量）
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("delBan")
    public Result delBan(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        String ids = jsonObject.getString("ids");
        if (CheckUtils.isObjectEmpty(ids)) {
            return Result.error("ids不能为空，多个用英文逗号隔开");
        }
        return banService.delBan(ids);
    }

}
