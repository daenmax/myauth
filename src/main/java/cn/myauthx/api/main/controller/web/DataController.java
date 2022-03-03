package cn.myauthx.api.main.controller.web;

import cn.myauthx.api.base.annotation.AdminLogin;
import cn.myauthx.api.base.annotation.OpenApi;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Data;
import cn.myauthx.api.main.entity.MyPage;
import cn.myauthx.api.main.service.IDataService;
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
public class DataController {
    @Resource
    private IDataService dataService;

    /**
     * 获取数据列表
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("getDataList")
    public Result getDataList(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Data data = jsonObject.toJavaObject(Data.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        if (CheckUtils.isObjectEmpty(data) || CheckUtils.isObjectEmpty(myPage)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(myPage.getPageIndex()) || CheckUtils.isObjectEmpty(myPage.getPageSize())) {
            return Result.error("页码和尺寸参数不能为空");
        }
        return dataService.getDataList(data, myPage);
    }

    /**
     * 删除数据
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("delData")
    public Result delData(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Data data = jsonObject.toJavaObject(Data.class);
        if(CheckUtils.isObjectEmpty(data)){
            data = new Data();
        }
        return dataService.delData(data);
    }


}
