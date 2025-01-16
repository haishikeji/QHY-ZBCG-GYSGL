package com.px.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 品讯科技
 * 采购标准
 */
@Data
@TableName("sys_procurement_standard")
public class SysProcurementStandard implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "采购标准ID")
    @TableField("procurement_standard_id")
    @TableId(type = IdType.AUTO)
    private Long procurementStandardId;

    @ApiModelProperty(value = "部门ID")
    @TableField("dept_id")
    private Long deptId;

    /** 类别(0装备类1服务类2物资类3工程类) */
    @ApiModelProperty(value = "类别")
    @TableField("category")
    private Integer category;

    /** 状态(0不限1限额) */
    @ApiModelProperty(value = "状态")
    @TableField("state")
    private Integer state;

    @ApiModelProperty(value = "最小值(万元)")
    @TableField("minimum_value")
    private BigDecimal minimumValue;

    @ApiModelProperty(value = "最大值(万元)")
    @TableField("maximum")
    private BigDecimal maximum;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    private Date updateTime;
}
