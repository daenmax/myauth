package cn.daenx.myauth.main.service.impl;

import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.Msg;
import cn.daenx.myauth.main.entity.Soft;
import cn.daenx.myauth.main.entity.StorageType;
import cn.daenx.myauth.main.entity.Version;
import cn.daenx.myauth.main.mapper.SoftMapper;
import cn.daenx.myauth.main.mapper.StorageMapper;
import cn.daenx.myauth.main.mapper.StorageTypeMapper;
import cn.daenx.myauth.main.service.IStorageTypeService;
import cn.daenx.myauth.util.CheckUtils;
import cn.daenx.myauth.util.MyUtils;
import cn.daenx.myauth.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author DaenMax
 * @since 2022-04-01
 */
@Service
public class StorageTypeServiceImpl extends ServiceImpl<StorageTypeMapper, StorageType> implements IStorageTypeService {
    @Resource
    private StorageTypeMapper storageTypeMapper;
    @Resource
    private SoftMapper softMapper;
    @Resource
    private RedisUtil redisUtil;
    /**
     * 获取查询条件构造器
     *
     * @param storageType
     * @return
     */
    public LambdaQueryWrapper<StorageType> getQwStorageType(StorageType storageType) {
        LambdaQueryWrapper<StorageType> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(storageType.getType()), StorageType::getType, storageType.getType());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(storageType.getStatus()), StorageType::getStatus, storageType.getStatus());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(storageType.getFromSoftId()), StorageType::getFromSoftId, storageType.getFromSoftId());
        return LambdaQueryWrapper;
    }

    /**
     * 获取额外存储类型列表
     *
     * @param storageType
     * @param myPage
     * @return
     */
    @Override
    public Result getStorageTypeList(StorageType storageType, MyPage myPage) {
        Page<StorageType> page = new Page<>(myPage.getPageIndex(), myPage.getPageSize(), true);
        if (!CheckUtils.isObjectEmpty(myPage.getOrders())) {
            for (int i = 0; i < myPage.getOrders().size(); i++) {
                myPage.getOrders().get(i).setColumn(MyUtils.camelToUnderline(myPage.getOrders().get(i).getColumn()));
            }
            page.setOrders(myPage.getOrders());
        }
        IPage<StorageType> msgPage = storageTypeMapper.selectPage(page, getQwStorageType(storageType));
        for (int i = 0; i < msgPage.getRecords().size(); i++) {
            Soft obj = (Soft) redisUtil.get("id:soft:" + msgPage.getRecords().get(i).getFromSoftId());
            if (!CheckUtils.isObjectEmpty(obj)) {
                msgPage.getRecords().get(i).setFromSoftName(obj.getName());
            }
        }
        return Result.ok("获取成功", msgPage);
    }

    /**
     * 获取额外存储类型，根据ID
     *
     * @param storageType
     * @return
     */
    @Override
    public Result getStorageType(StorageType storageType) {
        StorageType newStorageType = storageTypeMapper.selectById(storageType.getId());
        if (CheckUtils.isObjectEmpty(newStorageType)) {
            return Result.error("查询失败，未找到");
        }
        return Result.ok("查询成功", newStorageType);
    }

    /**
     * 修改额外存储类型
     *
     * @param storageType
     * @return
     */
    @Override
    public Result updStorageType(StorageType storageType) {
        LambdaQueryWrapper<StorageType> storageTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        storageTypeLambdaQueryWrapper.eq(StorageType::getType,storageType.getType());
        storageTypeLambdaQueryWrapper.eq(StorageType::getFromSoftId,storageType.getFromSoftId());
        StorageType storageType1 = storageTypeMapper.selectOne(storageTypeLambdaQueryWrapper);
        if(!CheckUtils.isObjectEmpty(storageType1)){
            return Result.error("该软件下，额外存储类型已存在");
        }
        int num = storageTypeMapper.updateById(storageType);
        if (num <= 0) {
            return Result.error("修改失败");
        }
        return Result.ok("修改成功");
    }

    /**
     * 添加额外存储类型
     *
     * @param storageType
     * @return
     */
    @Override
    public Result addStorageType(StorageType storageType) {
        Soft soft = softMapper.selectById(storageType.getFromSoftId());
        if (CheckUtils.isObjectEmpty(soft)) {
            return Result.error("fromSoftId错误");
        }
        LambdaQueryWrapper<StorageType> storageTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        storageTypeLambdaQueryWrapper.eq(StorageType::getType,storageType.getType());
        storageTypeLambdaQueryWrapper.eq(StorageType::getFromSoftId,storageType.getFromSoftId());
        StorageType storageType1 = storageTypeMapper.selectOne(storageTypeLambdaQueryWrapper);
        if(!CheckUtils.isObjectEmpty(storageType1)){
            return Result.error("该软件下，额外存储类型已存在");
        }
        int num = storageTypeMapper.insert(storageType);
        if (num <= 0) {
            return Result.error("添加失败");
        }
        return Result.ok("添加成功");
    }

    /**
     * 删除额外存储类型
     *
     * @param storageTypeC
     * @return
     */
    @Override
    public Result delStorageType(StorageType storageTypeC) {
        StorageType storageType = storageTypeMapper.selectById(storageTypeC.getId());
        if (CheckUtils.isObjectEmpty(storageType)) {
            return Result.error("删除失败，id错误");
        }
        storageTypeMapper.deleteById(storageType);
        return Result.ok("删除成功");
    }

    /**
     * 获取额外存储类型列表_全部_简要
     *
     * @param storageTypeC
     * @return
     */
    @Override
    public Result getStorageTypeListEx(StorageType storageTypeC) {
        LambdaQueryWrapper<StorageType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(StorageType::getId, StorageType::getType, StorageType::getType);
        lambdaQueryWrapper.eq(StorageType::getStatus,1);
        lambdaQueryWrapper.eq(StorageType::getFromSoftId,storageTypeC.getFromSoftId());
        if (!CheckUtils.isObjectEmpty(storageTypeC.getType())) {
            lambdaQueryWrapper.like(StorageType::getType, storageTypeC.getType());
        }
        List<Map<String, Object>> maps = storageTypeMapper.selectMaps(lambdaQueryWrapper);
        return Result.ok("获取成功", maps);
    }
}
