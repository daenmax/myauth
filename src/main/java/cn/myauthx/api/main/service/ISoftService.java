package cn.myauthx.api.main.service;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.MyPage;
import cn.myauthx.api.main.entity.Soft;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
public interface ISoftService extends IService<Soft> {
    /**
     * 获取软件列表
     * @param soft
     * @param myPage
     * @return
     */
    Result getSoftList(Soft soft, MyPage myPage);

}
