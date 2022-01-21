package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Js;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.enums.JsEnums;
import cn.myauthx.api.main.mapper.JsMapper;
import cn.myauthx.api.main.service.IJsService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.MyUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
public class JsServiceImpl extends ServiceImpl<JsMapper, Js> implements IJsService {
    @Autowired
    private JsMapper jsMapper;
    @Override
    public Result runJs(Soft soft, String func, String c1, String c2, String c3, String c4, String c5, String c6, String c7, String c8, String c9, String c10) {
        LambdaQueryWrapper<Js> jsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        jsLambdaQueryWrapper.eq(Js::getFromSoftId,soft.getId());
        jsLambdaQueryWrapper.eq(Js::getJsFun,func);
        Js js = jsMapper.selectOne(jsLambdaQueryWrapper);
        if(CheckUtils.isObjectEmpty(js)){
            return Result.error("js函数未找到");
        }
        if(js.getStatus().equals(JsEnums.STATUS_DISABLE.getCode())){
            return Result.error("js函数已被禁用");
        }
        String ret = MyUtils.runJs(js.getJsContent(),js.getJsFun(),c1,c2,c3,c4,c5,c6,c7,c8,c9,c10);
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("func",func);
        jsonObject.put("ret",ret);
        return Result.ok("js函数执行成功",jsonObject);
    }
}
