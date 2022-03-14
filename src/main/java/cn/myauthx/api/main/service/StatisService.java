package cn.myauthx.api.main.service;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Event;
import cn.myauthx.api.main.entity.MyPage;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
public interface StatisService {
    /**
     * 获取在线人数
     *
     * @param soft
     * @return
     */
    Result getOnlineUserCount(Soft soft);

    /**
     * 获取软件统计
     *
     * @param soft
     * @return
     */
    Result getSoftStatisData(Soft soft);

    /**
     * 获取数据排行
     *
     * @param soft
     * @param myPage
     * @return
     */
    Result getDataRanking(Soft soft, MyPage myPage);

    /**
     * 获取设备排行
     *
     * @param soft
     * @param myPage
     * @return
     */
    Result getUserDeviceInfoRanking(Soft soft, MyPage myPage);

    /**
     * 获取近7天每日新增用户数
     *
     * @return
     */
    Result getUserDatNew();

    /**
     * 获取用户分布比例
     *
     * @return
     */
    Result getUserDistribution();

    /**
     * 获取卡密状态比例
     *
     * @return
     */
    Result getCardDistribution();

    /**
     * 获取封禁类型数量
     *
     * @return
     */
    Result getBanTypeCount();
}
