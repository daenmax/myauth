package cn.myauthx.api.main.service;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Admin;
import cn.myauthx.api.main.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
public interface IMenuService extends IService<Menu> {
    /**
     * 获取菜单列表
     *
     * @param admin
     * @return
     */
    Result getMenuList(Admin admin);

    /**
     * 查询菜单，根据id
     *
     * @param menu
     * @return
     */
    Result getMenu(Menu menu);

    /**
     * 修改菜单
     *
     * @param menu
     * @return
     */
    Result updMenu(Menu menu);

    /**
     * 添加菜单
     *
     * @param menu
     * @return
     */
    Result addMenu(Menu menu);

    /**
     * 删除菜单
     *
     * @param menu
     * @return
     */
    Result delMenu(Menu menu);
}
