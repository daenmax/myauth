package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.*;
import cn.myauthx.api.main.mapper.MenuMapper;
import cn.myauthx.api.main.service.IMenuService;
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
    private RedisUtil redisUtil;

    /**
     * 获取菜单列表
     *
     * @param admin
     * @return
     */
    @Override
    public Result getMenuList(Admin admin) {
        Role role = (Role) redisUtil.get("role:" + admin.getRole());
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
        // 按照分数排名(从低到高)
        list.sort((menu1, menu2) -> {
            Integer sort1 = menu1.getSort();
            Integer sort2 = menu2.getSort();
            return sort1.compareTo(sort2);
        });
    }
}
