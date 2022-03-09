package cn.myauthx.api.main.entity;

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
 * @since 2022-03-09
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("ma_role")
public class Role extends baseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色名
     */
    private String name;
    /**
     * 0=超级管理员
     */
    private Integer fromSoftId;

    /**
     * 只存menu的id，json数组
     */
    private String meunIds;

    /**
     * 折扣，单位百分%
     */
    private Integer discount;


}
