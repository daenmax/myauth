package cn.myauthx.api.main.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
     * 0=收费，1=免费
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
     * 0=不允许多开，1=允许多开
     */
    private Integer batchSoft;

    /**
     * 0=不允许多地同时登录，1=允许多地同时登录
     */
    private Integer multipleLogin;

    /**
     * 心跳有效时间
     */
    private Integer heartTime;


}
