package cn.daenx.myauth.main.service;

import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.Soft;
import cn.daenx.myauth.main.entity.Storage;
import cn.daenx.myauth.main.entity.StorageType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author DaenMax
 * @since 2022-04-01
 */
public interface IStorageService extends IService<Storage> {
    /**
     * 获取额外存储类型列表
     *
     * @param storage
     * @param myPage
     * @return
     */
    Result getStorageList(Storage storage, MyPage myPage);

    /**
     * 查询额外存储，根据id
     *
     * @param storage
     * @return
     */
    Result getStorage(Storage storage);

    /**
     * 修改额外存储
     *
     * @param storage
     * @return
     */
    Result updStorage(Storage storage);

    /**
     * 添加额外存储
     *
     * @param storage
     * @return
     */
    Result addStorage(Storage storage);

    /**
     * 删除额外存储，支持批量
     *
     * @param ids 多个用英文逗号隔开
     * @return
     */
    Result delStorage(String ids);

    /**
     * 查询额外存储信息
     *
     * @param type
     * @param content
     * @param soft
     * @return
     */
    Result queryStorage(String type, String content, Soft soft);



    /**
     * 根据skey和type填充实体
     *
     * @param storage
     * @param type
     * @param skey
     * @return
     */
    Storage toStorage(Storage storage, String type, String skey);
}
