package cn.daenx.myauth.main.service;

import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.main.entity.Soft;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
public interface ISoftService extends IService<Soft> {
    /**
     * 获取软件列表
     *
     * @param soft
     * @param myPage
     * @return
     */
    Result getSoftList(Soft soft, MyPage myPage);

    /**
     * 添加软件
     *
     * @param soft
     * @return
     */
    Result addSoft(Soft soft);

    /**
     * 修改软件
     *
     * @param soft
     * @return
     */
    Result updSoft(Soft soft);

    /**
     * 删除软件，会同步删除版本、卡密、用户、事件、数据、封禁、JS、回复、日志
     *
     * @param soft
     * @return
     */
    Result delSoft(Soft soft);

    /**
     * 获取软件，通过id或者skey
     *
     * @param soft
     * @return
     */
    Result getSoft(Soft soft);

    /**
     * 获取软件列表_全部_简要
     *
     * @param soft
     * @return
     */
    Result getSoftListEx(Soft soft);

    /**
     * 获取软件列表_全部_简要_自助用
     * @param name
     * @return
     */
    Result getSoftListSimple(String name);
}
