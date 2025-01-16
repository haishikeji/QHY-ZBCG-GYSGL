package com.px.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.px.common.annotation.Excel;
import com.px.common.vo.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 文件模板管理对象 sys_file
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("文件实体")
@TableName("sys_file")
public class SysFileInfo extends BaseDto {


    /** 主键id */
    @ApiModelProperty("主键ID")
    @TableId(type = IdType.AUTO)
    private Long fileId;

    /** 文件名称 */
    @Excel(name = "文件名称")
    @ApiModelProperty("文件名称")
    @NotBlank(message = "文件名称不能为空")
    private String fileName;

    /** 文件新名称 */
    @Excel(name = "文件新名称")
    @ApiModelProperty("文件新名称")
    private String fileNewName;

    /** 文件映射路径 */
    @Excel(name = "文件映射路径")
    @ApiModelProperty("文件映射路径")
    private String fileMappingPath;

    /** 文件下载url */
    @Excel(name = "文件下载url")
    @ApiModelProperty("文件下载url")
    private String fileUrl;


}
