package com.px.pm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.pm.doman.vo.responseVo.PmBaseExpertResVo;
import com.px.pm.mapper.PmDemandExpertRefMapper;
import com.px.pm.doman.PmDemandExpertRef;
import com.px.pm.service.PmDemandExpertRefService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目专家关联表(PmDemandExpertRef)è¡¨æå¡å®ç°ç±»
 *
 * @author 品讯科技
 * @since 2023-02-03 15:37:07
 */
@Service
public class PmDemandExpertRefServiceImpl extends ServiceImpl<PmDemandExpertRefMapper, PmDemandExpertRef> implements PmDemandExpertRefService {

    @Override
    public List<Integer> selectByExpertId(Integer expertId) {
        return this.baseMapper.selectByExpertId(expertId);
    }
    @Override
    public List<PmBaseExpertResVo> getPmBaseExpertResVoList(Long demandId){
        return this.baseMapper.getPmBaseExpertResVoList(demandId);
    }
}
