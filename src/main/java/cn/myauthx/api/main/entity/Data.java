package cn.myauthx.api.main.entity;

import cn.myauthx.api.base.po.baseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
@lombok.Data
@Accessors(chain = true)
@TableName("ma_data")
public class Data extends Model {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 上报类型
     */
    private String type;

    /**
     * 上报内容
     */
    private String content;

    /**
     * IP
     */
    private String ip;



    /**
     * 上报时间
     */
    private Integer addTime;

    private Integer fromSoftId;

    private Integer fromVerId;
    /**
     * 设备信息
     */
    private String deviceInfo;
    /**
     * 机器码
     */
    private String deviceCode;

    /**
     * 所属软件名称
     */
    @TableField(exist = false)
    private String fromSoftName;

    /**
     * 所属版本号
     */
    @TableField(exist = false)
    private String fromVer;

}
