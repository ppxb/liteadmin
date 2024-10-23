package com.ppxb.la.admin.module.system.role.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RoleDataScopeVO {

    @Schema(description = "数据范围ID")
    private Integer dataScopeType;

    @Schema(description = "可见范围")
    private Integer viewType;
}
