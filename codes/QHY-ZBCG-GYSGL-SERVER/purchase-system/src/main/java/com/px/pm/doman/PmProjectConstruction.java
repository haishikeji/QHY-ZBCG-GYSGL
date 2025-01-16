package com.px.pm.doman;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 项目建设表(PmProjectConstruction)å®ä½ç±»
 *
 * @author 品讯科技
 * @since 2023-02-02 00:47:23
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pm_project_construction")
@ApiModel(value = "项目建设表", description = "")
public class PmProjectConstruction implements Serializable {
    private static final long serialVersionUID = -98627692890895867L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;


    @ApiModelProperty("需求ID")
    @TableField("DEMAND_ID")
    private Long demandId;


    @ApiModelProperty("开始时间")
    @TableField("START_TIME")
    private Date startTime;


    @ApiModelProperty("初步验收时间")
    @TableField("PRELIMINARY_INSPECTION_TIME")
    private Date preliminaryInspectionTime;


    @ApiModelProperty("终验时间")
    @TableField("FINAL_INSPECTION_TIME")
    private Date finalInspectionTime;


    @ApiModelProperty("结束时间")
    @TableField("END_TIME")
    private Date endTime;


    @ApiModelProperty("创建者")
    @TableField("CREATE_BY")
    private String createBy;


    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_TIME")
    private Date createTime;


    @ApiModelProperty("更新者")
    @TableField("UPDATE_BY")
    private String updateBy;


    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("UPDATE_TIME")
    private Date updateTime;


}
