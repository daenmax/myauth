package cn.daenx.myauth.main.controller.web;

import cn.daenx.myauth.main.service.StatisService;
import cn.daenx.myauth.util.CheckUtils;
import cn.daenx.myauth.base.annotation.AdminLogin;
import cn.daenx.myauth.base.annotation.NoEncryptNoSign;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.main.entity.Soft;
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
public class StatisController {
    @Resource
    private StatisService statisService;

    /**
     * 获取软件数据
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_super_role = false)
    @PostMapping("/getSoftStatisData")
    public Result getSoftStatisData(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = jsonObject.toJavaObject(Soft.class);
        if (CheckUtils.isObjectEmpty(soft)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(soft.getId()) && CheckUtils.isObjectEmpty(soft.getSkey())) {
            return Result.error("id和skey不能全部为空");
        }
        return statisService.getSoftStatisData(soft);
    }

    /**
     * 获取数据排行
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_super_role = false)
    @PostMapping("/getDataRanking")
    public Result getDataRanking(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = jsonObject.toJavaObject(Soft.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        if (CheckUtils.isObjectEmpty(soft) || CheckUtils.isObjectEmpty(myPage)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(soft.getId()) && CheckUtils.isObjectEmpty(soft.getSkey())) {
            return Result.error("id和skey不能全部为空");
        }
        if (CheckUtils.isObjectEmpty(myPage.getPageIndex()) || CheckUtils.isObjectEmpty(myPage.getPageSize())) {
            return Result.error("页码和尺寸参数不能为空");
        }
        return statisService.getDataRanking(soft, myPage);
    }

    /**
     * 获取设备排行
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_super_role = false)
    @PostMapping("/getUserDeviceInfoRanking")
    public Result getUserDeviceInfoRanking(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = jsonObject.toJavaObject(Soft.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        if (CheckUtils.isObjectEmpty(soft) || CheckUtils.isObjectEmpty(myPage)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(soft.getId()) && CheckUtils.isObjectEmpty(soft.getSkey())) {
            return Result.error("id和skey不能全部为空");
        }
        if (CheckUtils.isObjectEmpty(myPage.getPageIndex()) || CheckUtils.isObjectEmpty(myPage.getPageSize())) {
            return Result.error("页码和尺寸参数不能为空");
        }
        return statisService.getUserDeviceInfoRanking(soft, myPage);
    }

    /**
     * 获取近7天每日新增用户数
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_super_role = false)
    @GetMapping("/getUserDatNew")
    public Result getUserDatNew(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        return statisService.getUserDatNew();
    }

    /**
     * 获取用户分布比例
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_super_role = false)
    @GetMapping("/getUserDistribution")
    public Result getUserDistribution(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        return statisService.getUserDistribution();
    }

    /**
     * 获取卡密状态比例
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_super_role = false)
    @GetMapping("/getCardDistribution")
    public Result getCardDistribution(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        return statisService.getCardDistribution();
    }

    /**
     * 获取封禁类型数量
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_super_role = false)
    @GetMapping("/getBanTypeCount")
    public Result getBanTypeCount(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        return statisService.getBanTypeCount();
    }
}
