package com.ppxb.la.admin.module.system.department.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
