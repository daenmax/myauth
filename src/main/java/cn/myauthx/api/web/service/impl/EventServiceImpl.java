package cn.myauthx.api.web.service.impl;

import cn.myauthx.api.web.entity.Event;
import cn.myauthx.api.web.mapper.EventMapper;
import cn.myauthx.api.web.service.IEventService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
@Service
public class EventServiceImpl extends ServiceImpl<EventMapper, Event> implements IEventService {

}
