package cn.myauthx.api.web.service.impl;

import cn.myauthx.api.web.entity.Data;
import cn.myauthx.api.web.mapper.DataMapper;
import cn.myauthx.api.web.service.IDataService;
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
public class DataServiceImpl extends ServiceImpl<DataMapper, Data> implements IDataService {

}
