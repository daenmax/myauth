package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.main.entity.User;
import cn.myauthx.api.main.mapper.UserMapper;
import cn.myauthx.api.main.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
