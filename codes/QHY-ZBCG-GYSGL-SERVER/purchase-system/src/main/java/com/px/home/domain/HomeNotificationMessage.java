package com.px.home.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.px.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 首页通知消息
 * @author 品讯科技
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("首页通知消息")
public class HomeNotificationMessage extends BaseEntity {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long homeNotificationMessageId;

    /** 首页分类（0需求单位首页1采购部首页） */
    private Integer homepageClassification;

    /** 公消息分类(0预警消息1.发函催告2.逾期告警) */
    private Integer messageClassification;
    
    /** 项目类型（0：装备类；1：物资类；2：服务类；3：工程类） */
    private String projectType;

    /** 计划提报需求时间 */
    private Date planDemandSubTime;

    /** 是否填写完成滞后原因(0是1否) */
    private Integer state;

    /** 需求概况 */
    private String demandOverview;

    /** 采购项目名称 */
    private String projectName;

    /** 采购需求id */
    private Long demandId;
}
