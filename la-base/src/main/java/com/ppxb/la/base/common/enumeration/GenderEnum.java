package com.ppxb.la.base.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderEnum implements BaseEnum {

    UNKNOWN(0, "未知"),

    MAN(1, "男"),

    WOMAN(2, "女");

    private final Integer value;

    private final String desc;
}
