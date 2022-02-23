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
@TableName("ma_js")
public class Js extends Model {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 执行JS函数名，同一软件下不要重复
     */
    private String jsFun;
    /**
     * JS代码
     */
    private String jsContent;

    private Integer addTime;
    /**
     * 0=禁用，1=正常
     */
    private Integer status;

    private Integer fromSoftId;
    /**
     * 备注
     */
    private String remark;

    /**
     * 所属软件名称
     */
    @TableField(exist = false)
    private String fromSoftName;
}
