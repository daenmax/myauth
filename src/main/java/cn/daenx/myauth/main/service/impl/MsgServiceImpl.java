package cn.daenx.myauth.main.service.impl;

import cn.daenx.myauth.util.CheckUtils;
import cn.daenx.myauth.util.MyUtils;
import cn.daenx.myauth.util.RedisUtil;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.Msg;
import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.main.entity.Soft;
import cn.daenx.myauth.main.entity.Version;
import cn.daenx.myauth.main.mapper.MsgMapper;
import cn.daenx.myauth.main.mapper.SoftMapper;
import cn.daenx.myauth.main.mapper.VersionMapper;
import cn.daenx.myauth.main.service.IMsgService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public class MsgServiceImpl extends ServiceImpl<MsgMapper, Msg> implements IMsgService {
    @Resource
    private MsgMapper msgMapper;
    @Resource
    private SoftMapper softMapper;
    @Resource
    private VersionMapper versionMapper;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 获取查询条件构造器
     *
     * @param msg
     * @return
     */
    public LambdaQueryWrapper<Msg> getQwMsg(Msg msg) {
        LambdaQueryWrapper<Msg> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(msg.getKeyword()), Msg::getKeyword, msg.getKeyword());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(msg.getFromSoftId()), Msg::getFromSoftId, msg.getFromSoftId());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(msg.getFromVerId()), Msg::getFromVerId, msg.getFromVerId());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(msg.getStatus()), Msg::getStatus, msg.getStatus());
        return LambdaQueryWrapper;
    }

    /**
     * 获取回复列表
     *
     * @param msg
     * @param myPage
     * @return
     */
    @Override
    public Result getMsgList(Msg msg, MyPage myPage) {
        Page<Msg> page = new Page<>(myPage.getPageIndex(), myPage.getPageSize(), true);
        if (!CheckUtils.isObjectEmpty(myPage.getOrders())) {
            for (int i = 0; i < myPage.getOrders().size(); i++) {
                myPage.getOrders().get(i).setColumn(MyUtils.camelToUnderline(myPage.getOrders().get(i).getColumn()));
            }
            page.setOrders(myPage.getOrders());
        }
        IPage<Msg> msgPage = msgMapper.selectPage(page, getQwMsg(msg));
        for (int i = 0; i < msgPage.getRecords().size(); i++) {
            Soft obj = (Soft) redisUtil.get("id:soft:" + msgPage.getRecords().get(i).getFromSoftId());
            if (!CheckUtils.isObjectEmpty(obj)) {
                msgPage.getRecords().get(i).setFromSoftName(obj.getName());
            }
            Version obj2 = (Version) redisUtil.get("id:version:" + msgPage.getRecords().get(i).getFromVerId());
            if (!CheckUtils.isObjectEmpty(obj2)) {
                msgPage.getRecords().get(i).setFromVer(obj2.getVer());
            }
        }
        return Result.ok("获取成功", msgPage);
    }

    /**
     * 获取回复，根据ID
     *
     * @param msg
     * @return
     */
    @Override
    public Result getMsg(Msg msg) {
        Msg newMsg = msgMapper.selectById(msg.getId());
        if (CheckUtils.isObjectEmpty(newMsg)) {
            return Result.error("查询失败，未找到");
        }
        return Result.ok("查询成功", newMsg);
    }

    /**
     * 修改回复
     *
     * @param msg
     * @return
     */
    @Override
    public Result updMsg(Msg msg) {
        int num = msgMapper.updateById(msg);
        if (num <= 0) {
            return Result.error("修改失败");
        }
        return Result.ok("修改成功");
    }

    /**
     * 添加回复
     *
     * @param msg
     * @return
     */
    @Override
    public Result addMsg(Msg msg) {
        Soft soft = softMapper.selectById(msg.getFromSoftId());
        if (CheckUtils.isObjectEmpty(soft)) {
            return Result.error("fromSoftId错误");
        }
        if (!CheckUtils.isObjectEmpty(msg.getFromVerId())) {
            Version version = versionMapper.selectById(msg.getFromVerId());
            if (CheckUtils.isObjectEmpty(version)) {
                return Result.error("fromVerId错误");
            }
        }
        LambdaQueryWrapper<Msg> msgLambdaQueryWrapper = new LambdaQueryWrapper<>();
        msgLambdaQueryWrapper.eq(Msg::getKeyword, msg.getKeyword());
        msgLambdaQueryWrapper.eq(Msg::getFromSoftId, msg.getFromSoftId());
        if (!CheckUtils.isObjectEmpty(msg.getFromVerId())) {
            msgLambdaQueryWrapper.eq(Msg::getFromVerId, msg.getFromVerId());
        }
        Long selectCount = msgMapper.selectCount(msgLambdaQueryWrapper);
        if (selectCount > 0) {
            return Result.error("关键词已存在");
        }
        int num = msgMapper.insert(msg);
        if (num <= 0) {
            return Result.error("添加失败");
        }
        return Result.ok("添加成功");
    }

    /**
     * 删除回复
     *
     * @param msgC
     * @return
     */
    @Override
    public Result delMsg(Msg msgC) {
        Msg msg = msgMapper.selectById(msgC.getId());
        if (CheckUtils.isObjectEmpty(msg)) {
            return Result.error("删除失败，id错误");
        }
        msgMapper.deleteById(msg);
        return Result.ok("删除成功");
    }
}
