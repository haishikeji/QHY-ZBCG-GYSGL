package com.px.plan.doman;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.px.common.annotation.Excel;
import com.px.common.core.domain.BaseEntity;
import com.px.system.domain.SysFileInfo;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("月度对表对象")
@TableName("pm_demand")
public class MonthlyReconciliation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /*实际完成采购时间是指采购执行管理中，点击填制中标信息，弹出框中，填写的中标时间
    * 实际提报时间就是指采购需求管理中"提交"的时间*/

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long demandId;

    /** 计划ID */
    @Excel(name = "计划ID")
    private Long planId;

    /** 采购单位（登录账号的单位） */
    @Excel(name = "采购单位Id", readConverterExp = "登=录账号的单位")
    private Long purchaseDeptId;

    /** 采购单位（登录账号的单位） */
    @Excel(name = "采购单位", readConverterExp = "登=录账号的单位")
    @TableField(exist = false)
    private String purchaseDeptName;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectName;

    /** 项目类型（0：装备类；1：物资类；2：服务类；3：工程类） */
    @Excel(name = "项目类型", readConverterExp = "0=：装备类；1：物资类；2：服务类；3：工程类")
    private String projectType;

    /** 需求概况 */
    @Excel(name = "需求概况")
    private String demandOverview;

    /** 概算金额(万元（保留小数点后两位） */
    @Excel(name = "概算金额(万元", readConverterExp = "保=留小数点后两位")
    private BigDecimal evaluation;

    /** 是否为超限额计划(0:未超额，1:超额) */
    @Excel(name = "是否为超限额计划(0:未超额，1:超额)")
    private Integer isExcess;

    /** 采购服务站 */
    @Excel(name = "采购服务站编码")
    private String purchaseServices;

    /** 采购服务站 */
    @Excel(name = "采购服务站")
    @TableField(exist = false)
    private String purchaseServicesName;

    /** 采购方式  (0:公开招标, 1:单一来源,2:邀请招标,3:竞争式谈判,4:询价) */
    @Excel(name = "采购方式  (0:公开招标, 1:单一来源,2:邀请招标,3:竞争式谈判,4:询价)")
    private String purchaseMode;

    /** 计划提报需求时间--->需求单位成功提报采购需求的日期（具体到月） */
    @Excel(name = "计划提报需求时间--->需求单位成功提报采购需求的日期", readConverterExp = "具=体到月")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date planDemandSubTime;

    /** 计划完成采购时间--->公示中标结果的日期，即填制中标信息的日期（具体到月） */
    @Excel(name = "计划完成采购时间--->公示中标结果的日期，即填制中标信息的日期", readConverterExp = "具=体到月")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date planPurchaseFinishTime;

    /** 计划交付时间--->供应商完成并交付后，使用单位收到标的日期，即填制建设文档的日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划交付时间--->供应商完成并交付后，使用单位收到标的日期，即填制建设文档的日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planDeliverTime;

    /** 项目属性（0:应急应战项目， 1:重大规划任务项目， 2:规划任务项目， 3:一般项目） */
    @Excel(name = "项目属性", readConverterExp = "0=:应急应战项目，,1=:重大规划任务项目，,2=:规划任务项目，,3=:一般项目")
    private String projectAttr;

    /** 计划调整情况及理由--->如计划需要变更，在填写申请修改时需填写调整情况及理由，审核单位审核通过后显示该信息 */
    @Excel(name = "计划调整情况及理由--->如计划需要变更，在填写申请修改时需填写调整情况及理由，审核单位审核通过后显示该信息")
    private String adjustReason;

    /** 备注 */
    @Excel(name = "备注")
    private String remarks;

    /** 实际提报需求时间--->需求单位成功提报采购需求的日期（具体到日） */
    @Excel(name = "实际提报需求时间--->需求单位成功提报采购需求的日期", readConverterExp = "具=体到日")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date realDemandCommitTime;

    /** 实际完成采购时间--->公示中标结果的日期，即填制中标信息的日期（具体到日） */
    @Excel(name = "实际完成采购时间--->公示中标结果的日期，即填制中标信息的日期", readConverterExp = "具=体到日")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date realPurchaseFinishTime;

    /** 实际交付（实施）时间--->供应商完成并交付后，使用单位收到标的日期，即填制建设文档的日期 */
    @Excel(name = "实际交付", readConverterExp = "实=施")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date realDeliverTime;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date auditTime;

    /**
     * 预警状态(0:正常，1:预警)
     */
    @Excel(name = "预警状态(0:正常，1:预警)")
    private String warnStatus;

    /**
     * 预警状态回显
     */
    @TableField(exist = false)
    private String warnStatusStr;

    /**
     * 项目状态(4:需求待填制，5:需求待提交，6:需求待审核，7:任务待下达，8:任务已下达，9:中标信息待填制，10:中标信息已填制，11:合同待填制，12:合同已填制，13:项目建设中，14:项目建设完成)
     */
    @Excel(name = "项目状态(4:需求待填制，5:需求待提交，6:需求待审核，7:任务待下达，8:任务已下达，9:中标信息待填制，10:中标信息已填制，11:合同待填制，12:合同已填制，13:项目建设中，14:项目建设完成)")
    private Long projectStatus;

    /**
     * 项目状态回显
     */
    @TableField(exist = false)
    private String projectStatusStr;

    /**
     * 退回原因
     */
    @Excel(name = "退回原因")
    private String refuseReason;

    /**
     * 滞后原因
     */
    @Excel(name = "滞后原因")
    private String delayReason;

    /**
     * 调整建议
     */
    @Excel(name = "调整建议")
    private String adjustAdvice;

    /** 抽取招标代理机构id */
    @Excel(name = "抽取招标代理机构id")
    private Long agencyId;

    /** 抽取招标代理机构时间 */
    @Excel(name = "抽取招标代理机构时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date extractAgencyTime;

    /** 附件 */
    @Excel(name = "附件")
    @TableField(exist = false)
    private List<SysFileInfo> fileList;


}
