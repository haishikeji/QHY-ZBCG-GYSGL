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
 * 发布公告表历史表(PmReleaseAnnouncementHis)å®ä½ç±»
 *
 * @author 品讯科技
 * @since 2023-03-06 23:22:04
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pm_release_announcement_his")
@ApiModel(value = "发布公告表历史表", description = "")
public class PmReleaseAnnouncementHis implements Serializable {
    private static final long serialVersionUID = 809634277459668205L;

    @TableId(value = "HIS_ID", type = IdType.AUTO)
    private Long hisId;


    @ApiModelProperty("主表主键")
    @TableField("ID")
    private Long id;


    @ApiModelProperty("需求ID")
    @TableField("DEMAND_ID")
    private Long demandId;


    @ApiModelProperty("发布公告平台,1:军队采购网,2:全军武器装备采购网")
    @TableField("ANNOUNCEMENT_PLATFORM")
    private String announcementPlatform;


    @ApiModelProperty("发布时间")
    @TableField("RELEASE_TIME")
    private Date releaseTime;


    @ApiModelProperty("开标地点")
    @TableField("BID_OPEN_PLACE")
    private String bidOpenPlace;


    @ApiModelProperty("报名截止时间")
    @TableField("ENROLL_CLOSE_TIME")
    private Date enrollCloseTime;


    @ApiModelProperty("开标时间")
    @TableField("TENDER_OPEN_TIME")
    private Date tenderOpenTime;


    @ApiModelProperty("备注")
    @TableField("REMARKS")
    private String remarks;


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


    @ApiModelProperty("回退序号")
    @TableField("RETURN_ORDER_NUMBER")
    private Integer returnOrderNumber;


}
