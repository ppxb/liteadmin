package com.ppxb.la.base.common.domain;

import com.ppxb.la.base.common.enumeration.UserTypeEnum;

public interface RequestUser {

    Long getUserId();

    String getUserName();

    UserTypeEnum getUserType();

    String getIp();

    String getUserAgent();
}
