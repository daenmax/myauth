package cn.myauthx.api.web.mapper;

import cn.myauthx.api.web.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
