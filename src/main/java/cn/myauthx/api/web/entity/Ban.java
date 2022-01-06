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
@TableName("ma_ban")
public class Ban extends baseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 要封禁的机器码或者IP
     */
    private String value;

    private Integer addTime;

    /**
     * 封禁到期时间，0=永久
     */
    private Integer toTime;

    /**
     * 封禁原因
     */
    private String why;

    private Integer fromSoftId;


}
