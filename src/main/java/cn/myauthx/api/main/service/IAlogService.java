package cn.myauthx.api.main.service;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Admin;
import cn.myauthx.api.main.entity.Alog;
import cn.myauthx.api.base.vo.MyPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author DaenMax
 * @since 2022-03-10
 */
public interface IAlogService extends IService<Alog> {
    /**
     * 获取日志列表
     *
     * @param alog
     * @return
     */
    Result getAlogList(Alog alog, MyPage myPage);

    /**
     * 删除日志
     *
     * @param alog
     * @return
     */
    Result delAlog(Alog alog);

    /**
     * 获取我的余额日志
     *
     * @param alog
     * @param myPage
     * @param admin
     * @return
     */
    Result getMyAlogList(Alog alog, MyPage myPage, Admin admin);
}
