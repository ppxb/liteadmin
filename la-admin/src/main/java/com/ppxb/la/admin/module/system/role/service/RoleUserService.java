package com.ppxb.la.admin.module.system.role.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.ppxb.la.admin.module.system.department.dao.DepartmentDao;
import com.ppxb.la.admin.module.system.department.domain.entity.DepartmentEntity;
import com.ppxb.la.admin.module.system.role.dao.RoleDao;
import com.ppxb.la.admin.module.system.role.dao.RoleUserDao;
import com.ppxb.la.admin.module.system.role.domain.entity.RoleEntity;
import com.ppxb.la.admin.module.system.role.domain.entity.RoleUserEntity;
import com.ppxb.la.admin.module.system.role.domain.form.RoleUserQueryForm;
import com.ppxb.la.admin.module.system.role.domain.form.RoleUserUpdateForm;
import com.ppxb.la.admin.module.system.role.domain.vo.RoleSelectedVO;
import com.ppxb.la.admin.module.system.role.domain.vo.RoleVO;
import com.ppxb.la.admin.module.system.role.manager.RoleUserManager;
import com.ppxb.la.admin.module.system.user.domain.vo.UserVO;
import com.ppxb.la.base.common.constant.StringConstant;
import com.ppxb.la.base.common.domain.PageResult;
import com.ppxb.la.base.common.domain.ResponseDTO;
import com.ppxb.la.base.common.util.BeanUtil;
import com.ppxb.la.base.common.util.PageUtil;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleUserService {

    @Resource
    private RoleUserDao roleUserDao;

    @Resource
    private RoleDao roleDao;

    @Resource
    private DepartmentDao departmentDao;

    @Resource
    private RoleUserManager roleUserManager;

    /**
     * 批量插入
     */
    public void batchInsert(List<RoleUserEntity> roleUserEntityList) {
        roleUserManager.saveBatch(roleUserEntityList);
    }

    /**
     * 通过角色id，分页获取成员员工列表
     */
    public ResponseDTO<PageResult<UserVO>> queryUser(RoleUserQueryForm queryForm) {
        Page<?> page = PageUtil.convert2PageQuery(queryForm);
        List<UserVO> userVOList = roleUserDao.selectRoleUserByName(page, queryForm)
                .stream()
                .filter(Objects::nonNull)
                .toList();
        List<Long> departmentIdList = userVOList
                .stream()
                .filter(e -> e != null && e.getDepartmentId() != null)
                .map(UserVO::getDepartmentId)
                .toList();
        if (CollectionUtils.isNotEmpty(departmentIdList)) {
            List<DepartmentEntity> departmentEntityList = departmentDao.selectBatchIds(departmentIdList);
            Map<Long, String> departmentIdNameMap = departmentEntityList
                    .stream()
                    .collect(Collectors.toMap(DepartmentEntity::getDepartmentId, DepartmentEntity::getName));
            userVOList.forEach(e -> {
                e.setDepartmentName(departmentIdNameMap.getOrDefault(e.getDepartmentId(), StringConstant.EMPTY));
            });
        }
        PageResult<UserVO> pageResult = PageUtil.convert2PageResult(page, userVOList, UserVO.class);
        return ResponseDTO.ok(pageResult);
    }

    public List<UserVO> getAllUserByRoleId(Long roleId) {
        return roleUserDao.selectUserByRoleId(roleId);
    }

    /**
     * 移除员工角色
     */
    public ResponseDTO<String> removeRoleUser(Long userId, Long roleId) {
        if (userId == null || roleId == null) {
            return ResponseDTO.userErrorParam();
        }

        roleUserDao.deleteByUserIdRoleId(userId, roleId);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除角色的成员员工
     */
    public ResponseDTO<String> batchRemoveRoleUser(RoleUserUpdateForm updateForm) {
        roleUserDao.batchDeleteUserRole(updateForm.getRoleId(), updateForm.getUserIdList());
        return ResponseDTO.ok();
    }

    /**
     * 批量添加角色的成员员工
     */
    public ResponseDTO<String> batchAddRoleUser(RoleUserUpdateForm updateForm) {
        Long roleId = updateForm.getRoleId();
        Set<Long> selectedUserIdList = updateForm.getUserIdList();
        Set<Long> dbUserIdList = roleUserDao.selectUserIdByRoleIdList(Lists.newArrayList());
        Set<Long> addUserIdList = selectedUserIdList
                .stream()
                .filter(id -> !dbUserIdList.contains(id)).collect(Collectors.toSet());

        if (CollectionUtils.isNotEmpty(addUserIdList)) {
            List<RoleUserEntity> roleUserEntityList = addUserIdList
                    .stream()
                    .map(userId -> new RoleUserEntity(roleId, userId))
                    .toList();
            roleUserManager.saveBatch(roleUserEntityList);
        }
        return ResponseDTO.ok();
    }

    /**
     * 通过员工id获取员工角色
     */
    public List<RoleSelectedVO> getRoleInfoListByUserId(Long userId) {
        List<Long> roleIds = roleUserDao.selectRoleIdByUserId(userId);
        List<RoleEntity> roleEntityList = roleDao.selectList(null);
        List<RoleSelectedVO> result = BeanUtil.copyList(roleEntityList, RoleSelectedVO.class);
        result.forEach(item -> item.setSelected(roleIds.contains(item.getRoleId())));
        return result;
    }

    /**
     * 根据员工id 查询角色id集合
     */
    public List<RoleVO> getRoleIdList(Long userId) {
        return roleUserDao.selectRoleByUserId(userId);
    }

//    TODO:ROLE SERVICE TO BEGIN NEXT WORK
}
