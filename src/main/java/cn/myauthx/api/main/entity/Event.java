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
@TableName("ma_event")
public class Event extends Model {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 事件名称，同一软件下禁止重复
     */
    private String name;

    /**
     * 点数变动值，增加为正数，扣除为负数
     */
    private Integer point;

    /**
     * 秒数变动值，增加为正数，扣除为负数
     */
    private Integer seconds;

    private Integer addTime;

    /**
     * 0=禁用，1=正常
     */
    private String status;

    private Integer fromSoftId;

    /**
     * 所属软件名称
     */
    @TableField(exist = false)
    private String fromSoftName;
}
