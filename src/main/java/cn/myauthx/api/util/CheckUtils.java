package cn.myauthx.api.util;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

/**
 * 检查工具类
 *
 * @author DaenMax
 */
public class CheckUtils {


    /**
     * 判断Object对象为空或空字符串
     * 为NULL或者""或者" "都会返回true
     *
     * @param obj
     * @return
     */
    public static Boolean isObjectEmpty(Object obj) {
        String str = ObjectUtils.toString(obj, new Supplier<String>() {
            @Override
            public String get() {
                return "";
            }
        });
        Boolean flag = StringUtils.isBlank(str);
        return flag;
    }
}
