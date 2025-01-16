package com.px.base.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.px.common.annotation.Excel;
import com.px.common.vo.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * 公告管理对象 base_notice
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("公告管理对象")
public class BaseNotice extends BaseDto {

    /** 主键id */
    @ApiModelProperty("主键ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 主键id */
    @ApiModelProperty("项目ID")
    @NotBlank(message = "项目ID")
    private Long demandId;

    /** 单位ID */
    @ApiModelProperty("单位ID")
    @NotBlank(message = "单位ID")
    private Long deptId;

    /** 超限额 */
    @ApiModelProperty("超限额")
    @NotBlank(message = "超限额")
    private Integer isExcess;

    /** 项目名称 */
    @Excel(name = "项目名称")
    @ApiModelProperty("项目名称")
    @NotBlank(message = "项目名称不能为空")
    private String name;

    /** 公告标题 */
    @Excel(name = "公告标题")
    @ApiModelProperty("公告标题")
    @NotBlank(message = "公告标题不能为空")
    private String title;

    /** 公告类型 */
    @Excel(name = "公告类型")
    @ApiModelProperty("公告类型")
    @NotEmpty(message = "公告类型不能为空")
    private Long type;

    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("发布时间")
    @NotEmpty(message = "发布时间不能为空")
    private Date releaseTime;

    /** 附件名称 */
    @Excel(name = "附件名称")
    @ApiModelProperty("附件名称")
    @NotBlank(message = "附件名称不能为空")
    private String fileName;

    /** 附件下载地址 */
    @Excel(name = "附件下载地址")
    @ApiModelProperty("附件下载地址")
    @NotBlank(message = "附件下载地址不能为空")
    private String fileUrl;

}
