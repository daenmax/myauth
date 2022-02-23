package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Config;
import cn.myauthx.api.main.mapper.ConfigMapper;
import cn.myauthx.api.main.service.IConfigService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    /**
     * 获取web信息，redis
     *
     * @return
     */
    @Override
    public Result getWebInfo() {
        Config config = (Config) redisUtil.get("config");
        if (CheckUtils.isObjectEmpty(config)) {
            return Result.error("配置获取失败，请重启服务");
        }
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("title", config.getSeoTitle());
        jsonObject.put("keywords", config.getSeoKeywords());
        jsonObject.put("description", config.getSeoDescription());
        return Result.ok("获取成功", jsonObject);
    }
}
