package cn.myauthx.api.main.service;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Config;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
public interface IConfigService extends IService<Config> {
    /**
     * 获取web信息，redis
     * @return
     */
    Result getWebInfo();
}
