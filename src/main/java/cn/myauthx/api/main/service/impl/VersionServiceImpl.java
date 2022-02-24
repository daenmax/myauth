package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.*;
import cn.myauthx.api.main.mapper.*;
import cn.myauthx.api.main.service.IVersionService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.MyUtils;
import cn.myauthx.api.util.RedisUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
@Service
public class VersionServiceImpl extends ServiceImpl<VersionMapper, Version> implements IVersionService {
    @Resource
    private VersionMapper versionMapper;
    @Resource
    private SoftMapper softMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private DataMapper dataMapper;
    @Resource
    private MsgMapper msgMapper;
    @Resource
    private PlogMapper plogMapper;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 检测版本更新，根据vkey
     *
     * @param versionC
     * @param soft
     * @return
     */
    @Override
    public Result checkUpdate(Version versionC, Soft soft) {
        Version version = (Version) redisUtil.get("version:" + versionC.getVkey());
        if (CheckUtils.isObjectEmpty(version)) {
            return Result.error("vkey错误");
        }
        if (!version.getFromSoftId().equals(soft.getId())) {
            return Result.error("vkey与skey不匹配");
        }
        LambdaQueryWrapper<Version> versionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        versionLambdaQueryWrapper.eq(Version::getFromSoftId, version.getFromSoftId());
        versionLambdaQueryWrapper.orderBy(true, false, Version::getVer);
        List<Version> versionList = versionMapper.selectList(versionLambdaQueryWrapper);
        JSONArray jsonArray = new JSONArray();
        for (Version version1 : versionList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ver", version1.getVer());
            jsonObject.put("updLog", version1.getUpdLog());
            jsonObject.put("updTime", version1.getUpdTime());
            jsonObject.put("updType", version1.getUpdType());
            jsonObject.put("status", version1.getStatus());
            jsonArray.add(jsonObject);
            if (version1.getVer().equals(version.getVer())) {
                break;
            }
        }
        String msg = jsonArray.size() > 1 ? "检测到有新版本" : "已是最新版本";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ver", version.getVer());
        jsonObject.put("haveNew", jsonArray.size() > 1 ? "1" : "0");
        jsonObject.put("list", jsonArray);
        return Result.ok(msg, jsonObject);
    }

    /**
     * 获取最新的一个版本
     *
     * @param soft
     * @return
     */
    @Override
    public Result getNewVersion(Soft soft) {
        LambdaQueryWrapper<Version> versionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        versionLambdaQueryWrapper.eq(Version::getFromSoftId, soft.getId());
        versionLambdaQueryWrapper.orderBy(true, false, Version::getVer);
        List<Version> versionList = versionMapper.selectList(versionLambdaQueryWrapper);
        if (versionList.size() <= 0) {
            return Result.error("该软件没有任何版本");
        }
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ver", versionList.get(0).getVer());
        jsonObject.put("updLog", versionList.get(0).getUpdLog());
        jsonObject.put("updTime", versionList.get(0).getUpdTime());
        jsonObject.put("updType", versionList.get(0).getUpdType());
        jsonObject.put("status", versionList.get(0).getStatus());
        jsonArray.add(jsonObject);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("list", jsonArray);
        return Result.ok("获取成功", jsonObject1);
    }

    /**
     * 获取版本列表
     *
     * @param versionC
     * @param myPage
     * @return
     */
    @Override
    public Result getVersionList(Version versionC, MyPage myPage) {
        Page<Version> page = new Page<>(myPage.getPageIndex(), myPage.getPageSize(), true);
        if (!CheckUtils.isObjectEmpty(myPage.getOrders())) {
            for (int i = 0; i < myPage.getOrders().size(); i++) {
                myPage.getOrders().get(i).setColumn(MyUtils.camelToUnderline(myPage.getOrders().get(i).getColumn()));
            }
            page.setOrders(myPage.getOrders());
        } else {
            OrderItem orderItem = new OrderItem();
            orderItem.setColumn("UPD_TIME");
            orderItem.setAsc(false);
            List<OrderItem> orderItems = new ArrayList<>();
            orderItems.add(orderItem);
            myPage.setOrders(orderItems);
            page.setOrders(myPage.getOrders());
        }
        IPage<Version> versionPage = versionMapper.selectPage(page, getQwVersion(versionC));
        for (int i = 0; i < versionPage.getRecords().size(); i++) {
            Soft obj = (Soft) redisUtil.get("id:soft:" + versionPage.getRecords().get(i).getFromSoftId());
            if (!CheckUtils.isObjectEmpty(obj)) {
                versionPage.getRecords().get(i).setFromSoftName(obj.getName());
            }
        }
        return Result.ok("获取成功", versionPage);
    }

