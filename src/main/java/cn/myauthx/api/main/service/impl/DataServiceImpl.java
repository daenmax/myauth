package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Data;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.entity.Version;
import cn.myauthx.api.main.mapper.DataMapper;
import cn.myauthx.api.main.service.IDataService;
import cn.myauthx.api.util.MyUtils;
import com.alibaba.fastjson.JSONObject;
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
public class DataServiceImpl extends ServiceImpl<DataMapper, Data> implements IDataService {
    @Autowired
    private DataMapper dataMapper;
    @Override
    public Result upData(String type, String content, String ip,String device_info, String device_code,  Soft soft, Version version) {
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
        jsonObject.put("ip",ip);
        if(num > 0){
            return Result.ok("上报成功",jsonObject);
        }
        return Result.error("上报失败");
    }
}
