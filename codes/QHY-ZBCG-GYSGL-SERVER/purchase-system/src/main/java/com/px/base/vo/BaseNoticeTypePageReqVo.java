package com.px.base.vo;

import com.px.common.constant.ModularConstans;
import com.px.common.vo.PageVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "BaseNoticeTypePageReqVo", description = ModularConstans.noticeType+ "分页查询实体")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseNoticeTypePageReqVo extends PageVo {

    @ApiModelProperty("公告类型名称")
    private String name;

}
