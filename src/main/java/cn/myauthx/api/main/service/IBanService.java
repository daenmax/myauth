package cn.myauthx.api.main.service;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Ban;
import cn.myauthx.api.main.entity.MyPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
public interface IBanService extends IService<Ban> {
    /**
     * 获取封禁列表
     *
     * @param ban
     * @param myPage
     * @return
     */
    Result getBanList(Ban ban, MyPage myPage);

    /**
     * 查询封禁，根据id
     *
     * @param ban
     * @return
     */
    Result getBan(Ban ban);

    /**
     * 修改封禁
     *
     * @param ban
     * @return
     */
    Result updBan(Ban ban);

    /**
     * 添加封禁
     *
     * @param ban
     * @return
     */
    Result addBan(Ban ban);

    /**
     * 删除封禁（支持批量）
     * @param ids
     * @return
     */
    Result delBan(String ids);
}
