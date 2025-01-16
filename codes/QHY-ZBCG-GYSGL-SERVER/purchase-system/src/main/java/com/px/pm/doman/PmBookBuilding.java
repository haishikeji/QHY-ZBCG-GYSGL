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
 * 需求建档表(PmBookBuilding)å®ä½ç±»
 *
 * @author 品讯科技
 * @since 2023-02-08 12:50:30
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pm_book_building")
@ApiModel(value = "需求建档表", description = "")
public class PmBookBuilding implements Serializable {
    private static final long serialVersionUID = 554636460409301951L;

    @TableId(value = "ID", type = IdType.AUTO)
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

    /** 项目类型（0：装备类；1：物资类；2：服务类；3：工程类） */
    @ApiModelProperty("项目类型（0：装备类；1：物资类；2：服务类；3：工程类）")
    @TableField("PROJECT_TYPE")
    private String projectType;

    /** 采购方式  (0:公开招标, 1:单一来源,2:邀请招标,3:竞争式谈判,4:询价) */
    @ApiModelProperty("采购方式  (0:公开招标, 1:单一来源,2:邀请招标,3:竞争式谈判,4:询价)")
    @TableField("PURCHASE_MODE")
    private String purchaseMode;

    @ApiModelProperty("联系人")
    @TableField("CONTACTS")
    private String contacts;

    @ApiModelProperty("联系电话")
    @TableField("CONTACT_PHONE")
    private String contactPhone;

    @ApiModelProperty("备注")
    @TableField("REMARKS")
    private String remarks;


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
