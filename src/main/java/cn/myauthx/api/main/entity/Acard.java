package cn.myauthx.api.main.entity;

import cn.myauthx.api.base.po.baseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author DaenMax
 * @since 2022-03-16
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("ma_acard")
public class Acard extends baseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String ckey;

    /**
     * 余额
     */
    private String money;

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
     * 卡密状态，0=未使用，1=已使用，2=被禁用
     */
    @TableField(exist = false)
    private String statusName;

    /**
     * 生成时间
     */

    @TableField(exist = false)
    private Date addTimeName;

    /**
     * 使用时间
     */
    @TableField(exist = false)
    private Date letTimeName;
}
