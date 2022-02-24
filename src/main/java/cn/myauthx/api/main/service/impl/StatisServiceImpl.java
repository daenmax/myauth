package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.mapper.SoftMapper;
import cn.myauthx.api.main.service.StatisService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
@Service
public class StatisServiceImpl implements StatisService {
    @Resource
    private SoftMapper softMapper;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 获取在线人数
     *
     * @param soft
     * @return
     */
    @Override
    public Result getOnlineUserCount(Soft soft) {
        LambdaQueryWrapper<Soft> softLambdaQueryWrapper = new LambdaQueryWrapper<>();
        softLambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(soft.getId()), Soft::getId, soft.getId());
        softLambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(soft.getSkey()), Soft::getSkey, soft.getSkey());
        Soft newSoft = softMapper.selectOne(softLambdaQueryWrapper);
        if (CheckUtils.isObjectEmpty(newSoft)) {
            return Result.error("获取失败，未找到软件");
        }
        Set<String> scan = redisUtil.scan("user:" + newSoft.getId() + "*");
        JSONObject retJson = new JSONObject(true);
        retJson.put("softName", newSoft.getName());
        retJson.put("onlineCount", scan.size());
        return Result.ok("获取成功", retJson);
    }
}
