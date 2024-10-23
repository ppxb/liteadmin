package com.ppxb.la.admin.module.system.role.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ppxb.la.admin.module.system.role.domain.entity.RoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface RoleDao extends BaseMapper<RoleEntity> {

    RoleEntity getByRoleName(@Param("roleName") String roleName);

    RoleEntity getByRoleCode(@Param("roleCode") String roleCode);
}
