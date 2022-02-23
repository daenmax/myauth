package cn.myauthx.api.main.service;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Data;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.entity.Version;
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
}
