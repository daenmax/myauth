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
@TableName("ma_ban")
public class Ban extends Model {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 要封禁的对象
     */
    private String value;

    private Integer addTime;

    /**
     * 封禁到期时间，-1=永久
     */
    private Integer toTime;

    /**
     * 封禁原因
     */
    private String why;

    private Integer fromSoftId;
    /**
     * 封禁类型，1=机器码，2=IP，3=账号
     */
    private Integer type;

    /**
     * 所属软件名称
     */
    @TableField(exist = false)
    private String fromSoftName;

}
