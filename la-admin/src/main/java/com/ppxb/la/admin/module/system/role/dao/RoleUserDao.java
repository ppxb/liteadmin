package com.ppxb.la.admin.module.system.role.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ppxb.la.admin.module.system.role.domain.entity.RoleUserEntity;
import com.ppxb.la.admin.module.system.role.domain.form.RoleUserQueryForm;
import com.ppxb.la.admin.module.system.role.domain.vo.RoleUserVO;
import com.ppxb.la.admin.module.system.role.domain.vo.RoleVO;
import com.ppxb.la.admin.module.system.user.domain.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Mapper
@Component
public interface RoleUserDao extends BaseMapper<RoleUserEntity> {

    List<RoleVO> selectRoleByUserId(@Param("userId") Long userId);

    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);

    List<RoleUserEntity> selectRoleIdByUserIdList(@Param("userIdList") List<Long> userIdList);

    List<RoleUserVO> selectRoleByUserIdList(@Param("userIdList") List<Long> userIdList);

    Set<Long> selectUserIdByRoleIdList(@Param("roleIdList") List<Long> roleIdList);

    List<UserVO> selectRoleUserByName(Page page, @Param("queryForm") RoleUserQueryForm queryForm);

    List<UserVO> selectUserByRoleId(@Param("roleId") Long roleId);

    void deleteByUserId(@Param("userId") Long userId);

    void deleteByRoleId(@Param("roleId") Long roleId);

    void deleteByUserIdRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);

    void batchDeleteUserRole(@Param("roleId") Long roleId, @Param("userIdList") Set<Long> userIdList);

    Integer existsByRoleId(@Param("roleId") Long roleId);
}
