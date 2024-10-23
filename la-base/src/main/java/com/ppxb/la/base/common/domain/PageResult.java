package com.ppxb.la.base.common.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {

    @Schema(description = "当前页")
    private Long pageNum;

    @Schema(description = "每页数量")
    private Long pageSize;

    @Schema(description = "总数")
    private Long total;

    @Schema(description = "总页数")
    private Long pages;

    @Schema(description = "结果集")
    private List<T> list;

    @Schema(description = "是否为空")
    private Boolean emptyFlag;
}
