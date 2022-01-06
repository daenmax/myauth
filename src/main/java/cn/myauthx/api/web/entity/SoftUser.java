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
 * @since 2022-01-05
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("ma_soft_user")
public class SoftUser extends baseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 账号，卡密登录时，为卡密，免费用户时为机器码
     */
    private String user;

    /**
     * 密码
     */
    private String pass;

    /**
     * 昵称
     */
    private String name;

    /**
     * 点数
     */
    private Integer point;

    /**
     * 联系人信息，例如QQ
     */
    private String userInfo;

    /**
     * 最后登录的设备信息
     */
    private String deviceInfo;

    /**
     * 最后登录的IP
     */
    private String lastIp;

    /**
     * 最后登录的时间戳
     */
    private Integer lastTime;

    /**
     * 用户注册的时间戳
     */
    private Integer regTime;

    /**
     * 授权到期的时间戳，-1=永久
     */
    private Integer authTime;

    /**
     * 所属软件key
     */
    private String softKey;

    /**
     * 最后登录的软件的版本key
     */
    private String verKey;

    /**
     * 登录成功的token
     */
    private String token;

    /**
     * 上级代理id
     */
    private Integer upproxyId;

    /**
     * 0=禁用，1=正常
     */
    private Integer status;


}
