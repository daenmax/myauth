package cn.daenx.myauth.main.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 是否为开放API
 * @author DaenMax
 */

@AllArgsConstructor
@Getter
public enum AdminEnums {
    TOKEN_VALIDITY(86400,"有效期：秒"),
    STATUS_DISABLE(0,"禁用"),
    STATUS_ABLE(1,"正常");
    private Integer code;
    private String desc;
    private static final Map<Integer, String> codeMap;
    private static final Map<String, Integer> descMap;
    private static final List<Integer> list;
    static {
        codeMap = new HashMap<>();
        descMap = new HashMap<>();
        list = new ArrayList<>();
        for (AdminEnums value : AdminEnums.values()) {
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
