package cn.myauthx.api.main.entity;

import cn.myauthx.api.base.po.baseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
@Data
@Accessors(chain = true)
@TableName("ma_card")
public class Card extends Model {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String ckey;

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
     * 生成时间
     */

    @TableField(exist = false)
    private Date addTimeName;

    /**
     * 使用时间
     */
    @TableField(exist = false)
    private Date letTimeName;

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
     * 所属软件id
     */
    private Integer fromSoftId;

    /**
     * 所属软件名称
     */
    @TableField(exist = false)
    private String fromSoftName;

}
