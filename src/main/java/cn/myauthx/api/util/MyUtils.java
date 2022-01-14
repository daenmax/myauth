package cn.myauthx.api.util;

import cn.myauthx.api.base.exception.MyException;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Soft;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 常用工具类
 *
 * @author DaenMax
 */
@Slf4j
public class MyUtils {
    /**
     * 获取Request中的JSON字符串
     * @param request
     * @return
     * @throws IOException
     */
    public static String getRequestPostStr(HttpServletRequest request) throws IOException {
        if(!"POST".equals(request.getMethod())){
            return null;
        }
        byte[] buffer = getRequestPostBytes(request);
        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }
        if(CheckUtils.isObjectEmpty(buffer)){
            log.error("取请求体为空");
            return null;
        }
        return new String(buffer, charEncoding);
    }
    private static byte[] getRequestPostBytes(HttpServletRequest request) throws IOException {
        int contentLength = request.getContentLength();
        if(contentLength<0){
            return null;
        }
        byte[] buffer = new byte[contentLength];
        for (int i = 0; i < contentLength;) {
            int readlen = request.getInputStream().read(buffer, i,
                    contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }

    /**
     * JSON对象转为网址传参格式（按key的首字母从小到大排序）
     * @param jsonObject
     * @return
     */
    public static String json2pathValue(JSONObject jsonObject)  {
        Map map = jsonObject.toJavaObject(Map.class);
        Set<String> set = map.keySet();
        List<String> keyList = new ArrayList<>(set);
        List<String> collect = keyList.stream().sorted().collect(Collectors.toList());
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : collect) {
            String value =map.get(s).toString();
            stringBuilder.append(s).append("=").append(value).append("&");
        }
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("&"));
        return stringBuilder.toString();
    }

    /**
     * 计算sign签名
     * @param jsonObject
     * @param genKey
     * @return
     */
    public static String calculateSign(JSONObject jsonObject, String genKey) {
        String pathvalue = json2pathValue(jsonObject);
        pathvalue = pathvalue + "&gen_key=" + genKey;
        String sign = DigestUtils.md5DigestAsHex(pathvalue.getBytes(StandardCharsets.UTF_8));
        return sign;
    }

    public static Result calculateSignReturn(String msg,JSONObject jsonObject,Integer gen_status,String gen_key){
        jsonObject.put("timeStamp",MyUtils.getTimeStamp());
        String pathvalue = json2pathValue(jsonObject);
        pathvalue = pathvalue + "&gen_key=" + gen_key;
        String sign = DigestUtils.md5DigestAsHex(pathvalue.getBytes(StandardCharsets.UTF_8));
        if(gen_status != 1){
            return Result.ok(msg,jsonObject).sign(sign);
        }
        String enStr = AESUtils.encrypt(jsonObject.toJSONString(),gen_key);
        return Result.ok(msg,enStr).sign(sign);
    }

    /**+
     * 获取10位时间戳
     * @return
     */
    public static String getTimeStamp(){
        return String.valueOf(System.currentTimeMillis()/1000L);
    }
}