package com.ppxb.la.admin.module.system.department.service;

import com.ppxb.la.admin.module.system.department.dao.DepartmentDao;
import com.ppxb.la.admin.module.system.user.dao.UserDao;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

    @Resource
    private DepartmentDao departmentDao;

    @Resource
    private UserDao userDao;


}
