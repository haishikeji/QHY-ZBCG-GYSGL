package com.px.pm.doman.vo.responseVo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 发布公告表(PmReleaseAnnouncement)å®ä½ç±»
 *
 * @author 品讯科技
 * @since 2023-02-02 00:47:23
 */

@Data
@ApiModel(value = "发布公告表", description = "")
public class PmReleaseAnnouncementResVo implements Serializable {
    private static final long serialVersionUID = 403614463746791218L;

    @TableId(value = "ID", type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Long id;


    @ApiModelProperty("需求ID")
    private Long demandId;


    @ApiModelProperty("发布公告平台,1:军队采购网,2:全军武器装备采购网")
    private String announcementPlatform;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("发布时间 yyyy-MM-dd")
    private Date releaseTime;


    @ApiModelProperty("开标地点")
    private String bidOpenPlace;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("报名截止时间 yyyy-MM-dd")
    private Date enrollCloseTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("开标时间 yyyy-MM-dd")
    private Date tenderOpenTime;

    @ApiModelProperty("备注")
    private String remarks;

    /** 创建者 */
    @ApiModelProperty("创建者")
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /** 更新者 */
    @ApiModelProperty("更新者")
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private Date updateTime;


}
