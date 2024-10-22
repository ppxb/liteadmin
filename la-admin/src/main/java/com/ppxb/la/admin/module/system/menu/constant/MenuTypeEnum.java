package com.ppxb.la.admin.module.system.menu.constant;

import com.ppxb.la.base.common.enumeration.BaseEnum;

public enum MenuTypeEnum implements BaseEnum {

    CATALOG(1, "目录"),

    MENU(2, "菜单"),

    POINTS(3, "功能点");

    private final Integer value;

    private final String desc;

    MenuTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }


    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
