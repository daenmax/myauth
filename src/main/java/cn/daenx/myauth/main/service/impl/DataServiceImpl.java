package cn.daenx.myauth.main.service.impl;

import cn.daenx.myauth.main.entity.Data;
import cn.daenx.myauth.main.entity.Soft;
import cn.daenx.myauth.main.entity.Version;
import cn.daenx.myauth.main.mapper.DataMapper;
import cn.daenx.myauth.util.CheckUtils;
import cn.daenx.myauth.util.MyUtils;
import cn.daenx.myauth.util.RedisUtil;
import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.*;
import cn.daenx.myauth.main.service.IDataService;
import com.alibaba.fastjson.JSONObject;
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
public class DataServiceImpl extends ServiceImpl<DataMapper, Data> implements IDataService {
    @Resource
    private DataMapper dataMapper;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 上报数据
     *
     * @param type
     * @param content
     * @param ip
     * @param device_info
     * @param device_code
     * @param soft
     * @param version
     * @return
     */
    @Override
    public Result upData(String type, String content, String ip, String device_info, String device_code, Soft soft, Version version) {
        Data data = new Data();
        data.setType(type);
        data.setContent(content);
        data.setIp(ip);
        data.setAddTime(Integer.valueOf(MyUtils.getTimeStamp()));
        data.setFromSoftId(soft.getId());
        data.setFromVerId(version.getId());
        data.setDeviceInfo(device_info);
        data.setDeviceCode(device_code);
        int num = dataMapper.insert(data);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ip", ip);
        if (num > 0) {
            return Result.ok("上报成功", jsonObject);
        }
        return Result.error("上报失败");
    }

    /**
     * 获取查询条件构造器
     *
     * @param data
     * @return
     */
    public LambdaQueryWrapper<Data> getQwData(Data data) {
        LambdaQueryWrapper<Data> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(data.getType()), Data::getType, data.getType());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(data.getContent()), Data::getContent, data.getContent());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(data.getIp()), Data::getIp, data.getIp());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(data.getAddTime()), Data::getAddTime, data.getAddTime());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(data.getFromSoftId()), Data::getFromSoftId, data.getFromSoftId());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(data.getFromVerId()), Data::getFromVerId, data.getFromVerId());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(data.getDeviceInfo()), Data::getDeviceInfo, data.getDeviceInfo());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(data.getDeviceCode()), Data::getDeviceCode, data.getDeviceCode());
        return LambdaQueryWrapper;
    }

    /**
     * 获取数据列表
     *
     * @param data
     * @param myPage
     * @return
     */
    @Override
    public Result getDataList(Data data, MyPage myPage) {
        Page<Data> page = new Page<>(myPage.getPageIndex(), myPage.getPageSize(), true);
        if (!CheckUtils.isObjectEmpty(myPage.getOrders())) {
            for (int i = 0; i < myPage.getOrders().size(); i++) {
                myPage.getOrders().get(i).setColumn(MyUtils.camelToUnderline(myPage.getOrders().get(i).getColumn()));
            }
            page.setOrders(myPage.getOrders());
        }
        IPage<Data> msgPage = dataMapper.selectPage(page, getQwData(data));
        for (int i = 0; i < msgPage.getRecords().size(); i++) {
            Soft soft = (Soft) redisUtil.get("id:soft:" + msgPage.getRecords().get(i).getFromSoftId());
            msgPage.getRecords().get(i).setFromSoftName(soft.getName());
            Version version = (Version) redisUtil.get("id:version:" + msgPage.getRecords().get(i).getFromVerId());
            msgPage.getRecords().get(i).setFromVer(version.getVer());
        }
        return Result.ok("获取成功", msgPage);
    }

    /**
     * 删除数据
     *
     * @param data
     * @return
     */
    @Override
    public Result delData(Data data) {
        int num = dataMapper.delete(getQwData(data));
        return Result.ok("成功删除 " + num + " 条数据");
    }
}
