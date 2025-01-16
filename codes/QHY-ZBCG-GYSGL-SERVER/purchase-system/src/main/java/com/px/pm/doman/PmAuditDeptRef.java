package com.px.pm.doman;

import java.math.BigDecimal;
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
 * 审核关联表(PmAuditDeptRef)å®ä½ç±»
 *
 * @author 品讯科技
 * @since 2023-02-22 22:10:02
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pm_audit_dept_ref")
@ApiModel(value = "审核关联表", description = "")
public class PmAuditDeptRef implements Serializable {
    private static final long serialVersionUID = -97141395683787484L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;


    @ApiModelProperty("关联主键id")
    @TableField("REF_ID")
    private Long refId;


    @ApiModelProperty("关联表(0:plan_year_id，1:plan_practical_id，2:demand_id)")
    @TableField("REF_TYPE")
    private String refType;


    @ApiModelProperty("单位ID（登录账号的单位）")
    @TableField("DEPT_ID")
    private Long deptId;

    @TableField(exist = false)
    private String deptName;


    @ApiModelProperty("单位级别(A:最低级，B:中间，C:最高级)")
    @TableField("DEPT_LEVEL")
    private String deptLevel;


    @ApiModelProperty("项目状态(0:待审核，1:已审核)")
    @TableField("STATUS")
    private Integer status;

    @TableField(exist = false)
    private String statusStr;

    @ApiModelProperty("创建者")
    @TableField("CREATED")
    private String created;


    @ApiModelProperty("创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;


    @ApiModelProperty("修改者")
    @TableField("UPDATED")
    private String updated;


    @ApiModelProperty("修改时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField(exist = false)
    private BigDecimal evaluation;

    @TableField(exist = false)
    private String projectType;

    public PmAuditDeptRef() {
    }

    public PmAuditDeptRef(Long id, Long refId, String refType, Long deptId, String deptLevel, Integer status, String created, Date createTime, String updated, Date updateTime) {
        this.id = id;
        this.refId = refId;
        this.refType = refType;
        this.deptId = deptId;
        this.deptLevel = deptLevel;
        this.status = status;
        this.created = created;
        this.createTime = createTime;
        this.updated = updated;
        this.updateTime = updateTime;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }

    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptLevel() {
        return deptLevel;
    }

    public void setDeptLevel(String deptLevel) {
        this.deptLevel = deptLevel;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public BigDecimal getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(BigDecimal evaluation) {
        this.evaluation = evaluation;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }
}
