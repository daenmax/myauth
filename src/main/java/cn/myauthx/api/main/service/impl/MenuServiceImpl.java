package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.*;
import cn.myauthx.api.main.mapper.MenuMapper;
import cn.myauthx.api.main.mapper.RoleMapper;
import cn.myauthx.api.main.service.IMenuService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.MyUtils;
import cn.myauthx.api.util.RedisUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Resource
    private MenuMapper menuMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 获取菜单列表
     *
     * @param admin
     * @return
     */
    @Override
    public Result getMenuList(Admin admin) {
        Role role = roleMapper.selectById(admin.getRole());
        JSONArray jsonArray = JSONArray.parseArray(role.getMeunIds());
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (role.getFromSoftId().equals(0)) {
            menuLambdaQueryWrapper.orderBy(true, true, Menu::getLevel);
        } else {
            menuLambdaQueryWrapper.in(Menu::getId, jsonArray);
            menuLambdaQueryWrapper.orderBy(true, true, Menu::getLevel);
        }
        List<Menu> menuList = menuMapper.selectList(menuLambdaQueryWrapper);
        List<Menu> tmpMenuList = new ArrayList<>();
        if (menuList.size() == 0) {
            return Result.error("没有任何菜单", tmpMenuList);
        }
        //最深的层次数
        Integer maxLevel = menuList.get(menuList.size() - 1).getLevel();
        //从最里层开始循环
        for (Integer i = maxLevel; i > 0; i--) {
            List<Menu> menuListByLevel = getMenuListByLevel(menuList, i);
            if (i.equals(maxLevel)) {
                tmpMenuList = menuListByLevel;
            } else {
                //外循环父节点
                for (Menu menu : menuListByLevel) {
                    List<Menu> children = new ArrayList<>();
                    //内循环子节点
                    for (Menu tmpMenu : tmpMenuList) {
                        if (menu.getId().equals(tmpMenu.getParentId())) {
                            children.add(tmpMenu);
                        }
                    }
                    menu.setChildren(children);
                }
                tmpMenuList = menuListByLevel;
            }
        }
        return Result.ok("获取成功", tmpMenuList);
    }

    /**
     * 取指定level（层次）的数据
     *
     * @param list
     * @param level
     * @return
     */
    public List<Menu> getMenuListByLevel(List<Menu> list, Integer level) {
        List<Menu> newMenuList = new ArrayList<>();
        for (Menu menu : list) {
            if (level.equals(menu.getLevel())) {
                newMenuList.add(menu);
            }
        }
        sortMenuList(newMenuList);
        return newMenuList;
    }

    /**
     * 按照sort字段排序
     *
     * @param list
     */
    public void sortMenuList(List<Menu> list) {
        list.sort((menu1, menu2) -> {
            Integer sort1 = menu1.getSort();
            Integer sort2 = menu2.getSort();
            return sort1.compareTo(sort2);
        });
    }

    /**
     * 查询菜单，根据id
     *
     * @param menu
     * @return
     */
    @Override
    public Result getMenu(Menu menu) {
        Menu newMenu = menuMapper.selectById(menu.getId());
        if (CheckUtils.isObjectEmpty(newMenu)) {
            return Result.error("查询失败，未找到");
        }
        return Result.ok("查询成功", newMenu);
    }

    /**
     * 修改菜单
     *
     * @param menu
     * @return
     */
    @Override
    public Result updMenu(Menu menu) {
        int num = menuMapper.updateById(menu);
        if (num <= 0) {
            return Result.error("修改失败");
        }
        return Result.ok("修改成功");
    }

    /**
     * 添加菜单
     *
     * @param menu
     * @return
     */
    @Override
    public Result addMenu(Menu menu) {
        menu.setId(MyUtils.getUUID(true));
        int num = menuMapper.insert(menu);
        if (num <= 0) {
            return Result.error("添加失败");
        }
        return Result.ok("添加成功");
    }

    /**
     * 删除菜单
     *
     * @param menu
     * @return
     */
    @Override
    public Result delMenu(Menu menu) {
        int num = menuMapper.deleteById(menu.getId());
        if (num <= 0) {
            return Result.error("删除失败");
        }
        return Result.ok("删除成功");
    }
}
