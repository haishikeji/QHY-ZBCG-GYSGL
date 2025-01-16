package com.px.pm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.pm.mapper.PmCallQuestionHisMapper;
import com.px.pm.doman.PmCallQuestionHis;
import com.px.pm.service.PmCallQuestionHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 质疑表历史表(PmCallQuestionHis)è¡¨æå¡å®ç°ç±»
 *
 * @author 品讯科技
 * @since 2023-03-06 16:43:47
 */
@Service
public class PmCallQuestionHisServiceImpl extends ServiceImpl<PmCallQuestionHisMapper, PmCallQuestionHis> implements PmCallQuestionHisService {

    @Autowired
    PmCallQuestionHisMapper questionHisMapper;

    @Override
    public List<Long> selectDemandIdAll() {
        return this.questionHisMapper.selectDemandIdAll();
    }

    @Override
    public List<Long> selectDemandIdAllWith() {
        return this.questionHisMapper.selectDemandIdAllWith();
    }

}
