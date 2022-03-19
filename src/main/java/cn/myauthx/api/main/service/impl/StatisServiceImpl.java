package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.base.vo.*;
import cn.myauthx.api.main.entity.*;
import cn.myauthx.api.main.enums.CardEnums;
import cn.myauthx.api.main.mapper.*;
import cn.myauthx.api.main.service.StatisService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.MyUtils;
import cn.myauthx.api.util.RedisUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
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
    private UserMapper userMapper;
    @Resource
    private DataMapper dataMapper;
    @Resource
    private CardMapper cardMapper;
    @Resource
    private BanMapper banMapper;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 获取在线人数
     *
     * @param skey
     * @return
     */
    @Override
    public Result getOnlineUserCount(String skey) {
        LambdaQueryWrapper<Soft> softLambdaQueryWrapper = new LambdaQueryWrapper<>();
        softLambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(skey), Soft::getSkey, skey);
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

    /**
     * 获取用户总数
     *
     * @param skey
     * @return
     */
    @Override
    public Result getUserCount(String skey) {
        LambdaQueryWrapper<Soft> softLambdaQueryWrapper = new LambdaQueryWrapper<>();
        softLambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(skey), Soft::getSkey, skey);
        Soft newSoft = softMapper.selectOne(softLambdaQueryWrapper);
        if (CheckUtils.isObjectEmpty(newSoft)) {
            return Result.error("获取失败，未找到软件");
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getFromSoftId, newSoft.getId());
        Long count = userMapper.selectCount(userLambdaQueryWrapper);
        JSONObject retJson = new JSONObject(true);
        retJson.put("softName", newSoft.getName());
        retJson.put("userCount", count);
        return Result.ok("获取成功", retJson);
    }

    /**
     * 获取软件统计
     *
     * @param soft
     * @return
     */
    @Override
    public Result getSoftStatisData(Soft soft) {
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

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        Long allCount = userMapper.selectCount(wrapper);
        retJson.put("allCount", allCount);

        Integer now = Integer.valueOf(MyUtils.getTimeStamp());

        LambdaQueryWrapper<User> wrapperL1 = new LambdaQueryWrapper<>();
        wrapperL1.eq(User::getFromSoftId, newSoft.getId());
        Integer L1 = now - 1 * 24 * 60 * 60;
        wrapperL1.ge(User::getRegTime, L1);
        Long L1C = userMapper.selectCount(wrapperL1);
        retJson.put("nearly1", L1C);

        LambdaQueryWrapper<User> wrapperL7 = new LambdaQueryWrapper<>();
        wrapperL7.eq(User::getFromSoftId, newSoft.getId());
        Integer L7 = now - 7 * 24 * 60 * 60;
        wrapperL7.ge(User::getRegTime, L7);
        Long L7C = userMapper.selectCount(wrapperL7);
        retJson.put("nearly7", L7C);

        LambdaQueryWrapper<User> wrapperL30 = new LambdaQueryWrapper<>();
        wrapperL30.eq(User::getFromSoftId, newSoft.getId());
        Integer L30 = now - 30 * 24 * 60 * 60;
        wrapperL30.ge(User::getRegTime, L30);
        Long L30C = userMapper.selectCount(wrapperL30);
        retJson.put("nearly30", L30C);


        return Result.ok("获取成功", retJson);
    }

    /**
     * 获取数据排行
     *
     * @param soft
     * @param myPage
     * @return
     */
    @Override
    public Result getDataRanking(Soft soft, MyPage myPage) {
        LambdaQueryWrapper<Soft> softLambdaQueryWrapper = new LambdaQueryWrapper<>();
        softLambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(soft.getId()), Soft::getId, soft.getId());
        softLambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(soft.getSkey()), Soft::getSkey, soft.getSkey());
        Soft newSoft = softMapper.selectOne(softLambdaQueryWrapper);
        if (CheckUtils.isObjectEmpty(newSoft)) {
            return Result.error("获取失败，未找到软件");
        }
        DataRanking dataRanking = new DataRanking();
        dataRanking.setFromSoftId(newSoft.getId());
        dataRanking.setPageIndex(myPage.getPageIndex() + (myPage.getPageIndex() - 1) * 10);
        dataRanking.setPageSize(myPage.getPageSize());
        List<DataRanking> dataRankingList = dataMapper.getDataRanking(dataRanking);
        JSONObject retJson = new JSONObject(true);
        retJson.put("pageIndex", myPage.getPageIndex());
        retJson.put("pageSize", myPage.getPageSize());
        Integer count = dataMapper.getDataRankingCount(dataRanking);
        retJson.put("pageNum", MyUtils.getPageNum(count, myPage.getPageSize()));
        retJson.put("total", count);
        JSONArray array = new JSONArray();
        for (DataRanking ranking : dataRankingList) {
            JSONObject object = new JSONObject(true);
            object.put("type", ranking.getType());
            object.put("content", ranking.getContent());
            object.put("count", ranking.getNum());
            object.put("fromSoftName", newSoft.getName());
            array.add(object);
        }
        retJson.put("list", array);
        return Result.ok("获取成功", retJson);
    }

    /**
     * 获取设备排行
     *
     * @param soft
     * @param myPage
     * @return
     */
    @Override
    public Result getUserDeviceInfoRanking(Soft soft, MyPage myPage) {
        LambdaQueryWrapper<Soft> softLambdaQueryWrapper = new LambdaQueryWrapper<>();
        softLambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(soft.getId()), Soft::getId, soft.getId());
        softLambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(soft.getSkey()), Soft::getSkey, soft.getSkey());
        Soft newSoft = softMapper.selectOne(softLambdaQueryWrapper);
        if (CheckUtils.isObjectEmpty(newSoft)) {
            return Result.error("获取失败，未找到软件");
        }
        UserDeviceInfoRanking userDeviceInfoRanking = new UserDeviceInfoRanking();
        userDeviceInfoRanking.setFromSoftId(newSoft.getId());
        userDeviceInfoRanking.setPageIndex(myPage.getPageIndex() + (myPage.getPageIndex() - 1) * 10);
        userDeviceInfoRanking.setPageSize(myPage.getPageSize());
        List<UserDeviceInfoRanking> userDeviceInfoRankingList = userMapper.getUserDeviceInfoRanking(userDeviceInfoRanking);
        JSONObject retJson = new JSONObject(true);
        retJson.put("pageIndex", myPage.getPageIndex());
        retJson.put("pageSize", myPage.getPageSize());
        Integer count = userMapper.getUserDeviceInfoRankingCount(userDeviceInfoRanking);
        retJson.put("pageNum", MyUtils.getPageNum(count, myPage.getPageSize()));
        retJson.put("total", count);
        JSONArray array = new JSONArray();
        for (UserDeviceInfoRanking deviceInfoRanking : userDeviceInfoRankingList) {
            JSONObject object = new JSONObject(true);
            object.put("deviceInfo", deviceInfoRanking.getDeviceInfo());
            object.put("count", deviceInfoRanking.getNum());
            object.put("fromSoftName", newSoft.getName());
            array.add(object);
        }
        retJson.put("list", array);
        return Result.ok("获取成功", retJson);
    }

    /**
     * 获取近7天每日新增用户数
     *
     * @return
     */
    @Override
    public Result getUserDatNew() {
        UserDayNew userDayNew = userMapper.getUserDayNew();
        JSONObject retJson = new JSONObject(true);
        JSONArray array = new JSONArray();

        JSONObject object6 = new JSONObject(true);
        object6.put("count", userDayNew.getD6());
        object6.put("date", MyUtils.getDateID(-6));
        array.add(object6);

        JSONObject object5 = new JSONObject(true);
        object5.put("count", userDayNew.getD5());
        object5.put("date", MyUtils.getDateID(-5));
        array.add(object5);

        JSONObject object4 = new JSONObject(true);
        object4.put("count", userDayNew.getD4());
        object4.put("date", MyUtils.getDateID(-4));
        array.add(object4);

        JSONObject object3 = new JSONObject(true);
        object3.put("count", userDayNew.getD3());
        object3.put("date", MyUtils.getDateID(-3));
        array.add(object3);

        JSONObject object2 = new JSONObject(true);
        object2.put("count", userDayNew.getD2());
        object2.put("date", MyUtils.getDateID(-2));
        array.add(object2);

        JSONObject object1 = new JSONObject(true);
        object1.put("count", userDayNew.getD1());
        object1.put("date", MyUtils.getDateID(-1));
        array.add(object1);

        JSONObject object0 = new JSONObject(true);
        object0.put("count", userDayNew.getD0());
        object0.put("date", MyUtils.getDateID(0));

        retJson.put("list", array);
        return Result.ok("获取成功", retJson);
    }

    /**
     * 获取用户分布比例
     *
     * @return
     */
    @Override
    public Result getUserDistribution() {
        JSONObject retJson = new JSONObject(true);
        JSONArray array = new JSONArray();
        LambdaQueryWrapper<Soft> softLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<Soft> softList = softMapper.selectList(softLambdaQueryWrapper);
        for (Soft soft : softList) {
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getFromSoftId, soft.getId());
            Long count = userMapper.selectCount(userLambdaQueryWrapper);
            JSONObject object = new JSONObject(true);
            object.put("fromSoftName", soft.getName());
            object.put("count", count);
            array.add(object);
        }
        retJson.put("list", array);
        return Result.ok("获取成功", retJson);
    }

    /**
     * 获取卡密状态比例
     *
     * @return
     */
    @Override
    public Result getCardDistribution() {
        JSONObject retJson = new JSONObject(true);
        JSONArray array = new JSONArray();

        LambdaQueryWrapper<Card> cardLambdaQueryWrapper0 = new LambdaQueryWrapper<>();
        cardLambdaQueryWrapper0.eq(Card::getStatus, CardEnums.STATUS_NOTUSEd.getCode());
        Long count0 = cardMapper.selectCount(cardLambdaQueryWrapper0);
        JSONObject object0 = new JSONObject(true);
        object0.put("count", count0);
        object0.put("desc", CardEnums.STATUS_NOTUSEd.getDesc());
        array.add(object0);

        LambdaQueryWrapper<Card> cardLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        cardLambdaQueryWrapper1.eq(Card::getStatus, CardEnums.STATUS_USED.getCode());
        Long count1 = cardMapper.selectCount(cardLambdaQueryWrapper1);
        JSONObject object1 = new JSONObject(true);
        object1.put("count", count1);
        object1.put("desc", CardEnums.STATUS_USED.getDesc());
        array.add(object1);

        LambdaQueryWrapper<Card> cardLambdaQueryWrapper2 = new LambdaQueryWrapper<>();
        cardLambdaQueryWrapper2.eq(Card::getStatus, CardEnums.STATUS_DISABLE.getCode());
        Long count2 = cardMapper.selectCount(cardLambdaQueryWrapper2);
        JSONObject object2 = new JSONObject(true);
        object2.put("count", count2);
        object2.put("desc", CardEnums.STATUS_DISABLE.getDesc());
        array.add(object2);

        retJson.put("list", array);
        return Result.ok("获取成功", retJson);
    }

    /**
     * 获取封禁类型数量
     *
     * @return
     */
    @Override
    public Result getBanTypeCount() {
        JSONObject retJson = new JSONObject(true);
        JSONArray array = new JSONArray();

        LambdaQueryWrapper<Ban> banLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        banLambdaQueryWrapper1.eq(Ban::getType, 1);
        Long count1 = banMapper.selectCount(banLambdaQueryWrapper1);
        JSONObject object1 = new JSONObject(true);
        object1.put("count", count1);
        object1.put("type", "机器码");
        array.add(object1);

        LambdaQueryWrapper<Ban> banLambdaQueryWrapper2 = new LambdaQueryWrapper<>();
        banLambdaQueryWrapper2.eq(Ban::getType, 2);
        Long count2 = banMapper.selectCount(banLambdaQueryWrapper2);
        JSONObject object2 = new JSONObject(true);
        object2.put("count", count2);
        object2.put("type", "IP");
        array.add(object2);

        LambdaQueryWrapper<Ban> banLambdaQueryWrapper3 = new LambdaQueryWrapper<>();
        banLambdaQueryWrapper3.eq(Ban::getType, 3);
        Long count3 = banMapper.selectCount(banLambdaQueryWrapper3);
        JSONObject object3 = new JSONObject(true);
        object3.put("count", count3);
        object3.put("type", "账号");
        array.add(object3);


        retJson.put("list", array);
        return Result.ok("获取成功", retJson);
    }
}
