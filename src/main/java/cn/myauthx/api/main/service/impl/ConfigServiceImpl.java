package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.main.entity.Config;
import cn.myauthx.api.main.mapper.ConfigMapper;
import cn.myauthx.api.main.service.IConfigService;
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
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements IConfigService {

}
