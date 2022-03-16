package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.*;
import cn.myauthx.api.main.enums.CardEnums;
import cn.myauthx.api.main.mapper.*;
import cn.myauthx.api.main.service.IAcardService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.MyUtils;
import cn.myauthx.api.util.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author DaenMax
 * @since 2022-03-16
 */
@Service
public class AcardServiceImpl extends ServiceImpl<AcardMapper, Acard> implements IAcardService {
    @Resource
    private AcardMapper acardMapper;
    @Resource
    private SoftMapper softMapper;
    @Resource
    private StrategyMapper strategyMapper;
    @Resource
    private AdminMapper adminMapper;
    @Resource
    private AlogMapper alogMapper;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 获取卡密列表
     *
     * @param acard
     * @param myPage
     * @return
     */
    @Override
    public Result getACardList(Acard acard, MyPage myPage) {
        Page<Acard> page = new Page<>(myPage.getPageIndex(), myPage.getPageSize(), true);
        if (!CheckUtils.isObjectEmpty(myPage.getOrders())) {
            for (int i = 0; i < myPage.getOrders().size(); i++) {
                myPage.getOrders().get(i).setColumn(MyUtils.camelToUnderline(myPage.getOrders().get(i).getColumn()));
            }
            page.setOrders(myPage.getOrders());
        }
        IPage<Acard> msgPage = acardMapper.selectPage(page, getQwACard(acard));
        for (int i = 0; i < msgPage.getRecords().size(); i++) {
            msgPage.getRecords().get(i).setStatusName(cardStatus2Str(msgPage.getRecords().get(i).getStatus()));
        }
        return Result.ok("获取成功", msgPage);
    }

    /**
     * 卡密状态转换
     *
     * @param status
     * @return
     */
    private String cardStatus2Str(Integer status) {
        if (status == 0) {
            return "未使用";
        }
        if (status == 1) {
            return "已使用";
        }
        if (status == 2) {
            return "被禁用";
        }
        return "未知";
    }

