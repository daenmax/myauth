package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.base.vo.MyPage;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.*;
import cn.myauthx.api.main.mapper.AdminMapper;
import cn.myauthx.api.main.mapper.MenuMapper;
import cn.myauthx.api.main.mapper.RoleMapper;
import cn.myauthx.api.main.mapper.SoftMapper;
import cn.myauthx.api.main.service.IRoleService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.MyUtils;
import cn.myauthx.api.util.RedisUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author DaenMax
 * @since 2022-03-09
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private SoftMapper softMapper;
    @Resource
    private AdminMapper adminMapper;
    @Resource
    private MenuMapper menuMapper;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 获取查询条件构造器
     *
     * @param role
     * @return
     */
    public LambdaQueryWrapper<Role> getQwRole(Role role) {
        LambdaQueryWrapper<Role> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(role.getName()), Role::getName, role.getName());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(role.getFromSoftId()), Role::getFromSoftId, role.getFromSoftId());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(role.getDiscount()), Role::getDiscount, role.getDiscount());
        return LambdaQueryWrapper;
    }

    /**
     * 获取角色列表
     *
     * @param role
     * @param myPage
     * @return
     */
    @Override
    public Result getRoleList(Role role, MyPage myPage) {
        Page<Role> page = new Page<>(myPage.getPageIndex(), myPage.getPageSize(), true);
        if (!CheckUtils.isObjectEmpty(myPage.getOrders())) {
            for (int i = 0; i < myPage.getOrders().size(); i++) {
                myPage.getOrders().get(i).setColumn(MyUtils.camelToUnderline(myPage.getOrders().get(i).getColumn()));
            }
            page.setOrders(myPage.getOrders());
        }
        IPage<Role> msgPage = roleMapper.selectPage(page, getQwRole(role));
        for (int i = 0; i < msgPage.getRecords().size(); i++) {
            JSONArray jsonArray = JSONArray.parseArray(msgPage.getRecords().get(i).getMeunIds());
            List<String> list = (List<String>) JSONArray.toJavaObject(jsonArray, List.class);
            msgPage.getRecords().get(i).setMeunList(list);
            if (!CheckUtils.isObjectEmpty(msgPage.getRecords().get(i).getFromSoftId()) && !msgPage.getRecords().get(i).getFromSoftId().equals(0)) {
                Soft obj = (Soft) redisUtil.get("id:soft:" + msgPage.getRecords().get(i).getFromSoftId());
                msgPage.getRecords().get(i).setFromSoftName(obj.getName());
            }
        }
        return Result.ok("获取成功", msgPage);
    }

    /**
     * 获取角色列表_全部_简要
     *
     * @param role
     * @return
     */
    @Override
    public Result getRoleListEx(Role role) {
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!CheckUtils.isObjectEmpty(role.getFromSoftId())) {
            roleLambdaQueryWrapper.eq(Role::getFromSoftId, role.getFromSoftId());
        }
        roleLambdaQueryWrapper.select(Role::getId, Role::getName);
        if (!CheckUtils.isObjectEmpty(role.getName())) {
            roleLambdaQueryWrapper.like(Role::getName, role.getName());
        }
        List<Map<String, Object>> maps = roleMapper.selectMaps(roleLambdaQueryWrapper);
        return Result.ok("获取成功", maps);
    }

    /**
     * 查询角色，根据id
     *
     * @param role
     * @return
     */
    @Override
    public Result getRole(Role role) {
        Role newRole = roleMapper.selectById(role.getId());
        if (CheckUtils.isObjectEmpty(newRole)) {
            return Result.error("查询失败，未找到");
        }
        JSONArray jsonArray = JSONArray.parseArray(newRole.getMeunIds());
        List<String> list = (List<String>) JSONArray.toJavaObject(jsonArray, List.class);
        newRole.setMeunList(list);
        if (!CheckUtils.isObjectEmpty(newRole.getFromSoftId()) && !newRole.getFromSoftId().equals(0)) {
            Soft obj = (Soft) redisUtil.get("id:soft:" + newRole.getFromSoftId());
            newRole.setFromSoftName(obj.getName());
        }
        return Result.ok("查询成功", newRole);
    }

    /**
     * 判断List里是否包含一个String，不包含就返回真
     *
     * @param menuList
     * @param parentId
     * @return
     */
    public Boolean isNeedAdd(List<String> menuList, String parentId) {
        Boolean flag = true;
        for (String s : menuList) {
            if (s.equals(parentId)) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 遍历获取ParentId
     *
     * @param allMenuList
     * @param id
     * @return
     */
    public String getParId(List<Menu> allMenuList, String id) {
        Boolean flag = false;
        for (Menu menu1 : allMenuList) {
            if (menu1.getId().equals(id)) {
                if("0".equals(menu1.getParentId()) || CheckUtils.isObjectEmpty(menu1.getParentId())){
                    return "";
                }
                return menu1.getParentId();
            }
        }
        return "";
    }

    /**
     * 修改角色
     *
     * @param role
     * @return
     */
    @Override
    public Result updRole(Role role) {
        Role newRole = roleMapper.selectById(role.getId());
        if (CheckUtils.isObjectEmpty(newRole)) {
            return Result.error("角色ID错误");
        }
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        List<Menu> allMenuList = menuMapper.selectList(wrapper);
        //拷贝一个LIST
        List<String> menuList = new ArrayList<>();
        menuList.addAll(role.getMeunList());
        for (String s : menuList) {
            String parId = getParId(allMenuList, s);
            if(!CheckUtils.isObjectEmpty(parId)){
                if (isNeedAdd(role.getMeunList(), parId)) {
                    role.getMeunList().add(parId);
                }
            }
        }
        if (!CheckUtils.isObjectEmpty(role.getMeunList())) {
            JSONArray jsonArray = new JSONArray();
            for (String s : role.getMeunList()) {
                jsonArray.add(s);
            }
            role.setMeunIds(jsonArray.toJSONString());
        } else {
            role.setMeunIds("");
        }

        int num = roleMapper.updateById(role);
        if (num <= 0) {
            return Result.error("修改失败");
        }
        redisUtil.set("role:" + role.getId(), role);
        return Result.ok("修改成功");
    }

    /**
     * 添加角色
     *
     * @param role
     * @return
     */
    @Override
    public Result addRole(Role role) {
        if(!role.getFromSoftId().equals(0)){
            Soft soft = softMapper.selectById(role.getFromSoftId());
            if (CheckUtils.isObjectEmpty(soft)) {
                return Result.error("fromSoftId错误");
            }
        }
        if (!CheckUtils.isObjectEmpty(role.getMeunList())) {
            JSONArray jsonArray = new JSONArray();
            for (String s : role.getMeunList()) {
                jsonArray.add(s);
            }
            role.setMeunIds(jsonArray.toJSONString());
        } else {
            role.setMeunIds("");
        }

        int num = roleMapper.insert(role);
        if (num <= 0) {
            return Result.error("添加失败");
        }
        redisUtil.set("role:" + role.getId(), role);
        return Result.ok("添加成功");
    }

    /**
     * 删除角色
     *
     * @param role
     * @return
     */
    @Override
    public Result delRole(Role role) {
        Role newRole = roleMapper.selectById(role.getId());
        if (CheckUtils.isObjectEmpty(newRole)) {
            return Result.error("角色ID错误");
        }
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getRole, role.getId());
        List<Admin> adminList = adminMapper.selectList(wrapper);
        if (adminList.size() > 0) {
            return Result.error("请先删除使用了该角色的管理员");
        }
        int num = roleMapper.deleteById(role.getId());
        if (num <= 0) {
            return Result.error("删除失败");
        }
        redisUtil.del("role:" + role.getId());
        return Result.ok("删除成功");
    }
}
