package com.ppxb.la.admin.module.system.role.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ppxb.la.admin.module.system.role.dao.RoleMenuDao;
import com.ppxb.la.admin.module.system.role.domain.entity.RoleMenuEntity;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleMenuManager extends ServiceImpl<RoleMenuDao, RoleMenuEntity> {

    @Resource
    private RoleMenuDao roleMenuDao;

    @Transactional(rollbackFor = Exception.class)
    public void updateRoleMenu(Long roleId, List<RoleMenuEntity> roleMenuEntityList) {
        roleMenuDao.deleteByRoleId(roleId);
        saveBatch(roleMenuEntityList);
    }
}
