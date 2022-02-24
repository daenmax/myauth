package cn.myauthx.api.main.service;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Event;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
public interface StatisService {
    /**
     * 获取在线人数
     *
     * @param soft
     * @return
     */
    Result getOnlineUserCount(Soft soft);
}
