package cn.myauthx.api.main.service;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.base.vo.MyPage;
import cn.myauthx.api.main.entity.Strategy;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author DaenMax
 * @since 2022-03-09
 */
public interface IStrategyService extends IService<Strategy> {
    /**
     * 获取策略列表
     *
     * @param strategy
     * @param myPage
     * @return
     */
    Result getStrategyList(Strategy strategy, MyPage myPage);

    /**
     * 获取策略列表_全部_简要
     *
     * @param strategy
     * @return
     */
    Result getStrategyListEx(Strategy strategy);

    /**
     * 查询策略，根据id
     *
     * @param strategy
     * @return
     */
    Result getStrategy(Strategy strategy);

    /**
     * 修改策略
     *
     * @param strategy
     * @return
     */
    Result updStrategy(Strategy strategy);

    /**
     * 添加策略
     *
     * @param strategy
     * @return
     */
    Result addStrategy(Strategy strategy);

    /**
     * 删除策略
     *
     * @param strategy
     * @return
     */
    Result delStrategy(Strategy strategy);
}
