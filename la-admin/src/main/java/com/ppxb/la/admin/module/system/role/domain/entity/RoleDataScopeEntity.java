package com.ppxb.la.admin.module.system.role.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "t_role_data_scope")
public class RoleDataScopeEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Integer dataScopeType;

    private Integer viewType;

    private Long roleId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
