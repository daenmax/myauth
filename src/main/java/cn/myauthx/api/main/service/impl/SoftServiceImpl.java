package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.*;
import cn.myauthx.api.main.mapper.*;
import cn.myauthx.api.main.service.ISoftService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.MyUtils;
import cn.myauthx.api.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private SoftMapper softMapper;
    @Autowired
    private VersionMapper versionMapper;
    @Autowired
    private CardMapper cardMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EventMapper eventMapper;
    @Autowired
    private DataMapper dataMapper;
    @Autowired
    private BanMapper banMapper;
    @Autowired
    private JsMapper jsMapper;
    @Autowired
    private MsgMapper msgMapper;
    @Autowired
    private PlogMapper plogMapper;

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

    /**
     * 添加软件
     *
     * @param soft
     * @return
     */
    @Override
    public Result addSoft(Soft soft) {
        soft.setAddTime(Integer.valueOf(MyUtils.getTimeStamp()));
        soft.setSkey(MyUtils.getUUID(true));
        int num = softMapper.insert(soft);
        if(num <= 0){
            return Result.error("添加失败");
        }
        redisUtil.set("soft:" + soft.getSkey(),soft);
        return Result.ok("添加成功");
    }

    /**
     * 修改软件
     *
     * @param soft
     * @return
     */
    @Override
    public Result updSoft(Soft soft) {
        int num = softMapper.updateById(soft);
        if(num <= 0){
            return Result.error("修改失败");
        }
        Soft newSoft = softMapper.selectById(soft.getId());
        redisUtil.set("soft:" + newSoft.getSkey(),newSoft);
        return Result.ok("修改成功");
    }

    /**
     * 删除软件，会同步删除版本、卡密、用户、事件、数据、封禁、JS、回复、日志
     *
     * @param softC
     * @return
     */
    @Override
    public Result delSoft(Soft softC) {
        Soft soft = softMapper.selectById(softC.getId());
        if(CheckUtils.isObjectEmpty(soft)){
            return Result.error("删除失败，id错误");
        }
        //删除软件表
        LambdaQueryWrapper<Soft> softLambdaQueryWrapper = new LambdaQueryWrapper<>();
        softLambdaQueryWrapper.eq(Soft::getId,soft.getId());
        softMapper.delete(softLambdaQueryWrapper);
        //删除版本表
        LambdaQueryWrapper<Version> versionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        versionLambdaQueryWrapper.eq(Version::getFromSoftId,soft.getId());
        versionMapper.delete(versionLambdaQueryWrapper);
        //删除卡密表
        LambdaQueryWrapper<Card> cardLambdaQueryWrapper = new LambdaQueryWrapper<>();
        cardLambdaQueryWrapper.eq(Card::getFromSoftId,soft.getId());
        cardMapper.delete(cardLambdaQueryWrapper);
        //删除用户表
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getFromSoftId,soft.getId());
        userMapper.delete(userLambdaQueryWrapper);
        //删除事件表
        LambdaQueryWrapper<Event> eventLambdaQueryWrapper = new LambdaQueryWrapper<>();
        eventLambdaQueryWrapper.eq(Event::getFromSoftId,soft.getId());
        eventMapper.delete(eventLambdaQueryWrapper);
        //删除数据表
        LambdaQueryWrapper<Data> dataLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dataLambdaQueryWrapper.eq(Data::getFromSoftId,soft.getId());
        dataMapper.delete(dataLambdaQueryWrapper);
        //删除封禁表
        LambdaQueryWrapper<Ban> banLambdaQueryWrapper = new LambdaQueryWrapper<>();
        banLambdaQueryWrapper.eq(Ban::getFromSoftId,soft.getId());
        banMapper.delete(banLambdaQueryWrapper);
        //删除JS表
        LambdaQueryWrapper<Js> jsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        jsLambdaQueryWrapper.eq(Js::getFromSoftId,soft.getId());
        jsMapper.delete(jsLambdaQueryWrapper);
        //删除回复表
        LambdaQueryWrapper<Msg> msgLambdaQueryWrapper = new LambdaQueryWrapper<>();
        msgLambdaQueryWrapper.eq(Msg::getFromSoftId,soft.getId());
        msgMapper.delete(msgLambdaQueryWrapper);
        //删除日志表
        LambdaQueryWrapper<Plog> plogLambdaQueryWrapper = new LambdaQueryWrapper<>();
        plogLambdaQueryWrapper.eq(Plog::getFromSoftId,soft.getId());
        plogMapper.delete(plogLambdaQueryWrapper);
        redisUtil.del("soft:" + soft.getSkey());
        return Result.ok("删除成功");
    }

    /**
     * 获取软件，通过id或者skey
     *
     * @param soft
     * @return
     */
    @Override
    public Result getSoft(Soft soft) {
        Soft newSoft = new Soft();
        newSoft.setId(soft.getId());
        newSoft.setSkey(soft.getSkey());
        LambdaQueryWrapper<Soft> softLambdaQueryWrapper = new LambdaQueryWrapper<>();
        softLambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(soft.getId()),Soft::getId,soft.getId());
        softLambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(soft.getSkey()),Soft::getSkey,soft.getSkey());
        newSoft = softMapper.selectOne(softLambdaQueryWrapper);
        if(CheckUtils.isObjectEmpty(newSoft)){
            return Result.error("查询失败，未找到");
        }
        return Result.ok("查询成功",newSoft);
    }


    /**
     * 获取软件查询条件构造器
     * @param soft
     * @return
     */
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
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(soft.getBindDeviceCode()),Soft::getBindDeviceCode,soft.getBindDeviceCode());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(soft.getHeartTime()),Soft::getHeartTime,soft.getHeartTime());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(soft.getRegister()),Soft::getType,soft.getRegister());
        return LambdaQueryWrapper;
    }
}
