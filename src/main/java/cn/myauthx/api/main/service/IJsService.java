package cn.myauthx.api.main.service;

import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Js;
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
public interface IJsService extends IService<Js> {
    Result runJs(Soft soft, String func, String c1, String c2, String c3, String c4, String c5, String c6, String c7, String c8, String c9, String c10);

}
