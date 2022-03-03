package cn.myauthx.api.main.service;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.MyPage;
import cn.myauthx.api.main.entity.Plog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
public interface IPlogService extends IService<Plog> {
    /**
     * 获取日志列表
     *
     * @param plog
     * @return
     */
    Result getPlogList(Plog plog, MyPage myPage);

    /**
     * 删除日志
     *
     * @param plog
     * @return
     */
    Result delPlog(Plog plog);
}
