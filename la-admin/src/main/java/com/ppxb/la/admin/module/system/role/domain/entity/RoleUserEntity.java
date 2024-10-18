package com.ppxb.la.admin.module.system.role.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_role_user")
public class RoleUserEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long roleId;

    private Long userId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public RoleUserEntity() {
    }

    public RoleUserEntity(Long roleId, Long userId) {
        this.roleId = roleId;
        this.userId = userId;
    }
}
