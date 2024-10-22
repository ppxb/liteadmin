package com.ppxb.la.admin.module.system.role.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "t_role")
public class RoleEntity {

    @TableId(type = IdType.AUTO)
    private Long roleId;

    private String roleName;

    private String roleCode;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
