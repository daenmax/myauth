package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Card;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.entity.User;
import cn.myauthx.api.main.enums.CardEnums;
import cn.myauthx.api.main.enums.SoftEnums;
import cn.myauthx.api.main.mapper.CardMapper;
import cn.myauthx.api.main.mapper.UserMapper;
import cn.myauthx.api.main.service.IUserService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.MyUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserMapper userMapper;
    @Autowired
    private CardMapper cardMapper;
    @Override
    public Result register(User userC, Soft softC) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUser,userC.getUser());
        User userA = userMapper.selectOne(userLambdaQueryWrapper);
        if(!CheckUtils.isObjectEmpty(userA)){
            return Result.error("账号已存在");
        }
        if(softC.getType().equals(SoftEnums.TYPE_FREE.getCode())){
            //免费模式
            User user = new User();
            user.setUser(userC.getUser());
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
}
