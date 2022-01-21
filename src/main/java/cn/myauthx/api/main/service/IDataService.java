package cn.myauthx.api.main.service;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Data;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.entity.Version;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
public interface IDataService extends IService<Data> {
    Result upData(String type, String content, String ip,String device_info, String device_code,  Soft soft, Version version);
}
