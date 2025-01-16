package com.px.system.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 行政区划表(GtAdministrativeDivision)å®ä½ç±»
 *
 * @author 品讯科技
 * @since 2022-05-27 10:06:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_region")
@ApiModel(value = "行政区划表", description = "")
public class SysRegion implements Serializable {

    private static final long serialVersionUID = -29268323465542897L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;


    @ApiModelProperty("行政区划")
    @TableField("ADMINISTRATIVE_DIVISION")
    private String administrativeDivision;


    @ApiModelProperty("父节点")
    @TableField("PID")
    private Long pid;


    @ApiModelProperty("代码")
    @TableField("CODE")
    private String code;


    @ApiModelProperty("等级(0中国,1省/直辖市,2地级市,3区县)")
    @TableField("LEVEL")
    private Integer level;


}
