package cn.myauthx.api.main.service;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Config;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
public interface IConfigService extends IService<Config> {
    /**
     * APIKEY是否正确，一致返回1，不一致返回0，未设置apikey返回-1
     *
     * @param skey
     * @return
     */
    Integer apiKeyIsOk(String skey);

    /**
     * 获取web信息，redis
     *
     * @return
     */
    Result getWebInfo();

    /**
     * 修改系统设置
     *
     * @param config
     * @return
     */
    Result editConfig(Config config);

    /**
     * 获取系统设置
     *
     * @return
     */
    Result getConfig();
}
