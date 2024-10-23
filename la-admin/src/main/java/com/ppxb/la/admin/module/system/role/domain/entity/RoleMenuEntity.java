package com.ppxb.la.admin.module.system.role.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "t_role_menu")
public class RoleMenuEntity {

    /**
     * 角色目录ID
     */
    @TableId(type = IdType.AUTO)
    private Long roleMenuId;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 目录ID
     */
    private Long menuId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
