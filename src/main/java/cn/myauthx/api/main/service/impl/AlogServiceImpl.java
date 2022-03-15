package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.*;
import cn.myauthx.api.main.mapper.AlogMapper;
import cn.myauthx.api.main.mapper.PlogMapper;
import cn.myauthx.api.main.service.IAlogService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.MyUtils;
import cn.myauthx.api.util.RedisUtil;
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
 * @since 2022-03-10
 */
@Service
public class AlogServiceImpl extends ServiceImpl<AlogMapper, Alog> implements IAlogService {
    @Resource
    private AlogMapper alogMapper;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 获取查询条件构造器
     *
     * @param alog
     * @return
     */
    public LambdaQueryWrapper<Alog> getQwAlog(Alog alog) {
        LambdaQueryWrapper<Alog> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(alog.getMoney()), Alog::getMoney, alog.getMoney());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(alog.getAfterMoney()), Alog::getAfterMoney, alog.getAfterMoney());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(alog.getAdminId()), Alog::getAdminId, alog.getAdminId());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(alog.getData()), Alog::getData, alog.getData());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(alog.getType()), Alog::getType, alog.getType());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(alog.getAddTime()), Alog::getAddTime, alog.getAddTime());
        LambdaQueryWrapper.orderBy(true, false, Alog::getId);
        return LambdaQueryWrapper;
    }

    /**
     * 获取日志列表
     *
     * @param alog
     * @return
     */
    @Override
    public Result getAlogList(Alog alog, MyPage myPage) {
        Page<Alog> page = new Page<>(myPage.getPageIndex(), myPage.getPageSize(), true);
        if (!CheckUtils.isObjectEmpty(myPage.getOrders())) {
            for (int i = 0; i < myPage.getOrders().size(); i++) {
                myPage.getOrders().get(i).setColumn(MyUtils.camelToUnderline(myPage.getOrders().get(i).getColumn()));
            }
            page.setOrders(myPage.getOrders());
        }
        IPage<Alog> msgPage = alogMapper.selectPage(page, getQwAlog(alog));
        return Result.ok("获取成功", msgPage);
    }

    /**
     * 删除日志
     *
     * @param alog
     * @return
     */
    @Override
    public Result delAlog(Alog alog) {
        int num = alogMapper.delete(getQwAlog(alog));
        return Result.ok("成功删除 " + num + " 条日志");
    }


    /**
     * 获取查询条件构造器_我的
     *
     * @param alog
     * @return
     */
    public LambdaQueryWrapper<Alog> getQwAlogMy(Alog alog) {
        LambdaQueryWrapper<Alog> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(alog.getMoney()), Alog::getMoney, alog.getMoney());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(alog.getAfterMoney()), Alog::getAfterMoney, alog.getAfterMoney());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(alog.getAdminId()), Alog::getAdminId, alog.getAdminId());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(alog.getData()), Alog::getData, alog.getData());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(alog.getType()), Alog::getType, alog.getType());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(alog.getAddTime()), Alog::getAddTime, alog.getAddTime());
        LambdaQueryWrapper.orderBy(true, false, Alog::getId);
        return LambdaQueryWrapper;
    }

    /**
     * 获取我的余额日志
     *
     * @param alog
     * @return
     */
    @Override
    public Result getMyAlogList(Alog alog, MyPage myPage, Admin admin) {
        Role role = (Role) redisUtil.get("role:" + admin.getRole());
        if (role.getFromSoftId() == 0) {
            return Result.error("超级管理员无法使用此接口");
        }
        alog.setAdminId(admin.getId());
        Page<Alog> page = new Page<>(myPage.getPageIndex(), myPage.getPageSize(), true);
        if (!CheckUtils.isObjectEmpty(myPage.getOrders())) {
            for (int i = 0; i < myPage.getOrders().size(); i++) {
                myPage.getOrders().get(i).setColumn(MyUtils.camelToUnderline(myPage.getOrders().get(i).getColumn()));
            }
            page.setOrders(myPage.getOrders());
        }
        IPage<Alog> msgPage = alogMapper.selectPage(page, getQwAlogMy(alog));
        return Result.ok("获取成功", msgPage);
    }
}
