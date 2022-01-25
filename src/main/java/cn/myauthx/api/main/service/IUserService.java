package cn.myauthx.api.main.service;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.entity.User;
import cn.myauthx.api.main.entity.Version;
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

    /**
     * 心跳
     * @param user
     * @param softC
     * @return
     */
    Result heart(User user, Soft softC);

    /**
     * 使用卡密
     * @param user
     * @param soft
     * @return
     */
    Result useCkey(User user,Soft soft);

    /**
     * 获取回复
     * @param soft
     * @param version
     * @param keyword
     * @return
     */
    Result getMsg(Soft soft, Version version, String keyword);

    /**
     * 解绑
     * @param user
     * @param soft
     * @return
     */
    Result unbind(User user,Soft soft);

    /**
     * 修改密码
     * @param user
     * @param nowPass
     * @param newPass
     * @return
     */
    Result editPass(String user,String nowPass,String newPass,Soft soft);

    /**
     * 修改资料：QQ和昵称
     * @param user
     * @param soft
     * @return
     */
    Result editInfo(User user,Soft soft);
}
