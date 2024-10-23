package com.ppxb.la.admin.module.system.role.domain.vo;

import com.ppxb.la.admin.module.system.menu.domain.vo.MenuSimpleTreeVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class RoleMenuTreeVO {

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "菜单列表")
    private List<MenuSimpleTreeVO> menuTreeList;

    @Schema(description = "选中的菜单ID")
    private List<Long> selectedMenuId;
}
