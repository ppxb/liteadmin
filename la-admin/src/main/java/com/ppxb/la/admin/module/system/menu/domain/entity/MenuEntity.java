package com.ppxb.la.admin.module.system.menu.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "t_menu")
public class MenuEntity {

    @TableId(type = IdType.AUTO)
    private Long menuId;

    private String menuName;

    private String menuType;

    private Long parentId;

    private Integer sort;

    private String path;

    private String component;

    private Boolean frameFlag;

    private String frameUrl;

    private Boolean cacheFlag;

    private Boolean visibleFlag;

    private Boolean disabledFlag;

    private Integer permsType;

    private String apiPerms;

    private String webPerms;

    private String icon;

    private Long contextMenuId;

    private Boolean deletedFlag;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUserId;

    private Long updateUserId;
}
