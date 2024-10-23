package com.ppxb.la.admin.module.system.role.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RoleUserVO {

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "角色名称")
    private String roleName;
}
