package com.ppxb.la.admin.module.system.menu.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class MenuPointsOperationForm {

    @Schema(description = "菜单ID")
    private Long menuId;

    @Schema(description = "功能点名称")
    @NotBlank(message = "功能点名称不能为空")
    @Length(max = 30, message = "功能点名称最多30个字符")
    private String menuName;

    @Schema(description = "是否禁用")
    @NotNull(message = "是否禁用不能为空")
    private Boolean disabledFlag;

    @Schema(description = "后端接口权限结合")
    private List<String> apiPermsList;

    @Schema(description = "权限字符串")
    private String webPerms;

    @Schema(description = "功能点关联菜单ID")
    private Long contextMenuId;
}
