package cn.myauthx.api.base.vo;

import cn.myauthx.api.util.CheckUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口返回数据格式
 * @author DaenMax
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 返回代码
     */
    private Integer code = 0;

    /**
     * 成功标志
     */
    private boolean success = true;

    /**
     * 返回处理消息
     */
    private String msg = "操作成功";

    /**
     * 签名校验sign
     */
    private String sign = "";

    /**
     * 返回数据对象 data
     */
    private T result;

    /**
     * 13位时间戳
     */
    private long timestamp = System.currentTimeMillis();

    public Result() {
    }

    /**
     * success
     */
    public Result<T> success() {
        this.code = 200;
        return this;
    }
    public Result<T> success(String msg) {
        this.msg = msg;
        this.code = 200;
        return this;
    }

    /**
     * ok
     */
    public static Result<Object> ok() {
        Result<Object> r = new Result<>();
        r.setCode(200);
        r.setSuccess(true);
        return r;
    }

    public static Result<Object> ok(String msg) {
        Result<Object> r = new Result<>();
        r.setCode(200);
        r.setSuccess(true);
        r.setMsg(msg);
        return r;
    }

    public static Result<Object> ok(Object data) {
        Result<Object> r = new Result<>();
        r.setCode(200);
        r.setSuccess(true);
        r.setResult(data);
        return r;
    }

    public static Result<Object> ok(String msg,Object data) {
        Result<Object> r = new Result<>();
        r.setSuccess(true);
        r.setCode(200);
        r.setMsg(msg);
        r.setResult(data);
        return r;
    }
    public static Result<Object> ok(String msg,String data) {
        Result<Object> r = new Result<>();
        r.setSuccess(true);
        r.setCode(200);
        r.setMsg(msg);
        r.setResult(data);
        return r;
    }
    public Result<T> sign(String sign) {
        this.sign = sign;
        return this;
    }

    /**
     * error500
     */
    public Result<T> error500() {
        this.code = 500;
        this.msg = "操作失败";
        this.success = false;
        return this;
    }
    public Result<T> error500(String msg) {
        this.msg = msg;
        this.code = 500;
        this.success = false;
        return this;
    }

    public Result<T> error420(String msg) {
        this.msg = msg;
        this.code = 420;
        this.success = false;
        return this;
    }

    /**
     * error
     */
    public static Result<Object> error(String msg) {
        return error(500, msg);
    }

    public static Result<Object> error(int code, String msg) {
        Result<Object> r = new Result<>();
        r.setCode(code);
        r.setMsg(msg);
        r.setSuccess(false);
        return r;
    }

    /**
     * 无权限，包含令牌不存在和令牌过期的情况
     */
    public static Result<Object> noAuth(String msg) {
        return error(510, msg);
    }

    /**
     * 手动转到JSON字符串
     * @return
     */
    public String toJsonString(){
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("code",this.code);
        jsonObject.put("success",this.success);
        jsonObject.put("msg",this.msg);
        if(!CheckUtils.isObjectEmpty(this.result)){
            jsonObject.put("result",this.result);
        }
        jsonObject.put("timestamp",this.timestamp);
        if(!CheckUtils.isObjectEmpty(this.sign)){
            jsonObject.put("sign",this.sign);
        }
        return jsonObject.toJSONString();
    }

}
