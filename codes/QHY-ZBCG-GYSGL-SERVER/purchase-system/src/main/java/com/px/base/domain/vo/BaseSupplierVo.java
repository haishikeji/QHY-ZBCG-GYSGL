package com.px.base.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.px.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class BaseSupplierVo {

    @Excel(name = "单位名称")
    @ApiModelProperty("供应商名称")
    private String supplierName;

    @Excel(name = "法定代表人")
    @ApiModelProperty("供应商负责人")
    private String supplierResponsiblePerson;

    @Excel(name = "单位地址")
    @ApiModelProperty("供应商地址")
    private String supplierAddress;

    @ApiModelProperty("状态 黑白名单")
    private String supplierState;

    @Excel(name = "预先采购")
    @ApiModelProperty("预先采购")
    private String supplierAdvancePurchase;

    @Excel(name = "邮政编码")
    @ApiModelProperty(value = "邮政编码")
    private String postalCode;

    @Excel(name = "项目联系人")
    @ApiModelProperty(value = "项目联系人")
    private String projectPerson;

    @Excel(name = "联系电话")
    @ApiModelProperty(value = "联系电话")
    private String telephone;

    @Excel(name = "银行账户名称")
    @ApiModelProperty(value = "银行账户名称")
    private String bankAccountName;

    @Excel(name = "开户银行")
    @ApiModelProperty(value = "开户银行")
    private String bankOfDeposit;

    @Excel(name = "银行账号")
    @ApiModelProperty(value = "银行账号")
    private String bankAccountNumber;

    @ApiModelProperty(value = "创建者")
    private String created;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改者")
    private String updated;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 当前记录起始索引
     */
    @ApiModelProperty("页数")
    private Long pageNum;
    /**
     * 每页显示记录数
     */
    @ApiModelProperty("页大小")
    private Long pageSize;

    public BaseSupplierVo() {
    }

    public BaseSupplierVo(String supplierName, String supplierResponsiblePerson, String supplierAddress, String postalCode,
                          String projectPerson, String telephone, String bankAccountName, String bankOfDeposit,
                          String bankAccountNumber, String supplierAdvancePurchase) {
        this.supplierName = supplierName;
        this.supplierResponsiblePerson = supplierResponsiblePerson;
        this.supplierAddress = supplierAddress;
        this.postalCode = postalCode;
        this.projectPerson = projectPerson;
        this.telephone = telephone;
        this.bankAccountName = bankAccountName;
        this.bankOfDeposit = bankOfDeposit;
        this.bankAccountNumber = bankAccountNumber;
        this.supplierAdvancePurchase = supplierAdvancePurchase;
    }
}
