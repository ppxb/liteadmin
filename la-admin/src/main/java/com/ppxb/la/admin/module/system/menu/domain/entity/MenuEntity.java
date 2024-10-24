package com.ppxb.la.admin.module.system.menu.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.ppxb.la.admin.module.system.menu.constant.MenuTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "t_menu")
public class MenuEntity {

    /**
     * 菜单ID
     */
    @TableId(type = IdType.AUTO)
    private Long menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单类型
     *
     * @see MenuTypeEnum
     */
    private String menuType;

    /**
     * 父级菜单ID
     */
    private Long parentId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 是否为外链
     */
    private Boolean frameFlag;

    /**
     * 外链地址
     */
    private String frameUrl;

    /**
     * 是否缓存
     */
    private Boolean cacheFlag;

    /**
     * 是否显示
     */
    private Boolean visibleFlag;

    /**
     * 是否禁用
     */
    private Boolean disabledFlag;

    /**
     * 权限类型
     */
    private Integer permsType;

    /**
     * 前端权限字符串
     */
    private String webPerms;

    /**
     * 后端权限字符串
     */
    private String apiPerms;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 功能点关联菜单ID
     */
    private Long contextMenuId;

    /**
     * 是否被删除（逻辑删除）
     */
    private Boolean deletedFlag;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Long createUserId;

    /**
     * 更新人
     */
    private Long updateUserId;
}
