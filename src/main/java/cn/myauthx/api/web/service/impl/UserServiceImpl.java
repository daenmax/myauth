package cn.myauthx.api.web.service.impl;

import cn.myauthx.api.web.entity.User;
import cn.myauthx.api.web.mapper.UserMapper;
import cn.myauthx.api.web.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public void test() {

        User user = new User();
        user.setName("李四");
        user = userMapper.getUser(user);


        return;
    }

}
