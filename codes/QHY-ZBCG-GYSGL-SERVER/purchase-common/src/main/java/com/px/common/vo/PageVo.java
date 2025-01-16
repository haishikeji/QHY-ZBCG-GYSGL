package com.px.common.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("分页实体")
public class PageVo {
    /**
     * 当前记录起始索引
     */
    @ApiModelProperty("页数")
    private Long pageNum;
    /**
     * 每页显示记录数
     */
    @ApiModelProperty("页大小")
    private Long pageSize;
}
