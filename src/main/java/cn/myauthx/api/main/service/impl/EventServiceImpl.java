package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Event;
import cn.myauthx.api.main.entity.Plog;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.entity.User;
import cn.myauthx.api.main.enums.EventEnums;
import cn.myauthx.api.main.mapper.EventMapper;
import cn.myauthx.api.main.mapper.PlogMapper;
import cn.myauthx.api.main.mapper.UserMapper;
import cn.myauthx.api.main.service.IEventService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.MyUtils;
import cn.myauthx.api.util.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
@Service
public class EventServiceImpl extends ServiceImpl<EventMapper, Event> implements IEventService {
    @Resource
    private EventMapper eventMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private PlogMapper plogMapper;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 触发事件
     *
     * @param name
     * @param user
     * @param soft
     * @return
     */
    @Override
    public Result letEvent(String name, User user, Soft soft) {
        Plog plog = new Plog();
        LambdaQueryWrapper<Event> eventLambdaQueryWrapper = new LambdaQueryWrapper<>();
        eventLambdaQueryWrapper.eq(Event::getName, name);
        eventLambdaQueryWrapper.eq(Event::getFromSoftId, soft.getId());
        Event event = eventMapper.selectOne(eventLambdaQueryWrapper);
        if (CheckUtils.isObjectEmpty(event)) {
            return Result.error("事件name错误或者不存在");
        }
        if (event.getStatus().equals(EventEnums.STATUS_DISABLE.getCode())) {
            return Result.error("事件已被禁用");
        }
        plog.setPoint(event.getPoint());
        if (event.getPoint() != 0) {
            if (event.getPoint() > 0) {
                user.setPoint(user.getPoint() + event.getPoint());
            } else {
                Integer af = user.getPoint() + event.getPoint();
                if (af < 0) {
                    return Result.error("点数不足，至少需要" + event.getPoint() + "点");
                } else {
                    user.setPoint(af);
                }
            }
        }
        plog.setAfterPoint(user.getPoint());
        plog.setSeconds(event.getSeconds());
        if (event.getSeconds() != 0) {
            if (event.getSeconds() > 0) {
                user.setAuthTime(user.getAuthTime() + event.getSeconds());
            } else {
                Integer af = user.getAuthTime() + event.getSeconds();
                if (af < Integer.parseInt(MyUtils.getTimeStamp())) {
                    return Result.error("授权期限不足");
                } else {
                    user.setAuthTime(af);
                }
            }
        }
        plog.setAfterSeconds(user.getAuthTime());
        plog.setFromUser(user.getUser());
        plog.setAddTime(Integer.parseInt(MyUtils.getTimeStamp()));
        plog.setFromEventName(event.getName());
        plog.setFromEventId(event.getId());
        plog.setFromSoftId(event.getFromSoftId());
        int num = userMapper.updateById(user);
        if (num > 0) {
            redisUtil.set("user:" + user.getFromSoftId() + ":" + user.getUser(), user);
            plogMapper.insert(plog);
            JSONObject jsonObject = new JSONObject(true);
            jsonObject.put("user", user.getUser());
            jsonObject.put("name", user.getName());
            jsonObject.put("qq", user.getQq());
            jsonObject.put("point", user.getPoint());
            jsonObject.put("ckey", user.getCkey());
            jsonObject.put("regTime", user.getRegTime());
            jsonObject.put("remark", user.getRemark());
            jsonObject.put("authTime", user.getAuthTime());
            return Result.ok("触发事件成功", jsonObject);
        } else {
            return Result.error("触发事件失败");
        }
    }
}
