package com.ppxb.la.admin.module.system.role.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ppxb.la.admin.module.system.role.domain.entity.RoleMenuEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMenuDao extends BaseMapper<RoleMenuEntity> {

    void deleteByRoleId(@Param("roleId") Long roleId);

    List<Long> queryMenuIdsByRoleId(@Param("roleId") Long roleId);

    List<RoleMenuEntity> queryAllRoleMenu();
}
