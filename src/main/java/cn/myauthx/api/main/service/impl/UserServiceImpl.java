package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Ban;
import cn.myauthx.api.main.entity.Card;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.entity.User;
import cn.myauthx.api.main.enums.CardEnums;
import cn.myauthx.api.main.enums.SoftEnums;
import cn.myauthx.api.main.enums.UserEnums;
import cn.myauthx.api.main.mapper.BanMapper;
import cn.myauthx.api.main.mapper.CardMapper;
import cn.myauthx.api.main.mapper.UserMapper;
import cn.myauthx.api.main.service.IUserService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.MyUtils;
import cn.myauthx.api.util.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BanMapper banMapper;
    @Autowired
    private CardMapper cardMapper;
    @Value("${genKey}")
    private String genKey;
    /**
     * 注册
     * @param userC
     * @param softC
     * @return
     */
    @Override
    public Result register(User userC, Soft softC) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUser,userC.getUser());
        userLambdaQueryWrapper.eq(User::getFromSoftId,softC.getId());
        User userA = userMapper.selectOne(userLambdaQueryWrapper);
        if(!CheckUtils.isObjectEmpty(userA)){
            return Result.error("账号已存在");
        }
        if(softC.getType().equals(SoftEnums.TYPE_FREE.getCode())){
            //免费模式
            User user = new User();
            user.setUser(userC.getUser());
            user.setPass(userC.getPass());
            user.setName(userC.getName());
            user.setQq(userC.getQq());
            user.setLastIp(userC.getLastIp());
            user.setRegTime(Integer.valueOf(MyUtils.getTimeStamp()));
            user.setFromSoftId(softC.getId());
            user.setFromSoftKey(softC.getSkey());
            user.setRemark(userC.getRemark());
            user.setDeviceInfo(userC.getDeviceInfo());
            user.setDeviceCode(userC.getDeviceCode());
            int num = userMapper.insert(user);
            if(num > 0){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("user",user.getUser());
                jsonObject.put("authTime",-1);
                jsonObject.put("point",0);
                return Result.ok("注册成功",jsonObject);
            }else{
                return Result.error("注册失败");
            }
        }else{
            //收费模式
            if(CheckUtils.isObjectEmpty(userC.getCkey())){
                //账号+密码
                if(CheckUtils.isObjectEmpty(userC.getPass())){
                    return Result.error("卡密为空时，密码不能为空");
                }
                User user = new User();
                user.setUser(userC.getUser());
                user.setName(userC.getName());
                user.setQq(userC.getQq());
                user.setPass(userC.getUser());
                user.setLastIp(userC.getLastIp());
                user.setAuthTime(Integer.valueOf(MyUtils.getTimeStamp()));
                user.setRegTime(Integer.valueOf(MyUtils.getTimeStamp()));
                user.setFromSoftId(softC.getId());
                user.setFromSoftKey(softC.getSkey());
                user.setRemark(userC.getRemark());
                user.setDeviceInfo(userC.getDeviceInfo());
                user.setDeviceCode(userC.getDeviceCode());
                int num = userMapper.insert(user);
                if(num > 0){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("user",user.getUser());
                    jsonObject.put("authTime",user.getAuthTime());
                    jsonObject.put("point",0);
                    return Result.ok("注册成功",jsonObject);
                }else{
                    return Result.error("注册失败");
                }
            }else {
                //账号+卡密
                LambdaQueryWrapper<Card> cardLambdaQueryWrapper = new LambdaQueryWrapper<>();
                cardLambdaQueryWrapper.eq(Card::getCkey,userC.getCkey());
                Card card = cardMapper.selectOne(cardLambdaQueryWrapper);
                if(CheckUtils.isObjectEmpty(card)){
                    return Result.error("卡密错误或者不存在");
                }
                if(card.getStatus().equals(CardEnums.STATUS_USED.getCode())){
                    return Result.error("卡密已被使用");
                }
                if(card.getStatus().equals(CardEnums.STATUS_DISABLE.getCode())){
                    return Result.error("卡密已被禁用");
                }
                if(!card.getFromSoftId().equals(softC.getId())){
                    return Result.error("此卡密不属于当前软件");
                }
                User user = new User();
                user.setUser(userC.getUser());
                user.setName(userC.getName());
                user.setQq(userC.getQq());
                user.setCkey(card.getCkey());
                user.setLastIp(userC.getLastIp());
                user.setPoint(card.getPoint());
                user.setAuthTime(Integer.valueOf(MyUtils.getTimeStamp()) + card.getSeconds());
                user.setRegTime(Integer.valueOf(MyUtils.getTimeStamp()));
                user.setFromSoftId(softC.getId());
                user.setFromSoftKey(softC.getSkey());
                user.setRemark(userC.getRemark());
                user.setDeviceInfo(userC.getDeviceInfo());
                user.setDeviceCode(userC.getDeviceCode());
                int num = userMapper.insert(user);
                if(num > 0){
                    card.setLetUser(user.getUser());
                    card.setLetTime(Integer.valueOf(MyUtils.getTimeStamp()));
                    card.setStatus(CardEnums.STATUS_USED.getCode());
                    cardMapper.updateById(card);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("user",user.getUser());
                    jsonObject.put("authTime",user.getAuthTime());
                    jsonObject.put("point",user.getPoint());
                    return Result.ok("注册成功",jsonObject);
                }else{
                    return Result.error("注册失败");
                }
            }
        }
    }


    @Override
    public Result login(User userC, Soft softC) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUser,userC.getUser());
        userLambdaQueryWrapper.eq(User::getFromSoftId,softC.getId());
        User userA = userMapper.selectOne(userLambdaQueryWrapper);
        if(CheckUtils.isObjectEmpty(userA)){
            return Result.error("账号不存在");
        }
        LambdaQueryWrapper<Ban> banLambdaQueryWrapper = new LambdaQueryWrapper<>();
        banLambdaQueryWrapper.eq(Ban::getValue,userA.getUser());
        banLambdaQueryWrapper.eq(Ban::getType,3);
        Ban ban = banMapper.selectOne(banLambdaQueryWrapper);
        if(!CheckUtils.isObjectEmpty(ban)){
            if(ban.getToTime() == -1){
                String msg = "msg=被封禁" + "&type=user" + "&value=" + userA.getUser() + "&toTime=-1&time=" + ban.getAddTime()
                        + "&why=" + ban.getWhy();
                return Result.error(300,msg);
            }else{
                Integer seconds = ban.getToTime() - Integer.parseInt(MyUtils.getTimeStamp());
                if(seconds > 0){
                    String msg = "msg=被封禁" + "&type=user" + "&value=" + userA.getUser() + "&toTime=" + ban.getToTime() + "&time=" + ban.getAddTime()
                            + "&why=" + ban.getWhy();
                    return Result.error(300,msg);
                }
            }
        }

        userA.setFromVerId(userC.getFromVerId());
        userA.setFromVerKey(userC.getFromVerKey());
        userA.setLastIp(userC.getLastIp());
        userA.setLastTime(Integer.valueOf(MyUtils.getTimeStamp()));
        JSONObject jsonObject = new JSONObject(true);
        if(softC.getType().equals(SoftEnums.TYPE_FREE.getCode())) {
            //免费模式
            if(CheckUtils.isObjectEmpty(userA.getPass())){
                //密码为空
                if(softC.getBindDeviceCode().equals(SoftEnums.BIND_ABLE.getCode())){
                    //绑定机器码
                    if(!CheckUtils.isObjectEmpty(userA.getDeviceCode())){
                        //已经有历史机器码记录
                        if(!userA.getDeviceCode().equals(userC.getDeviceCode())){
                            return Result.error("绑定了机器码，请先换绑");
                        }
                    }
                }else{
                    userA.setDeviceCode(userC.getDeviceCode());
                    userA.setDeviceInfo(userC.getDeviceInfo());
                }
                String token = MyUtils.encUserToken(userA.getUser(),String.valueOf(userA.getLastTime()),String.valueOf(softC.getId()),genKey);
                userA.setToken(token);
                int num = userMapper.updateById(userA);
                if(num > 0){
                    redisUtil.set("user:" + userA.getFromSoftId() + ":" + userA.getUser(),userA);
                    jsonObject.put("user",userA.getUser());
                    jsonObject.put("name",userA.getName());
                    jsonObject.put("qq",userA.getQq());
                    jsonObject.put("point",userA.getPoint());
                    jsonObject.put("ckey",userA.getCkey());
                    jsonObject.put("regTime",userA.getRegTime());
                    jsonObject.put("remark",userA.getRemark());
                    jsonObject.put("authTime",userA.getAuthTime());
                    jsonObject.put("token",userA.getToken());
                    return Result.ok("登录成功",jsonObject);
                }
            }else{
                //密码不为空
                if(CheckUtils.isObjectEmpty(userC.getPass())){
                    return Result.error("密码不能为空");
                }
                if(!userA.getPass().equals(userC.getPass())){
                    return Result.error("密码错误");
                }
                if(softC.getBindDeviceCode().equals(SoftEnums.BIND_ABLE.getCode())){
                    //绑定机器码
                    if(!CheckUtils.isObjectEmpty(userA.getDeviceCode())){
                        //已经有历史机器码记录
                        if(!userA.getDeviceCode().equals(userC.getDeviceCode())){
                            return Result.error("绑定了机器码，请先换绑");
                        }
                    }
                }else{
                    userA.setDeviceCode(userC.getDeviceCode());
                    userA.setDeviceInfo(userC.getDeviceInfo());
                }
                String token = MyUtils.encUserToken(userA.getUser(),String.valueOf(userA.getLastTime()),String.valueOf(softC.getId()),genKey);
                userA.setToken(token);
                int num = userMapper.updateById(userA);
                if(num > 0){
                    redisUtil.set("user:" + userA.getFromSoftId() + ":" + userA.getUser(),userA);
                    jsonObject.put("user",userA.getUser());
                    jsonObject.put("name",userA.getName());
                    jsonObject.put("qq",userA.getQq());
                    jsonObject.put("point",userA.getPoint());
                    jsonObject.put("ckey",userA.getCkey());
                    jsonObject.put("regTime",userA.getRegTime());
                    jsonObject.put("remark",userA.getRemark());
                    jsonObject.put("authTime",userA.getAuthTime());
                    jsonObject.put("token",userA.getToken());
                    return Result.ok("登录成功",jsonObject);
                }
            }

        }else{
            //收费模式
            if(CheckUtils.isObjectEmpty(userA.getPass())){
                //密码为空
                if(Integer.parseInt(MyUtils.getTimeStamp()) > userA.getAuthTime()){
                    //授权未到期
                    if(softC.getBindDeviceCode().equals(SoftEnums.BIND_ABLE.getCode())){
                        //绑定机器码
                        if(!CheckUtils.isObjectEmpty(userA.getDeviceCode())){
                            //已经有历史机器码记录
                            if(!userA.getDeviceCode().equals(userC.getDeviceCode())){
                                return Result.error("绑定了机器码，请先换绑");
                            }
                        }
                    }else{
                        userA.setDeviceCode(userC.getDeviceCode());
                        userA.setDeviceInfo(userC.getDeviceInfo());
                    }
                    String token = MyUtils.encUserToken(userA.getUser(),String.valueOf(userA.getLastTime()),String.valueOf(softC.getId()),genKey);
                    userA.setToken(token);
                    int num = userMapper.updateById(userA);
                    if(num > 0){
                        redisUtil.set("user:" + userA.getFromSoftId() + ":" + userA.getUser(),userA);
                        jsonObject.put("user",userA.getUser());
                        jsonObject.put("name",userA.getName());
                        jsonObject.put("qq",userA.getQq());
                        jsonObject.put("point",userA.getPoint());
                        jsonObject.put("ckey",userA.getCkey());
                        jsonObject.put("regTime",userA.getRegTime());
                        jsonObject.put("remark",userA.getRemark());
                        jsonObject.put("authTime",userA.getAuthTime());
                        jsonObject.put("token",userA.getToken());
                        return Result.ok("登录成功",jsonObject);
                    }
                }else{
                    //授权已到期
                    return Result.error("授权已到期");
                }
            }else{
                //密码不为空
                if(CheckUtils.isObjectEmpty(userC.getPass())){
                    return Result.error("密码不能为空");
                }
                if(!userA.getPass().equals(userC.getPass())){
                    return Result.error("密码错误");
                }
                if(Integer.parseInt(MyUtils.getTimeStamp()) < userA.getAuthTime()){
                    //授权未到期
                    if(softC.getBindDeviceCode().equals(SoftEnums.BIND_ABLE.getCode())){
                        //绑定机器码
                        if(!CheckUtils.isObjectEmpty(userA.getDeviceCode())){
                            //已经有历史机器码记录
                            if(!userA.getDeviceCode().equals(userC.getDeviceCode())){
                                return Result.error("绑定了机器码，请先换绑");
                            }
                        }
                    }else{
                        userA.setDeviceCode(userC.getDeviceCode());
                        userA.setDeviceInfo(userC.getDeviceInfo());
                    }
                    String token = MyUtils.encUserToken(userA.getUser(),String.valueOf(userA.getLastTime()),String.valueOf(softC.getId()),genKey);
                    userA.setToken(token);
                    int num = userMapper.updateById(userA);
                    if(num > 0){
                        redisUtil.set("user:" + userA.getFromSoftId() + ":" + userA.getUser(),userA);
                        jsonObject.put("user",userA.getUser());
                        jsonObject.put("name",userA.getName());
                        jsonObject.put("qq",userA.getQq());
                        jsonObject.put("point",userA.getPoint());
                        jsonObject.put("ckey",userA.getCkey());
                        jsonObject.put("regTime",userA.getRegTime());
                        jsonObject.put("remark",userA.getRemark());
                        jsonObject.put("authTime",userA.getAuthTime());
                        jsonObject.put("token",userA.getToken());
                        return Result.ok("登录成功",jsonObject);
                    }
                }else{
                    //授权已到期
                    return Result.error("授权已到期");
                }
            }
        }
        return null;
    }
}
