package com.ppxb.la.admin.module.system.role.service;

import com.google.common.collect.Lists;
import com.ppxb.la.admin.module.system.role.domain.entity.RoleDataScopeEntity;
import com.ppxb.la.admin.module.system.role.domain.form.RoleDataScopeUpdateForm;
import com.ppxb.la.admin.module.system.role.domain.vo.RoleDataScopeVO;
import com.ppxb.la.admin.module.system.role.manager.RoleDataScopeManager;
import com.ppxb.la.base.common.code.UserErrorCode;
import com.ppxb.la.base.common.domain.ResponseDTO;
import com.ppxb.la.base.common.util.BeanUtil;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleDataScopeService {

    @Resource
    private RoleDataScopeManager roleDataScopeManager;

    public ResponseDTO<List<RoleDataScopeVO>> getRoleDataScopeList(Long roleId) {
        List<RoleDataScopeEntity> roleDataScopeEntityList = roleDataScopeManager.getBaseMapper().listByRoleId(roleId);
        if (CollectionUtils.isEmpty(roleDataScopeEntityList)) {
            return ResponseDTO.ok(Lists.newArrayList());
        }

        List<RoleDataScopeVO> roleDataScopeVOList = BeanUtil.copyList(roleDataScopeEntityList, RoleDataScopeVO.class);
        return ResponseDTO.ok(roleDataScopeVOList);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateRoleDataScopeList(RoleDataScopeUpdateForm updateForm) {
        List<RoleDataScopeUpdateForm.RoleUpdateDataScopeListFormItem> batchSetList = updateForm.getDataScopeItemList();
        if (CollectionUtils.isEmpty(batchSetList)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR);
        }

        List<RoleDataScopeEntity> roleDataScopeEntityList = BeanUtil.copyList(batchSetList, RoleDataScopeEntity.class);
        roleDataScopeEntityList.forEach(entity -> entity.setRoleId(updateForm.getRoleId()));
        roleDataScopeManager.getBaseMapper().deleteByRoleId(updateForm.getRoleId());
        roleDataScopeManager.saveBatch(roleDataScopeEntityList);
        return ResponseDTO.ok();
    }
}
