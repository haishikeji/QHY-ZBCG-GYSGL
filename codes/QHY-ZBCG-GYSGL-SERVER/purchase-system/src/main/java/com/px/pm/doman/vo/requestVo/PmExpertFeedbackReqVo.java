package com.px.pm.doman.vo.requestVo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.px.system.domain.SysFileRef;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 专家意见反馈表(PmExpertFeedback)å®ä½ç±»
 *
 * @author 品讯科技
 * @since 2023-03-05 17:19:46
 */

@Data
@ApiModel(value = "专家意见反馈表", description = "")
public class PmExpertFeedbackReqVo implements Serializable {
    private static final long serialVersionUID = 472163385609090741L;


    private Long id;


    @ApiModelProperty("需求ID")
    private Long demandId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("文件评审时间")
    private Date documentReviewTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("反馈时间")
    private Date feedbackTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("答复时间")
    private Date replyTime;


    @ApiModelProperty("项目编号")
    private String projectNumber;


    @ApiModelProperty("创建者")
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createTime;


    @ApiModelProperty("更新者")
    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 上传附件
     */
    @ApiModelProperty("上传附件")
    private List<SysFileRef> sysFileRefs;
}
