package com.ppxb.la.base.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SystemErrorCode implements ErrorCode {

    SYSTEM_ERROR(10001, "系统错误，请稍后再试");

    private final int code;

    private final String msg;

    private final String level;

    SystemErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.level = LEVEL_SYSTEM;
    }
}
