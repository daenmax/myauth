package cn.daenx.myauth.main.service.impl;

import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.Soft;
import cn.daenx.myauth.main.entity.Storage;
import cn.daenx.myauth.main.entity.StorageType;
import cn.daenx.myauth.main.mapper.SoftMapper;
import cn.daenx.myauth.main.mapper.StorageMapper;
import cn.daenx.myauth.main.mapper.StorageTypeMapper;
import cn.daenx.myauth.main.service.IStorageService;
import cn.daenx.myauth.util.CheckUtils;
import cn.daenx.myauth.util.MyUtils;
import cn.daenx.myauth.util.RedisUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author DaenMax
 * @since 2022-04-01
 */
@Service
public class StorageServiceImpl extends ServiceImpl<StorageMapper, Storage> implements IStorageService {
    @Resource
    private StorageMapper storageMapper;
    @Resource
    private SoftMapper softMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private StorageTypeMapper storageTypeMapper;

    /**
     * 获取查询条件构造器
     *
     * @param storage
     * @return
     */
    public LambdaQueryWrapper<Storage> getQwStorage(Storage storage) {
        LambdaQueryWrapper<Storage> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(storage.getFromSoftId()), Storage::getFromSoftId, storage.getFromSoftId());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(storage.getFromStorageTypeId()), Storage::getFromStorageTypeId, storage.getFromStorageTypeId());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(storage.getContent()), Storage::getContent, storage.getContent());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(storage.getNumber()), Storage::getNumber, storage.getNumber());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(storage.getStatus()), Storage::getStatus, storage.getStatus());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(storage.getRemark()), Storage::getRemark, storage.getRemark());
        return LambdaQueryWrapper;
    }

    /**
     * 根据ID获取名称
     *
     * @param storageTypeList
     * @param fromStorageTypeId
     * @return
     */
    public String getFromStorageTypeName(List<StorageType> storageTypeList, Integer fromStorageTypeId) {
        for (StorageType storageType : storageTypeList) {
            if (storageType.getId().equals(fromStorageTypeId)) {
                return storageType.getType();
            }
        }
        return "";
    }

    /**
     * 获取额外存储类型列表
     *
     * @param storage
     * @param myPage
     * @return
     */
    @Override
    public Result getStorageList(Storage storage, MyPage myPage) {
        Page<Storage> page = new Page<>(myPage.getPageIndex(), myPage.getPageSize(), true);
        if (!CheckUtils.isObjectEmpty(myPage.getOrders())) {
            for (int i = 0; i < myPage.getOrders().size(); i++) {
                myPage.getOrders().get(i).setColumn(MyUtils.camelToUnderline(myPage.getOrders().get(i).getColumn()));
            }
            page.setOrders(myPage.getOrders());
        }
        LambdaQueryWrapper<StorageType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<StorageType> storageTypeList = storageTypeMapper.selectList(lambdaQueryWrapper);
        IPage<Storage> msgPage = storageMapper.selectPage(page, getQwStorage(storage));
        for (int i = 0; i < msgPage.getRecords().size(); i++) {
            Soft obj = (Soft) redisUtil.get("id:soft:" + msgPage.getRecords().get(i).getFromSoftId());
            if (!CheckUtils.isObjectEmpty(obj)) {
                msgPage.getRecords().get(i).setFromSoftName(obj.getName());
            }
            String fromStorageType = getFromStorageTypeName(storageTypeList, msgPage.getRecords().get(i).getFromStorageTypeId());
            msgPage.getRecords().get(i).setFromStorageTypeName(fromStorageType);
        }
        return Result.ok("获取成功", msgPage);
    }

    /**
     * 查询额外存储，根据id
     *
     * @param storage
     * @return
     */
    @Override
    public Result getStorage(Storage storage) {
        Storage newStorage = storageMapper.selectById(storage.getId());
        if (CheckUtils.isObjectEmpty(newStorage)) {
            return Result.error("查询失败，未找到");
        }
        return Result.ok("查询成功", newStorage);
    }

    /**
     * 修改额外存储
     *
     * @param storage
     * @return
     */
    @Override
    public Result updStorage(Storage storage) {
        int num = storageMapper.updateById(storage);
        if (num <= 0) {
            return Result.error("修改失败");
        }
        return Result.ok("修改成功");
    }

    /**
     * 添加额外存储
     *
     * @param storage
     * @return
     */
    @Override
    public Result addStorage(Storage storage) {
        LambdaQueryWrapper<Storage> storageLambdaQueryWrapper = new LambdaQueryWrapper<>();
        storageLambdaQueryWrapper.eq(Storage::getContent, storage.getContent());
        storageLambdaQueryWrapper.eq(Storage::getFromStorageTypeId, storage.getFromStorageTypeId());
        storageLambdaQueryWrapper.eq(Storage::getFromSoftId, storage.getFromSoftId());
        Storage storage1 = storageMapper.selectOne(storageLambdaQueryWrapper);
        if (CheckUtils.isObjectEmpty(storage1)) {
            int num = storageMapper.insert(storage);
            if (num <= 0) {
                return Result.error("添加失败");
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("number", 1);
            return Result.ok("添加成功", jsonObject);
        } else {
            storage1.setNumber(storage1.getNumber() + 1);
            int num = storageMapper.updateById(storage1);
            if (num <= 0) {
                return Result.error("添加失败");
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("number", storage1.getNumber());
            return Result.ok("添加成功", jsonObject);
        }
    }

    /**
     * 删除额外存储，支持批量
     *
     * @param ids 多个用英文逗号隔开
     * @return
     */
    @Override
    public Result delStorage(String ids) {
        String[] idArray = ids.split(",");
        List<String> strings = Arrays.asList(idArray);
        if (idArray.length == 0) {
            return Result.error("ids参数格式可能错误");
        }
        int okCount = storageMapper.deleteBatchIds(strings);
        return Result.ok("成功删除 " + okCount + " 条额外存储");
    }

    /**
     * 查询额外存储信息
     *
     * @param type
     * @param content
     * @param soft
     * @return
     */
    @Override
    public Result queryStorage(String type, String content, Soft soft) {
        LambdaQueryWrapper<StorageType> storageTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        storageTypeLambdaQueryWrapper.eq(StorageType::getType, type);
        StorageType storageType = storageTypeMapper.selectOne(storageTypeLambdaQueryWrapper);
        if (CheckUtils.isObjectEmpty(storageType)) {
            return Result.error("类型不正确");
        }
        LambdaQueryWrapper<Storage> storageLambdaQueryWrapper = new LambdaQueryWrapper<>();
        storageLambdaQueryWrapper.eq(Storage::getFromStorageTypeId, storageType.getId());
        storageLambdaQueryWrapper.eq(Storage::getContent, content);
        storageLambdaQueryWrapper.eq(Storage::getFromSoftId, soft.getId());
        Storage storage = storageMapper.selectOne(storageLambdaQueryWrapper);
        if (CheckUtils.isObjectEmpty(storage)) {
            return Result.error("查询信息不存在");
        }
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("id", storage.getId());
        jsonObject.put("content", storage.getContent());
        jsonObject.put("number", storage.getNumber());
        jsonObject.put("remark", storage.getRemark());
        jsonObject.put("status", storage.getStatus());
        jsonObject.put("fromSoftName", soft.getName());
        return Result.ok("查询成功", jsonObject);
    }


    /**
     * 根据skey和type填充实体
     *
     * @param storage
     * @param type
     * @param skey
     * @return
     */
    @Override
    public Storage toStorage(Storage storage, String type, String skey) {
        LambdaQueryWrapper<StorageType> storageTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        storageTypeLambdaQueryWrapper.eq(StorageType::getType, type);
        StorageType storageType = storageTypeMapper.selectOne(storageTypeLambdaQueryWrapper);
        if (CheckUtils.isObjectEmpty(storageType)) {
            return null;
        }
        Soft soft = (Soft) redisUtil.get("soft:" + skey);
        if (CheckUtils.isObjectEmpty(soft)) {
            return null;
        }
        storage.setFromStorageTypeId(storageType.getId());
        storage.setFromSoftId(soft.getId());
        return storage;
    }

    /**
     * 获取额外存储列表信息
     *
     * @param storage
     * @param type
     * @return
     */
    @Override
    public Result getStorageListInfo(Storage storage, String type) {
        LambdaQueryWrapper<Storage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Storage::getFromSoftId, storage.getFromSoftId());
        lambdaQueryWrapper.eq(Storage::getFromStorageTypeId, storage.getFromStorageTypeId());
        List<Storage> storageList = storageMapper.selectList(lambdaQueryWrapper);
        JSONArray jsonArray = new JSONArray();
        for (Storage storage1 : storageList) {
            JSONObject jsonObject = new JSONObject(true);
            jsonObject.put("type", type);
            jsonObject.put("content", storage1.getContent());
            jsonObject.put("number", storage1.getNumber());
            jsonObject.put("remark", storage1.getRemark());
            jsonObject.put("status", storage1.getStatus());
            jsonArray.add(jsonObject);
        }
        return Result.ok("获取成功", jsonArray);
    }
}
