package com.ppxb.la.admin.module.system.role.domain.form;

import com.ppxb.la.base.common.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RoleQueryForm extends PageParam {

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色ID")
    private String roleId;
}
