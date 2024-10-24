package com.ppxb.la.admin.module.system.role.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RoleMenuUpdateForm {

    @Schema(description = "角色ID")
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    @Schema(description = "菜单ID集合")
    @NotNull(message = "菜单ID不能为空")
    private List<Long> menuIdList;
}
