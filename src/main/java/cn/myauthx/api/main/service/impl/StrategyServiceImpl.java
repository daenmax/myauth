package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.base.vo.MyPage;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.*;
import cn.myauthx.api.main.mapper.SoftMapper;
import cn.myauthx.api.main.mapper.StrategyMapper;
import cn.myauthx.api.main.service.IStrategyService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.MyUtils;
import cn.myauthx.api.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author DaenMax
 * @since 2022-03-09
 */
@Service
public class StrategyServiceImpl extends ServiceImpl<StrategyMapper, Strategy> implements IStrategyService {
    @Resource
    private StrategyMapper strategyMapper;
    @Resource
    private SoftMapper softMapper;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 获取查询条件构造器
     *
     * @param strategy
     * @return
     */
    public LambdaQueryWrapper<Strategy> getQwStrategy(Strategy strategy) {
        LambdaQueryWrapper<Strategy> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(strategy.getName()), Strategy::getName, strategy.getName());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(strategy.getType()), Strategy::getType, strategy.getType());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(strategy.getValue()), Strategy::getValue, strategy.getValue());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(strategy.getPrice()), Strategy::getPrice, strategy.getPrice());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(strategy.getFromSoftId()), Strategy::getFromSoftId, strategy.getFromSoftId());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(strategy.getStatus()), Strategy::getStatus, strategy.getStatus());
        LambdaQueryWrapper.orderBy(true, true, Strategy::getSort);
        return LambdaQueryWrapper;
    }

    /**
     * 获取策略列表
     *
     * @param strategy
     * @param myPage
     * @return
     */
    @Override
    public Result getStrategyList(Strategy strategy, MyPage myPage) {
        Page<Strategy> page = new Page<>(myPage.getPageIndex(), myPage.getPageSize(), true);
        if (!CheckUtils.isObjectEmpty(myPage.getOrders())) {
            for (int i = 0; i < myPage.getOrders().size(); i++) {
                myPage.getOrders().get(i).setColumn(MyUtils.camelToUnderline(myPage.getOrders().get(i).getColumn()));
            }
            page.setOrders(myPage.getOrders());
        }
        IPage<Strategy> msgPage = strategyMapper.selectPage(page, getQwStrategy(strategy));
        for (int i = 0; i < msgPage.getRecords().size(); i++) {
            Soft obj = (Soft) redisUtil.get("id:soft:" + msgPage.getRecords().get(i).getFromSoftId());
            msgPage.getRecords().get(i).setFromSoftName(obj.getName());
        }
        return Result.ok("获取成功", msgPage);
    }

    /**
     * 获取策略列表_全部_简要
     *
     * @param strategy
     * @return
     */
    @Override
    public Result getStrategyListEx(Strategy strategy) {
        LambdaQueryWrapper<Strategy> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Strategy::getFromSoftId, strategy.getFromSoftId());
        queryWrapper.eq(Strategy::getType, strategy.getType());
        queryWrapper.eq(Strategy::getStatus, 1);
        queryWrapper.select(Strategy::getId, Strategy::getName);
        if (!CheckUtils.isObjectEmpty(strategy.getName())) {
            queryWrapper.like(Strategy::getName, strategy.getName());
        }
        queryWrapper.orderBy(true, true, Strategy::getSort);
        List<Map<String, Object>> maps = strategyMapper.selectMaps(queryWrapper);
        return Result.ok("获取成功", maps);
    }

    /**
     * 查询策略，根据id
     *
     * @param strategy
     * @return
     */
    @Override
    public Result getStrategy(Strategy strategy) {
        Strategy newStrategy = strategyMapper.selectById(strategy.getId());
        if (CheckUtils.isObjectEmpty(newStrategy)) {
            return Result.error("查询失败，未找到");
        }
        return Result.ok("查询成功", newStrategy);
    }

    /**
     * 修改策略
     *
     * @param strategy
     * @return
     */
    @Override
    public Result updStrategy(Strategy strategy) {
        Strategy newStrategy = strategyMapper.selectById(strategy.getId());
        if (CheckUtils.isObjectEmpty(newStrategy)) {
            return Result.error("策略ID错误");
        }
        int num = strategyMapper.updateById(strategy);
        if (num <= 0) {
            return Result.error("修改失败");
        }
        return Result.ok("修改成功");
    }

    /**
     * 添加策略
     *
     * @param strategy
     * @return
     */
    @Override
    public Result addStrategy(Strategy strategy) {
        Soft soft = softMapper.selectById(strategy.getFromSoftId());
        if (CheckUtils.isObjectEmpty(soft)) {
            return Result.error("fromSoftId错误");
        }
        int num = strategyMapper.insert(strategy);
        if (num <= 0) {
            return Result.error("添加失败");
        }
        return Result.ok("添加成功");
    }

    /**
     * 删除策略
     *
     * @param strategy
     * @return
     */
    @Override
    public Result delStrategy(Strategy strategy) {
        Strategy newStrategy = strategyMapper.selectById(strategy.getId());
        if (CheckUtils.isObjectEmpty(newStrategy)) {
            return Result.error("策略ID错误");
        }
        int num = strategyMapper.deleteById(strategy.getId());
        if (num <= 0) {
            return Result.error("删除失败");
        }
        return Result.ok("删除成功");
    }
}
