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
 * 中标开标关联表历史表(PmBidWinningOpeningRefHis)å®ä½ç±»
 *
 * @author 品讯科技
 * @since 2023-03-06 23:22:08
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pm_bid_winning_opening_ref_his")
@ApiModel(value = "中标开标关联表历史表", description = "")
public class PmBidWinningOpeningRefHis implements Serializable {
    private static final long serialVersionUID = -97173124967861825L;

    @TableId(value = "HIS_ID", type = IdType.AUTO)
    private Long hisId;


    @ApiModelProperty("主表主键")
    @TableField("ID")
    private Long id;

    @ApiModelProperty("中标ID")
    @TableField("WINNING_ID")
    private Long winningId;


    @ApiModelProperty("开标ID")
    @TableField("OPENING_ID")
    private Long openingId;

    @ApiModelProperty("需求ID")
    @TableField("DEMAND_ID")
    private Long demandId;

    @ApiModelProperty("状态：0正常，1被替换，2被质疑流标")
    @TableField("STATUS")
    private String status;


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
