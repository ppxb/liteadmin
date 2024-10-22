package com.ppxb.la.admin.module.system.menu.constant;

import com.ppxb.la.base.common.enumeration.BaseEnum;

public enum MenuPermsTypeEnum implements BaseEnum {

    SA_TOKEN(1, "Sa-Token模式");

    private final Integer value;

    private final String desc;

    MenuPermsTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
