package com.ppxb.la.admin.module.system.role.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ppxb.la.admin.module.system.role.dao.RoleUserDao;
import com.ppxb.la.admin.module.system.role.domain.entity.RoleUserEntity;
import org.springframework.stereotype.Service;

@Service
public class RoleUserManager extends ServiceImpl<RoleUserDao, RoleUserEntity> {
}
