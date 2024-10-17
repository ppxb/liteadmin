package com.ppxb.la.base.common.code;

import static com.ppxb.la.base.common.code.ErrorCodeRangeContainer.register;

public class ErrorCodeRegister {

    static {
        register(SystemErrorCode.class, 10001, 20000);

        register(UnexpectedErrorCode.class, 20001, 30000);

        register(UserErrorCode.class, 30001, 40000);
    }

    public static int initialize() {
        return ErrorCodeRangeContainer.initialize();
    }
}
