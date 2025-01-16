package com.px.plan.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.plan.doman.MonthlyReconciliation;
import com.px.plan.doman.vo.requestVo.MonthlyReconciliationPageReqVo;

public interface MonthlyReconciliationService extends IService<MonthlyReconciliation> {


    IPage<MonthlyReconciliation> queryPage(MonthlyReconciliationPageReqVo vo);

}
