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
@TableName("ma_storage_type")
public class StorageType extends baseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 类型
     */
    private String type;

    /**
     * 状态，0=禁用，1=正常
     */
    private Integer status;

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
