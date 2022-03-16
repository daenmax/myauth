package cn.myauthx.api.main.controller.web;

import cn.myauthx.api.base.annotation.AdminLogin;
import cn.myauthx.api.base.annotation.NoEncryptNoSign;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Msg;
import cn.myauthx.api.base.vo.MyPage;
import cn.myauthx.api.main.service.IMsgService;
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
public class MsgController {
    @Resource
    private IMsgService msgService;

    /**
     * 获取软件列表
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("getMsgList")
    public Result getMsgList(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Msg msg = jsonObject.toJavaObject(Msg.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        if (CheckUtils.isObjectEmpty(msg) || CheckUtils.isObjectEmpty(myPage)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(myPage.getPageIndex()) || CheckUtils.isObjectEmpty(myPage.getPageSize())) {
            return Result.error("页码和尺寸参数不能为空");
        }
        if (CheckUtils.isObjectEmpty(msg.getFromSoftId())) {
            return Result.error("fromSoftId参数不能为空");
        }
        return msgService.getMsgList(msg, myPage);
    }

    /**
     * 查询回复，根据id
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("getMsg")
    public Result getMsg(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Msg msg = jsonObject.toJavaObject(Msg.class);
        if (CheckUtils.isObjectEmpty(msg)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(msg.getId())) {
            return Result.error("id不能为空");
        }
        return msgService.getMsg(msg);
    }

    /**
     * 修改回复
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("updMsg")
    public Result updMsg(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Msg msg = jsonObject.toJavaObject(Msg.class);
        if (CheckUtils.isObjectEmpty(msg)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(msg.getId())) {
            return Result.error("id不能为空");
        }
        if (CheckUtils.isObjectEmpty(msg.getKeyword()) && CheckUtils.isObjectEmpty(msg.getMsg())
                && CheckUtils.isObjectEmpty(msg.getStatus()) && CheckUtils.isObjectEmpty(msg.getFromSoftId())
                && CheckUtils.isObjectEmpty(msg.getFromVerId())) {
            return Result.error("参数不能全部为空");
        }
        return msgService.updMsg(msg);
    }

    /**
     * 添加回复
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("addMsg")
    public Result addMsg(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Msg msg = jsonObject.toJavaObject(Msg.class);
        if (CheckUtils.isObjectEmpty(msg)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(msg.getKeyword()) || CheckUtils.isObjectEmpty(msg.getMsg()) || CheckUtils.isObjectEmpty(msg.getStatus())
                || CheckUtils.isObjectEmpty(msg.getFromSoftId())) {
            return Result.error("参数不全");
        }
        return msgService.addMsg(msg);
    }

    /**
     * 删除回复
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("delMsg")
    public Result delMsg(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Msg msg = jsonObject.toJavaObject(Msg.class);
        if (CheckUtils.isObjectEmpty(msg)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(msg.getId())) {
            return Result.error("id不能为空");
        }
        return msgService.delMsg(msg);
    }

}
