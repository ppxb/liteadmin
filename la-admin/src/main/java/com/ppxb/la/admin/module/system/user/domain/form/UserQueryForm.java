package com.ppxb.la.admin.module.system.user.domain.form;

import com.ppxb.la.base.common.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryForm extends PageParam {

    @Schema(description = "关键字")
    @Length(max = 20, message = "关键字不能超过20个字符")
    private String keywords;

    @Schema(description = "部门ID")
    private Long departmentId;

    @Schema(description = "是否被禁用")
    private Boolean disabledFlag;

    @Schema(description = "用户ID集合")
    @Size(max = 99, message = "最多查询99个用户")
    private List<Long> userIdList;

    @Schema(description = "是否被删除", hidden = true)
    private Boolean deletedFlag;
}

//TODO: start user form