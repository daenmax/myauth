package cn.daenx.myauth.main.service;

import cn.daenx.myauth.main.entity.Admin;
import cn.daenx.myauth.main.entity.Menu;
import cn.daenx.myauth.base.vo.Result;
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
     * 获取权限菜单，treeListMap版
     *
     * @param admin
     * @return
     */
    Result getMenuListEx(Admin admin);

    /**
     * 获取菜单列表，全部，treeListMap版
     *
     * @return
     */
    Result getMenuListExAll();

    /**
     * 获取权限菜单，算法版
     *
     * @param admin
     * @return
     */
    Result getMenuList(Admin admin);

    /**
     * 获取菜单列表，全部，算法版
     *
     * @return
     */
    Result getMenuListAll();

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
