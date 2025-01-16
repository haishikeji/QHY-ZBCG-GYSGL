package com.px.pm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.px.pm.doman.PmBidOpeningHis;
import com.px.pm.doman.vo.responseVo.PmBidOpeningResVo;

import java.util.List;

/**
 * 开标信息表历史表(PmBidOpeningHis)è¡¨æå¡æ¥å£
 *
 * @author 品讯科技
 * @since 2023-03-06 16:43:45
 */
public interface PmBidOpeningHisService extends IService<PmBidOpeningHis> {
    List<PmBidOpeningResVo> getPmBidOpeningResVoList(Long demandId,Integer returnOrderNumber);
}
