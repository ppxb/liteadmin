package com.ppxb.la.admin.module.system.department.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DepartmentVO {

    @Schema(description = "部门ID")
    private Long departmentId;

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "部门负责人姓名")
    private String managerName;

    @Schema(description = "部门负责人ID")
    private Long managerId;

    @Schema(description = "父级部门ID")
    private Long parentId;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
