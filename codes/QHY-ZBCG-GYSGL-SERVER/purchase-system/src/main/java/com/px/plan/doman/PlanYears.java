package com.px.plan.doman;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import nonapi.io.github.classgraph.json.Id;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 品讯科技
 */
@Data
public class PlanYears {

    /**
     * 主键编号
     */
    @Id
    @TableId(type = IdType.AUTO)
    private Long planYearId;
    /**
     * 采购单位（登录账号的单位）
     */
    @NotNull(message = "采购单位不能为空")
    private String purchaseDeptId;

    @TableField(exist = false)
    private String purchaseDeptName;
    /**
     * 项目名称
     */
    @NotNull(message = "项目名称不能为空")
    private String projectName;
    /**
     * 项目类型（0：装备类；1：物资类；2：服务类；3：工程类）
     */
    @NotNull(message = "项目类型不能为空")
    private String projectType;
    @TableField(exist = false)
    private String projectTypeStr;
    /**
     * 需求概况
     */
    @NotNull(message = "需求概况不能为空")
    private String demandOverview;
    /**
     * 概算金额(万元（保留小数点后两位）
     */
    @NotNull(message = "概算金额不能为空")
    private BigDecimal evaluation;
    /**
     * 是否为超限额计划(0:未超额，1:超额)
     */
    private String isExcess;
    /**
     * 采购服务站
     */
    @NotNull(message = "采购服务站不能为空")
    private String purchaseServices;
    @TableField(exist = false)
    private String purchaseServicesStr;
    /**
     * 机关业务指导处(科)
     */
    private String organDivision;
    @TableField(exist = false)
    private String organDivisionStr;
    /**
     * 预算科目
     */
    private String budgetAccount;
    @TableField(exist = false)
    private String budgetAccountStr;

    /**
     * 计划提报需求时间--->需求单位成功提报采购需求的日期（具体到月）
     */
    @NotNull(message = "计划提报需求时间不能为空")
    @JsonFormat(pattern = "yyyy-MM")
    private Date planDemandSubTime;
    /**
     * 计划完成采购时间--->公示中标结果的日期，即填制中标信息的日期（具体到月）
     */
    @NotNull(message = "计划完成采购时间不能为空")
    @JsonFormat(pattern = "yyyy-MM")
    private Date planPurchaseFinishTime;
    /**
     * 计划交付时间--->供应商完成并交付后，使用单位收到标的日期，即填制建设文档的日期
     */
    @NotNull(message = "计划交付时间不能为空")
    @JsonFormat(pattern = "yyyy-MM")
    private Date planDeliverTime;
    /**
     * 项目属性--->重点采购项目、重大规划任务项目、规划任务项目或一般采购项目
     */
    @NotNull(message = "项目属性不能为空")
    private String projectAttr;

    @TableField(exist = false)
    private String projectAttrName;
    /**
     * 退回原因--->审核不通过就是退回
     */
    private String refuseReason;
    /**
     * 滞后原因
     */
    private String delayReason;
    /**
     * 计划调整情况及理由--->如计划需要变更，在填写申请修改时需填写调整情况及理由，审核单位审核通过后显示
     */
    @NotNull(message = "计划调整情况及理由不能为空")
    private String adjustReason;
    /**
     * 备注
     */
    private String remarks;

    /**
     * 项目状态--->包括计划待提交、计划待审核、计划已退回、计划已审核
     * (0:计划待提交，1:计划待审核，2:计划已退回，3:计划已审核)
     */
    private String projectStatus;
    @TableField(exist = false)
    private String projectStatusStr;
    /**
     * 处理申请撤销
     * 请求根据字段 appDelete 判断，值是true就是已申请
     */
    private String appDelete;
    /**
     * 是否可以申请撤销
     * 请求根据字段 appDeleteDo 判断，值是true就是可以申请
     */
    @TableField(exist = false)
    private String appDeleteDo;
    /**
     * 发函状态（0：未发函或已处理，1：确认发函）
     */
    private String sendLetter;
    /**
     * 发函内容
     */
    private String letterStr;
    /**
     * 发函时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date letterTime;

    /**
     * 创建者
     */
    private String created;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 更改者
     */
    private String updated;
    /**
     * 更改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 逻辑删除字段--->（0：正常，1：已删除）
     */
    private Integer delFlay;

    /**
     * 是否显示审核按钮
     */
    @TableField(exist = false)
    private Boolean flag;
    /**
     * 查询参数开始时间和结束时间
     */
    @JsonFormat(pattern = "yyyy-MM")
    @TableField(exist = false)
    private Date beginTime;
    @JsonFormat(pattern = "yyyy-MM")
    @TableField(exist = false)
    private Date endTime;

}
