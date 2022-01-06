package cn.myauthx.api.web.entity;

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
 * @since 2022-01-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("ma_card")
public class Card extends baseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String key;

    /**
     * 点数
     */
    private Integer point;

    /**
     * 秒数
     */
    private Integer seconds;

    /**
     * 生成时间
     */
    private Integer addTime;

    /**
     * 使用时间
     */
    private Integer letTime;

    /**
     * 使用人账号
     */
    private String letUser;

    /**
     * 卡密状态，0=未使用，1=已使用，2=被禁用
     */
    private Integer status;

    /**
     * 所属软件id
     */
    private Integer fromSoftId;


}
