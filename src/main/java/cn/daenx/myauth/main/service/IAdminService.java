package cn.daenx.myauth.main.service;

import cn.daenx.myauth.main.entity.Admin;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.base.vo.MyPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
public interface IAdminService extends IService<Admin> {
    /**
     * 登录
     *
     * @param user
     * @param pass
     * @return
     */
    Result login(String user, String pass, String ip);

    /**
     * 修改密码
     *
     * @param nowPass
     * @param newPass
     * @return
     */
    Result editPass(String nowPass, String newPass, Admin admin);

    /**
     * 校验token
     *
     * @param token
     * @return
     */
    Admin tokenIsOk(String token);

    /**
     * 修改QQ
     *
     * @param qq
     * @param admin
     * @return
     */
    Result editQQ(String qq, Admin admin);

    /**
     * 获取管理员列表
     *
     * @param admin
     * @param myPage
     * @return
     */
    Result getAdminList(Admin admin, MyPage myPage);

    /**
     * 修改管理员
     *
     * @param admin
     * @return
     */
    Result updAdmin(Admin admin);

    /**
     * 查询管理员，根据id
     *
     * @param admin
     * @return
     */
    Result getAdmin(Admin admin);

    /**
     * 添加管理员
     *
     * @param admin
     * @return
     */
    Result addAdmin(Admin admin);

    /**
     * 删除管理员
     *
     * @param admin
     * @return
     */
    Result delAdmin(Admin admin);

    /**
     * 奖惩管理员
     *
     * @param admin   操作对象
     * @param myAdmin 自己
     * @return
     */
    Result chaMoney(Admin admin, Admin myAdmin);

    /**
     * 获取我的信息
     *
     * @param admin
     * @return
     */
    Result getMyInfo(Admin admin);
}
