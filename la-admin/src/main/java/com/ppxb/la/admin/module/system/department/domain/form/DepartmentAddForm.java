package com.ppxb.la.admin.module.system.department.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class DepartmentAddForm {

    @Schema(description = "部门名称")
    @Length(min = 1, max = 50, message = "请输入正确的部门名称（1-50字符）")
    @NotNull(message = "请输入正确的部门名称（1-50字符）")
    private String name;

    @Schema(description = "排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @Schema(description = "部门负责人ID")
    private Long managerId;

    @Schema(description = "父级部门ID")
    private Long parentId;
}
