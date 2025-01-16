package com.px.pm.mapper;

import com.px.pm.doman.PmDemandExpertRefHis;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.px.pm.doman.vo.responseVo.PmBaseExpertResVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 项目专家关联表历史表(PmDemandExpertRefHis)è¡¨æ°æ®åºè®¿é®å±
 *
 * @author 品讯科技
 * @since 2023-03-06 16:43:48
 */
public interface PmDemandExpertRefHisMapper extends BaseMapper<PmDemandExpertRefHis> {
    List<PmBaseExpertResVo> getPmBaseExpertResVoList(@Param("demandId") Long demandId,@Param("returnOrderNumber") Integer returnOrderNumber);
}
