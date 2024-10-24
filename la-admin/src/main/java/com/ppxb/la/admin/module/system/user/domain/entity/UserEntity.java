package com.ppxb.la.admin.module.system.user.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_user")
public class UserEntity {

    @TableId(type = IdType.AUTO)
    private Long userId;

    /**
     * 登录账号
     */
    private String loginName;

    /**
     * 登录密码
     */
    private String loginPwd;

    /**
     * 姓名
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 部门ID
     */
    private Long departmentId;

    /**
     * 职务ID
     */
    private Long positionId;

    /**
     * 是否为超级管理员: 0-否 1-是
     */
    private Boolean administratorFlag;

    /**
     * 是否被禁用 0-否 1-是
     */
    private Boolean disabledFlag;

    /**
     * 是否被删除 0-否 1-是
     */
    private Boolean deletedFlag;

    /**
     * 备注
     */
    private String remark;

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
}
