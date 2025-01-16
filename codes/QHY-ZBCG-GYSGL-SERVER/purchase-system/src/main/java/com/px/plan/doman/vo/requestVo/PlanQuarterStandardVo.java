package com.px.plan.doman.vo.requestVo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.px.common.annotation.Excel;
import com.px.common.vo.PageVo;
import com.px.system.domain.SysFileRef;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 品讯科技
 */
@Data
public class PlanQuarterStandardVo extends PageVo {

    /**
     * 主键编号
     */
    @ApiModelProperty(value = "主键编号")
    private Long planPracticalId;
    /**
     * 年度计划id（年度计划审批后同步到季度计划）
     */
    private Long planYearId;

    /**
     * 采购单位（登录账号的单位）
     */
    @Excel(name = "采购单位")
    @ApiModelProperty(value = "采购单位")
    private String purchaseDeptId;
    /**
     * 部门ID
     */
    private Long deptId;
    /**
     * 项目名称
     */
    @Excel(name = "项目名称")
    @ApiModelProperty(value = "项目名称")
    private String projectName;

    /**
     * 项目类型（1：装备类；2：物资类；3：服务类；）
     */
    @Excel(name = "项目类型")
    @ApiModelProperty(value = "项目类型不能为空（1：装备类；2：物资类；3：服务类；）")
    private String projectType;

    /**
     * 需求概况
     */
    @Excel(name = "需求概况")
    @ApiModelProperty(value = "需求概况")
    private String demandOverview;

    /**
     * 概算金额(万元（保留小数点后两位）
     */
    @Excel(name = "概算金额（万元）")
    @ApiModelProperty(value = "概算金额")
    private BigDecimal evaluation;

    /**
     * 采购服务站
     */
    @Excel(name = "采购服务站")
    @ApiModelProperty(value = "采购服务站")
    private String purchaseServices;

    /**
     * 机关业务指导处(科)
     */
    private String organDivision;
    /**
     * 预算科目
     */
    private String budgetAccount;
    /**
     * 计划提报需求时间--->需求单位成功提报采购需求的日期（具体到月）
     */
    @Excel(name = "计划提报需求时间", dateFormat = "yyyy-MM")
    @ApiModelProperty(value = "计划提报需求时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date planDemandSubTime;

    /**
     * 计划完成采购时间--->公示中标结果的日期，即填制中标信息的日期（具体到月）
     */
    @Excel(name = "计划完成采购时间", dateFormat = "yyyy-MM")
    @ApiModelProperty(value = "计划完成采购时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date planPurchaseFinishTime;

    /**
     * 计划交付时间--->供应商完成并交付后，使用单位收到标的日期，即填制建设文档的日期
     */
    @Excel(name = "计划交付（实施）时间", dateFormat = "yyyy-MM")
    @ApiModelProperty(value = "计划交付时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date planDeliverTime;

    /**
     * 项目属性--->应急应战项目、重大规划任务项目、规划任务项目或一般项目
     */
    @Excel(name = "项目属性")
    @ApiModelProperty(value = "项目属性")
    private String projectAttr;

    /**
     * 项目状态--->包括计划待提交、计划待审核、计划已退回、计划已审核
     */
    @ApiModelProperty(value = "项目状态(0:计划待提交，1:计划待审核，2:计划已退回，3:计划已审核)")
    private String projectStatus;
    /**
     * 发函内容
     */
    @ApiModelProperty(value = "发函内容")
    private String letterStr;
    /**
     * 退回原因--->审核不通过就是退回
     */
    @ApiModelProperty(value = "退回原因")
    private String refuseReason;
    /**
     * 滞后原因
     */
    @ApiModelProperty(value = "滞后原因")
    private String delayReason;
    /**
     * 备注
     */
    @Excel(name = "备注")
    @ApiModelProperty(value = "备注")
    private String remarks;

    /**
     * 计划调整情况及理由--->如计划需要变更，在填写申请修改时需填写调整情况及理由，审核单位审核通过后显示
     */
    @Excel(name = "计划调整情况及理由")
    private String adjustReason;

    /**
     * 创建者
     */
    private String created;

    /**
     * 更改者
     */
    private String updated;

    /**
     * 上传附件
     */
    private List<SysFileRef> sysFileRefs;

    ///**
    // * 查询参数开始时间和结束时间
    // */
    //@JsonFormat(pattern = "yyyy-MM")
    //private Date beginTime;
    //@JsonFormat(pattern = "yyyy-MM")
    //private Date endTime;
    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ApiModelProperty("请求参数")
    private Map<String, Object> params;
}
