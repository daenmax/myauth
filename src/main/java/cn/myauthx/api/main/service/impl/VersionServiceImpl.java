package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.main.entity.Version;
import cn.myauthx.api.main.mapper.VersionMapper;
import cn.myauthx.api.main.service.IVersionService;
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
public class VersionServiceImpl extends ServiceImpl<VersionMapper, Version> implements IVersionService {

}
