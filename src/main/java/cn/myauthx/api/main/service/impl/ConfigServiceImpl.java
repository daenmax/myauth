package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Config;
import cn.myauthx.api.main.mapper.ConfigMapper;
import cn.myauthx.api.main.service.IConfigService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements IConfigService {
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ConfigMapper configMapper;

    /**
     * 获取web信息，redis
     *
     * @return
     */
    @Override
    public Result getWebInfo() {
        Config config = (Config) redisUtil.get("config");
        if (CheckUtils.isObjectEmpty(config)) {
            return Result.error("设置获取失败，请重启服务");
        }
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("title", config.getSeoTitle());
        jsonObject.put("keywords", config.getSeoKeywords());
        jsonObject.put("description", config.getSeoDescription());
        return Result.ok("获取成功", jsonObject);
    }

    /**
     * 修改系统设置
     *
     * @param config
     * @return
     */
    @Override
    public Result editConfig(Config config) {
        LambdaQueryWrapper<Config> configLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<Config> configList = configMapper.selectList(configLambdaQueryWrapper);
        if (configList.size() == 0) {
            return Result.error("设置修改失败，请重启服务");
        } else {
            config.setId(null);
            LambdaQueryWrapper<Config> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Config::getId, configList.get(0).getId());
            int num = configMapper.update(config, queryWrapper);
            if (num == 0) {
                return Result.error("设置修改失败");
            } else {
                Config selectById = configMapper.selectById(configList.get(0).getId());
                redisUtil.set("config", selectById);
                return Result.error("设置修改成功");
            }
        }
    }

    /**
     * 获取系统设置
     *
     * @return
     */
    @Override
    public Result getConfig() {
        LambdaQueryWrapper<Config> configLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<Config> configList = configMapper.selectList(configLambdaQueryWrapper);
        if (configList.size() == 0) {
            return Result.error("设置获取失败，请重启服务");
        } else {
            return Result.ok("获取成功", configList.get(0));
        }
    }
}
