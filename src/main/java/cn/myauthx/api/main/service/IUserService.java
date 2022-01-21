package cn.myauthx.api.main.service;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-07
 */
public interface IUserService extends IService<User> {
    /**
     * 注册
     * @param user
     * @param softC
     * @return
     */
    Result register(User user, Soft softC);

    /**
     * 登录
     * @param user
     * @param softC
     * @return
     */
    Result login(User user, Soft softC);

    Result heart(User user, Soft softC);

    Result useCkey(User user,Soft soft);

}
