package com.ppxb.la.admin.module.system.user.domain.vo;

import com.ppxb.la.base.common.enumeration.GenderEnum;
import com.ppxb.la.base.common.swagger.SchemaEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserVO {

    @Schema(description = "主键ID")
    private Long userId;

    @Schema(description = "登录账号")
    private String loginName;

    @SchemaEnum(GenderEnum.class)
    private Integer gender;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "手机号码")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "部门ID")
    private Long departmentId;

    @Schema(description = "部门名称")
    private String departmentName;

    @Schema(description = "角色列表")
    private List<Long> roleIdList;

    @Schema(description = "角色名称列表")
    private List<String> roleNameList;

    @Schema(description = "职务ID")
    private Long positionId;

    @Schema(description = "是否被禁用")
    private Boolean disabledFlag;

    @Schema(description = "是否为超级管理员")
    private Boolean administratorFlag;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
