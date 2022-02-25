package cn.myauthx.api.main.service;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Card;
import cn.myauthx.api.main.entity.MyPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
public interface ICardService extends IService<Card> {
    /**
     * 导出卡密
     *
     * @param card
     * @return
     */
    List<Card> exportCard(Card card);

    /**
     * 获取卡密列表
     *
     * @param card
     * @param myPage
     * @return
     */
    Result getCardList(Card card, MyPage myPage);

    /**
     * 查询卡密，根据id或者ckey
     *
     * @param card
     * @return
     */
    Result getCard(Card card);

    /**
     * 修改卡密
     *
     * @param card
     * @return
     */
    Result updCard(Card card);

    /**
     * 生成卡密
     *
     * @param prefix 前缀
     * @param count  张数
     * @param card
     * @return
     */
    Result addCard(String prefix, Integer count, Card card);

    /**
     * 删除卡密，支持批量
     *
     * @param ids 多个用英文逗号隔开
     * @return
     */
    Result delCard(String ids);

    /**
     * 禁用卡密，支持批量
     *
     * @param ids 多个用英文逗号隔开
     * @return
     */
    Result banCard(String ids);

    /**
     * 解禁卡密，支持批量
     *
     * @param ids 多个用英文逗号隔开
     * @return
     */
    Result unBanCard(String ids);
}
