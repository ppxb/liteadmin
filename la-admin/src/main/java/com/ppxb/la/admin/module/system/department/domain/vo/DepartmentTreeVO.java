package com.ppxb.la.admin.module.system.department.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class DepartmentTreeVO extends DepartmentVO {

    @Schema(description = "同级上一个元素ID")
    private Long preId;

    @Schema(description = "同级下一个元素ID")
    private Long nextId;

    @Schema(description = "子部门")
    private List<DepartmentTreeVO> children;

    @Schema(description = "自己和所有递归子部门的ID集合")
    private List<Long> selfAndAllChildrenIdList;


}
