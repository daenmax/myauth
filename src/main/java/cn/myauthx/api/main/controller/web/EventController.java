package cn.myauthx.api.main.controller.web;

import cn.myauthx.api.base.annotation.AdminLogin;
import cn.myauthx.api.base.annotation.OpenApi;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Event;
import cn.myauthx.api.main.entity.Js;
import cn.myauthx.api.main.entity.MyPage;
import cn.myauthx.api.main.service.IEventService;
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
public class EventController {
    @Resource
    private IEventService eventService;

    /**
     * 获取事件列表
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("getEventList")
    public Result getEventList(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Event event = jsonObject.toJavaObject(Event.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        if (CheckUtils.isObjectEmpty(event) || CheckUtils.isObjectEmpty(myPage)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(myPage.getPageIndex()) || CheckUtils.isObjectEmpty(myPage.getPageSize())) {
            return Result.error("页码和尺寸参数不能为空");
        }
        if (CheckUtils.isObjectEmpty(event.getFromSoftId())) {
            return Result.error("fromSoftId参数不能为空");
        }
        return eventService.getEventList(event, myPage);
    }

    /**
     * 查询事件，根据id
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("getEvent")
    public Result getEvent(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Event event = jsonObject.toJavaObject(Event.class);
        if (CheckUtils.isObjectEmpty(event)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(event.getId())) {
            return Result.error("id不能为空");
        }
        return eventService.getEvent(event);
    }

    /**
     * 修改事件
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("updEvent")
    public Result updEvent(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Event event = jsonObject.toJavaObject(Event.class);
        if (CheckUtils.isObjectEmpty(event)) {
            return Result.error("参数错误");
        }
        event.setAddTime(null);
        if (CheckUtils.isObjectEmpty(event.getId())) {
            return Result.error("id不能为空");
        }
        if (CheckUtils.isObjectEmpty(event.getName()) && CheckUtils.isObjectEmpty(event.getSeconds())
                && CheckUtils.isObjectEmpty(event.getPoint()) && CheckUtils.isObjectEmpty(event.getStatus())) {
            return Result.error("参数不能全部为空");
        }
        return eventService.updEvent(event);
    }

    /**
     * 添加事件
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("addEvent")
    public Result addEvent(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Event event = jsonObject.toJavaObject(Event.class);
        if (CheckUtils.isObjectEmpty(event)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(event.getFromSoftId())) {
            return Result.error("fromSoftId参数不能为空");
        }
        if (CheckUtils.isObjectEmpty(event.getName())) {
            return Result.error("name参数不能为空");
        }
        if (CheckUtils.isObjectEmpty(event.getSeconds()) && CheckUtils.isObjectEmpty(event.getPoint())) {
            return Result.error("Seconds和Point不能都为空");
        }
        return eventService.addEvent(event);
    }

    /**
     * 删除事件
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("delEvent")
    public Result delEvent(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Event event = jsonObject.toJavaObject(Event.class);
        if (CheckUtils.isObjectEmpty(event)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(event.getId())) {
            return Result.error("id不能为空");
        }
        return eventService.delEvent(event);
    }
}
