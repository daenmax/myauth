package cn.myauthx.api.web.service.impl;

import cn.myauthx.api.web.entity.Config;
import cn.myauthx.api.web.mapper.ConfigMapper;
import cn.myauthx.api.web.service.IConfigService;
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
