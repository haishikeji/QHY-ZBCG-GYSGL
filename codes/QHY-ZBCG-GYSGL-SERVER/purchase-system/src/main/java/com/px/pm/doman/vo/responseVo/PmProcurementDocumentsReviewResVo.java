package com.px.pm.doman.vo.responseVo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.px.system.domain.SysFileInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 采购文件编制审核表(PmProcurementDocumentsReview)å®ä½ç±»
 *
 * @author 品讯科技
 * @since 2023-03-05 17:24:32
 */

@Data
@ApiModel(value = "采购文件编制审核表", description = "")
public class PmProcurementDocumentsReviewResVo implements Serializable {
    private static final long serialVersionUID = -21768252534981356L;

    private Long id;


    @ApiModelProperty("需求ID")
    private Long demandId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("采购办审批时间")
    private Date procurementOfficeApprovalTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("需求单位审批时间")
    private Date demandUnitApprovalTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("采购管理部门审批时间")
    private Date procurementManagerApprovalTime;


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
     * 已上传的关联附件信息
     */
    @ApiModelProperty("已上传的关联附件信息")
    private List<SysFileInfo> fileInfos;

}
