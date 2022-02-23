package cn.myauthx.api.main.service;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Msg;
import cn.myauthx.api.main.entity.MyPage;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.entity.Version;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
public interface IVersionService extends IService<Version> {
    /**
     * 检测版本更新，根据vkey
     *
     * @param versionC
     * @param soft
     * @return
     */
    Result checkUpdate(Version versionC, Soft soft);

    /**
     * 获取最新的一个版本
     *
     * @param soft
     * @return
     */
    Result getNewVersion(Soft soft);

    /**
     * 获取版本列表
     *
     * @param versionC
     * @param myPage
     * @return
     */
    Result getVersionList(Version versionC, MyPage myPage);

    /**
     * 获取版本，通过id或者vkey
     *
     * @param version
     * @return
     */
    Result getVersion(Version version);

    /**
     * 修改版本
     *
     * @param version
     * @return
     */
    Result updVersion(Version version);

    /**
     * 添加版本
     *
     * @param version
     * @return
     */
    Result addVersion(Version version);

    /**
     * 添加版本_同时添加回复
     *
     * @param version
     * @return
     */
    Result addVersionAndMsg(Version version, Msg msg);

    /**
     * 删除版本，会同步删除用户、数据、回复、日志
     *
     * @param version
     * @return
     */
    Result delVersion(Version version);

    /**
     * 获取版本列表_全部_简要
     *
     * @param versionC
     * @return
     */
    Result getVersionListEx(Version versionC);
}
