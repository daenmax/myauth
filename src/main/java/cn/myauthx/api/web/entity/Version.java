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
@TableName("ma_version")
public class Version extends baseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String ver;

    private String verKey;

    /**
     * 所属软件id
     */
    private Integer fromSoftId;

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
