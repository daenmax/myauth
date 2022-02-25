package cn.myauthx.api.main.entity;

import cn.myauthx.api.base.po.baseEntity;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

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
@TableName("ma_card")
public class Card extends Model {

    private static final long serialVersionUID = 1L;
    @ExcelProperty(value = "ID",index = 0)
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ExcelProperty(value = "卡密",index = 1)
    private String ckey;

    /**
     * 点数
     */
    @ExcelProperty(value = "点数",index = 2)
    private Integer point;

    /**
     * 秒数
     */
    @ExcelProperty(value = "描述",index = 3)
    private Integer seconds;

    /**
     * 生成时间
     */
    @ExcelIgnore //表明我在写的时候不用这列
    private Integer addTime;

    /**
     * 使用时间
     */
    @ExcelIgnore //表明我在写的时候不用这列
    private Integer letTime;

    /**
     * 生成时间
     */

    @ExcelProperty(value = "生成时间",index = 4)
    @TableField(exist = false)
    private Date addTimeName;

    /**
     * 使用时间
     */
    @ExcelProperty(value = "使用时间",index = 5)
    @TableField(exist = false)
    private Date letTimeName;

    /**
     * 使用人账号
     */
    @ExcelProperty(value = "使用人账号",index = 6)
    private String letUser;

    /**
     * 卡密状态，0=未使用，1=已使用，2=被禁用
     */
    @ExcelIgnore //表明我在写的时候不用这列
    private Integer status;

    /**
     * 卡密状态，0=未使用，1=已使用，2=被禁用
     */
    @ExcelProperty(value = "卡密状态",index = 7)
    @TableField(exist = false)
    private String statusName;

    /**
     * 所属软件id
     */
    @ExcelIgnore //表明我在写的时候不用这列
    private Integer fromSoftId;

    /**
     * 所属软件名称
     */
    @ExcelProperty(value = "所属软件",index = 8)
    @TableField(exist = false)
    private String fromSoftName;

}
