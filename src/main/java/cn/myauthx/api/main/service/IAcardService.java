package cn.myauthx.api.main.service;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Acard;
import cn.myauthx.api.main.entity.Admin;
import cn.myauthx.api.base.vo.MyPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author DaenMax
 * @since 2022-03-16
 */
public interface IAcardService extends IService<Acard> {
    /**
     * 获取卡密列表
     *
     * @param acard
     * @param myPage
     * @return
     */
    Result getACardList(Acard acard, MyPage myPage);

    /**
     * 查询卡密，根据id或者ckey
     *
     * @param acard
     * @return
     */
    Result getACard(Acard acard);

    /**
     * 修改卡密
     *
     * @param acard
     * @return
     */
    Result updACard(Acard acard);

    /**
     * 生成卡密
     *
     * @param prefix 前缀
     * @param count  张数
     * @param acard
     * @return
     */
    Result addACard(String prefix, Integer count, Acard acard, Admin admin);

    /**
     * 删除卡密，支持批量
     *
     * @param ids 多个用英文逗号隔开
     * @return
     */
    Result delACard(String ids);

    /**
     * 禁用卡密，支持批量
     *
     * @param ids 多个用英文逗号隔开
     * @return
     */
    Result banACard(String ids);

    /**
     * 解禁卡密，支持批量
     *
     * @param ids 多个用英文逗号隔开
     * @return
     */
    Result unBanACard(String ids);

    /**
     * 导出卡密
     *
     * @param acard
     * @return
     */
    List<Acard> exportACard(Acard acard);

    /**
     * 使用代理卡密
     *
     * @param ckey
     * @param admin
     * @return
     */
    Result letACard(String ckey, Admin admin);
}
