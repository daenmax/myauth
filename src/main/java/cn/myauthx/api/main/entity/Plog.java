package cn.myauthx.api.main.entity;

import cn.myauthx.api.base.po.baseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
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
@Data
@Accessors(chain = true)
@TableName("ma_plog")
public class Plog extends Model {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 变动点数，扣除为负数
     */
    private Integer point;

    /**
     * 变动后的点数
     */
    private Integer afterPoint;

    /**
     * 变动秒数，扣除为负数
     */
    private Integer seconds;

    /**
     * 变动后的到期时间
     */
    private Integer afterSeconds;

    /**
     * 变动账号
     */
    private String fromUser;

    /**
     * 变动时间
     */
    private Integer addTime;

    /**
     * 所属事件名称
     */
    private String fromEventName;

    /**
     * 所属事件ID
     */
    private Integer fromEventId;

    private Integer fromSoftId;

    private Integer fromVerId;

    /**
     * 日志的说明内容，使用卡密时包含使用的卡密
     */
    private String remark;


}
