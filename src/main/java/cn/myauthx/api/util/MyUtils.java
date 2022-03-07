package cn.myauthx.api.util;

import cn.myauthx.api.base.vo.Result;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String getRequestPostStr(HttpServletRequest request) throws IOException {
        if (!"POST".equals(request.getMethod())) {
            return null;
        }
        byte[] buffer = getRequestPostBytes(request);
        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }
        if (CheckUtils.isObjectEmpty(buffer)) {
            log.error("取请求体为空");
            return null;
        }
        return new String(buffer, charEncoding);
    }

    private static byte[] getRequestPostBytes(HttpServletRequest request) throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte[] buffer = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {
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
     *
     * @param jsonObject
     * @return
     */
    public static String json2pathValue(JSONObject jsonObject) {
        Map map = jsonObject.toJavaObject(Map.class);
        Set<String> set = map.keySet();
        List<String> keyList = new ArrayList<>(set);
        List<String> collect = keyList.stream().sorted().collect(Collectors.toList());
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : collect) {
            String value = "";
            if (CheckUtils.isObjectEmpty(map.get(s))) {
                value = "";
            } else {
                value = map.get(s).toString();
            }
            stringBuilder.append(s).append("=").append(value).append("&");
        }
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("&"));
        return stringBuilder.toString();
    }

    /**
     * 计算sign签名
     *
     * @param jsonObject
     * @param genKey
     * @return
     */
    public static String calculateSign(JSONObject jsonObject, String genKey) {
        String pathvalue = json2pathValue(jsonObject);
        pathvalue = pathvalue + "&gen_key=" + genKey;
        pathvalue = pathvalue.replaceAll("\\\\r\\\\n", "\\\\n").replaceAll("\\\\n", "\\\\r\\\\n");
        String sign = DigestUtils.md5DigestAsHex(pathvalue.getBytes(StandardCharsets.UTF_8));
        return sign;
    }

    /**
     * 计算sign签名，返回时用
     *
     * @param msg
     * @param jsonObject
     * @param gen_status
     * @param gen_key
     * @return
     */
    public static Result calculateSignReturn(String msg, JSONObject jsonObject, Integer gen_status, String gen_key) {
        jsonObject.put("timeStamp", MyUtils.getTimeStamp());
        String pathvalue = json2pathValue(jsonObject);
        pathvalue = pathvalue + "&gen_key=" + gen_key;
        pathvalue = pathvalue.replaceAll("\\\\r\\\\n", "\\\\n").replaceAll("\\\\n", "\\\\r\\\\n");
        String sign = DigestUtils.md5DigestAsHex(pathvalue.getBytes(StandardCharsets.UTF_8));
        if (gen_status != 1) {
            return Result.ok(msg, jsonObject).sign(sign);
        }
        String enStr = AESUtils.encrypt(jsonObject.toJSONString(), gen_key);
        return Result.ok(msg, enStr).sign(sign);
    }

    /**
     * base64编码
     *
     * @param txt
     * @return
     */
    public static String base64Encode(String txt) {
        return Base64Utils.encodeToString(txt.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * base64解码
     *
     * @param txt
     * @return
     */
    public static String base64Decode(String txt) {
        return Base64Utils.decodeFromString(txt).toString();
    }

    /**
     * +
     * 获取10位时间戳
     *
     * @return
     */
    public static String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000L);
    }

    /**
     * 生成UUID
     *
     * @param hg 是否带横杠
     * @return
     */
    public static String getUUID(boolean hg) {
        String uuid = null;
        if (hg) {
            uuid = UUID.randomUUID().toString().toLowerCase();
        } else {
            uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        }
        return uuid;
    }

    /**
     * 生成用户token
     *
     * @param user
     * @param time
     * @param genKey
     * @return
     */
    public static String encUserToken(String user, String time, String softId, String genKey) {
        int kk = (int) (1000000 + Math.random() * 9999999);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", user);
        jsonObject.put("time", time);
        jsonObject.put("softId", softId);
        jsonObject.put("radom", String.valueOf(kk));
        String encrypt = AESUtils.encrypt(jsonObject.toJSONString(), genKey);
        return encrypt;
    }

    /**
     * 解密用户token
     *
     * @param token
     * @param genKey
     * @return
     */
    public static JSONObject decUserToken(String token, String genKey) {
        String decrypt = AESUtils.decrypt(token, genKey);
        JSONObject jsonObject = JSONObject.parseObject(decrypt);
        return jsonObject;
    }

    /**
     * 执行JS函数，参数和返回值都是String类型
     *
     * @param jsStr
     * @param func
     * @param parameter
     * @return
     */
    public static String runJs(String jsStr, String func, String... parameter) {
        String regular = jsStr;
        //创建引擎实例
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        Object result = "";
        try {
            //编译
            engine.eval(regular);
            if (engine instanceof Invocable) {
                // 执行方法
                result = ((Invocable) engine).invokeFunction(func, parameter);
                return String.valueOf(result);
            }
        } catch (Exception e) {
            return "表达式runtime错误:" + e.getMessage();
        }
        return "";
    }

    /**
     * 下划线转驼峰法
     *
     * @param line       源字符串
     * @param smallCamel 大小驼峰,是否为小驼峰
     * @return 转换后的字符串
     */
    public static String underlineToCamel(String line, boolean smallCamel) {
        if (line == null || "".equals(line)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(smallCamel && matcher.start() == 0 ? Character.toLowerCase(word.charAt(0)) : Character.toUpperCase(word.charAt(0)));
            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰法转下划线
     *
     * @param line 源字符串
     * @return 转换后的字符串
     */
    public static String camelToUnderline(String line) {
        if (line == null || "".equals(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toUpperCase());
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }

    /**
     * 10位时间戳转Date类型
     *
     * @param timeStamp
     * @return
     */
    public static Date stamp2Date(String timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;

        try {
            date = sdf.parse(sdf.format(new Long(timeStamp) * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * Date类型转10位时间戳
     *
     * @param date
     * @return
     */
    public static String date2stamp(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStamp = null;
        try {
            timeStamp = String.valueOf(sdf.parse(sdf.format(date)).getTime() / 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

    /**
     * 时间文本转Date
     * @param strDate yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date strToDate(String strDate) {
        if(!"".equals(strDate)){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ParsePosition pos = new ParsePosition(0);
            Date strtodate = formatter.parse(strDate, pos);
            return strtodate;
        }
        return new Date();
    }

    /**
     * Date转时间文本，例如转成yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String dateToStr(Date date) {
        if(date == null){
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }
}