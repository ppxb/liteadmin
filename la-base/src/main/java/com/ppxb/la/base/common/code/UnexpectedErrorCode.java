package com.ppxb.la.base.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UnexpectedErrorCode implements ErrorCode {

    BUSINESS_HANDING(20001, "业务繁忙，请稍后再试");

    private final int code;

    private final String msg;

    private final String level;

    UnexpectedErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.level = LEVEL_UNEXPECTED;
    }
}
