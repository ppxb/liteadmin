package com.ppxb.la.admin.module.system.role.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RoleUserQueryForm {

    @Schema(description = "关键字")
    private String keywords;

    @Schema(description = "角色ID")
    private String roleId;

}
