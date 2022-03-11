package cn.myauthx.api.main.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

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
@TableName("ma_menu")
public class Menu extends Model {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 父ID，根则空
     */
    private String parentId;


    /**
     * 层级，从1开始
     */
    private Integer level;

    /**
     * 排序，越小越大，从1开始
     */
    private Integer sort;

    /**
     * 1=目录，2=菜单
     */
    private Integer type;

    private String path;
    private String title;
    private String icon;

    @TableField(exist = false)
    private List<Menu> children;

    @TableField(exist = false)
    private List<String> ids;

}
