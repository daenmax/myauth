package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.main.entity.Data;
import cn.myauthx.api.main.mapper.DataMapper;
import cn.myauthx.api.main.service.IDataService;
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
