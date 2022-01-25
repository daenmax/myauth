package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.*;
import cn.myauthx.api.main.enums.CardEnums;
import cn.myauthx.api.main.enums.MsgEnums;
import cn.myauthx.api.main.enums.SoftEnums;
import cn.myauthx.api.main.enums.UserEnums;
import cn.myauthx.api.main.mapper.*;
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
    @Autowired
    private MsgMapper msgMapper;
    @Autowired
    private PlogMapper plogMapper;
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

    /**
     * 登录
     * @param userC
     * @param softC
     * @return
     */
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
                            return Result.error("此账号已绑定其他机器码，请先解绑");
                        }
                    }else{
                        //没有历史机器码记录
                        userA.setDeviceCode(userC.getDeviceCode());
                        userA.setDeviceInfo(userC.getDeviceInfo());
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
                            return Result.error("此账号已绑定其他机器码，请先解绑");
                        }
                    }else{
                        //没有历史机器码记录
                        userA.setDeviceCode(userC.getDeviceCode());
                        userA.setDeviceInfo(userC.getDeviceInfo());
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
                                return Result.error("此账号已绑定其他机器码，请先解绑");
                            }
                        }else{
                            //没有历史机器码记录
                            userA.setDeviceCode(userC.getDeviceCode());
                            userA.setDeviceInfo(userC.getDeviceInfo());
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
                                return Result.error("此账号已绑定其他机器码，请先解绑");
                            }
                        }else{
                            //没有历史机器码记录
                            userA.setDeviceCode(userC.getDeviceCode());
                            userA.setDeviceInfo(userC.getDeviceInfo());
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

    /**
     * 心跳
     * @param userA
     * @param softC
     * @return
     */
    @Override
    public Result heart(User userA, Soft softC) {
        redisUtil.set("user:" + userA.getFromSoftId() + ":" + userA.getUser(),userA);
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("user",userA.getUser());
        jsonObject.put("name",userA.getName());
        jsonObject.put("qq",userA.getQq());
        jsonObject.put("point",userA.getPoint());
        jsonObject.put("ckey",userA.getCkey());
        jsonObject.put("regTime",userA.getRegTime());
        jsonObject.put("remark",userA.getRemark());
        jsonObject.put("authTime",userA.getAuthTime());
        return Result.ok("心跳成功",jsonObject);
    }


    /**
     * 使用卡密
     * @param userC
     * @param softC
     * @return
     */
    @Override
    public Result useCkey(User userC, Soft softC) {
        Plog plog = new Plog();
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
        if(CheckUtils.isObjectEmpty(userA.getAuthTime())){
            userA.setAuthTime(0);
        }
        if(CheckUtils.isObjectEmpty(userA.getPoint())){
            userA.setPoint(0);
        }
        //如果卡密包含授权时间
        if(!card.getSeconds().equals(0)){
            plog.setSeconds(card.getSeconds());
            if(userA.getAuthTime().equals(-1)){
                //已是永久授权
            }else{
                //不是永久授权
                if(userA.getAuthTime() < Integer.parseInt(MyUtils.getTimeStamp())){
                    //已经到期
                    userA.setAuthTime(Integer.parseInt(MyUtils.getTimeStamp())+ card.getSeconds());
                }else{
                    //未到期，则续费
                    userA.setAuthTime(Integer.valueOf(userA.getAuthTime()) + card.getSeconds());
                }

            }
        }
        plog.setAfterSeconds(userA.getAuthTime());
        //如果卡密包含点数
        if(!card.getSeconds().equals(0)){
            plog.setPoint(card.getPoint());
            userA.setPoint(Integer.valueOf(userA.getPoint()) + card.getPoint());
        }
        plog.setAfterPoint(userA.getPoint());
        plog.setFromUser(userA.getUser());
        plog.setAddTime(Integer.parseInt(MyUtils.getTimeStamp()));
        plog.setFromSoftId(card.getFromSoftId());
        plog.setRemark(card.getCkey());
        User userR = (User) redisUtil.get("user:" + softC.getId() + ":" + userC.getUser());
        if(!CheckUtils.isObjectEmpty(userR)){
            userA.setLastTime(userR.getLastTime());
        }
        int num = userMapper.updateById(userA);
        if(num > 0){
            if(!CheckUtils.isObjectEmpty(userR)) {
                redisUtil.set("user:" + userA.getFromSoftId() + ":" + userA.getUser(), userA);
            }
            plogMapper.insert(plog);
            card.setLetUser(userA.getUser());
            card.setLetTime(Integer.valueOf(MyUtils.getTimeStamp()));
            card.setStatus(CardEnums.STATUS_USED.getCode());
            cardMapper.updateById(card);
            JSONObject jsonObject = new JSONObject(true);
            jsonObject.put("user",userA.getUser());
            jsonObject.put("name",userA.getName());
            jsonObject.put("qq",userA.getQq());
            jsonObject.put("point",userA.getPoint());
            jsonObject.put("ckey",userA.getCkey());
            jsonObject.put("regTime",userA.getRegTime());
            jsonObject.put("remark",userA.getRemark());
            jsonObject.put("authTime",userA.getAuthTime());
            return Result.ok("使用卡密成功",jsonObject);
        }else{
            return Result.error("使用卡密失败");
        }
    }

    /**
     * 获取回复
     * @param soft
     * @param version
     * @param keyword
     * @return
     */
    @Override
    public Result getMsg(Soft soft, Version version, String keyword) {
        LambdaQueryWrapper<Msg> msgLambdaQueryWrapper = new LambdaQueryWrapper<>();
        msgLambdaQueryWrapper.eq(Msg::getFromSoftId,soft.getId());
        msgLambdaQueryWrapper.eq(Msg::getKeyword,keyword);
        msgLambdaQueryWrapper.eq(Msg::getFromVerId,version.getId());
        Msg msg = msgMapper.selectOne(msgLambdaQueryWrapper);
        JSONObject jsonObject = new JSONObject(true);
        if(CheckUtils.isObjectEmpty(msg)){
            LambdaQueryWrapper<Msg> msgLambdaQueryWrapper2 = new LambdaQueryWrapper<>();
            msgLambdaQueryWrapper2.eq(Msg::getFromSoftId,soft.getId());
            msgLambdaQueryWrapper2.eq(Msg::getKeyword,keyword);
            msg = msgMapper.selectOne(msgLambdaQueryWrapper2);
            if(CheckUtils.isObjectEmpty(msg)){
                return Result.error("回复不存在");
            }else {
                if(CheckUtils.isObjectEmpty(msg.getFromVerId())){
                    if(msg.getStatus().equals(MsgEnums.STATUS_DISABLE.getCode())){
                        return Result.error("回复已被禁用");
                    }
                    jsonObject.put("keyword",msg.getKeyword());
                    jsonObject.put("msg",MyUtils.base64Encode(msg.getMsg()));
                    return Result.ok("获取回复成功",jsonObject);
                }else{
                    if(msg.getFromVerId().equals(version.getId())){
                        if(msg.getStatus().equals(MsgEnums.STATUS_DISABLE.getCode())){
                            return Result.error("回复已被禁用");
                        }
                        jsonObject.put("keyword",msg.getKeyword());
                        jsonObject.put("msg", MyUtils.base64Encode(msg.getMsg()));
                        return Result.ok("获取回复成功",jsonObject);
                    }else{
                        return Result.error("该回复在当前版本不能使用");
                    }
                }

            }
        }else{
            if(msg.getStatus().equals(MsgEnums.STATUS_DISABLE.getCode())){
                return Result.error("回复已被禁用");
            }
            jsonObject.put("keyword",msg.getKeyword());
            jsonObject.put("msg",MyUtils.base64Encode(msg.getMsg()));
            return Result.ok("获取回复成功",jsonObject);
        }
    }

    /**
     * 解绑
     * @param userA
     * @param softC
     * @return
     */
    @Override
    public Result unbind(User userA, Soft softC) {
        int num = userMapper.updateById(userA);
        if(num == 0){
            return Result.error("解绑失败");
        }
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("user",userA.getUser());
        redisUtil.del("user:" + softC.getId() + ":" + userA.getUser());
        return Result.ok("解绑成功",jsonObject);
    }

    /**
     * 修改密码
     * @param userS
     * @param nowPass
     * @param newPass
     * @param softC
     * @return
     */
    @Override
    public Result editPass(String userS, String nowPass, String newPass,Soft softC) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUser,userS);
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
        if(CheckUtils.isObjectEmpty(userA.getPass())){
            return Result.error("账号不允许修改密码");
        }
        if(!userA.getPass().equals(nowPass)){
            return Result.error("旧密码错误");
        }
        userA.setPass(newPass);
        int num = userMapper.updateById(userA);
        if(num == 0){
            return Result.error("修改密码失败");
        }
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("user",userA.getUser());
        redisUtil.del("user:" + softC.getId() + ":" + userA.getUser());
        return Result.ok("修改密码成功，请重新登录",jsonObject);
    }

    /**
     * 修改资料：QQ和昵称
     *
     * @param user
     * @param soft
     * @return
     */
    @Override
    public Result editInfo(User user, Soft soft) {
        User userR = (User) redisUtil.get("user:" + soft.getId() + ":" + user.getUser());
        userR.setName(user.getName());
        userR.setQq(user.getQq());
        int num = userMapper.updateById(userR);
        if(num == 0){
            return Result.error("修改资料失败");
        }
        redisUtil.set("user:" + soft.getId() + ":" + user.getUser(),userR);
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("user",userR.getUser());
        jsonObject.put("name",userR.getName());
        jsonObject.put("qq",userR.getQq());
        jsonObject.put("point",userR.getPoint());
        jsonObject.put("ckey",userR.getCkey());
        jsonObject.put("regTime",userR.getRegTime());
        jsonObject.put("remark",userR.getRemark());
        jsonObject.put("authTime",userR.getAuthTime());
        return Result.ok("修改资料成功",jsonObject);
    }
}
