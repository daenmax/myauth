package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.entity.Version;
import cn.myauthx.api.main.mapper.VersionMapper;
import cn.myauthx.api.main.service.IVersionService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.RedisUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
@Service
public class VersionServiceImpl extends ServiceImpl<VersionMapper, Version> implements IVersionService {
    @Autowired
    private VersionMapper versionMapper;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 检测版本更新，根据vkey
     * @param versionC
     * @param soft
     * @return
     */
    @Override
    public Result checkUpdate(Version versionC, Soft soft) {
        Version version = (Version) redisUtil.get("version_" + versionC.getVkey());
        if(CheckUtils.isObjectEmpty(version)){
            return Result.error("vkey错误");
        }
        if(!version.getFromSoftId().equals(soft.getId())){
            return Result.error("vkey与skey不匹配");
        }
        LambdaQueryWrapper<Version> versionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        versionLambdaQueryWrapper.eq(Version::getFromSoftId,version.getFromSoftId());
        versionLambdaQueryWrapper.orderBy(true,false,Version::getVer);
        List<Version> versionList = versionMapper.selectList(versionLambdaQueryWrapper);
        JSONArray jsonArray = new JSONArray();
        for (Version version1 : versionList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ver",version1.getVer());
            jsonObject.put("updLog",version1.getUpdLog());
            jsonObject.put("updTime",version1.getUpdTime());
            jsonObject.put("updType",version1.getUpdType());
            jsonObject.put("status",version1.getStatus());
            jsonArray.add(jsonObject);
            if(version1.getVer().equals(version.getVer())){
                break;
            }
        }
        String msg = jsonArray.size() > 1?"检测到有新版本":"已是最新版本";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ver",version.getVer());
        jsonObject.put("haveNew",jsonArray.size() > 1?"1":"0");
        jsonObject.put("list",jsonArray);
        return Result.ok(msg,jsonObject);
    }
}
