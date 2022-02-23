package cn.myauthx.api.main.service;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Msg;
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
public interface IMsgService extends IService<Msg> {
    /**
     * 获取回复列表
     *
     * @param msg
     * @param myPage
     * @return
     */
    Result getMsgList(Msg msg, MyPage myPage);

    /**
     * 获取回复，根据ID
     *
     * @param msg
     * @return
     */
    Result getMsg(Msg msg);

    /**
     * 修改回复
     *
     * @param msg
     * @return
     */
    Result updMsg(Msg msg);

    /**
     * 添加回复
     *
     * @param msg
     * @return
     */
    Result addMsg(Msg msg);

    /**
     * 删除回复
     *
     * @param msg
     * @return
     */
    Result delMsg(Msg msg);
}
