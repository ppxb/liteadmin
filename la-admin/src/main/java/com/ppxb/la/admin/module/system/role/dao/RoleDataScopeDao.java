package com.ppxb.la.admin.module.system.role.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ppxb.la.admin.module.system.role.domain.entity.RoleDataScopeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface RoleDataScopeDao extends BaseMapper<RoleDataScopeEntity> {

    List<RoleDataScopeEntity> listByRoleId(@Param("roleId") Long roleId);

    List<RoleDataScopeEntity> listByRoleIdList(@Param("roleIdList") List<Long> roleIdList);

    void deleteByRoleId(@Param("roleId") Long roleId);
}
