package com.ppxb.la.admin.module.system.role.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class RoleUserUpdateForm {

    @Schema(description = "角色ID")
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    @Schema(description = "用户ID集合")
    @NotEmpty(message = "用户ID不能为空")
    private Set<Long> userIdList;
}
