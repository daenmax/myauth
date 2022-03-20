package cn.daenx.myauth.main.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 版本相关
 * @author DaenMax
 */

@AllArgsConstructor
@Getter
public enum VersionEnums {
    UPDTYPE_FREE(0,"可选更新"),
    UPDTYPE_FORCE(1,"强制更新"),
    STATUS_DISABLE(0,"停用"),
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
        for (VersionEnums value : VersionEnums.values()) {
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
