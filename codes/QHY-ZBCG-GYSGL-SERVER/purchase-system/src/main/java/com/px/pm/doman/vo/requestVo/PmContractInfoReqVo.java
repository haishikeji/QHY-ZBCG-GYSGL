package com.px.pm.doman.vo.requestVo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.px.system.domain.SysFileRef;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 合同信息表(PmContractInfo)å®ä½ç±»
 *
 * @author 品讯科技
 * @since 2023-02-02 00:47:22
 */

@Data
@ApiModel(value = "合同信息表", description = "")
public class PmContractInfoReqVo implements Serializable {
    private static final long serialVersionUID = -59348130033569267L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("需求ID")
    private Long demandId;


    @ApiModelProperty("合同编号")
    private String contractNumber;


    @ApiModelProperty("合同名称")
    private String contractName;


    @ApiModelProperty("合同金额（万元）")
    private Double contractAmount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("合同起始日期")
    private Date contractStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("合同终止日期")
    private Date contractTerminationDate;


    @ApiModelProperty("创建者")
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新者")
    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private Date updateTime;


    /**
     * 上传附件
     */
    @ApiModelProperty("上传附件")
    private List<SysFileRef> sysFileRefs;
}
