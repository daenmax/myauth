package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.main.entity.Event;
import cn.myauthx.api.main.mapper.EventMapper;
import cn.myauthx.api.main.service.IEventService;
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
