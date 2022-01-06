package cn.myauthx.api.web.service.impl;

import cn.myauthx.api.web.entity.Msg;
import cn.myauthx.api.web.mapper.MsgMapper;
import cn.myauthx.api.web.service.IMsgService;
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
public class MsgServiceImpl extends ServiceImpl<MsgMapper, Msg> implements IMsgService {

}
