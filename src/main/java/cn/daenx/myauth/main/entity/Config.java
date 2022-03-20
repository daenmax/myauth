package cn.daenx.myauth.main.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
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
@TableName("ma_config")
public class Config extends Model {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;



    private String seoTitle;

    private String seoKeywords;

    private String seoDescription;

    /**
     * 钉钉机器人
     */
    private String dingbotAccessToken;

    /**
     * 0=关闭通知，1=开启通知
     */
    private Integer dingbotMsg;

    /**
     * 开放接口key
     */
    private String openApiKey;
}
