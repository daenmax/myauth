package cn.daenx.myauth.main.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("ma_version")
public class Version extends Model {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String ver;

    private String vkey;

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
     * 更新日志
     */
    private String updLog;

    private Integer updTime;

    /**
     * 更新模式，0=可选，1=强制
     */
    private Integer updType;

    /**
     * 0=停用，1=正常
     */
    private Integer status;


}
