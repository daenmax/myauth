package cn.myauthx.api.main.entity;

import cn.myauthx.api.base.po.baseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author DaenMax
 * @since 2022-03-10
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("ma_alog")
public class Alog extends baseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 变动余额，负数为扣除
     */
    private String money;

    /**
     * 变动后的余额
     */
    private String afterMoney;

    /**
     * 管理员ID
     */
    private Integer adminId;

    /**
     * 可以为管理员ID、生成卡密的信息、添加授权的信息、使用的卡密
     */
    private String data;

    /**
     * 管理员奖惩、生成卡密、添加授权、使用卡密
     */
    private String type;

    /**
     * 变动时间
     */
    private Integer addTime;
}
