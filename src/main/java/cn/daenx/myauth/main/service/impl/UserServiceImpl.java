package cn.daenx.myauth.main.service.impl;

import cn.daenx.myauth.main.entity.*;
import cn.daenx.myauth.main.mapper.*;
import cn.daenx.myauth.util.CheckUtils;
import cn.daenx.myauth.util.MyUtils;
import cn.daenx.myauth.util.RedisUtil;
import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.*;
import cn.daenx.myauth.main.enums.CardEnums;
import cn.daenx.myauth.main.enums.MsgEnums;
import cn.daenx.myauth.main.enums.SoftEnums;
import cn.daenx.myauth.main.mapper.*;
import cn.daenx.myauth.main.service.IUserService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private UserMapper userMapper;
    @Resource
    private BanMapper banMapper;
    @Resource
    private CardMapper cardMapper;
    @Resource
    private SoftMapper softMapper;
    @Resource
    private MsgMapper msgMapper;
    @Resource
    private PlogMapper plogMapper;
    @Resource
    private VersionMapper versionMapper;
    @Resource
    private AdminMapper adminMapper;
    @Resource
    private RoleMapper roleMapper;
    @Value("${genKey}")
    private String genKey;

    /**
     * 注册
     *
     * @param userC
     * @param softC
     * @return
     */
    @Override
    public Result register(User userC, Soft softC) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUser, userC.getUser());
        userLambdaQueryWrapper.eq(User::getFromSoftId, softC.getId());
        User userA = userMapper.selectOne(userLambdaQueryWrapper);
        if (!CheckUtils.isObjectEmpty(userA)) {
            return Result.error("账号已存在");
        }
        if (softC.getType().equals(SoftEnums.TYPE_FREE.getCode())) {
            //免费模式
            User user = new User();
            user.setUser(userC.getUser());
            user.setPass(userC.getPass());
            user.setName(userC.getName());
            user.setCkey(userC.getCkey());
            user.setQq(userC.getQq());
            user.setLastIp(userC.getLastIp());
            user.setAuthTime(Integer.valueOf(MyUtils.getTimeStamp()));
            user.setRegTime(Integer.valueOf(MyUtils.getTimeStamp()));
            user.setFromSoftId(softC.getId());
            user.setFromSoftKey(softC.getSkey());
            user.setRemark(userC.getRemark());
            user.setDeviceInfo(userC.getDeviceInfo());
            user.setDeviceCode(userC.getDeviceCode());
            user.setFromAdminId(1);
            int num = userMapper.insert(user);
            if (num > 0) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("user", user.getUser());
                jsonObject.put("authTime", -1);
                jsonObject.put("point", 0);
                return Result.ok("注册成功", jsonObject);
            } else {
                return Result.error("注册失败");
            }
        } else {
            //收费模式
            if (CheckUtils.isObjectEmpty(userC.getCkey())) {
                //账号+密码
                if (CheckUtils.isObjectEmpty(userC.getPass())) {
                    return Result.error("卡密为空时，密码不能为空");
                }
                User user = new User();
                user.setUser(userC.getUser());
                user.setPass(userC.getPass());
                user.setName(userC.getName());
                user.setCkey(userC.getCkey());
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
                user.setFromAdminId(1);
                int num = userMapper.insert(user);
                if (num > 0) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("user", user.getUser());
                    jsonObject.put("authTime", user.getAuthTime());
                    jsonObject.put("point", 0);
                    return Result.ok("注册成功", jsonObject);
                } else {
                    return Result.error("注册失败");
                }
            } else {
                //账号+卡密
                LambdaQueryWrapper<Card> cardLambdaQueryWrapper = new LambdaQueryWrapper<>();
                cardLambdaQueryWrapper.eq(Card::getCkey, userC.getCkey());
                Card card = cardMapper.selectOne(cardLambdaQueryWrapper);
                if (CheckUtils.isObjectEmpty(card)) {
                    return Result.error("卡密错误或者不存在");
                }
                if (card.getStatus().equals(CardEnums.STATUS_USED.getCode())) {
                    return Result.error("卡密已被使用");
                }
                if (card.getStatus().equals(CardEnums.STATUS_DISABLE.getCode())) {
                    return Result.error("卡密已被禁用");
                }
                if (!card.getFromSoftId().equals(softC.getId())) {
                    return Result.error("此卡密不属于当前软件");
                }
                User user = new User();
                user.setUser(userC.getUser());
                user.setPass(userC.getPass());
                user.setName(userC.getName());
                user.setCkey(userC.getCkey());
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
                user.setFromAdminId(card.getFromAdminId());
                int num = userMapper.insert(user);
                if (num > 0) {
                    card.setLetUser(user.getUser());
                    card.setLetTime(Integer.valueOf(MyUtils.getTimeStamp()));
                    card.setStatus(CardEnums.STATUS_USED.getCode());
                    cardMapper.updateById(card);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("user", user.getUser());
                    jsonObject.put("authTime", user.getAuthTime());
                    jsonObject.put("point", user.getPoint());
                    return Result.ok("注册成功", jsonObject);
                } else {
                    return Result.error("注册失败");
                }
            }
        }
    }

    /**
     * 登录
     *
     * @param userC
     * @param softC
     * @return
     */
    @Override
    public Result login(User userC, Soft softC) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUser, userC.getUser());
        userLambdaQueryWrapper.eq(User::getFromSoftId, softC.getId());
        User userA = userMapper.selectOne(userLambdaQueryWrapper);
        if (CheckUtils.isObjectEmpty(userA)) {
            return Result.error("账号不存在");
        }
        LambdaQueryWrapper<Ban> banLambdaQueryWrapper = new LambdaQueryWrapper<>();
        banLambdaQueryWrapper.eq(Ban::getValue, userA.getUser());
        banLambdaQueryWrapper.eq(Ban::getType, 3);
        banLambdaQueryWrapper.eq(Ban::getFromSoftId, softC.getId());
        Ban ban = banMapper.selectOne(banLambdaQueryWrapper);
        if (!CheckUtils.isObjectEmpty(ban)) {
            if (ban.getToTime() == -1) {
                String msg = "msg=被封禁" + "&type=user" + "&value=" + userA.getUser() + "&toTime=-1&time=" + ban.getAddTime()
                        + "&why=" + ban.getWhy();
                return Result.error(300, msg);
            } else {
                Integer seconds = ban.getToTime() - Integer.parseInt(MyUtils.getTimeStamp());
                if (seconds > 0) {
                    String msg = "msg=被封禁" + "&type=user" + "&value=" + userA.getUser() + "&toTime=" + ban.getToTime() + "&time=" + ban.getAddTime()
                            + "&why=" + ban.getWhy();
                    return Result.error(300, msg);
                }
            }
        }

        userA.setFromVerId(userC.getFromVerId());
        userA.setFromVerKey(userC.getFromVerKey());
        userA.setLastIp(userC.getLastIp());
        userA.setLastTime(Integer.valueOf(MyUtils.getTimeStamp()));
        JSONObject jsonObject = new JSONObject(true);
        if (softC.getType().equals(SoftEnums.TYPE_FREE.getCode())) {
            //免费模式
            if (CheckUtils.isObjectEmpty(userA.getPass())) {
                //密码为空
                if (softC.getBindDeviceCode().equals(SoftEnums.BIND_ABLE.getCode())) {
                    //绑定机器码
                    if (!CheckUtils.isObjectEmpty(userA.getDeviceCode())) {
                        //已经有历史机器码记录
                        if (!userA.getDeviceCode().equals(userC.getDeviceCode())) {
                            return Result.error("此账号已绑定其他机器码，请先解绑");
                        }
                    } else {
                        //没有历史机器码记录
                        userA.setDeviceCode(userC.getDeviceCode());
                        userA.setDeviceInfo(userC.getDeviceInfo());
                    }
                } else {
                    userA.setDeviceCode(userC.getDeviceCode());
                    userA.setDeviceInfo(userC.getDeviceInfo());
                }
                String token = MyUtils.encUserToken(userA.getUser(), String.valueOf(userA.getLastTime()), String.valueOf(softC.getId()), genKey);
                userA.setToken(token);
                int num = userMapper.updateById(userA);
                if (num > 0) {
                    redisUtil.set("user:" + userA.getFromSoftId() + ":" + userA.getUser(), userA, softC.getHeartTime());
                    jsonObject.put("user", userA.getUser());
                    jsonObject.put("name", userA.getName());
                    jsonObject.put("qq", userA.getQq());
                    jsonObject.put("point", userA.getPoint());
                    jsonObject.put("ckey", userA.getCkey());
                    jsonObject.put("regTime", userA.getRegTime());
                    jsonObject.put("remark", userA.getRemark());
                    jsonObject.put("authTime", userA.getAuthTime());
                    jsonObject.put("token", userA.getToken());
                    jsonObject.put("fromAdminId", userA.getFromAdminId());
                    return Result.ok("登录成功", jsonObject);
                }
            } else {
                //密码不为空
                if (CheckUtils.isObjectEmpty(userC.getPass())) {
                    return Result.error("密码不能为空");
                }
                if (!userA.getPass().equals(userC.getPass())) {
                    return Result.error("密码错误");
                }
                if (softC.getBindDeviceCode().equals(SoftEnums.BIND_ABLE.getCode())) {
                    //绑定机器码
                    if (!CheckUtils.isObjectEmpty(userA.getDeviceCode())) {
                        //已经有历史机器码记录
                        if (!userA.getDeviceCode().equals(userC.getDeviceCode())) {
                            return Result.error("此账号已绑定其他机器码，请先解绑");
                        }
                    } else {
                        //没有历史机器码记录
                        userA.setDeviceCode(userC.getDeviceCode());
                        userA.setDeviceInfo(userC.getDeviceInfo());
                    }
                } else {
                    userA.setDeviceCode(userC.getDeviceCode());
                    userA.setDeviceInfo(userC.getDeviceInfo());
                }
                String token = MyUtils.encUserToken(userA.getUser(), String.valueOf(userA.getLastTime()), String.valueOf(softC.getId()), genKey);
                userA.setToken(token);
                int num = userMapper.updateById(userA);
                if (num > 0) {
                    redisUtil.set("user:" + userA.getFromSoftId() + ":" + userA.getUser(), userA, softC.getHeartTime());
                    jsonObject.put("user", userA.getUser());
                    jsonObject.put("name", userA.getName());
                    jsonObject.put("qq", userA.getQq());
                    jsonObject.put("point", userA.getPoint());
                    jsonObject.put("ckey", userA.getCkey());
                    jsonObject.put("regTime", userA.getRegTime());
                    jsonObject.put("remark", userA.getRemark());
                    jsonObject.put("authTime", userA.getAuthTime());
                    jsonObject.put("token", userA.getToken());
                    jsonObject.put("fromAdminId", userA.getFromAdminId());
                    return Result.ok("登录成功", jsonObject);
                }
            }

        } else {
            //收费模式
            if (CheckUtils.isObjectEmpty(userA.getPass())) {
                //密码为空
                if (Integer.parseInt(MyUtils.getTimeStamp()) > userA.getAuthTime()) {
                    //授权未到期
                    if (softC.getBindDeviceCode().equals(SoftEnums.BIND_ABLE.getCode())) {
                        //绑定机器码
                        if (!CheckUtils.isObjectEmpty(userA.getDeviceCode())) {
                            //已经有历史机器码记录
                            if (!userA.getDeviceCode().equals(userC.getDeviceCode())) {
                                return Result.error("此账号已绑定其他机器码，请先解绑");
                            }
                        } else {
                            //没有历史机器码记录
                            userA.setDeviceCode(userC.getDeviceCode());
                            userA.setDeviceInfo(userC.getDeviceInfo());
                        }
                    } else {
                        userA.setDeviceCode(userC.getDeviceCode());
                        userA.setDeviceInfo(userC.getDeviceInfo());
                    }
                    String token = MyUtils.encUserToken(userA.getUser(), String.valueOf(userA.getLastTime()), String.valueOf(softC.getId()), genKey);
                    userA.setToken(token);
                    int num = userMapper.updateById(userA);
                    if (num > 0) {
                        redisUtil.set("user:" + userA.getFromSoftId() + ":" + userA.getUser(), userA, softC.getHeartTime());
                        jsonObject.put("user", userA.getUser());
                        jsonObject.put("name", userA.getName());
                        jsonObject.put("qq", userA.getQq());
                        jsonObject.put("point", userA.getPoint());
                        jsonObject.put("ckey", userA.getCkey());
                        jsonObject.put("regTime", userA.getRegTime());
                        jsonObject.put("remark", userA.getRemark());
                        jsonObject.put("authTime", userA.getAuthTime());
                        jsonObject.put("token", userA.getToken());
                        jsonObject.put("fromAdminId", userA.getFromAdminId());
                        return Result.ok("登录成功", jsonObject);
                    }
                } else {
                    //授权已到期
                    return Result.error("授权已到期");
                }
            } else {
                //密码不为空
                if (CheckUtils.isObjectEmpty(userC.getPass())) {
                    return Result.error("密码不能为空");
                }
                if (!userA.getPass().equals(userC.getPass())) {
                    return Result.error("密码错误");
                }
                if (Integer.parseInt(MyUtils.getTimeStamp()) < userA.getAuthTime()) {
                    //授权未到期
                    if (softC.getBindDeviceCode().equals(SoftEnums.BIND_ABLE.getCode())) {
                        //绑定机器码
                        if (!CheckUtils.isObjectEmpty(userA.getDeviceCode())) {
                            //已经有历史机器码记录
                            if (!userA.getDeviceCode().equals(userC.getDeviceCode())) {
                                return Result.error("此账号已绑定其他机器码，请先解绑");
                            }
                        } else {
                            //没有历史机器码记录
                            userA.setDeviceCode(userC.getDeviceCode());
                            userA.setDeviceInfo(userC.getDeviceInfo());
                        }
                    } else {
                        userA.setDeviceCode(userC.getDeviceCode());
                        userA.setDeviceInfo(userC.getDeviceInfo());
                    }
                    String token = MyUtils.encUserToken(userA.getUser(), String.valueOf(userA.getLastTime()), String.valueOf(softC.getId()), genKey);
                    userA.setToken(token);
                    int num = userMapper.updateById(userA);
                    if (num > 0) {
                        redisUtil.set("user:" + userA.getFromSoftId() + ":" + userA.getUser(), userA, softC.getHeartTime());
                        jsonObject.put("user", userA.getUser());
                        jsonObject.put("name", userA.getName());
                        jsonObject.put("qq", userA.getQq());
                        jsonObject.put("point", userA.getPoint());
                        jsonObject.put("ckey", userA.getCkey());
                        jsonObject.put("regTime", userA.getRegTime());
                        jsonObject.put("remark", userA.getRemark());
                        jsonObject.put("authTime", userA.getAuthTime());
                        jsonObject.put("token", userA.getToken());
                        jsonObject.put("fromAdminId", userA.getFromAdminId());
                        return Result.ok("登录成功", jsonObject);
                    }
                } else {
                    //授权已到期
                    return Result.error("授权已到期");
                }
            }
        }
        return null;
    }

    /**
     * 心跳
     *
     * @param userA
     * @param softC
     * @return
     */
    @Override
    public Result heart(User userA, Soft softC) {
        if (softC.getType().equals(SoftEnums.TYPE_FREE.getCode())) {
            //免费模式
        }else{
            //收费模式
            if (userA.getAuthTime().equals(-1)) {
                //已是永久授权
            } else {
                //不是永久授权
                if (userA.getAuthTime() < Integer.parseInt(MyUtils.getTimeStamp())) {
                    //已经到期
                    return Result.error("授权已到期");
                } else {
                    //未到期
                }
            }
        }
        redisUtil.set("user:" + userA.getFromSoftId() + ":" + userA.getUser(), userA, softC.getHeartTime());
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("user", userA.getUser());
        jsonObject.put("name", userA.getName());
        jsonObject.put("qq", userA.getQq());
        jsonObject.put("point", userA.getPoint());
        jsonObject.put("ckey", userA.getCkey());
        jsonObject.put("regTime", userA.getRegTime());
        jsonObject.put("remark", userA.getRemark());
        jsonObject.put("authTime", userA.getAuthTime());
        return Result.ok("心跳成功", jsonObject);
    }


    /**
     * 使用卡密
     *
     * @param userC
     * @param softC
     * @return
     */
    @Override
    public Result useCkey(User userC, Soft softC) {
        Plog plog = new Plog();
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUser, userC.getUser());
        userLambdaQueryWrapper.eq(User::getFromSoftId, softC.getId());
        User userA = userMapper.selectOne(userLambdaQueryWrapper);
        if (CheckUtils.isObjectEmpty(userA)) {
            return Result.error("账号不存在");
        }
        LambdaQueryWrapper<Ban> banLambdaQueryWrapper = new LambdaQueryWrapper<>();
        banLambdaQueryWrapper.eq(Ban::getValue, userA.getUser());
        banLambdaQueryWrapper.eq(Ban::getType, 3);
        banLambdaQueryWrapper.eq(Ban::getFromSoftId, softC.getId());
        Ban ban = banMapper.selectOne(banLambdaQueryWrapper);
        if (!CheckUtils.isObjectEmpty(ban)) {
            if (ban.getToTime() == -1) {
                String msg = "msg=被封禁" + "&type=user" + "&value=" + userA.getUser() + "&toTime=-1&time=" + ban.getAddTime()
                        + "&why=" + ban.getWhy();
                return Result.error(300, msg);
            } else {
                Integer seconds = ban.getToTime() - Integer.parseInt(MyUtils.getTimeStamp());
                if (seconds > 0) {
                    String msg = "msg=被封禁" + "&type=user" + "&value=" + userA.getUser() + "&toTime=" + ban.getToTime() + "&time=" + ban.getAddTime()
                            + "&why=" + ban.getWhy();
                    return Result.error(300, msg);
                }
            }
        }
        LambdaQueryWrapper<Card> cardLambdaQueryWrapper = new LambdaQueryWrapper<>();
        cardLambdaQueryWrapper.eq(Card::getCkey, userC.getCkey());
        Card card = cardMapper.selectOne(cardLambdaQueryWrapper);
        if (CheckUtils.isObjectEmpty(card)) {
            return Result.error("卡密错误或者不存在");
        }
        if (card.getStatus().equals(CardEnums.STATUS_USED.getCode())) {
            return Result.error("卡密已被使用");
        }
        if (card.getStatus().equals(CardEnums.STATUS_DISABLE.getCode())) {
            return Result.error("卡密已被禁用");
        }
        if (!card.getFromSoftId().equals(softC.getId())) {
            return Result.error("此卡密不属于当前软件");
        }
        if (CheckUtils.isObjectEmpty(userA.getAuthTime())) {
            userA.setAuthTime(0);
        }
        if (CheckUtils.isObjectEmpty(userA.getPoint())) {
            userA.setPoint(0);
        }
        //如果卡密包含授权时间
        if (!card.getSeconds().equals(0)) {
            plog.setSeconds(card.getSeconds());
            if (userA.getAuthTime().equals(-1)) {
                //已是永久授权
            } else {
                //不是永久授权
                if (userA.getAuthTime() < Integer.parseInt(MyUtils.getTimeStamp())) {
                    //已经到期
                    userA.setAuthTime(Integer.parseInt(MyUtils.getTimeStamp()) + card.getSeconds());
                } else {
                    //未到期，则续费
                    userA.setAuthTime(Integer.valueOf(userA.getAuthTime()) + card.getSeconds());
                }

            }
        }
        plog.setAfterSeconds(userA.getAuthTime());
        //如果卡密包含点数
        if (!card.getSeconds().equals(0)) {
            plog.setPoint(card.getPoint());
            userA.setPoint(Integer.valueOf(userA.getPoint()) + card.getPoint());
        }
        plog.setAfterPoint(userA.getPoint());
        plog.setFromUser(userA.getUser());
        plog.setAddTime(Integer.parseInt(MyUtils.getTimeStamp()));
        plog.setFromSoftId(card.getFromSoftId());
        plog.setRemark(card.getCkey());
        User userR = (User) redisUtil.get("user:" + softC.getId() + ":" + userC.getUser());
        if (!CheckUtils.isObjectEmpty(userR)) {
            userA.setLastTime(userR.getLastTime());
        }
        userA.setFromAdminId(card.getFromAdminId());
        userA.setCkey(card.getCkey());
        int num = userMapper.updateById(userA);
        if (num > 0) {
            if (!CheckUtils.isObjectEmpty(userR)) {
                redisUtil.set("user:" + userA.getFromSoftId() + ":" + userA.getUser(), userA, softC.getHeartTime());
            }
            plogMapper.insert(plog);
            card.setLetUser(userA.getUser());
            card.setLetTime(Integer.valueOf(MyUtils.getTimeStamp()));
            card.setStatus(CardEnums.STATUS_USED.getCode());
            cardMapper.updateById(card);
            JSONObject jsonObject = new JSONObject(true);
            jsonObject.put("user", userA.getUser());
            jsonObject.put("name", userA.getName());
            jsonObject.put("qq", userA.getQq());
            jsonObject.put("point", userA.getPoint());
            jsonObject.put("ckey", userA.getCkey());
            jsonObject.put("regTime", userA.getRegTime());
            jsonObject.put("remark", userA.getRemark());
            jsonObject.put("authTime", userA.getAuthTime());
            return Result.ok("使用卡密成功", jsonObject);
        } else {
            return Result.error("使用卡密失败");
        }
    }

    /**
     * 获取回复
     *
     * @param soft
     * @param version
     * @param keyword
     * @return
     */
    @Override
    public Result getMsg(Soft soft, Version version, String keyword, String ver) {
        Version version1 = new Version();
        if (!CheckUtils.isObjectEmpty(ver)) {
            LambdaQueryWrapper<Version> versionLambdaQueryWrapper = new LambdaQueryWrapper<>();
            versionLambdaQueryWrapper.eq(Version::getFromSoftId, soft.getId());
            versionLambdaQueryWrapper.eq(Version::getVer, ver);
            version1 = versionMapper.selectOne(versionLambdaQueryWrapper);
            if (CheckUtils.isObjectEmpty(version1)) {
                return Result.error("ver错误");
            } else {

            }
        }

        LambdaQueryWrapper<Msg> msgLambdaQueryWrapper = new LambdaQueryWrapper<>();
        msgLambdaQueryWrapper.eq(Msg::getFromSoftId, soft.getId());
        msgLambdaQueryWrapper.eq(Msg::getKeyword, keyword);
        if (CheckUtils.isObjectEmpty(ver)) {
            msgLambdaQueryWrapper.eq(Msg::getFromVerId, version.getId());
        } else {
            msgLambdaQueryWrapper.eq(Msg::getFromVerId, version1.getId());
        }


        Msg msg = msgMapper.selectOne(msgLambdaQueryWrapper);
        JSONObject jsonObject = new JSONObject(true);
        if (CheckUtils.isObjectEmpty(msg)) {
            LambdaQueryWrapper<Msg> msgLambdaQueryWrapper2 = new LambdaQueryWrapper<>();
            msgLambdaQueryWrapper2.eq(Msg::getFromSoftId, soft.getId());
            msgLambdaQueryWrapper2.eq(Msg::getKeyword, keyword);
            List<Msg> msgList = msgMapper.selectList(msgLambdaQueryWrapper2);

            if (msgList.size() == 0) {
                return Result.error("回复不存在");
            } else {
                msg = msgList.get(0);
                if (CheckUtils.isObjectEmpty(msg.getFromVerId())) {
                    if (msg.getStatus().equals(MsgEnums.STATUS_DISABLE.getCode())) {
                        return Result.error("回复已被禁用");
                    }
                    jsonObject.put("keyword", msg.getKeyword());
                    jsonObject.put("msg", MyUtils.base64Encode(msg.getMsg()));
                    return Result.ok("获取回复成功", jsonObject);
                } else {
                    if (msg.getFromVerId().equals(version.getId())) {
                        if (msg.getStatus().equals(MsgEnums.STATUS_DISABLE.getCode())) {
                            return Result.error("回复已被禁用");
                        }
                        jsonObject.put("keyword", msg.getKeyword());
                        jsonObject.put("msg", MyUtils.base64Encode(msg.getMsg()));
                        return Result.ok("获取回复成功", jsonObject);
                    } else {
                        return Result.error("该回复在当前版本不能使用");
                    }
                }

            }
        } else {
            if (msg.getStatus().equals(MsgEnums.STATUS_DISABLE.getCode())) {
                return Result.error("回复已被禁用");
            }
            jsonObject.put("keyword", msg.getKeyword());
            jsonObject.put("msg", MyUtils.base64Encode(msg.getMsg()));
            return Result.ok("获取回复成功", jsonObject);
        }
    }

    /**
     * 解绑
     *
     * @param userA
     * @param softC
     * @return
     */
    @Override
    public Result unbind(User userA, Soft softC) {
        int num = userMapper.updateById(userA);
        if (num == 0) {
            return Result.error("解绑失败");
        }
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("user", userA.getUser());
        redisUtil.del("user:" + softC.getId() + ":" + userA.getUser());
        return Result.ok("解绑成功", jsonObject);
    }

    /**
     * 修改密码
     *
     * @param userS
     * @param nowPass
     * @param newPass
     * @param softC
     * @return
     */
    @Override
    public Result editPass(String userS, String nowPass, String newPass, Soft softC) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUser, userS);
        userLambdaQueryWrapper.eq(User::getFromSoftId, softC.getId());
        User userA = userMapper.selectOne(userLambdaQueryWrapper);
        if (CheckUtils.isObjectEmpty(userA)) {
            return Result.error("账号不存在");
        }
        LambdaQueryWrapper<Ban> banLambdaQueryWrapper = new LambdaQueryWrapper<>();
        banLambdaQueryWrapper.eq(Ban::getValue, userA.getUser());
        banLambdaQueryWrapper.eq(Ban::getType, 3);
        banLambdaQueryWrapper.eq(Ban::getFromSoftId, softC.getId());
        Ban ban = banMapper.selectOne(banLambdaQueryWrapper);
        if (!CheckUtils.isObjectEmpty(ban)) {
            if (ban.getToTime() == -1) {
                String msg = "msg=被封禁" + "&type=user" + "&value=" + userA.getUser() + "&toTime=-1&time=" + ban.getAddTime()
                        + "&why=" + ban.getWhy();
                return Result.error(300, msg);
            } else {
                Integer seconds = ban.getToTime() - Integer.parseInt(MyUtils.getTimeStamp());
                if (seconds > 0) {
                    String msg = "msg=被封禁" + "&type=user" + "&value=" + userA.getUser() + "&toTime=" + ban.getToTime() + "&time=" + ban.getAddTime()
                            + "&why=" + ban.getWhy();
                    return Result.error(300, msg);
                }
            }
        }
        if (CheckUtils.isObjectEmpty(userA.getPass())) {
            return Result.error("账号不允许修改密码");
        }
        if (!userA.getPass().equals(nowPass)) {
            return Result.error("旧密码错误");
        }
        userA.setPass(newPass);
        int num = userMapper.updateById(userA);
        if (num == 0) {
            return Result.error("修改密码失败");
        }
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("user", userA.getUser());
        redisUtil.del("user:" + softC.getId() + ":" + userA.getUser());
        return Result.ok("修改密码成功，请重新登录", jsonObject);
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
        if (num == 0) {
            return Result.error("修改资料失败");
        }
        redisUtil.set("user:" + soft.getId() + ":" + user.getUser(), userR, soft.getHeartTime());
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("user", userR.getUser());
        jsonObject.put("name", userR.getName());
        jsonObject.put("qq", userR.getQq());
        jsonObject.put("point", userR.getPoint());
        jsonObject.put("ckey", userR.getCkey());
        jsonObject.put("regTime", userR.getRegTime());
        jsonObject.put("remark", userR.getRemark());
        jsonObject.put("authTime", userR.getAuthTime());
        return Result.ok("修改资料成功", jsonObject);
    }

    /**
     * 获取用户列表
     *
     * @param user
     * @param myPage
     * @return
     */
    @Override
    public Result getUserList(User user, MyPage myPage) {
        Page<User> page = new Page<>(myPage.getPageIndex(), myPage.getPageSize(), true);
        if (!CheckUtils.isObjectEmpty(myPage.getOrders())) {
            for (int i = 0; i < myPage.getOrders().size(); i++) {
                myPage.getOrders().get(i).setColumn(MyUtils.camelToUnderline(myPage.getOrders().get(i).getColumn()));
            }
            page.setOrders(myPage.getOrders());
        }
        IPage<User> msgPage = userMapper.selectPage(page, getQwUser(user));
        for (int i = 0; i < msgPage.getRecords().size(); i++) {
            if (!CheckUtils.isObjectEmpty(msgPage.getRecords().get(i).getFromSoftId())) {
                Soft obj = (Soft) redisUtil.get("id:soft:" + msgPage.getRecords().get(i).getFromSoftId());
                msgPage.getRecords().get(i).setFromSoftName(obj.getName());
            }
            if (!CheckUtils.isObjectEmpty(msgPage.getRecords().get(i).getFromVerId())) {
                Version obj2 = (Version) redisUtil.get("id:version:" + msgPage.getRecords().get(i).getFromVerId());
                msgPage.getRecords().get(i).setFromVerName(obj2.getVer());
            }
        }
        return Result.ok("获取成功", msgPage);
    }

    /**
     * 获取查询条件构造器
     *
     * @param user
     * @return
     */
    public LambdaQueryWrapper<User> getQwUser(User user) {
        LambdaQueryWrapper<User> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(user.getUser()), User::getUser, user.getUser());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(user.getName()), User::getName, user.getName());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(user.getPoint()), User::getPoint, user.getPoint());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(user.getQq()), User::getQq, user.getQq());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(user.getLastIp()), User::getLastIp, user.getLastIp());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(user.getLastTime()), User::getLastTime, user.getLastTime());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(user.getAuthTime()), User::getAuthTime, user.getAuthTime());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(user.getFromSoftId()), User::getFromSoftId, user.getFromSoftId());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(user.getFromVerId()), User::getFromVerId, user.getFromVerId());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(user.getRemark()), User::getRemark, user.getRemark());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(user.getDeviceInfo()), User::getDeviceInfo, user.getDeviceInfo());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(user.getDeviceCode()), User::getDeviceCode, user.getDeviceCode());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(user.getCkey()), User::getCkey, user.getCkey());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(user.getFromAdminId()), User::getFromAdminId, user.getFromAdminId());
        return LambdaQueryWrapper;
    }

    /**
     * 查询用户
     *
     * @param user
     * @return
     */
    @Override
    public Result getUser(User user) {
        User newUser = userMapper.selectById(user.getId());
        if (CheckUtils.isObjectEmpty(newUser)) {
            return Result.error("查询失败，未找到");
        }
        return Result.ok("查询成功", newUser);
    }

    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    @Override
    public Result updUser(User user) {
        User oldUser = userMapper.selectById(user.getId());
        if (CheckUtils.isObjectEmpty(oldUser)) {
            return Result.error("用户ID错误");
        }
        int num = userMapper.updateById(user);
        if (num <= 0) {
            return Result.error("修改失败");
        }
        redisUtil.del("user:" + oldUser.getFromSoftId() + ":" + oldUser.getUser());
        return Result.ok("修改成功");
    }

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    @Override
    public Result addUser(User user, Admin admin) {
        Soft soft = softMapper.selectById(user.getFromSoftId());
        if (CheckUtils.isObjectEmpty(soft)) {
            return Result.error("fromSoftId错误");
        }
        user.setFromSoftKey(soft.getSkey());
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUser, user.getUser());
        userLambdaQueryWrapper.eq(User::getFromSoftId, user.getFromSoftId());
        User user1 = userMapper.selectOne(userLambdaQueryWrapper);
        if (!CheckUtils.isObjectEmpty(user1)) {
            return Result.error("账号在当前软件中已存在");
        }
        user.setFromAdminId(admin.getId());
        user.setFromAdminId(1);
        user.setRegTime(Integer.valueOf(MyUtils.getTimeStamp()));
        int num = userMapper.insert(user);
        if (num <= 0) {
            return Result.error("添加失败");
        }
        return Result.ok("添加成功");
    }

    /**
     * 删除用户，支持批量
     *
     * @param ids 多个用英文逗号隔开
     * @return
     */
    @Override
    public Result delUser(String ids) {
        String[] idArray = ids.split(",");
        List<String> strings = Arrays.asList(idArray);
        if (idArray.length == 0) {
            return Result.error("ids参数格式可能错误");
        }
        int okCount = userMapper.deleteBatchIds(strings);
        return Result.ok("成功删除 " + okCount + " 个用户");
    }

    /**
     * 获取我的授权
     *
     * @param user
     * @param myPage
     * @return
     */
    @Override
    public Result getMyUserList(User user, MyPage myPage, Admin admin) {
        Role role = (Role) redisUtil.get("role:" + admin.getRole());
        if (role.getFromSoftId() == 0) {
            return Result.error("超级管理员无法使用此接口");
        }
        user.setFromAdminId(admin.getId());
        user.setFromSoftId(role.getFromSoftId());
        Page<User> page = new Page<>(myPage.getPageIndex(), myPage.getPageSize(), true);
        if (!CheckUtils.isObjectEmpty(myPage.getOrders())) {
            for (int i = 0; i < myPage.getOrders().size(); i++) {
                myPage.getOrders().get(i).setColumn(MyUtils.camelToUnderline(myPage.getOrders().get(i).getColumn()));
            }
            page.setOrders(myPage.getOrders());
        }
        IPage<User> msgPage = userMapper.selectPage(page, getQwUserMy(user));
        for (int i = 0; i < msgPage.getRecords().size(); i++) {
            if (!CheckUtils.isObjectEmpty(msgPage.getRecords().get(i).getFromSoftId())) {
                Soft obj = (Soft) redisUtil.get("id:soft:" + msgPage.getRecords().get(i).getFromSoftId());
                msgPage.getRecords().get(i).setFromSoftName(obj.getName());
            }
            if (!CheckUtils.isObjectEmpty(msgPage.getRecords().get(i).getFromVerId())) {
                Version obj2 = (Version) redisUtil.get("id:version:" + msgPage.getRecords().get(i).getFromVerId());
                msgPage.getRecords().get(i).setFromVerName(obj2.getVer());
            }
            msgPage.getRecords().get(i).setFromVerKey(null);
            msgPage.getRecords().get(i).setFromSoftKey(null);
            msgPage.getRecords().get(i).setFromVerId(null);
            msgPage.getRecords().get(i).setFromSoftId(null);
            msgPage.getRecords().get(i).setToken(null);
        }
        return Result.ok("获取成功", msgPage);
    }

    /**
     * 获取查询条件构造器_我的
     *
     * @param user
     * @return
     */
    public LambdaQueryWrapper<User> getQwUserMy(User user) {
        LambdaQueryWrapper<User> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(user.getUser()), User::getUser, user.getUser());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(user.getName()), User::getName, user.getName());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(user.getPoint()), User::getPoint, user.getPoint());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(user.getQq()), User::getQq, user.getQq());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(user.getLastIp()), User::getLastIp, user.getLastIp());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(user.getLastTime()), User::getLastTime, user.getLastTime());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(user.getAuthTime()), User::getAuthTime, user.getAuthTime());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(user.getRemark()), User::getRemark, user.getRemark());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(user.getDeviceInfo()), User::getDeviceInfo, user.getDeviceInfo());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(user.getDeviceCode()), User::getDeviceCode, user.getDeviceCode());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(user.getCkey()), User::getCkey, user.getCkey());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(user.getFromAdminId()), User::getFromAdminId, user.getFromAdminId());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(user.getFromSoftId()), User::getFromSoftId, user.getFromSoftId());
        return LambdaQueryWrapper;
    }

    /**
     * 自助修改账号
     *
     * @param user
     * @param newUser
     * @param pass
     * @param softId
     * @param ckey
     * @return
     */
    @Override
    public Result selfChangeUser(String user, String newUser, String pass, Integer softId, String ckey) {
        if (user.equals(newUser)) {
            return Result.error("原账号和新账号不能一样");
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUser, user);
        userLambdaQueryWrapper.eq(User::getFromSoftId, softId);
        User selectOne = userMapper.selectOne(userLambdaQueryWrapper);
        if (CheckUtils.isObjectEmpty(selectOne)) {
            return Result.error("账号不存在");
        }
        if (CheckUtils.isObjectEmpty(selectOne.getPass()) && CheckUtils.isObjectEmpty(selectOne.getCkey())) {
            //密码和ckey都为空，说明免费软件，不允许修改账号
            return Result.error("当前账号类型无法修改账号");
        }
        Ban ban = (Ban) redisUtil.get("ban:" + selectOne.getUser() + "-" + 3 + "-" + softId);
        if (!CheckUtils.isObjectEmpty(ban)) {
            if (ban.getToTime() == -1) {
                return Result.error("账号已被永久封禁，不能修改");
            } else {
                Integer seconds = ban.getToTime() - Integer.parseInt(MyUtils.getTimeStamp());
                if (seconds > 0) {
                    return Result.error("账号已被封禁，不能修改，剩余解封时间：" + seconds + "秒");
                }
            }
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper1.eq(User::getUser, newUser);
        userLambdaQueryWrapper1.eq(User::getFromSoftId, softId);
        User newUser1 = userMapper.selectOne(userLambdaQueryWrapper1);
        if (!CheckUtils.isObjectEmpty(newUser1)) {
            return Result.error("新账号已存在");
        }
        if (CheckUtils.isObjectEmpty(selectOne.getPass())) {
            //密码为空，此时必须提供卡密
            if (CheckUtils.isObjectEmpty(ckey)) {
                return Result.error("当前修改需要填写最后一次使用的卡密");
            }
            if (CheckUtils.isObjectEmpty(selectOne.getCkey())) {
                return Result.error("账号信息存在异常，请联系管理员");
            }
            if (!selectOne.getCkey().equals(ckey)) {
                return Result.error("卡密错误");
            }
            selectOne.setUser(newUser);
            int num = userMapper.updateById(selectOne);
            if (num > 0) {
                redisUtil.del("user:" + softId + ":" + user);
                return Result.ok("修改成功");
            } else {
                return Result.error("修改失败，请联系管理员");
            }
        } else {
            //密码不为空，那么此时必须提供密码，卡密提供不提供都无所谓
            if (CheckUtils.isObjectEmpty(pass)) {
                return Result.error("当前修改需要填写当前账号密码");
            }
            if (!selectOne.getPass().equals(pass)) {
                return Result.error("当前密码错误");
            }
            selectOne.setUser(newUser);
            int num = userMapper.updateById(selectOne);
            if (num > 0) {
                redisUtil.del("user:" + softId + ":" + user);
                return Result.ok("修改成功");
            } else {
                return Result.error("修改失败，请联系管理员");
            }
        }
    }

    /**
     * 查询账号信息
     *
     * @param user
     * @param soft
     * @return
     */
    @Override
    public Result queryUserInfo(String user, Soft soft) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUser, user);
        userLambdaQueryWrapper.eq(User::getFromSoftId, soft.getId());
        User selectOne = userMapper.selectOne(userLambdaQueryWrapper);
        if (CheckUtils.isObjectEmpty(selectOne)) {
            return Result.error("账号不存在");
        }
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("user", selectOne.getUser());
        jsonObject.put("name", selectOne.getName());
        jsonObject.put("qq", selectOne.getQq());
        jsonObject.put("regTime", selectOne.getRegTime());
        jsonObject.put("authTime", selectOne.getAuthTime());
        jsonObject.put("point", selectOne.getPoint());
        jsonObject.put("remark", selectOne.getRemark());
        jsonObject.put("fromSoftName", soft.getName());
        Ban ban = (Ban) redisUtil.get("ban:" + selectOne.getUser() + "-" + 3 + "-" + soft.getId());
        if (!CheckUtils.isObjectEmpty(ban)) {
            if (ban.getToTime() == -1) {
                jsonObject.put("status", "账号已被永久封禁，封禁时间：" + MyUtils.dateToStr(MyUtils.stamp2Date(String.valueOf(ban.getAddTime()))) + "，封禁理由：" + ban.getWhy());
                return Result.ok("查询成功", jsonObject);
            } else {
                Integer seconds = ban.getToTime() - Integer.parseInt(MyUtils.getTimeStamp());
                if (seconds > 0) {
                    jsonObject.put("status", "账号已被封禁，封禁时间：" + MyUtils.dateToStr(MyUtils.stamp2Date(String.valueOf(ban.getAddTime()))) + "，剩余解封时间：" + seconds + "秒，封禁理由：" + ban.getWhy());
                    return Result.ok("查询成功", jsonObject);
                }
            }
        }
        jsonObject.put("status", "账号正常");
        return Result.ok("查询成功", jsonObject);
    }

    /**
     * 查询管理员信息
     *
     * @param user
     * @param soft
     * @return
     */
    @Override
    public Result queryAdminInfo(String user, Soft soft) {
        LambdaQueryWrapper<Admin> adminLambdaQueryWrapper = new LambdaQueryWrapper<>();
        adminLambdaQueryWrapper.eq(Admin::getUser,user);
        Admin admin = adminMapper.selectOne(adminLambdaQueryWrapper);
        if(CheckUtils.isObjectEmpty(admin)){
            return Result.error("账号不存在");
        }
        Role role = (Role) redisUtil.get("role:" + admin.getRole());
        if(!role.getFromSoftId().equals(soft.getId())){
            return Result.error("此账号不属于此软件");
        }
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("user", admin.getUser());
        jsonObject.put("qq", admin.getQq());
        jsonObject.put("regTime", admin.getRegTime());
        jsonObject.put("status", admin.getStatus());
        jsonObject.put("role", role.getName());
        jsonObject.put("fromSoftName", soft.getName());
        return Result.ok("查询成功", jsonObject);
    }
}
