package cn.daenx.myauth.main.controller.web;


import cn.daenx.myauth.base.annotation.AdminLogin;
import cn.daenx.myauth.base.annotation.NoEncryptNoSign;
import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.Storage;
import cn.daenx.myauth.main.entity.StorageType;
import cn.daenx.myauth.main.service.IStorageService;
import cn.daenx.myauth.main.service.IStorageTypeService;
import cn.daenx.myauth.util.CheckUtils;
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
public class StorageController {
    @Resource
    private IStorageService storageService;

    /**
     * 获取额外存储列表
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("getStorageList")
    public Result getStorageList(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Storage storage = jsonObject.toJavaObject(Storage.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        if (CheckUtils.isObjectEmpty(storage) || CheckUtils.isObjectEmpty(myPage)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(myPage.getPageIndex()) || CheckUtils.isObjectEmpty(myPage.getPageSize())) {
            return Result.error("页码和尺寸参数不能为空");
        }
        if (CheckUtils.isObjectEmpty(storage.getFromSoftId())) {
            return Result.error("fromSoftId参数不能为空");
        }
        return storageService.getStorageList(storage, myPage);
    }

    /**
     * 查询额外存储，根据id
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("getStorage")
    public Result getStorage(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Storage storage = jsonObject.toJavaObject(Storage.class);
        if (CheckUtils.isObjectEmpty(storage)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(storage.getId())) {
            return Result.error("id不能为空");
        }
        return storageService.getStorage(storage);
    }

    /**
     * 修改额外存储
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("updStorage")
    public Result updStorage(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Storage storage = jsonObject.toJavaObject(Storage.class);
        if (CheckUtils.isObjectEmpty(storage)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(storage.getId())) {
            return Result.error("id不能为空");
        }
        if (CheckUtils.isObjectEmpty(storage.getFromSoftId()) && CheckUtils.isObjectEmpty(storage.getStatus())
                && CheckUtils.isObjectEmpty(storage.getFromStorageTypeId()) && CheckUtils.isObjectEmpty(storage.getContent())
                && CheckUtils.isObjectEmpty(storage.getNumber()) && CheckUtils.isObjectEmpty(storage.getRemark())) {
            return Result.error("参数不能全部为空");
        }
        return storageService.updStorage(storage);
    }

    /**
     * 添加额外存储
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("addStorage")
    public Result addStorage(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Storage storage = jsonObject.toJavaObject(Storage.class);
        if (CheckUtils.isObjectEmpty(storage)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(storage.getFromSoftId()) || CheckUtils.isObjectEmpty(storage.getStatus())
                || CheckUtils.isObjectEmpty(storage.getFromStorageTypeId()) || CheckUtils.isObjectEmpty(storage.getContent())) {
            return Result.error("参数不全");
        }
        storage.setNumber(null);
        return storageService.addStorage(storage);
    }

    /**
     * 删除额外存储（支持批量）
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("delStorage")
    public Result delStorage(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        String ids = jsonObject.getString("ids");
        if (CheckUtils.isObjectEmpty(ids)) {
            return Result.error("ids不能为空，多个用英文逗号隔开");
        }
        return storageService.delStorage(ids);
    }
}
