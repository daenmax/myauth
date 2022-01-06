package cn.myauthx.api.web.entity;

import cn.myauthx.api.base.po.baseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
@Getter
@Setter
@Accessors(chain = true)
@TableName("ma_admin")
public class Admin extends baseEntity {

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


}
