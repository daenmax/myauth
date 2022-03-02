package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.*;
import cn.myauthx.api.main.mapper.BanMapper;
import cn.myauthx.api.main.mapper.SoftMapper;
import cn.myauthx.api.main.service.IBanService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.MyUtils;
import cn.myauthx.api.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class BanServiceImpl extends ServiceImpl<BanMapper, Ban> implements IBanService {
    @Resource
    private BanMapper banMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private SoftMapper softMapper;

    /**
     * 获取查询条件构造器
     *
     * @param ban
     * @return
     */
    public LambdaQueryWrapper<Ban> getQwBan(Ban ban) {
        LambdaQueryWrapper<Ban> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(ban.getValue()), Ban::getValue, ban.getValue());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(ban.getAddTime()), Ban::getAddTime, ban.getAddTime());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(ban.getToTime()), Ban::getToTime, ban.getToTime());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(ban.getAddTime()), Ban::getAddTime, ban.getAddTime());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(ban.getWhy()), Ban::getWhy, ban.getWhy());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(ban.getFromSoftId()), Ban::getFromSoftId, ban.getFromSoftId());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(ban.getType()), Ban::getType, ban.getType());
        return LambdaQueryWrapper;
    }

    /**
     * 获取封禁列表
     *
     * @param ban
     * @param myPage
     * @return
     */
    @Override
    public Result getBanList(Ban ban, MyPage myPage) {
        Page<Ban> page = new Page<>(myPage.getPageIndex(), myPage.getPageSize(), true);
        if (!CheckUtils.isObjectEmpty(myPage.getOrders())) {
            for (int i = 0; i < myPage.getOrders().size(); i++) {
                myPage.getOrders().get(i).setColumn(MyUtils.camelToUnderline(myPage.getOrders().get(i).getColumn()));
            }
            page.setOrders(myPage.getOrders());
        }
        IPage<Ban> msgPage = banMapper.selectPage(page, getQwBan(ban));
        for (int i = 0; i < msgPage.getRecords().size(); i++) {
            Soft obj = (Soft) redisUtil.get("id:soft:" + msgPage.getRecords().get(i).getFromSoftId());
            msgPage.getRecords().get(i).setFromSoftName(obj.getName());
        }
        return Result.ok("获取成功", msgPage);
    }

    /**
     * 查询封禁，根据id
     *
     * @param ban
     * @return
     */
    @Override
    public Result getBan(Ban ban) {
        Ban newBan = banMapper.selectById(ban.getId());
        if (CheckUtils.isObjectEmpty(newBan)) {
            return Result.error("查询失败，未找到");
        }
        return Result.ok("查询成功", newBan);
    }

    /**
     * 修改封禁
     *
     * @param ban
     * @return
     */
    @Override
    public Result updBan(Ban ban) {
        Ban newBan = banMapper.selectById(ban.getId());
        if (CheckUtils.isObjectEmpty(newBan)) {
            return Result.error("封禁ID错误");
        }
        int num = banMapper.updateById(ban);
        if (num <= 0) {
            return Result.error("修改失败");
        }
        Ban ban2 = banMapper.selectById(ban.getId());
        redisUtil.del("ban:" + newBan.getValue() + "-" + newBan.getType() + "-" + newBan.getFromSoftId());
        redisUtil.set("ban:" + ban2.getValue() + "-" + ban2.getType() + "-" + ban2.getFromSoftId(),ban);
        return Result.ok("修改成功");
    }

    /**
     * 添加封禁
     *
     * @param ban
     * @return
     */
    @Override
    public Result addBan(Ban ban) {
        Soft soft = softMapper.selectById(ban.getFromSoftId());
        if (CheckUtils.isObjectEmpty(soft)) {
            return Result.error("fromSoftId错误");
        }
        LambdaQueryWrapper<Ban> banLambdaQueryWrapper = new LambdaQueryWrapper<>();
        banLambdaQueryWrapper.eq(Ban::getValue, ban.getValue());
        banLambdaQueryWrapper.eq(Ban::getFromSoftId, ban.getFromSoftId());
        banLambdaQueryWrapper.eq(Ban::getType, ban.getType());
        Long selectCount = banMapper.selectCount(banLambdaQueryWrapper);
        if (selectCount > 0) {
            return Result.error("在该软件下该封禁类型中，封禁对象已存在");
        }
        ban.setAddTime(Integer.valueOf(MyUtils.getTimeStamp()));
        int num = banMapper.insert(ban);
        if (num <= 0) {
            return Result.error("添加失败");
        }
        redisUtil.set("ban:" + ban.getValue() + "-" + ban.getType() + "-" + soft.getId(),ban);
        return Result.ok("添加成功");
    }

    /**
     * 删除封禁（支持批量）
     *
     * @param ids
     * @return
     */
    @Override
    public Result delBan(String ids) {
        String[] idArray = ids.split(",");
        int okCount = 0;
        for (String id : idArray) {
            Ban newBan = banMapper.selectById(id);
            if (!CheckUtils.isObjectEmpty(newBan)) {
                redisUtil.del("ban:" + newBan.getValue() + "-" + newBan.getType() + "-" + newBan.getFromSoftId());
                int num = banMapper.deleteById(newBan.getId());
                okCount = okCount + num;
            }
        }
        return Result.ok("成功删除 " + okCount + " 个封禁");
    }
}
