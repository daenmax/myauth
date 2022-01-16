package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Admin;
import cn.myauthx.api.main.enums.AdminEnums;
import cn.myauthx.api.main.mapper.AdminMapper;
import cn.myauthx.api.main.service.IAdminService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.IpUtil;
import cn.myauthx.api.util.MyUtils;
import cn.myauthx.api.util.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    @Autowired
    private AdminMapper adminMapper;

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
    public Result login(String user, String pass,String ip) {
        LambdaQueryWrapper<Admin> adminLambdaQueryWrapper = new LambdaQueryWrapper<>();
        adminLambdaQueryWrapper.eq(Admin::getUser,user);
        Admin admin = adminMapper.selectOne(adminLambdaQueryWrapper);
        if(CheckUtils.isObjectEmpty(admin)){
            return Result.error("用户不存在");
        }
        if(!admin.getPass().equals(pass)){
            return Result.error("密码错误");
        }
        if(AdminEnums.STATUS_DISABLE.getCode().equals(admin.getStatus())){
            return Result.error("账号被禁用");
        }
        admin.setLastIp(ip);
        admin.setLastTime(Integer.valueOf(MyUtils.getTimeStamp()));
        String token = MyUtils.getUUID(false);
        admin.setToken(token);
        adminMapper.updateById(admin);
        redisUtil.set("admin_" + token,admin);
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("user",admin.getUser());
        jsonObject.put("qq",admin.getQq());
        jsonObject.put("regTime",admin.getRegTime());
        jsonObject.put("token",admin.getToken());
        return Result.ok("登录成功",jsonObject);
    }
}
