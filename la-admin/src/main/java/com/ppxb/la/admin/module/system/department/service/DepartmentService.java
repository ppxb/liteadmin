package com.ppxb.la.admin.module.system.department.service;

import com.ppxb.la.admin.module.system.department.dao.DepartmentDao;
import com.ppxb.la.admin.module.system.department.domain.entity.DepartmentEntity;
import com.ppxb.la.admin.module.system.department.domain.form.DepartmentAddForm;
import com.ppxb.la.admin.module.system.department.domain.form.DepartmentUpdateForm;
import com.ppxb.la.admin.module.system.department.domain.vo.DepartmentTreeVO;
import com.ppxb.la.admin.module.system.department.domain.vo.DepartmentVO;
import com.ppxb.la.admin.module.system.department.manager.DepartmentCacheManager;
import com.ppxb.la.admin.module.system.user.dao.UserDao;
import com.ppxb.la.base.common.code.UserErrorCode;
import com.ppxb.la.base.common.domain.ResponseDTO;
import com.ppxb.la.base.common.util.BeanUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class DepartmentService {

    @Resource
    private DepartmentDao departmentDao;

    @Resource
    private UserDao userDao;

    @Resource
    private DepartmentCacheManager departmentCacheManager;

    public ResponseDTO<String> addDepartment(DepartmentAddForm addForm) {
        var addEntity = BeanUtil.copy(addForm, DepartmentEntity.class);
        departmentDao.insert(addEntity);
        departmentCacheManager.clearCache();
        return ResponseDTO.ok();
    }

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

    public ResponseDTO<String> deleteDepartment(Long id) {
        DepartmentEntity entity = departmentDao.selectById(id);
        if (entity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        int subDepartmentNum = departmentDao.countSubDepartment(id);
        if (subDepartmentNum > 0) {
            return ResponseDTO.userErrorParam("请先删除子部门");
        }

        int userNum = userDao.countByDepartmentId(id, Boolean.FALSE);
        if (userNum > 0) {
            return ResponseDTO.userErrorParam("请先删除部门员工");
        }

        departmentDao.deleteById(id);
        departmentCacheManager.clearCache();
        return ResponseDTO.ok();
    }

    public ResponseDTO<List<DepartmentTreeVO>> departmentTree() {
        List<DepartmentTreeVO> list = departmentCacheManager.getDepartmentTree();
        return ResponseDTO.ok(list);
    }

    public List<Long> selfAndChildrenIdList(Long id) {
        return departmentCacheManager.getDepartmentSelfAndChildren(id);
    }

    public List<DepartmentVO> listAll() {
        return departmentCacheManager.getDepartmentList();
    }

    public DepartmentVO getDepartmentById(Long id) {
        return departmentCacheManager.getDepartmentMap().get(id);
    }

    public String getDepartmentPath(Long id) {
        return departmentCacheManager.getDepartmentPathMap().get(id);
    }

    public List<DepartmentVO> queryAllParentDepartment(Long id) {
        List<DepartmentVO> list = new ArrayList<>();
        Map<Long, DepartmentVO> departmentMap = departmentCacheManager.getDepartmentMap();
        DepartmentVO departmentVO = departmentMap.get(id);
        while (departmentVO != null) {
            list.add(departmentVO);
            departmentVO = departmentMap.get(departmentVO.getParentId());
        }
        Collections.reverse(list);
        return list;
    }

    public List<Long> queryAllParentDepartmentIdList(Long id) {
        List<Long> list = new ArrayList<>();
        Map<Long, DepartmentVO> departmentMap = departmentCacheManager.getDepartmentMap();
        DepartmentVO departmentVO = departmentMap.get(id);
        while (departmentVO != null) {
            list.add(departmentVO.getParentId());
            departmentVO = departmentMap.get(departmentVO.getParentId());
        }
        Collections.reverse(list);
        return list;
    }
}
