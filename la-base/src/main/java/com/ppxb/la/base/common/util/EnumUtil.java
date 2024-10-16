package com.ppxb.la.base.common.util;

import com.ppxb.la.base.common.enumeration.BaseEnum;

import java.util.stream.Stream;

public class EnumUtil {

    /**
     * 校验参数与枚举类比较是否合法
     *
     * @param value     参数
     * @param enumClass 枚举类必须实现BaseEnum接口
     * @return boolean
     * @author ppxb
     */
    public static boolean checkEnum(Object value, Class<? extends BaseEnum> enumClass) {
        if (value == null) {
            return false;
        }

        return Stream.of(enumClass.getEnumConstants()).anyMatch(e -> e.equalsValue(value));
    }

    /**
     * 根据参数获取枚举类的实例
     *
     * @param value     参数
     * @param enumClass 枚举类必须实现BaseEnum接口
     * @return BaseEnum 无匹配值返回null
     * @author ppxb
     */
    public static <T extends BaseEnum> T getEnumByValue(Object value, Class<T> enumClass) {
        if (value == null) {
            return null;
        }

        return Stream.of(enumClass.getEnumConstants())
                .filter(e -> e.equalsValue(value))
                .findFirst()
                .orElse(null);
    }
}
