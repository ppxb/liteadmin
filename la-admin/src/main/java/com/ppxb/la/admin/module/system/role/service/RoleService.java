package com.ppxb.la.admin.module.system.role.service;

import com.ppxb.la.admin.module.system.role.dao.RoleDao;
import com.ppxb.la.admin.module.system.role.dao.RoleMenuDao;
import com.ppxb.la.admin.module.system.role.dao.RoleUserDao;
import com.ppxb.la.admin.module.system.role.domain.entity.RoleEntity;
import com.ppxb.la.admin.module.system.role.domain.form.RoleAddForm;
import com.ppxb.la.admin.module.system.role.domain.form.RoleUpdateForm;
import com.ppxb.la.admin.module.system.role.domain.vo.RoleVO;
import com.ppxb.la.base.common.code.UserErrorCode;
import com.ppxb.la.base.common.domain.ResponseDTO;
import com.ppxb.la.base.common.util.BeanUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {

    @Resource
    private RoleDao roleDao;

    @Resource
    private RoleMenuDao roleMenuDao;

    @Resource
    private RoleUserDao roleUserDao;

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> addRole(RoleAddForm addForm) {
        RoleEntity existRoleEntity = roleDao.getByRoleName(addForm.getRoleName());
        if (existRoleEntity != null) {
            return ResponseDTO.userErrorParam("角色名称已存在");
        }
        existRoleEntity = roleDao.getByRoleCode(addForm.getRoleCode());
        if (existRoleEntity != null) {
            return ResponseDTO.userErrorParam("角色编码重复，重复的角色为" + existRoleEntity.getRoleName());
        }

        RoleEntity roleEntity = BeanUtil.copy(addForm, RoleEntity.class);
        roleDao.insert(roleEntity);
        return ResponseDTO.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> deleteRole(Long roleId) {
        RoleEntity roleEntity = roleDao.selectById(roleId);
        if (roleEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        Integer exists = roleUserDao.existsByRoleId(roleId);
        if (exists != null) {
            return ResponseDTO.userErrorParam("该角色下存在用户，无法删除");
        }
        roleDao.deleteById(roleId);
        roleMenuDao.deleteByRoleId(roleId);
        roleUserDao.deleteByRoleId(roleId);
        return ResponseDTO.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateRole(RoleUpdateForm updateForm) {
        if (roleDao.selectById(updateForm.getRoleId()) == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        RoleEntity existRoleEntity = roleDao.getByRoleName(updateForm.getRoleName());
        if (existRoleEntity != null && !existRoleEntity.getRoleId().equals(updateForm.getRoleId())) {
            return ResponseDTO.userErrorParam("角色名称重复");
        }

        existRoleEntity = roleDao.getByRoleCode(updateForm.getRoleCode());
        if (existRoleEntity != null && !existRoleEntity.getRoleName().equals(updateForm.getRoleName())) {
            return ResponseDTO.userErrorParam("角色编码重复，重复的角色为" + existRoleEntity.getRoleName());
        }

        RoleEntity roleEntity = BeanUtil.copy(updateForm, RoleEntity.class);
        roleDao.updateById(roleEntity);
        return ResponseDTO.ok();
    }

    public ResponseDTO<RoleVO> getRoleById(Long roleId) {
        RoleEntity roleEntity = roleDao.selectById(roleId);
        if (roleEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        RoleVO roleVO = BeanUtil.copy(roleEntity, RoleVO.class);
        return ResponseDTO.ok(roleVO);
    }

    public ResponseDTO<List<RoleVO>> getAllRole() {
        List<RoleEntity> roleEntityList = roleDao.selectList(null);
        List<RoleVO> roleList = BeanUtil.copyList(roleEntityList, RoleVO.class);
        return ResponseDTO.ok(roleList);
    }
}
