package cn.daenx.myauth.main.service;

import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.Data;
import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.main.entity.Soft;
import cn.daenx.myauth.main.entity.Version;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
public interface IDataService extends IService<Data> {
    /**
     * 上报数据
     *
     * @param type
     * @param content
     * @param ip
     * @param device_info
     * @param device_code
     * @param soft
     * @param version
     * @return
     */
    Result upData(String type, String content, String ip, String device_info, String device_code, Soft soft, Version version);

    /**
     * 获取数据列表
     *
     * @param data
     * @param myPage
     * @return
     */
    Result getDataList(Data data, MyPage myPage);

    /**
     * 删除数据
     *
     * @param data
     * @return
     */
    Result delData(Data data);
}
