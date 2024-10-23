package com.ppxb.la.admin.module.system.role.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ppxb.la.admin.module.system.menu.domain.entity.MenuEntity;
import com.ppxb.la.admin.module.system.role.domain.entity.RoleMenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface RoleMenuDao extends BaseMapper<RoleMenuEntity> {

    void deleteByRoleId(@Param("roleId") Long roleId);

    List<Long> queryMenuIdByRoleId(@Param("roleId") Long roleId);

    List<MenuEntity> selectMenuListByRoleIdList(@Param("roleIdList") List<Long> roleIdList, @Param("deletedFlag") Boolean deletedFlag);

    List<RoleMenuEntity> queryAllRoleMenu();
}
