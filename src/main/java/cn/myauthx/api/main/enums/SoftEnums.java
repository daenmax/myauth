package cn.myauthx.api.main.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 软件相关
 * @author DaenMax
 */

@AllArgsConstructor
@Getter
public enum SoftEnums {
    STATUS_DISABLE(0,"停用"),
    STATUS_ABLE(1,"正常"),
    STATUS_FIX(2,"维护"),
    TYPE_PAY(0,"收费"),
    TYPE_FREE(1,"免费");
    private Integer code;
    private String desc;
    private static final Map<Integer, String> codeMap;
    private static final Map<String, Integer> descMap;
    private static final List<Integer> list;
    static {
        codeMap = new HashMap<>();
        descMap = new HashMap<>();
        list = new ArrayList<>();
        for (SoftEnums value : SoftEnums.values()) {
            codeMap.put(value.getCode(), value.getDesc());
            descMap.put(value.getDesc(),value.getCode());
            list.add(value.getCode());
        }
    }
    public static String getDesc(Integer code) {
        String desc = codeMap.get(code);
        if (desc == null) {
            return "";
        }
        return desc;
    }
    public static Integer getCode(String desc) {
        Integer code = descMap.get(desc);
        if (code == null) {
            return -1;
        }
        return code;
    }
    public static boolean hasCode(String code) {
        return list.contains(code);
    }
}
