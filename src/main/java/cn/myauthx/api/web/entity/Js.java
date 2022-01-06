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
@TableName("ma_js")
public class Js extends baseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String jsFun;

    private String jsContent;

    private Integer addTime;

    private Integer status;

    private Integer fromSoftId;


}
