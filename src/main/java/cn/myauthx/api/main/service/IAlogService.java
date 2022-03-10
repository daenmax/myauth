package cn.myauthx.api.main.service;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Alog;
import cn.myauthx.api.main.entity.MyPage;
import cn.myauthx.api.main.entity.Plog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
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
}
