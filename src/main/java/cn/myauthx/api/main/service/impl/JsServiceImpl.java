package cn.myauthx.api.main.service.impl;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.*;
import cn.myauthx.api.main.enums.JsEnums;
import cn.myauthx.api.main.mapper.JsMapper;
import cn.myauthx.api.main.mapper.SoftMapper;
import cn.myauthx.api.main.service.IJsService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.MyUtils;
import cn.myauthx.api.util.RedisUtil;
import cn.myauthx.api.util.UnderlineToCamelUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class JsServiceImpl extends ServiceImpl<JsMapper, Js> implements IJsService {
    @Resource
    private JsMapper jsMapper;

    @Resource
    private SoftMapper softMapper;

    @Resource
    private RedisUtil redisUtil;


    /**
     * 运行Js
     *
     * @param soft
     * @param func
     * @param c1
     * @param c2
     * @param c3
     * @param c4
     * @param c5
     * @param c6
     * @param c7
     * @param c8
     * @param c9
     * @param c10
     * @return
     */
    @Override
    public Result runJs(Soft soft, String func, String c1, String c2, String c3, String c4, String c5, String c6, String c7, String c8, String c9, String c10) {
        LambdaQueryWrapper<Js> jsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        jsLambdaQueryWrapper.eq(Js::getFromSoftId, soft.getId());
        jsLambdaQueryWrapper.eq(Js::getJsFun, func);
        Js js = jsMapper.selectOne(jsLambdaQueryWrapper);
        if (CheckUtils.isObjectEmpty(js)) {
            return Result.error("js函数未找到");
        }
        if (js.getStatus().equals(JsEnums.STATUS_DISABLE.getCode())) {
            return Result.error("js函数已被禁用");
        }
        String ret = MyUtils.runJs(js.getJsContent(), js.getJsFun(), c1, c2, c3, c4, c5, c6, c7, c8, c9, c10);
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("func", func);
        jsonObject.put("ret", ret);
        return Result.ok("js函数执行成功", jsonObject);
    }

    /**
     * 获取查询条件构造器
     *
     * @param js
     * @return
     */
    public LambdaQueryWrapper<Js> getQwJs(Js js) {
        LambdaQueryWrapper<Js> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(js.getJsFun()), Js::getJsFun, js.getJsFun());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(js.getAddTime()), Js::getAddTime, js.getAddTime());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(js.getStatus()), Js::getStatus, js.getStatus());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(js.getFromSoftId()), Js::getFromSoftId, js.getFromSoftId());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(js.getRemark()), Js::getRemark, js.getRemark());
        return LambdaQueryWrapper;
    }

    /**
     * 获取函数列表
     *
     * @param js
     * @param myPage
     * @return
     */
    @Override
    public Result getJsList(Js js, MyPage myPage) {
        Page<Js> page = new Page<>(myPage.getPageIndex(), myPage.getPageSize(), true);
        if (!CheckUtils.isObjectEmpty(myPage.getOrders())) {
            for (int i = 0; i < myPage.getOrders().size(); i++) {
                myPage.getOrders().get(i).setColumn(UnderlineToCamelUtils.camelToUnderline(myPage.getOrders().get(i).getColumn()));
            }
            page.setOrders(myPage.getOrders());
        }
        IPage<Js> msgPage = jsMapper.selectPage(page, getQwJs(js));
        for (int i = 0; i < msgPage.getRecords().size(); i++) {
            Soft obj = (Soft) redisUtil.get("id:soft:" + msgPage.getRecords().get(i).getFromSoftId());
            msgPage.getRecords().get(i).setFromSoftName(obj.getName());
        }
        return Result.ok("获取成功", msgPage);
    }

    /**
     * 获取函数，根据id
     *
     * @param js
     * @return
     */
    @Override
    public Result getJs(Js js) {
        Js newJs = jsMapper.selectById(js.getId());
        if (CheckUtils.isObjectEmpty(newJs)) {
            return Result.error("查询失败，未找到");
        }
        return Result.ok("查询成功", newJs);
    }

    /**
     * 修改函数
     *
     * @param js
     * @return
     */
    @Override
    public Result updJs(Js js) {
        LambdaQueryWrapper<Js> jsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        jsLambdaQueryWrapper.eq(Js::getFromSoftId,js.getFromSoftId());
        jsLambdaQueryWrapper.eq(Js::getJsFun,js.getJsFun());
        List<Js> jsList = jsMapper.selectList(jsLambdaQueryWrapper);
        if(jsList.size()>0){
            return Result.error("函数名在当前软件中已存在");
        }
        js.setFromSoftId(null);
        int num = jsMapper.updateById(js);
        if (num <= 0) {
            return Result.error("修改失败");
        }
        return Result.ok("修改成功");
    }

    /**
     * 添加函数
     *
     * @param js
     * @return
     */
    @Override
    public Result addJs(Js js) {
        Soft soft = softMapper.selectById(js.getFromSoftId());
        if (CheckUtils.isObjectEmpty(soft)) {
            return Result.error("fromSoftId错误");
        }
        LambdaQueryWrapper<Js> jsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        jsLambdaQueryWrapper.eq(Js::getFromSoftId, js.getFromSoftId());
        jsLambdaQueryWrapper.eq(Js::getJsFun, js.getJsFun());
        Js js1 = jsMapper.selectOne(jsLambdaQueryWrapper);
        if (!CheckUtils.isObjectEmpty(js1)) {
            return Result.error("函数名在当前软件中已存在");
        }
        js.setAddTime(Integer.valueOf(MyUtils.getTimeStamp()));
        int num = jsMapper.insert(js);
        if (num <= 0) {
            return Result.error("添加失败");
        }
        return Result.ok("添加成功");
    }

    /**
     * 删除函数
     *
     * @param jsC
     * @return
     */
    @Override
    public Result delJs(Js jsC) {
        Js js = jsMapper.selectById(jsC.getId());
        if(CheckUtils.isObjectEmpty(js)){
            return Result.error("删除失败，id错误");
        }
        int num = jsMapper.deleteById(jsC.getId());
        if (num <= 0) {
            return Result.error("删除失败");
        }
        return Result.ok("删除成功");
    }
}
