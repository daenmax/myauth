package cn.daenx.myauth.main.service.impl;

import cn.daenx.myauth.main.entity.Plog;
import cn.daenx.myauth.main.entity.Soft;
import cn.daenx.myauth.main.entity.Version;
import cn.daenx.myauth.util.CheckUtils;
import cn.daenx.myauth.util.MyUtils;
import cn.daenx.myauth.util.RedisUtil;
import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.*;
import cn.daenx.myauth.main.mapper.PlogMapper;
import cn.daenx.myauth.main.service.IPlogService;
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
public class PlogServiceImpl extends ServiceImpl<PlogMapper, Plog> implements IPlogService {
    @Resource
    private PlogMapper plogMapper;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 获取查询条件构造器
     *
     * @param plog
     * @return
     */
    public LambdaQueryWrapper<Plog> getQwPlog(Plog plog) {
        LambdaQueryWrapper<Plog> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(plog.getPoint()), Plog::getPoint, plog.getPoint());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(plog.getAfterPoint()), Plog::getAfterPoint, plog.getAfterPoint());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(plog.getSeconds()), Plog::getSeconds, plog.getSeconds());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(plog.getAfterSeconds()), Plog::getAfterSeconds, plog.getAfterSeconds());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(plog.getFromUser()), Plog::getFromUser, plog.getFromUser());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(plog.getAddTime()), Plog::getAddTime, plog.getAddTime());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(plog.getFromEventId()), Plog::getFromEventId, plog.getFromEventId());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(plog.getFromSoftId()), Plog::getFromSoftId, plog.getFromSoftId());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(plog.getFromVerId()), Plog::getFromVerId, plog.getFromVerId());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(plog.getRemark()), Plog::getRemark, plog.getRemark());
        LambdaQueryWrapper.orderBy(true,false,Plog::getId);
        return LambdaQueryWrapper;
    }

    /**
     * 获取日志列表
     *
     * @param plog
     * @return
     */
    @Override
    public Result getPlogList(Plog plog, MyPage myPage) {
        Page<Plog> page = new Page<>(myPage.getPageIndex(), myPage.getPageSize(), true);
        if (!CheckUtils.isObjectEmpty(myPage.getOrders())) {
            for (int i = 0; i < myPage.getOrders().size(); i++) {
                myPage.getOrders().get(i).setColumn(MyUtils.camelToUnderline(myPage.getOrders().get(i).getColumn()));
            }
            page.setOrders(myPage.getOrders());
        }
        IPage<Plog> msgPage = plogMapper.selectPage(page, getQwPlog(plog));
        for (int i = 0; i < msgPage.getRecords().size(); i++) {
            if(!CheckUtils.isObjectEmpty(msgPage.getRecords().get(i).getFromSoftId())){
                Soft soft = (Soft) redisUtil.get("id:soft:" + msgPage.getRecords().get(i).getFromSoftId());
                msgPage.getRecords().get(i).setFromSoftName(soft.getName());
            }
            if(!CheckUtils.isObjectEmpty(msgPage.getRecords().get(i).getFromVerId())){
                Version version = (Version) redisUtil.get("id:version:" + msgPage.getRecords().get(i).getFromVerId());
                msgPage.getRecords().get(i).setFromVer(version.getVer());
            }
        }
        return Result.ok("获取成功", msgPage);
    }

    /**
     * 删除日志
     *
     * @param plog
     * @return
     */
    @Override
    public Result delPlog(Plog plog) {
        int num = plogMapper.delete(getQwPlog(plog));
        return Result.ok("成功删除 " + num + " 条日志");
    }
}
