package com.px.base.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author : sunhh
 * @create 2023/2/2 17:59
 */
@Data
@TableName("base_unit_information")
public class BaseUnitInformation {
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "id")
    @TableField("id")
    private Integer id;

    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    @TableField("unit_name")
    private String unitName;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("created")
    private String created;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;
}
