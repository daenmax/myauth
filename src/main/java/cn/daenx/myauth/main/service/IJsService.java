package cn.daenx.myauth.main.service;

import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.Js;
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
public interface IJsService extends IService<Js> {
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
    Result runJs(Soft soft, String func, String c1, String c2, String c3, String c4, String c5, String c6, String c7, String c8, String c9, String c10);

    /**
     * 获取函数列表
     *
     * @param js
     * @param myPage
     * @return
     */
    Result getJsList(Js js, MyPage myPage);

    /**
     * 查询函数，根据id
     *
     * @param js
     * @return
     */
    Result getJs(Js js);

    /**
     * 修改函数
     *
     * @param js
     * @return
     */
    Result updJs(Js js);

    /**
     * 添加函数
     *
     * @param js
     * @return
     */
    Result addJs(Js js);

    /**
     * 删除函数
     * @param js
     * @return
     */
    Result delJs(Js js);

    /**
     * 运行Js，网页接口用
     *
     * @param js
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
    Result runJsWeb(Js js, String c1, String c2, String c3, String c4, String c5, String c6, String c7, String c8, String c9, String c10);

    /**
     * 调试Js，网页接口用
     *
     * @param js
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
    Result testJsWeb(Js js, String c1, String c2, String c3, String c4, String c5, String c6, String c7, String c8, String c9, String c10);
}