    /**
     * 获取查询条件构造器
     *
     * @param version
     * @return
     */
    public LambdaQueryWrapper<Version> getQwVersion(Version version) {
        LambdaQueryWrapper<Version> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(version.getFromSoftId()), Version::getFromSoftId, version.getFromSoftId());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(version.getVer()), Version::getVer, version.getVer());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(version.getVkey()), Version::getVkey, version.getVkey());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(version.getUpdType()), Version::getUpdType, version.getUpdType());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(version.getStatus()), Version::getStatus, version.getStatus());
        return LambdaQueryWrapper;
    }

    /**
     * 获取版本，通过id或者vkey
     *
     * @param version
     * @return
     */
    @Override
    public Result getVersion(Version version) {
        LambdaQueryWrapper<Version> versionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        versionLambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(version.getId()), Version::getId, version.getId());
        versionLambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(version.getVkey()), Version::getVkey, version.getVkey());
        Version newVersion = versionMapper.selectOne(versionLambdaQueryWrapper);
        if (CheckUtils.isObjectEmpty(newVersion)) {
            return Result.error("查询失败，未找到");
        }
        return Result.ok("查询成功", newVersion);
    }

    /**
     * 修改版本
     *
     * @param version
     * @return
     */
    @Override
    public Result updVersion(Version version) {
        int num = versionMapper.updateById(version);
        if (num <= 0) {
            return Result.error("修改失败");
        }
        Version newVersion = versionMapper.selectById(version.getId());
        redisUtil.set("version:" + newVersion.getVkey(), newVersion);
        redisUtil.set("id:version:" + newVersion.getId(), newVersion);
        return Result.ok("修改成功");
    }

    /**
     * 添加版本
     *
     * @param version
     * @return
     */
    @Override
    public Result addVersion(Version version) {
        Soft soft = softMapper.selectById(version.getFromSoftId());
        if (CheckUtils.isObjectEmpty(soft)) {
            return Result.error("fromSoftId错误");
        }
        LambdaQueryWrapper<Version> versionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        versionLambdaQueryWrapper.eq(Version::getFromSoftId, version.getFromSoftId());
        versionLambdaQueryWrapper.eq(Version::getVer, version.getVer());
        Version version1 = versionMapper.selectOne(versionLambdaQueryWrapper);
        if (!CheckUtils.isObjectEmpty(version1)) {
            return Result.error("版本号已存在");
        }
        version.setUpdTime(Integer.valueOf(MyUtils.getTimeStamp()));
        version.setVkey(MyUtils.getUUID(true));
        int num = versionMapper.insert(version);
        if (num <= 0) {
            return Result.error("添加失败");
        }
        redisUtil.set("version:" + version.getVkey(), version);
        redisUtil.set("id:version:" + version.getId(), version);
        return Result.ok("添加成功");
    }

    /**
     * 添加版本_同时添加回复
     *
     * @param version
     * @param msg
     * @return
     */
    @Override
    public Result addVersionAndMsg(Version version, Msg msg) {
        Soft soft = softMapper.selectById(version.getFromSoftId());
        if (CheckUtils.isObjectEmpty(soft)) {
            return Result.error("fromSoftId错误");
        }
        LambdaQueryWrapper<Version> versionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        versionLambdaQueryWrapper.eq(Version::getFromSoftId, version.getFromSoftId());
        versionLambdaQueryWrapper.eq(Version::getVer, version.getVer());
        Version version1 = versionMapper.selectOne(versionLambdaQueryWrapper);
        if (!CheckUtils.isObjectEmpty(version1)) {
            return Result.error("版本号已存在");
        }
        version.setUpdTime(Integer.valueOf(MyUtils.getTimeStamp()));
        version.setVkey(MyUtils.getUUID(true));
        int num = versionMapper.insert(version);
        if (num <= 0) {
            return Result.error("添加失败");
        }
        LambdaQueryWrapper<Version> newVersionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        newVersionLambdaQueryWrapper.eq(Version::getFromSoftId, version.getFromSoftId());
        newVersionLambdaQueryWrapper.eq(Version::getVer, version.getVer());
        Version newVersion = versionMapper.selectOne(newVersionLambdaQueryWrapper);

        msg.setFromSoftId(newVersion.getFromSoftId());
        msg.setFromVerId(newVersion.getId());
        msg.setStatus(1);
        msgMapper.insert(msg);
        redisUtil.set("version:" + version.getVkey(), version);
        redisUtil.set("id:version:" + version.getId(), version);
        return Result.ok("添加成功");
    }

    /**
     * 删除版本，会同步删除用户、数据、回复、日志
     *
     * @param versionC
     * @return
     */
    @Override
    public Result delVersion(Version versionC) {
        Version version = versionMapper.selectById(versionC.getId());
        if (CheckUtils.isObjectEmpty(version)) {
            return Result.error("删除失败，id错误");
        }
        //删除版本表
        versionMapper.deleteById(version);
        //删除用户表
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getFromVerId, version.getId());
        userMapper.delete(userLambdaQueryWrapper);
        //删除数据表
        LambdaQueryWrapper<Data> dataLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dataLambdaQueryWrapper.eq(Data::getFromVerId, version.getId());
        dataMapper.delete(dataLambdaQueryWrapper);
        //删除回复表
        LambdaQueryWrapper<Msg> msgLambdaQueryWrapper = new LambdaQueryWrapper<>();
        msgLambdaQueryWrapper.eq(Msg::getFromVerId, version.getId());
        msgMapper.delete(msgLambdaQueryWrapper);
        //删除日志表
        LambdaQueryWrapper<Plog> plogLambdaQueryWrapper = new LambdaQueryWrapper<>();
        plogLambdaQueryWrapper.eq(Plog::getFromVerId, version.getId());
        plogMapper.delete(plogLambdaQueryWrapper);

        redisUtil.del("version:" + version.getVkey());
        redisUtil.del("id:version:" + version.getId());
        return Result.ok("删除成功");
    }

    /**
     * 获取版本列表_全部_简要
     *
     * @param versionC
     * @return
     */
    @Override
    public Result getVersionListEx(Version versionC) {
        LambdaQueryWrapper<Version> versionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        versionLambdaQueryWrapper.eq(Version::getFromSoftId, versionC.getFromSoftId());
        versionLambdaQueryWrapper.select(Version::getId, Version::getVer, Version::getVkey);
        if (!CheckUtils.isObjectEmpty(versionC.getVer())) {
            versionLambdaQueryWrapper.like(Version::getVer, versionC.getVer());
        }
        List<Map<String, Object>> maps = versionMapper.selectMaps(versionLambdaQueryWrapper);
        return Result.ok("获取成功", maps);
    }
}
