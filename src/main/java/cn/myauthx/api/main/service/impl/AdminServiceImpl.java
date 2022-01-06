package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.main.entity.Admin;
import cn.myauthx.api.main.mapper.AdminMapper;
import cn.myauthx.api.main.service.IAdminService;
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
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

}
