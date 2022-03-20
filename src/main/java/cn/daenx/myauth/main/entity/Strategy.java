package cn.daenx.myauth.main.entity;

import cn.daenx.myauth.base.po.baseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author DaenMax
 * @since 2022-03-09
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("ma_strategy")
public class Strategy extends baseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 策略名称，例如：月卡，年卡
     */
    private String name;

    /**
     * 1=期限卡，2=余额卡
     */
    private Integer type;

    /**
     * 卡面额
     */
    private Integer value;

    /**
     * 排序，越小越前，从1开始
     */
    private Integer sort;

    /**
     * 价格
     */
    private String price;

    private Integer fromSoftId;

    /**
     * 状态。1=正常，0=禁用
     */
    private Integer status;

    /**
     * 所属软件名称
     */
    @TableField(exist = false)
    private String fromSoftName;
}
