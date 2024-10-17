package com.ppxb.la.admin.module.system.user.service;

import com.ppxb.la.admin.module.system.department.dao.DepartmentDao;
import com.ppxb.la.admin.module.system.user.dao.UserDao;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final String PASSWORD_SALT_FORMAT = "lite_%s_admin_$^&*";

    @Resource
    private UserDao userDao;

    @Resource
    private DepartmentDao departmentDao;
}
