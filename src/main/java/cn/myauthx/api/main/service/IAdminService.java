package cn.myauthx.api.main.service;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
public interface IAdminService extends IService<Admin> {
    /**
     * 登录
     * @param user
     * @param pass
     * @return
     */
    Result login(String user,String pass,String ip);

    /**
     * 修改密码
     * @param nowPass
     * @param newPass
     * @return
     */
    Result editPass(String nowPass,String newPass,Admin admin);
}
