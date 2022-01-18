package cn.myauthx.api.main.service;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.entity.Version;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
public interface IVersionService extends IService<Version> {
    /**
     * 检测版本更新，根据vkey
     * @param versionC
     * @param soft
     * @return
     */
    Result checkUpdate(Version versionC, Soft soft);
}
