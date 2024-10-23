package com.ppxb.la.base.common.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class PageParam {

    @Schema(description = "页码", example = "1")
    @NotNull(message = "页码不能为空")
    private Long pageNum;

    @Schema(description = "每页数量", example = "10")
    @NotNull(message = "每页数量不能为空")
    @Max(value = 500, message = "每页数量最大不能超过500")
    private Long pageSize;

    @Schema(description = "是否查询总条数")
    private Boolean searchCount;

    @Schema(description = "排序字段集合")
    @Size(max = 10, message = "排序字段集合不能超过10个")
    @Valid
    private List<SortItem> sortItemList;


    @Data
    public static class SortItem {

        @Schema(description = "true 正序 | false 倒序")
        @NotNull(message = "排序规则不能为空")
        private Boolean isAsc;

        @Schema(description = "排序字段")
        @NotBlank(message = "排序字段不能为空")
        @Length(max = 30, message = "排序字段不能超过30个字符")
        private String column;
    }
}
