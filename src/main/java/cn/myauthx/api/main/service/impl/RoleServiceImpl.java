package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.main.entity.Role;
import cn.myauthx.api.main.mapper.RoleMapper;
import cn.myauthx.api.main.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author DaenMax
 * @since 2022-03-09
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
