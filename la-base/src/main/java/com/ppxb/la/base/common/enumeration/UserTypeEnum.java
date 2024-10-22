package com.ppxb.la.base.common.enumeration;

public enum UserTypeEnum implements BaseEnum {

    ADMIN_USER(1, "员工");


    private final Integer value;

    private final String desc;

    UserTypeEnum(Integer value, String desc) {
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
