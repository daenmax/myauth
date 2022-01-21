package cn.myauthx.api.main.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-07
 */
@Data
@Accessors(chain = true)
@TableName("ma_user")
public class User extends Model {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 账号
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

    private String qq;

    /**
     * 最后登录或注册时的IP
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
     * 所属软件id
     */
    private Integer fromSoftId;

    /**
     * 所属软件key
     */
    private String fromSoftKey;

    /**
     * 最后登录的软件的版本id
     */
    private Integer fromVerId;

    /**
     * 最后登录的软件的版本key
     */
    private String fromVerKey;

    /**
     * 登录成功的token
     */
    private String token;

    /**
     * 备注
     */
    private String remark;

    /**
     * 最后登录或注册时的设备信息
     */
    private String deviceInfo;

    /**
     * 最后登录或注册时的机器码
     */
    private String deviceCode;

    /**
     * 卡密
     */
    private String ckey;


}
