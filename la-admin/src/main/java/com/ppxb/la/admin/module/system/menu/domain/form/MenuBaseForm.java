package com.ppxb.la.admin.module.system.menu.domain.form;

import com.ppxb.la.admin.module.system.menu.constant.MenuPermsTypeEnum;
import com.ppxb.la.admin.module.system.menu.constant.MenuTypeEnum;
import com.ppxb.la.base.common.swagger.SchemaEnum;
import com.ppxb.la.base.common.validator.enumeration.CheckEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class MenuBaseForm {

    @Schema(description = "菜单名称")
    @NotBlank(message = "菜单名称不能为空")
    @Length(max = 30, message = "菜单名称最多30个字符")
    private String menuName;

    @SchemaEnum(value = MenuTypeEnum.class, desc = "菜单类型")
    @CheckEnum(value = MenuTypeEnum.class, message = "菜单类型错误")
    private Integer menuType;

    @Schema(description = "父级菜单ID，无父级菜单则传0")
    @NotNull(message = "父级菜单ID不能为空")
    private Long parentId;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "路由地址")
    private String path;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "是否为外链")
    @NotNull(message = "是否为外链不能为空")
    private Boolean frameFlag;

    @Schema(description = "外链地址")
    private String frameUrl;

    @Schema(description = "是否缓存")
    @NotNull(message = "是否缓存不能为空")
    private Boolean cacheFlag;

    @Schema(description = "是否显示")
    @NotNull(message = "是否显示不能为空")
    private Boolean visibleFlag;

    @Schema(description = "是否禁用")
    @NotNull(message = "是否禁用不能为空")
    private Boolean disabledFlag;

    @SchemaEnum(value = MenuPermsTypeEnum.class, desc = "权限类型")
    @CheckEnum(value = MenuPermsTypeEnum.class, message = "权限类型")
    private Integer permsType;

    @Schema(description = "前端权限字符串")
    private String webPerms;

    @Schema(description = "后端权限字符串")
    private String apiPerms;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "功能点关联菜单ID")
    private Long contentMenuId;
}
