package com.px.pm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.px.pm.doman.PmDemandExpertRefHis;
import com.px.pm.doman.vo.responseVo.PmBaseExpertResVo;

import java.util.List;

/**
 * 项目专家关联表历史表(PmDemandExpertRefHis)è¡¨æå¡æ¥å£
 *
 * @author 品讯科技
 * @since 2023-03-06 16:43:49
 */
public interface PmDemandExpertRefHisService extends IService<PmDemandExpertRefHis> {
    List<PmBaseExpertResVo> getPmBaseExpertResVoList(Long demandId,Integer returnOrderNumber);
}
