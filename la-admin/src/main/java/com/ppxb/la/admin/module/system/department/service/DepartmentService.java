package com.ppxb.la.admin.module.system.department.service;

import com.ppxb.la.admin.module.system.department.dao.DepartmentDao;
import com.ppxb.la.admin.module.system.department.domain.entity.DepartmentEntity;
import com.ppxb.la.admin.module.system.department.domain.form.DepartmentAddForm;
import com.ppxb.la.admin.module.system.department.domain.form.DepartmentUpdateForm;
import com.ppxb.la.admin.module.system.department.manager.DepartmentCacheManager;
import com.ppxb.la.admin.module.system.user.dao.UserDao;
import com.ppxb.la.base.common.code.UserErrorCode;
import com.ppxb.la.base.common.domain.ResponseDTO;
import com.ppxb.la.base.common.util.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentDao departmentDao;

    private final UserDao userDao;

    private final DepartmentCacheManager departmentCacheManager;

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> addDepartment(DepartmentAddForm addForm) {
        var addEntity = BeanUtil.copy(addForm, DepartmentEntity.class);
        departmentDao.insert(addEntity);
        departmentCacheManager.clearCache();
        return ResponseDTO.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateDepartment(DepartmentUpdateForm updateForm) {
        if (updateForm.getParentId() == null) {
            return ResponseDTO.userErrorParam("父级部门ID不能为空");
        }

        var entity = departmentDao.selectById(updateForm.getDepartmentId());
        if (entity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        var updateEntity = BeanUtil.copy(updateForm, DepartmentEntity.class);
        departmentDao.updateById(updateEntity);
        departmentCacheManager.clearCache();
        return ResponseDTO.ok();
    }

    // TODO: 开始删除逻辑
}
