package com.ppxb.la.admin.module.system.user.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_user")
public class UserEntity {

    @TableId(type = IdType.AUTO)
    private Long userId;

    private String loginName;

    private String loginPwd;

    private String name;

    private String avatar;

    private Integer gender;

    private String phone;

    private String email;

    private Long departmentId;

    private Long positionId;

    private Boolean administratorFlag;

    private Boolean disabledFlag;

    private Boolean deletedFlag;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