    /**
     * 获取查询条件构造器
     *
     * @param acard
     * @return
     */
    public LambdaQueryWrapper<Acard> getQwACard(Acard acard) {
        LambdaQueryWrapper<Acard> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(acard.getCkey()), Acard::getCkey, acard.getCkey());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(acard.getMoney()), Acard::getMoney, acard.getMoney());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(acard.getAddTime()), Acard::getAddTime, acard.getAddTime());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(acard.getLetTime()), Acard::getLetTime, acard.getLetTime());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(acard.getLetUser()), Acard::getLetUser, acard.getLetUser());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(acard.getStatus()), Acard::getStatus, acard.getStatus());
        LambdaQueryWrapper.orderBy(true, false, Acard::getId);
        return LambdaQueryWrapper;
    }

    /**
     * 查询卡密，根据id或者ckey
     *
     * @param acard
     * @return
     */
    @Override
    public Result getACard(Acard acard) {
        LambdaQueryWrapper<Acard> cardLambdaQueryWrapper = new LambdaQueryWrapper<>();
        cardLambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(acard.getId()), Acard::getId, acard.getId());
        cardLambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(acard.getCkey()), Acard::getCkey, acard.getCkey());
        Acard newCard = acardMapper.selectOne(cardLambdaQueryWrapper);
        if (CheckUtils.isObjectEmpty(newCard)) {
            return Result.error("查询失败，未找到");
        }
        return Result.ok("查询成功", newCard);
    }

    /**
     * 修改卡密
     *
     * @param acard
     * @return
     */
    @Override
    public Result updACard(Acard acard) {
        Acard newCard = acardMapper.selectById(acard.getId());
        if (CheckUtils.isObjectEmpty(newCard)) {
            return Result.error("卡密ID错误");
        }
        if (newCard.getStatus() == 1) {
            return Result.error("卡密已被使用，无法修改");
        }
        int num = acardMapper.updateById(acard);
        if (num <= 0) {
            return Result.error("修改失败");
        }
        return Result.ok("修改成功");
    }

    /**
     * 生成卡密
     *
     * @param prefix 前缀
     * @param count  张数
     * @param acard
     * @return
     */
    @Override
    public Result addACard(String prefix, Integer count, Acard acard, Admin admin) {
        String prefixStr = null;
        if (CheckUtils.isObjectEmpty(prefix)) {
            prefixStr = "";
        } else {
            prefixStr = prefix;
        }
        Integer timeStamp = Integer.valueOf(MyUtils.getTimeStamp());
        int okCount = 0;
        String outStr = "";
        for (Integer i = 0; i < count; i++) {
            String ckey = prefixStr + MyUtils.getUUID(false);
            outStr = outStr + ckey;
            if (i != count - 1) {
                outStr = outStr + "\r\n";
            }
            Acard newCard = new Acard();
            newCard.setCkey(ckey);
            newCard.setMoney(acard.getMoney());
            newCard.setAddTime(timeStamp);
            newCard.setStatus(0);
            int insert = acardMapper.insert(newCard);
            okCount = okCount + insert;
        }
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("ckeyStr", outStr);
        return Result.ok("成功生成 " + okCount + " 张卡密", jsonObject);
    }

    /**
     * 删除卡密，支持批量
     *
     * @param ids 多个用英文逗号隔开
     * @return
     */
    @Override
    public Result delACard(String ids) {
        String[] idArray = ids.split(",");
        List<String> strings = Arrays.asList(idArray);
        if (idArray.length == 0) {
            return Result.error("ids参数格式可能错误");
        }
        int okCount = acardMapper.deleteBatchIds(strings);
        return Result.ok("成功删除 " + okCount + " 张卡密");
    }

    /**
     * 禁用卡密，支持批量
     *
     * @param ids 多个用英文逗号隔开
     * @return
     */
    @Override
    public Result banACard(String ids) {
        String[] idArray = ids.split(",");
        if (idArray.length == 0) {
            return Result.error("ids参数格式可能错误");
        }
        LambdaUpdateWrapper<Acard> wrapper = new LambdaUpdateWrapper();
        wrapper.set(Acard::getStatus, CardEnums.STATUS_DISABLE.getCode()).in(Acard::getId, Arrays.asList(idArray)).eq(Acard::getStatus, CardEnums.STATUS_NOTUSEd.getCode());
        int okCount = acardMapper.update(null, wrapper);
        return Result.ok("成功禁用 " + okCount + " 张卡密");
    }

    /**
     * 解禁卡密，支持批量
     *
     * @param ids 多个用英文逗号隔开
     * @return
     */
    @Override
    public Result unBanACard(String ids) {
        String[] idArray = ids.split(",");
        if (idArray.length == 0) {
            return Result.error("ids参数格式可能错误");
        }
        LambdaUpdateWrapper<Acard> wrapper = new LambdaUpdateWrapper();
        wrapper.set(Acard::getStatus, CardEnums.STATUS_NOTUSEd.getCode()).in(Acard::getId, Arrays.asList(idArray)).eq(Acard::getStatus, CardEnums.STATUS_DISABLE.getCode());
        int okCount = acardMapper.update(null, wrapper);
        return Result.ok("成功解禁 " + okCount + " 张卡密");
    }

    /**
     * 导出卡密
     *
     * @param acard
     * @return
     */
    @Override
    public List<Acard> exportACard(Acard acard) {
        List<Acard> acardList = acardMapper.selectList(getQwACard(acard));
        for (int i = 0; i < acardList.size(); i++) {
            acardList.get(i).setStatusName(cardStatus2Str(acardList.get(i).getStatus()));
            acardList.get(i).setAddTimeName(MyUtils.stamp2Date(String.valueOf(acardList.get(i).getAddTime())));
            if (!CheckUtils.isObjectEmpty(acardList.get(i).getLetTime())) {
                acardList.get(i).setLetTimeName(MyUtils.stamp2Date(String.valueOf(acardList.get(i).getLetTime())));
            } else {
                acardList.get(i).setLetTimeName(null);
            }
        }
        return acardList;
    }

    /**
     * 使用代理卡密
     *
     * @param ckey
     * @param adminC
     * @return
     */
    @Override
    public Result letACard(String ckey, Admin adminC) {
        Role role = (Role) redisUtil.get("role:" + adminC.getRole());
        if (role.getFromSoftId() == 0) {
            return Result.error("超级管理员无法使用此接口");
        }
        LambdaQueryWrapper<Acard> acardLambdaQueryWrapper = new LambdaQueryWrapper<>();
        acardLambdaQueryWrapper.eq(Acard::getCkey, ckey);
        Acard acard = acardMapper.selectOne(acardLambdaQueryWrapper);
        if (CheckUtils.isObjectEmpty(acard)) {
            return Result.error("卡密不存在");
        }
        if (CardEnums.STATUS_USED.getCode().equals(acard.getStatus())) {
            return Result.error("卡密已被使用");
        }
        if (CardEnums.STATUS_DISABLE.getCode().equals(acard.getStatus())) {
            return Result.error("卡密已被禁用");
        }
        BigDecimal money = new BigDecimal(acard.getMoney());
        Admin admin = adminMapper.selectById(adminC.getId());
        BigDecimal myMoney = new BigDecimal(admin.getMoney());
        myMoney = myMoney.add(money);
        admin.setMoney(String.valueOf(myMoney));

        acard.setStatus(CardEnums.STATUS_USED.getCode());
        acard.setLetTime(Integer.valueOf(MyUtils.getTimeStamp()));
        acard.setLetUser(adminC.getUser());
        int updNum = acardMapper.updateById(acard);
        if (updNum == 0) {
            return Result.error("使用卡密失败[1001]");
        }
        int num = adminMapper.updateById(admin);
        if (num == 0) {
            return Result.error("使用卡密失败[1002]");
        }
        Alog alog = new Alog();
        alog.setMoney(acard.getMoney());
        alog.setAfterMoney(String.valueOf(myMoney));
        alog.setAdminId(admin.getId());
        alog.setData("使用卡密：" + ckey);
        alog.setType("使用卡密");
        alog.setAddTime(Integer.valueOf(MyUtils.getTimeStamp()));
        alogMapper.insert(alog);
        return Result.ok("使用卡密成功，余额+" + acard.getMoney());
    }
}
