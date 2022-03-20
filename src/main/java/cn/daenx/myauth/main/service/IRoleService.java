package cn.daenx.myauth.main.service;

import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.main.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author DaenMax
 * @since 2022-03-09
 */
public interface IRoleService extends IService<Role> {
    /**
     * 获取角色列表
     *
     * @param role
     * @param myPage
     * @return
     */
    Result getRoleList(Role role, MyPage myPage);

    /**
     * 获取角色列表_全部_简要
     *
     * @param role
     * @return
     */
    Result getRoleListEx(Role role);

    /**
     * 查询角色，根据id
     *
     * @param role
     * @return
     */
    Result getRole(Role role);

    /**
     * 修改角色
     *
     * @param role
     * @return
     */
    Result updRole(Role role);

    /**
     * 添加角色
     *
     * @param role
     * @return
     */
    Result addRole(Role role);

    /**
     * 删除角色
     *
     * @param role
     * @return
     */
    Result delRole(Role role);
}
