package com.ppxb.la.admin.module.system.role.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "t_role_menu")
public class RoleMenuEntity {

    @TableId(type = IdType.AUTO)
    private Long roleMenuId;

    private Long roleId;

    private Long menuId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
