package cn.myauthx.api.web.service.impl;

import cn.myauthx.api.web.entity.User;
import cn.myauthx.api.web.mapper.UserMapper;
import cn.myauthx.api.web.service.IUserService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
