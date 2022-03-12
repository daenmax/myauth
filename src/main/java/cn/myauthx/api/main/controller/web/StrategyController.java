package cn.myauthx.api.main.controller.web;


import cn.myauthx.api.base.annotation.AdminLogin;
import cn.myauthx.api.base.annotation.OpenApi;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Event;
import cn.myauthx.api.main.entity.MyPage;
import cn.myauthx.api.main.entity.Role;
import cn.myauthx.api.main.entity.Strategy;
import cn.myauthx.api.main.service.IEventService;
import cn.myauthx.api.main.service.IStrategyService;
import cn.myauthx.api.util.CheckUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author DaenMax
 * @since 2022-03-09
 */
@RestController
@RequestMapping("/web")
public class StrategyController {
    @Resource
    private IStrategyService strategyService;

    /**
     * 获取策略列表
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("getStrategyList")
    public Result getStrategyList(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Strategy strategy = jsonObject.toJavaObject(Strategy.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        if (CheckUtils.isObjectEmpty(strategy) || CheckUtils.isObjectEmpty(myPage)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(myPage.getPageIndex()) || CheckUtils.isObjectEmpty(myPage.getPageSize())) {
            return Result.error("页码和尺寸参数不能为空");
        }
        if (CheckUtils.isObjectEmpty(strategy.getFromSoftId())) {
            return Result.error("fromSoftId参数不能为空");
        }
        return strategyService.getStrategyList(strategy, myPage);
    }

    /**
     * 获取策略列表_全部_简要
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("getStrategyListEx")
    public Result getStrategyListEx(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Strategy strategy = jsonObject.toJavaObject(Strategy.class);
        if (CheckUtils.isObjectEmpty(strategy)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(strategy.getFromSoftId())) {
            return Result.error("fromSoftId参数不能为空");
        }
        if (CheckUtils.isObjectEmpty(strategy.getType())) {
            return Result.error("type参数不能为空");
        }
        return strategyService.getStrategyListEx(strategy);
    }

    /**
     * 查询策略，根据id
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("getStrategy")
    public Result getStrategy(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Strategy strategy = jsonObject.toJavaObject(Strategy.class);
        if (CheckUtils.isObjectEmpty(strategy)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(strategy.getId())) {
            return Result.error("id不能为空");
        }
        return strategyService.getStrategy(strategy);
    }

    /**
     * 修改策略
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("updStrategy")
    public Result updStrategy(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Strategy strategy = jsonObject.toJavaObject(Strategy.class);
        if (CheckUtils.isObjectEmpty(strategy)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(strategy.getId())) {
            return Result.error("id不能为空");
        }
        if (CheckUtils.isObjectEmpty(strategy.getName()) && CheckUtils.isObjectEmpty(strategy.getType())
                && CheckUtils.isObjectEmpty(strategy.getValue()) && CheckUtils.isObjectEmpty(strategy.getSort())
                && CheckUtils.isObjectEmpty(strategy.getPrice()) && CheckUtils.isObjectEmpty(strategy.getFromSoftId())
                && CheckUtils.isObjectEmpty(strategy.getStatus())) {
            return Result.error("参数不能全部为空");
        }
        return strategyService.updStrategy(strategy);
    }

    @OpenApi
    @AdminLogin
    @PostMapping("addStrategy")
    public Result addStrategy(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Strategy strategy = jsonObject.toJavaObject(Strategy.class);
        if (CheckUtils.isObjectEmpty(strategy)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(strategy.getName()) || CheckUtils.isObjectEmpty(strategy.getType())
                || CheckUtils.isObjectEmpty(strategy.getValue()) || CheckUtils.isObjectEmpty(strategy.getSort())
                || CheckUtils.isObjectEmpty(strategy.getPrice()) || CheckUtils.isObjectEmpty(strategy.getFromSoftId())
                || CheckUtils.isObjectEmpty(strategy.getStatus())) {
            return Result.error("参数不全");
        }
        return strategyService.addStrategy(strategy);
    }

    /**
     * 删除策略
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("delStrategy")
    public Result delStrategy(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Strategy strategy = jsonObject.toJavaObject(Strategy.class);
        if (CheckUtils.isObjectEmpty(strategy)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(strategy.getId())) {
            return Result.error("id不能为空");
        }
        return strategyService.delStrategy(strategy);
    }
}
