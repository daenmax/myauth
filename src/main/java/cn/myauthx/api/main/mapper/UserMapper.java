package cn.myauthx.api.main.mapper;

import cn.myauthx.api.base.vo.UserDayNew;
import cn.myauthx.api.base.vo.UserDeviceInfoRanking;
import cn.myauthx.api.main.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-07
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 获取设备信息排行
     * @param userDeviceInfoRanking
     * @return
     */
    List<UserDeviceInfoRanking> getUserDeviceInfoRanking(UserDeviceInfoRanking userDeviceInfoRanking);
    Integer getUserDeviceInfoRankingCount(UserDeviceInfoRanking userDeviceInfoRanking);

    /**
     * 获取近7天每日新增用户数
     * @return
     */
    UserDayNew getUserDayNew();
}
