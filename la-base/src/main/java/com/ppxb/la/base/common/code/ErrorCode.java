package com.ppxb.la.base.common.code;

public interface ErrorCode {

    String LEVEL_SYSTEM = "system";

    String LEVEL_USER = "user";

    String LEVEL_UNEXPECTED = "unexpected";

    int getCode();

    String getMsg();

    String getLevel();
}
