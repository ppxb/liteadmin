package com.ppxb.la.admin.module.system.menu.domain.vo;

import com.ppxb.la.admin.module.system.menu.domain.form.MenuBaseForm;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MenuVO extends MenuBaseForm {

    @Schema(description = "菜单ID")
    private Long menuId;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "创建人")
    private Long createUserId;

    @Schema(description = "更新人")
    private Long updateUserId;
}
