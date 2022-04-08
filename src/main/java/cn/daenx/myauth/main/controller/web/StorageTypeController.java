package cn.daenx.myauth.main.controller.web;


import cn.daenx.myauth.base.annotation.AdminLogin;
import cn.daenx.myauth.base.annotation.NoEncryptNoSign;
import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.StorageType;
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
public class StorageTypeController {
    @Resource
    private IStorageTypeService storageTypeService;

    /**
     * 获取额外存储类型列表
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("getStorageTypeList")
    public Result getStorageTypeList(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        StorageType storageType = jsonObject.toJavaObject(StorageType.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        if (CheckUtils.isObjectEmpty(storageType) || CheckUtils.isObjectEmpty(myPage)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(myPage.getPageIndex()) || CheckUtils.isObjectEmpty(myPage.getPageSize())) {
            return Result.error("页码和尺寸参数不能为空");
        }
        if (CheckUtils.isObjectEmpty(storageType.getFromSoftId())) {
            return Result.error("fromSoftId参数不能为空");
        }
        return storageTypeService.getStorageTypeList(storageType, myPage);
    }

    /**
     * 查询额外存储类型，根据id
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("getStorageType")
    public Result getStorageType(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        StorageType storageType = jsonObject.toJavaObject(StorageType.class);
        if (CheckUtils.isObjectEmpty(storageType)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(storageType.getId())) {
            return Result.error("id不能为空");
        }
        return storageTypeService.getStorageType(storageType);
    }

    /**
     * 修改额外存储类型
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("updStorageType")
    public Result updStorageType(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        StorageType storageType = jsonObject.toJavaObject(StorageType.class);
        if (CheckUtils.isObjectEmpty(storageType)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(storageType.getId())) {
            return Result.error("id不能为空");
        }
        if (CheckUtils.isObjectEmpty(storageType.getType()) && CheckUtils.isObjectEmpty(storageType.getStatus())
                && CheckUtils.isObjectEmpty(storageType.getFromSoftId())) {
            return Result.error("参数不能全部为空");
        }
        return storageTypeService.updStorageType(storageType);
    }

    /**
     * 添加额外存储类型
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("addStorageType")
    public Result addStorageType(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        StorageType storageType = jsonObject.toJavaObject(StorageType.class);
        if (CheckUtils.isObjectEmpty(storageType)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(storageType.getType()) || CheckUtils.isObjectEmpty(storageType.getStatus())
                || CheckUtils.isObjectEmpty(storageType.getFromSoftId())) {
            return Result.error("参数不全");
        }
        return storageTypeService.addStorageType(storageType);
    }

    /**
     * 删除额外存储类型
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("delStorageType")
    public Result delStorageType(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        StorageType storageType = jsonObject.toJavaObject(StorageType.class);
        if (CheckUtils.isObjectEmpty(storageType)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(storageType.getId())) {
            return Result.error("id不能为空");
        }
        return storageTypeService.delStorageType(storageType);
    }

    /**
     * 获取额外存储类型列表_全部_简要
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("getStorageTypeListEx")
    public Result getStorageTypeListEx(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        StorageType storageType = jsonObject.toJavaObject(StorageType.class);
        if (CheckUtils.isObjectEmpty(storageType)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(storageType.getFromSoftId())) {
            return Result.error("fromSoftId参数不能为空");
        }
        return storageTypeService.getStorageTypeListEx(storageType);
    }
}
