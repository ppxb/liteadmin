package com.ppxb.la.admin.module.system.role.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RoleAddForm {

    @Schema(description = "角色名称")
    @NotNull(message = "角色名称不能为空")
    @Length(min = 1, max = 20, message = "角色名称（1-20）个字符")
    private String roleName;

    @Schema(description = "角色编码")
    @NotNull(message = "角色编码不能为空")
    @Length(min = 1, max = 20, message = "角色编码（1-20）个字符")
    private String roleCode;

    @Schema(description = "备注")
    @Length(max = 255, message = "备注最多255个字符")
    private String remark;
}
