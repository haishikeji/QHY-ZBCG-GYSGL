package com.px.pm.doman.vo.responseVo;



import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 流标基本情况表(PmBidFailure)å®ä½ç±»
 *
 * @author 品讯科技
 * @since 2023-02-02 00:47:21
 */

@Data
public class PmBidFailureResVo implements Serializable {
    private static final long serialVersionUID = 491684278331772388L;

    private Long id;


    @ApiModelProperty("需求ID")
    private Long demandId;


    @ApiModelProperty("流标原因")
    private String bidFailureReason;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("流标时间")
    private Date bidFailureTime;

    @ApiModelProperty("是否有质疑，1是，0否")
    private String callQuestion;

    @ApiModelProperty("质疑时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date callQuestionTime;

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

}
