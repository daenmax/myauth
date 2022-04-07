package cn.daenx.myauth.main.entity;

import cn.daenx.myauth.base.po.baseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author DaenMax
 * @since 2022-04-01
 */
@Data
@Accessors(chain = true)
@TableName("ma_storage")
public class Storage extends baseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer fromSoftId;

    /**
     * 所属存储类型ID
     */
    private Integer fromStorageTypeId;

    private String content;

    private String remark;

    /**
     * 数量
     */
    private Integer number;

    /**
     * 状态，0=禁用，1=正常
     */
    private Integer status;

    /**
     * 所属软件名称
     */
    @TableField(exist = false)
    private String fromSoftName;

    /**
     * 所属存储类型名称
     */
    @TableField(exist = false)
    private String fromStorageTypeName;
}
