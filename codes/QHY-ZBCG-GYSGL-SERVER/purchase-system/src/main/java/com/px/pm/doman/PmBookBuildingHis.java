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
 * 需求建档表历史表(PmBookBuildingHis)å®ä½ç±»
 *
 * @author 品讯科技
 * @since 2023-03-06 23:22:07
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pm_book_building_his")
@ApiModel(value = "需求建档表历史表", description = "")
public class PmBookBuildingHis implements Serializable {
    private static final long serialVersionUID = -82887734072906881L;

    @TableId(value = "HIS_ID", type = IdType.AUTO)
    private Long hisId;


    @ApiModelProperty("主表主键")
    @TableField("ID")
    private Long id;


    @ApiModelProperty("需求ID")
    @TableField("DEMAND_ID")
    private Long demandId;


    @ApiModelProperty("预算金额(万元)")
    @TableField("BUDGET_AMOUNT")
    private Double budgetAmount;


    @ApiModelProperty("需求提报时间")
    @TableField("DEMAND_COMMIT_TIME")
    private Date demandCommitTime;


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


    @ApiModelProperty("联系人")
    @TableField("CONTACTS")
    private String contacts;


    @ApiModelProperty("联系电话")
    @TableField("CONTACT_PHONE")
    private String contactPhone;


    @ApiModelProperty("项目类型（0：装备类；1：物资类；2：服务类；3：工程类）")
    @TableField("PROJECT_TYPE")
    private String projectType;


    @ApiModelProperty("采购方式  (0:公开招标, 1:单一来源,2:邀请招标,3:竞争式谈判,4:询价)")
    @TableField("PURCHASE_MODE")
    private String purchaseMode;


    @ApiModelProperty("回退序号")
    @TableField("RETURN_ORDER_NUMBER")
    private Integer returnOrderNumber;


}
