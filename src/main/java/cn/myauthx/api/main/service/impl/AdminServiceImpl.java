package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.base.vo.MyPage;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.*;
import cn.myauthx.api.main.enums.AdminEnums;
import cn.myauthx.api.main.enums.AlogEnums;
import cn.myauthx.api.main.mapper.AdminMapper;
import cn.myauthx.api.main.mapper.AlogMapper;
import cn.myauthx.api.main.service.IAdminService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.MyUtils;
import cn.myauthx.api.util.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    @Resource
    private AdminMapper adminMapper;
    @Resource
    private AlogMapper alogMapper;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 登录
     *
     * @param user
     * @param pass
     * @return
     */
    @Override
    public Result login(String user, String pass, String ip) {
        LambdaQueryWrapper<Admin> adminLambdaQueryWrapper = new LambdaQueryWrapper<>();
        adminLambdaQueryWrapper.eq(Admin::getUser, user);
        Admin admin = adminMapper.selectOne(adminLambdaQueryWrapper);
        if (CheckUtils.isObjectEmpty(admin)) {
            return Result.error("用户不存在");
        }
        if (!admin.getPass().equals(pass)) {
            return Result.error("密码错误");
        }
        if (AdminEnums.STATUS_DISABLE.getCode().equals(admin.getStatus())) {
            return Result.error("账号被禁用");
        }
        admin.setLastIp(ip);
        admin.setLastTime(Integer.valueOf(MyUtils.getTimeStamp()));
        String token = MyUtils.getUUID(false);
        admin.setToken(token);
        adminMapper.updateById(admin);
        redisUtil.set("admin:" + token, admin, AdminEnums.TOKEN_VALIDITY.getCode());
        Role role = (Role) redisUtil.get("role:" + admin.getRole());
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("user", admin.getUser());
        jsonObject.put("qq", admin.getQq());
        jsonObject.put("regTime", admin.getRegTime());
        jsonObject.put("token", admin.getToken());
        jsonObject.put("role", admin.getRole());
        jsonObject.put("roleName", role.getName());
        jsonObject.put("money", admin.getMoney());
        if(!CheckUtils.isObjectEmpty(role.getFromSoftId())){
            Soft obj = (Soft) redisUtil.get("id:soft:" + role.getFromSoftId());
            if(!CheckUtils.isObjectEmpty(obj)){
                jsonObject.put("fromSoftName", obj.getName());
            }
        }
        return Result.ok("登录成功", jsonObject);
    }

    /**
     * 修改密码
     *
     * @param nowPass
     * @param newPass
     * @return
     */
    @Override
    public Result editPass(String nowPass, String newPass, Admin admin) {
        if (!nowPass.equals(admin.getPass())) {
            return Result.error("旧密码错误");
        }
        if (nowPass.equals(newPass)) {
            return Result.error("新密码与旧密码不能一样");
        }
        String token = admin.getToken();
        admin.setToken("");
        admin.setPass(newPass);
        int num = adminMapper.updateById(admin);
        if (num < 1) {
            return Result.error("修改密码失败");
        }
        redisUtil.del("admin:" + token);
        return Result.ok("密码修改成功，请重新登录");
    }

    /**
     * 校验token
     *
     * @param token
     * @return
     */
    @Override
    public Admin tokenIsOk(String token) {
        if (CheckUtils.isObjectEmpty(token)) {
            return null;
        }
        LambdaQueryWrapper<Admin> adminLambdaQueryWrapper = new LambdaQueryWrapper<>();
        adminLambdaQueryWrapper.eq(Admin::getToken, token);
        Admin admin = adminMapper.selectOne(adminLambdaQueryWrapper);
        if (CheckUtils.isObjectEmpty(admin)) {
            return null;
        }
        if (admin.getStatus().equals(AdminEnums.STATUS_DISABLE.getCode())) {
            return null;
        }
        if (admin.getLastTime() + AdminEnums.TOKEN_VALIDITY.getCode() < Integer.parseInt(MyUtils.getTimeStamp())) {
            return null;
        }
        return admin;
    }

    /**
     * 修改QQ
     *
     * @param qq
     * @param admin
     * @return
     */
    @Override
    public Result editQQ(String qq, Admin admin) {
        String token = admin.getToken();
        admin.setQq(qq);
        int num = adminMapper.updateById(admin);
        if (num < 1) {
            return Result.error("修改QQ失败");
        }
        redisUtil.set("admin:" + token, admin);
        return Result.ok("QQ修改成功");
    }

    /**
     * 获取查询条件构造器
     *
     * @param admin
     * @return
     */
    public LambdaQueryWrapper<Admin> getQwAdmin(Admin admin) {
        LambdaQueryWrapper<Admin> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(admin.getUser()), Admin::getUser, admin.getUser());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(admin.getQq()), Admin::getQq, admin.getQq());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(admin.getRegTime()), Admin::getRegTime, admin.getRegTime());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(admin.getLastTime()), Admin::getLastTime, admin.getLastTime());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(admin.getLastIp()), Admin::getLastIp, admin.getLastIp());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(admin.getStatus()), Admin::getStatus, admin.getStatus());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(admin.getRole()), Admin::getRole, admin.getRole());
        return LambdaQueryWrapper;
    }

    /**
     * 获取管理员列表
     *
     * @param admin
     * @param myPage
     * @return
     */
    @Override
    public Result getAdminList(Admin admin, MyPage myPage) {
        Page<Admin> page = new Page<>(myPage.getPageIndex(), myPage.getPageSize(), true);
        if (!CheckUtils.isObjectEmpty(myPage.getOrders())) {
            for (int i = 0; i < myPage.getOrders().size(); i++) {
                myPage.getOrders().get(i).setColumn(MyUtils.camelToUnderline(myPage.getOrders().get(i).getColumn()));
            }
            page.setOrders(myPage.getOrders());
        }
        LambdaQueryWrapper<Admin> qwAdmin = getQwAdmin(admin);
        qwAdmin.ne(Admin::getUser, "admin");
        IPage<Admin> msgPage = adminMapper.selectPage(page, qwAdmin);
        for (int i = 0; i < msgPage.getRecords().size(); i++) {
            Role role = (Role) redisUtil.get("role:" + msgPage.getRecords().get(i).getRole());
            if (!CheckUtils.isObjectEmpty(role)) {
                msgPage.getRecords().get(i).setRoleName(role.getName());
                if(role.getFromSoftId() != 0){
                    Soft obj = (Soft) redisUtil.get("id:soft:" + role.getFromSoftId());
                    msgPage.getRecords().get(i).setFromSoftName(obj.getName());
                }
            }
        }
        return Result.ok("获取成功", msgPage);
    }

    /**
     * 修改管理员
     *
     * @param adminC
     * @return
     */
    @Override
    public Result updAdmin(Admin adminC) {
        Admin admin = adminMapper.selectById(adminC.getId());
        if (CheckUtils.isObjectEmpty(admin)) {
            return Result.error("管理员ID错误");
        }
        if ("admin".equals(admin.getUser())) {
            return Result.error("你不能修改admin");
        }
        if (!CheckUtils.isObjectEmpty(adminC.getUser())) {
            if (!admin.getUser().equals(adminC.getUser())) {
                LambdaQueryWrapper<Admin> adminLambdaQueryWrapper = new LambdaQueryWrapper<>();
                adminLambdaQueryWrapper.eq(Admin::getUser, adminC.getUser());
                List<Admin> adminList = adminMapper.selectList(adminLambdaQueryWrapper);
                if (adminList.size() > 0) {
                    return Result.error("账号已存在");
                }
            }
        }
        int num = adminMapper.updateById(adminC);
        if (num <= 0) {
            return Result.error("修改失败");
        }
        redisUtil.del("admin:" + admin.getToken());
        return Result.ok("修改成功");
    }

    /**
     * 查询管理员，根据id
     *
     * @param admin
     * @return
     */
    @Override
    public Result getAdmin(Admin admin) {
        Admin newAdmin = adminMapper.selectById(admin.getId());
        if (CheckUtils.isObjectEmpty(newAdmin)) {
            return Result.error("管理员ID错误");
        }
        return Result.ok("查询成功", newAdmin);
    }

    /**
     * 添加管理员
     *
     * @param adminC
     * @return
     */
    @Override
    public Result addAdmin(Admin adminC) {
        LambdaQueryWrapper<Admin> adminLambdaQueryWrapper = new LambdaQueryWrapper<>();
        adminLambdaQueryWrapper.eq(Admin::getUser, adminC.getUser());
        List<Admin> adminList = adminMapper.selectList(adminLambdaQueryWrapper);
        if (adminList.size() > 0) {
            return Result.error("账号已存在");
        }
        adminC.setRegTime(Integer.valueOf(MyUtils.getTimeStamp()));
        int num = adminMapper.insert(adminC);
        if (num <= 0) {
            return Result.error("添加失败");
        }
        return Result.ok("添加成功");
    }

    /**
     * 删除管理员
     *
     * @param adminC
     * @return
     */
    @Override
    public Result delAdmin(Admin adminC) {
        Admin admin = adminMapper.selectById(adminC.getId());
        if (CheckUtils.isObjectEmpty(admin)) {
            return Result.error("管理员ID错误");
        }
        if ("admin".equals(admin.getUser())) {
            return Result.error("你不能删除admin");
        }
        int num = adminMapper.deleteById(adminC.getId());
        if (num <= 0) {
            return Result.error("删除失败");
        }
        redisUtil.del("admin:" + admin.getToken());
        return Result.ok("删除成功");
    }

    /**
     * 奖惩管理员
     * @param admin 操作对象
     * @param myAdmin 自己
     * @return
     */
    @Override
    public Result chaMoney(Admin admin,Admin myAdmin) {
        LambdaQueryWrapper<Admin> adminLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!CheckUtils.isObjectEmpty(admin.getId())) {
            adminLambdaQueryWrapper.eq(Admin::getId, admin.getId());
        }
        if (!CheckUtils.isObjectEmpty(admin.getUser())) {
            adminLambdaQueryWrapper.eq(Admin::getUser, admin.getUser());
        }
        Admin one = adminMapper.selectOne(adminLambdaQueryWrapper);
        if (CheckUtils.isObjectEmpty(one)) {
            return Result.error("未找到");
        }

        BigDecimal cha = new BigDecimal(admin.getMoney());
        BigDecimal now = new BigDecimal(one.getMoney());
        String afterMoney = String.valueOf(cha.add(now));
        one.setMoney(afterMoney);
        int num = adminMapper.updateById(one);
        if (num <= 0) {
            return Result.error("奖惩失败");
        }
        Alog alog = new Alog();
        alog.setMoney(admin.getMoney());
        alog.setAfterMoney(afterMoney);
        alog.setAdminId(one.getId());
        alog.setData("操作管理员：" + myAdmin.getUser());
        alog.setType(AlogEnums.ADMIN_MAKE.getDesc());
        alog.setAddTime(Integer.valueOf(MyUtils.getTimeStamp()));
        alogMapper.insert(alog);
        return Result.ok("奖惩成功");
    }

    /**
     * 获取我的信息
     *
     * @param adminC
     * @return
     */
    @Override
    public Result getMyInfo(Admin adminC) {
        Admin admin = adminMapper.selectById(adminC.getId());
        Role role = (Role) redisUtil.get("role:" + admin.getRole());
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("user", admin.getUser());
        jsonObject.put("qq", admin.getQq());
        jsonObject.put("regTime", admin.getRegTime());
        jsonObject.put("role", admin.getRole());
        jsonObject.put("roleName", role.getName());
        jsonObject.put("money", admin.getMoney());
        if(!CheckUtils.isObjectEmpty(role.getFromSoftId())){
            Soft obj = (Soft) redisUtil.get("id:soft:" + role.getFromSoftId());
            if(!CheckUtils.isObjectEmpty(obj)){
                jsonObject.put("fromSoftName", obj.getName());
            }
        }
        return Result.ok("获取成功", jsonObject);
    }
}
