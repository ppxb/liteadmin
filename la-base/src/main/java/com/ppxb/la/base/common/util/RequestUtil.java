package com.ppxb.la.base.common.util;

import com.ppxb.la.base.common.domain.RequestUser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestUtil {

    private static final ThreadLocal<RequestUser> REQUEST_USER_THREAD_LOCAL = new ThreadLocal<>();

    public static RequestUser getRequestUser() {
        return REQUEST_USER_THREAD_LOCAL.get();
    }

    public static void setRequestUser(RequestUser user) {
        REQUEST_USER_THREAD_LOCAL.set(user);
    }

    public static Long getRequestUserId() {
        RequestUser user = getRequestUser();
        return user == null ? null : user.getUserId();
    }

    public static void remove() {
        REQUEST_USER_THREAD_LOCAL.remove();
    }
}
