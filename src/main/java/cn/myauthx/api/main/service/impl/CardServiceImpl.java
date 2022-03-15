package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.*;
import cn.myauthx.api.main.enums.CardEnums;
import cn.myauthx.api.main.mapper.*;
import cn.myauthx.api.main.service.ICardService;
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
 * @since 2022-01-06
 */
@Service
public class CardServiceImpl extends ServiceImpl<CardMapper, Card> implements ICardService {
    @Resource
    private CardMapper cardMapper;
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
     * 获取查询条件构造器
     *
     * @param card
     * @return
     */
    public LambdaQueryWrapper<Card> getQwCard(Card card) {
        LambdaQueryWrapper<Card> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(card.getCkey()), Card::getCkey, card.getCkey());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(card.getPoint()), Card::getPoint, card.getPoint());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(card.getSeconds()), Card::getSeconds, card.getSeconds());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(card.getAddTime()), Card::getAddTime, card.getAddTime());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(card.getLetTime()), Card::getLetTime, card.getLetTime());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(card.getLetUser()), Card::getLetUser, card.getLetUser());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(card.getStatus()), Card::getStatus, card.getStatus());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(card.getFromAdminId()), Card::getFromAdminId, card.getFromAdminId());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(card.getFromSoftId()), Card::getFromSoftId, card.getFromSoftId());
        LambdaQueryWrapper.orderBy(true, false, Card::getId);
        return LambdaQueryWrapper;
    }

    /**
     * 导出卡密
     *
     * @param card
     * @return
     */
    @Override
    public List<Card> exportCard(Card card) {
        List<Card> cardList = cardMapper.selectList(getQwCard(card));
        for (int i = 0; i < cardList.size(); i++) {
            Soft obj = (Soft) redisUtil.get("id:soft:" + cardList.get(i).getFromSoftId());
            cardList.get(i).setFromSoftName(obj.getName());
            cardList.get(i).setStatusName(cardStatus2Str(cardList.get(i).getStatus()));
            cardList.get(i).setAddTimeName(MyUtils.stamp2Date(String.valueOf(cardList.get(i).getAddTime())));
            if (!CheckUtils.isObjectEmpty(cardList.get(i).getLetTime())) {
                cardList.get(i).setLetTimeName(MyUtils.stamp2Date(String.valueOf(cardList.get(i).getLetTime())));
            }
        }
        return cardList;
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
     * 获取卡密列表
     *
     * @param card
     * @param myPage
     * @return
     */
    @Override
    public Result getCardList(Card card, MyPage myPage) {
        Page<Card> page = new Page<>(myPage.getPageIndex(), myPage.getPageSize(), true);
        if (!CheckUtils.isObjectEmpty(myPage.getOrders())) {
            for (int i = 0; i < myPage.getOrders().size(); i++) {
                myPage.getOrders().get(i).setColumn(MyUtils.camelToUnderline(myPage.getOrders().get(i).getColumn()));
            }
            page.setOrders(myPage.getOrders());
        }
        IPage<Card> msgPage = cardMapper.selectPage(page, getQwCard(card));
        for (int i = 0; i < msgPage.getRecords().size(); i++) {
            Soft obj = (Soft) redisUtil.get("id:soft:" + msgPage.getRecords().get(i).getFromSoftId());
            msgPage.getRecords().get(i).setFromSoftName(obj.getName());
            msgPage.getRecords().get(i).setStatusName(cardStatus2Str(msgPage.getRecords().get(i).getStatus()));
        }
        return Result.ok("获取成功", msgPage);
    }

    /**
     * 查询卡密，根据id或者ckey
     *
     * @param card
     * @return
     */
    @Override
    public Result getCard(Card card) {
        LambdaQueryWrapper<Card> cardLambdaQueryWrapper = new LambdaQueryWrapper<>();
        cardLambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(card.getId()), Card::getId, card.getId());
        cardLambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(card.getCkey()), Card::getCkey, card.getCkey());
        Card newCard = cardMapper.selectOne(cardLambdaQueryWrapper);
        if (CheckUtils.isObjectEmpty(newCard)) {
            return Result.error("查询失败，未找到");
        }
        return Result.ok("查询成功", newCard);
    }

    /**
     * 修改卡密
     *
     * @param card
     * @return
     */
    @Override
    public Result updCard(Card card) {
        Card newCard = cardMapper.selectById(card.getId());
        if (CheckUtils.isObjectEmpty(newCard)) {
            return Result.error("卡密ID错误");
        }
        if (newCard.getStatus() == 1) {
            return Result.error("卡密已被使用，无法修改");
        }
        int num = cardMapper.updateById(card);
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
     * @param card
     * @return
     */
    @Override
    public Result addCard(String prefix, Integer count, Card card, Admin admin) {
        Soft soft = softMapper.selectById(card.getFromSoftId());
        if (CheckUtils.isObjectEmpty(soft)) {
            return Result.error("fromSoftId错误");
        }
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
            Card newCard = new Card();
            newCard.setCkey(ckey);
            newCard.setPoint(card.getPoint());
            newCard.setSeconds(card.getSeconds());
            newCard.setAddTime(timeStamp);
            newCard.setStatus(0);
            newCard.setFromSoftId(card.getFromSoftId());
            newCard.setFromAdminId(admin.getId());
            int insert = cardMapper.insert(newCard);
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
    public Result delCard(String ids) {
        String[] idArray = ids.split(",");
        List<String> strings = Arrays.asList(idArray);
        if (idArray.length == 0) {
            return Result.error("ids参数格式可能错误");
        }
        int okCount = cardMapper.deleteBatchIds(strings);
        return Result.ok("成功删除 " + okCount + " 张卡密");
    }

    /**
     * 禁用卡密，支持批量
     *
     * @param ids 多个用英文逗号隔开
     * @return
     */
    @Override
    public Result banCard(String ids) {
        String[] idArray = ids.split(",");
        if (idArray.length == 0) {
            return Result.error("ids参数格式可能错误");
        }
        LambdaUpdateWrapper<Card> wrapper = new LambdaUpdateWrapper();
        wrapper.set(Card::getStatus, CardEnums.STATUS_DISABLE.getCode()).in(Card::getId, Arrays.asList(idArray)).eq(Card::getStatus, CardEnums.STATUS_NOTUSEd.getCode());
        int okCount = cardMapper.update(null, wrapper);
        return Result.ok("成功禁用 " + okCount + " 张卡密");
    }

    /**
     * 解禁卡密，支持批量
     *
     * @param ids 多个用英文逗号隔开
     * @return
     */
    @Override
    public Result unBanCard(String ids) {
        String[] idArray = ids.split(",");
        if (idArray.length == 0) {
            return Result.error("ids参数格式可能错误");
        }
        LambdaUpdateWrapper<Card> wrapper = new LambdaUpdateWrapper();
        wrapper.set(Card::getStatus, CardEnums.STATUS_NOTUSEd.getCode()).in(Card::getId, Arrays.asList(idArray)).eq(Card::getStatus, CardEnums.STATUS_DISABLE.getCode());
        int okCount = cardMapper.update(null, wrapper);
        return Result.ok("成功解禁 " + okCount + " 张卡密");
    }

    /**
     * 获取我的卡密
     *
     * @param card
     * @param myPage
     * @return
     */
    @Override
    public Result getMyCardList(Card card, MyPage myPage, Admin admin) {
        Role role = (Role) redisUtil.get("role:" + admin.getRole());
        if (role.getFromSoftId() == 0) {
            return Result.error("超级管理员无法使用此接口");
        }
        card.setFromAdminId(admin.getId());
        card.setFromSoftId(role.getFromSoftId());
        Page<Card> page = new Page<>(myPage.getPageIndex(), myPage.getPageSize(), true);
        if (!CheckUtils.isObjectEmpty(myPage.getOrders())) {
            for (int i = 0; i < myPage.getOrders().size(); i++) {
                myPage.getOrders().get(i).setColumn(MyUtils.camelToUnderline(myPage.getOrders().get(i).getColumn()));
            }
            page.setOrders(myPage.getOrders());
        }
        IPage<Card> msgPage = cardMapper.selectPage(page, getQwCardMy(card));
        for (int i = 0; i < msgPage.getRecords().size(); i++) {
            Soft obj = (Soft) redisUtil.get("id:soft:" + msgPage.getRecords().get(i).getFromSoftId());
            msgPage.getRecords().get(i).setFromSoftName(obj.getName());
            msgPage.getRecords().get(i).setFromSoftId(null);
            msgPage.getRecords().get(i).setStatusName(cardStatus2Str(msgPage.getRecords().get(i).getStatus()));
        }
        return Result.ok("获取成功", msgPage);
    }

    /**
     * 获取查询条件构造器_我的
     *
     * @param card
     * @return
     */
    public LambdaQueryWrapper<Card> getQwCardMy(Card card) {
        LambdaQueryWrapper<Card> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(card.getCkey()), Card::getCkey, card.getCkey());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(card.getPoint()), Card::getPoint, card.getPoint());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(card.getSeconds()), Card::getSeconds, card.getSeconds());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(card.getAddTime()), Card::getAddTime, card.getAddTime());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(card.getLetTime()), Card::getLetTime, card.getLetTime());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(card.getLetUser()), Card::getLetUser, card.getLetUser());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(card.getStatus()), Card::getStatus, card.getStatus());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(card.getFromAdminId()), Card::getFromAdminId, card.getFromAdminId());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(card.getFromSoftId()), Card::getFromSoftId, card.getFromSoftId());
        LambdaQueryWrapper.orderBy(true, false, Card::getId);
        return LambdaQueryWrapper;
    }

    /**
     * 生成我的卡密
     *
     * @param strategyId
     * @param prefix
     * @param count
     * @param adminC
     * @return
     */
    @Override
    public Result addMyCard(Integer strategyId, String prefix, Integer count, Admin adminC) {
        Role role = (Role) redisUtil.get("role:" + adminC.getRole());
        if (role.getFromSoftId() == 0) {
            return Result.error("超级管理员无法使用此接口");
        }
        Strategy strategy = strategyMapper.selectById(strategyId);
        if (CheckUtils.isObjectEmpty(strategy)) {
            return Result.error("策略不存在");
        }
        if (!strategy.getStatus().equals(1)) {
            return Result.error("策略已被禁用");
        }
        if (!strategy.getFromSoftId().equals(role.getFromSoftId())) {
            return Result.error("非法行为：跨软件使用策略");
        }
        BigDecimal price = new BigDecimal(strategy.getPrice());
        BigDecimal allPrice = price.multiply(new BigDecimal(count));
        Admin admin = adminMapper.selectById(adminC.getId());
        BigDecimal myMoney = new BigDecimal(admin.getMoney());
        if (myMoney.compareTo(allPrice) == -1) {
            return Result.error("余额不足，此操作需要" + allPrice + "元");
        }
        myMoney = myMoney.subtract(allPrice);
        admin.setMoney(String.valueOf(myMoney));
        int num = adminMapper.updateById(admin);
        if (num == 0) {
            return Result.error("扣余额失败");
        }
        Soft obj = (Soft) redisUtil.get("id:soft:" + strategy.getFromSoftId());
        Alog alog = new Alog();
        alog.setMoney("-" + String.valueOf(allPrice));
        alog.setAfterMoney(String.valueOf(myMoney));
        alog.setAdminId(admin.getId());
        alog.setData("生成卡密：" + obj.getName() + "," + strategy.getName() + ",x" + count);
        alog.setType("生成卡密");
        alog.setAddTime(Integer.valueOf(MyUtils.getTimeStamp()));
        alogMapper.insert(alog);
        Card card = new Card();
        if (strategy.getType().equals(1)) {
            card.setSeconds(strategy.getValue());
            card.setPoint(0);
        }
        if (strategy.getType().equals(2)) {
            card.setSeconds(0);
            card.setPoint(strategy.getValue());
        }
        card.setFromSoftId(strategy.getFromSoftId());
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
            Card newCard = new Card();
            newCard.setCkey(ckey);
            newCard.setPoint(card.getPoint());
            newCard.setSeconds(card.getSeconds());
            newCard.setAddTime(timeStamp);
            newCard.setStatus(0);
            newCard.setFromSoftId(card.getFromSoftId());
            newCard.setFromAdminId(admin.getId());
            int insert = cardMapper.insert(newCard);
            okCount = okCount + insert;
        }
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("ckeyStr", outStr);
        return Result.ok("成功生成 " + okCount + " 张卡密", jsonObject);
    }
}
