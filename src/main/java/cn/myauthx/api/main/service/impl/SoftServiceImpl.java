package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.MyPage;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.mapper.AdminMapper;
import cn.myauthx.api.main.mapper.SoftMapper;
import cn.myauthx.api.main.service.ISoftService;
import cn.myauthx.api.util.CheckUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
@Service
public class SoftServiceImpl extends ServiceImpl<SoftMapper, Soft> implements ISoftService {
    @Autowired
    private SoftMapper softMapper;

    /**
     * 获取软件列表
     * @param soft
     * @param myPage
     * @return
     */
    @Override
    public Result getSoftList(Soft soft, MyPage myPage) {
        Page<Soft> page = new Page<>(myPage.getPageIndex(),myPage.getPageSize(),true);
        IPage<Soft> softPage = softMapper.selectPage(page, getQwSoft(soft));
        return Result.ok("获取成功",softPage);
    }

    public LambdaQueryWrapper<Soft> getQwSoft(Soft soft){
        LambdaQueryWrapper<Soft> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(soft.getId()),Soft::getId,soft.getId());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(soft.getSkey()),Soft::getSkey,soft.getSkey());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(soft.getName()),Soft::getName,soft.getName());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(soft.getStatus()),Soft::getStatus,soft.getStatus());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(soft.getType()),Soft::getType,soft.getType());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(soft.getAddTime()),Soft::getAddTime,soft.getAddTime());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(soft.getGenKey()),Soft::getGenKey,soft.getGenKey());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(soft.getGenStatus()),Soft::getGenStatus,soft.getGenStatus());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(soft.getBatchSoft()),Soft::getBatchSoft,soft.getBatchSoft());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(soft.getMultipleLogin()),Soft::getMultipleLogin,soft.getMultipleLogin());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(soft.getHeartTime()),Soft::getHeartTime,soft.getHeartTime());
        return LambdaQueryWrapper;
    }
}
