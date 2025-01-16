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
 * 流程图表(PmFlowChart)å®ä½ç±»
 *
 * @author 品讯科技
 * @since 2023-02-04 19:36:15
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pm_flow_chart")
@ApiModel(value = "流程图表", description = "")
public class PmFlowChart implements Serializable {
    private static final long serialVersionUID = 313515206301363060L;

    @ApiModelProperty("主键")
    @TableId(value = "FLOW_CHART_ID", type = IdType.AUTO)
    private Long flowChartId;


    @ApiModelProperty("模块名称")
    @TableField("MODULE_NAME")
    private String moduleName;


    @ApiModelProperty("部门角色")
    @TableField("DEPT_ROLE")
    private String deptRole;


    @ApiModelProperty("提示文案")
    @TableField("PROMPT_WORDS")
    private String promptWords;


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
