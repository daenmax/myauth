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
@TableName("ma_soft")
public class Soft extends Model {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String skey;

    private String name;

    /**
     * 0=停用，1=正常，2=维护
     */
    private Integer status;

    /**
     * 0=需要授权，1=不需要授权
     */
    private Integer type;

    private Integer addTime;

    /**
     * 数据加密秘钥
     */
    private String genKey;

    /**
     * 0=数据不加密，1=数据加密
     */
    private Integer genStatus;

    /**
     * 0=不绑定机器码，1=绑定机器码
     */
    private Integer bindDeviceCode;

    /**
     * 心跳有效时间
     */
    private Integer heartTime;
    /**
     * 0=关闭注册，1=开启注册
     */
    private Integer register;



}
