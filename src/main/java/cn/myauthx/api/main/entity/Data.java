package cn.myauthx.api.main.entity;

import cn.myauthx.api.base.po.baseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("ma_data")
public class Data extends baseEntity {

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
     * 设备信息
     */
    private String deviceInfo;

    /**
     * 上报时间
     */
    private Integer addTime;

    private Integer fromSoftId;

    private Integer fromVerId;


}
