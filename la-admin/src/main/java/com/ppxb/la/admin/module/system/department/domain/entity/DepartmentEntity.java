package com.ppxb.la.admin.module.system.department.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "t_department")
public class DepartmentEntity {

    @TableId(type = IdType.AUTO)
    private Long departmentId;

    private String name;

    private Long managerId;

    private Long parentId;

    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
