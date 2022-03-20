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
@TableName("ma_admin")
public class Admin extends Model {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String user;

    private String pass;

    private String qq;

    private Integer regTime;

    private Integer lastTime;

    private String lastIp;

    private String token;

    /**
     * 0=禁用，1=正常
     */
    private Integer status;

    private Integer role;

    private String money;

    @TableField(exist = false)
    private String roleName;

    @TableField(exist = false)
    private String fromSoftName;
}
