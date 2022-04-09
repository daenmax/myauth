package cn.daenx.myauth.main.controller;


import cn.daenx.myauth.base.annotation.NoEncryptNoSign;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.Storage;
import cn.daenx.myauth.main.service.IConfigService;
import cn.daenx.myauth.main.service.IStorageService;
import cn.daenx.myauth.main.service.StatisService;
import cn.daenx.myauth.util.CheckUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 开放API，需要开放接口key
 *
 * @author DaenMax
 */
@Slf4j
@RestController
@RequestMapping("/web")
public class OpenApiController {
    @Resource
    private StatisService statisService;
    @Resource
    private IConfigService configService;
    @Resource
    private IStorageService storageService;

    /**
     * 获取在线人数
     *
     * @param skey
     * @return
     */
    @NoEncryptNoSign
    @GetMapping("/getOnlineUserCount")
    public Result getOnlineUserCount(String skey, String apikey) {
        if (CheckUtils.isObjectEmpty(apikey)) {
            return Result.error("apikey不能为空");
        }
        if (CheckUtils.isObjectEmpty(skey)) {
            return Result.error("skey不能为空");
        }
        Integer apiKeyIsOk = configService.apiKeyIsOk(apikey);
        if (apiKeyIsOk.equals(-1)) {
            return Result.error("系统未设置apikey，无法使用开放接口");
        }
        if (apiKeyIsOk.equals(0)) {
            return Result.error("apikey不正确");
        }
        return statisService.getOnlineUserCount(skey);
    }

    /**
     * 获取用户总数
     *
     * @param skey
     * @return
     */
    @NoEncryptNoSign
    @GetMapping("/getUserCount")
    public Result getUserCount(String skey, String apikey) {
        if (CheckUtils.isObjectEmpty(apikey)) {
            return Result.error("apikey不能为空");
        }
        if (CheckUtils.isObjectEmpty(skey)) {
            return Result.error("skey不能为空");
        }
        Integer apiKeyIsOk = configService.apiKeyIsOk(apikey);
        if (apiKeyIsOk.equals(-1)) {
            return Result.error("系统未设置apikey，无法使用开放接口");
        }
        if (apiKeyIsOk.equals(0)) {
            return Result.error("apikey不正确");
        }
        return statisService.getUserCount(skey);
    }

    /**
     * 添加额外存储信息
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @PostMapping("addStorageInfo")
    public Result addStorageInfo(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Storage storage = jsonObject.toJavaObject(Storage.class);
        String apikey = jsonObject.getString("apikey");
        String skey = jsonObject.getString("skey");
        String type = jsonObject.getString("type");
        if (CheckUtils.isObjectEmpty(apikey)) {
            return Result.error("apikey不能为空");
        }
        Integer apiKeyIsOk = configService.apiKeyIsOk(apikey);
        if (apiKeyIsOk.equals(0)) {
            return Result.error("apikey不正确");
        }
        if (apiKeyIsOk.equals(-1)) {
            return Result.error("系统未设置apikey，无法使用开放接口");
        }

        if (CheckUtils.isObjectEmpty(skey)) {
            return Result.error("skey不能为空");
        }
        if (CheckUtils.isObjectEmpty(type)) {
            return Result.error("type不能为空");
        }


        if (CheckUtils.isObjectEmpty(storage)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(storage.getContent())) {
            return Result.error("参数不全");
        }
        storage = storageService.toStorage(storage, type, skey);
        if (CheckUtils.isObjectEmpty(storage)) {
            return Result.error("参数错误");
        }
        storage.setNumber(null);
        return storageService.addStorage(storage);
    }

    /**
     * 删除额外存储信息（支持批量）
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @PostMapping("delStorageInfo")
    public Result delStorageInfo(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        String ids = jsonObject.getString("ids");
        String apikey = jsonObject.getString("apikey");
        if (CheckUtils.isObjectEmpty(ids)) {
            return Result.error("ids不能为空，多个用英文逗号隔开");
        }
        if (CheckUtils.isObjectEmpty(apikey)) {
            return Result.error("apikey不能为空");
        }
        Integer apiKeyIsOk = configService.apiKeyIsOk(apikey);
        if (apiKeyIsOk.equals(0)) {
            return Result.error("apikey不正确");
        }
        if (apiKeyIsOk.equals(-1)) {
            return Result.error("系统未设置apikey，无法使用开放接口");
        }
        return storageService.delStorage(ids);
    }

    /**
     * 获取额外存储列表信息
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @PostMapping("getStorageListInfo")
    public Result getStorageListInfo(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Storage storage = jsonObject.toJavaObject(Storage.class);
        String apikey = jsonObject.getString("apikey");
        String skey = jsonObject.getString("skey");
        String type = jsonObject.getString("type");
        if (CheckUtils.isObjectEmpty(apikey)) {
            return Result.error("apikey不能为空");
        }
        Integer apiKeyIsOk = configService.apiKeyIsOk(apikey);
        if (apiKeyIsOk.equals(0)) {
            return Result.error("apikey不正确");
        }
        if (apiKeyIsOk.equals(-1)) {
            return Result.error("系统未设置apikey，无法使用开放接口");
        }

        if (CheckUtils.isObjectEmpty(skey)) {
            return Result.error("skey不能为空");
        }
        if (CheckUtils.isObjectEmpty(type)) {
            return Result.error("type不能为空");
        }


        if (CheckUtils.isObjectEmpty(storage)) {
            return Result.error("参数错误");
        }
        storage = storageService.toStorage(storage, type, skey);
        if (CheckUtils.isObjectEmpty(storage)) {
            return Result.error("参数错误");
        }
        return storageService.getStorageListInfo(storage, type);
    }
}
