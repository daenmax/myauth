package cn.daenx.myauth.main.service;

import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.StorageType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author DaenMax
 * @since 2022-04-01
 */
public interface IStorageTypeService extends IService<StorageType> {
    /**
     * 获取额外存储类型列表
     *
     * @param storageType
     * @param myPage
     * @return
     */
    Result getStorageTypeList(StorageType storageType, MyPage myPage);

    /**
     * 获取额外存储类型，根据ID
     *
     * @param storageType
     * @return
     */
    Result getStorageType(StorageType storageType);

    /**
     * 修改额外存储类型
     *
     * @param storageType
     * @return
     */
    Result updStorageType(StorageType storageType);

    /**
     * 添加额外存储类型
     *
     * @param storageType
     * @return
     */
    Result addStorageType(StorageType storageType);

    /**
     * 删除额外存储类型
     *
     * @param storageType
     * @return
     */
    Result delStorageType(StorageType storageType);

    /**
     * 获取额外存储类型列表_全部_简要
     *
     * @param storageType
     * @return
     */
    Result getStorageTypeListEx(StorageType storageType);
}
