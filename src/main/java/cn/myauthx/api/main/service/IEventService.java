package cn.myauthx.api.main.service;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Event;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
public interface IEventService extends IService<Event> {
    /**
     * 触发事件
     * @param name
     * @param user
     * @param soft
     * @return
     */
    Result letEvent(String name,User user, Soft soft);
}
