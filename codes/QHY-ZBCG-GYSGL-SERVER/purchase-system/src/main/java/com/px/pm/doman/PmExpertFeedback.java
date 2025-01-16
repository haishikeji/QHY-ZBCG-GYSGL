package com.px.pm.doman;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 专家意见反馈表(PmExpertFeedback)å®ä½ç±»
 *
 * @author 品讯科技
 * @since 2023-03-05 17:19:46
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pm_expert_feedback")
@ApiModel(value = "专家意见反馈表", description = "")
public class PmExpertFeedback implements Serializable {
    private static final long serialVersionUID = 472163385609090741L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;


    @ApiModelProperty("需求ID")
    @TableField("DEMAND_ID")
    private Long demandId;


    @ApiModelProperty("文件评审时间")
    @TableField("DOCUMENT_REVIEW_TIME")
    private Date documentReviewTime;


    @ApiModelProperty("反馈时间")
    @TableField("FEEDBACK_TIME")
    private Date feedbackTime;


    @ApiModelProperty("答复时间")
    @TableField("REPLY_TIME")
    private Date replyTime;


    @ApiModelProperty("项目编号")
    @TableField("PROJECT_NUMBER")
    private String projectNumber;


    @ApiModelProperty("创建者")
    @TableField("CREATE_BY")
    private String createBy;


    @ApiModelProperty("创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;


    @ApiModelProperty("更新者")
    @TableField("UPDATE_BY")
    private String updateBy;


    @ApiModelProperty("更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;


}
