package cn.daenx.myauth.main.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
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
@TableName("ma_msg")
public class Msg extends Model {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String keyword;

    private String msg;

    /**
     * 0=禁用，1=正常
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

    /**
     * 所属软件版本，留空则全版本可用
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer fromVerId;

    /**
     * 所属版本号
     */
    @TableField(exist = false)
    private String fromVer;


}
