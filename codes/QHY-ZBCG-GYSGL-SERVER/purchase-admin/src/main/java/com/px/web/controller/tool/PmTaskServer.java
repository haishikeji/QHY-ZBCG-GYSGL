package com.px.web.controller.tool;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.px.common.core.domain.entity.SysDictData;
import com.px.common.enums.PmProjectStatus;
import com.px.common.enums.WarnStatus;
import com.px.common.utils.DateUtils;
import com.px.pm.doman.PmDemand;
import com.px.pm.service.IPmDemandService;
import com.px.system.service.ISysDictTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目管理任务调度
 */
@Component
@Slf4j
public class PmTaskServer {
    @Autowired
    private IPmDemandService pmDemandService;
    @Autowired
    private ISysDictTypeService iSysDictTypeService;

    /**
     * 更新预警状态
     */
    public void updateDemandWarnStatus() {
        log.info("******** 项目更新预警状态任务  开始");
        LambdaQueryWrapper<PmDemand> lw = new LambdaQueryWrapper<>();
        lw.ne(PmDemand::getProjectStatus, PmProjectStatus.COMPLETION_CONSTRUCTION.getCode());
        List<PmDemand> list = this.pmDemandService.list(lw);
        if (ObjectUtils.isEmpty(list)) {
            return;
        }
        Date now = DateUtils.parseDate(DateUtils.getDate());
        List<PmDemand> updateList = new ArrayList<>();
        List<SysDictData> data = iSysDictTypeService.selectDictDataByType("alert_time_setting");
        int setting1 = Integer.parseInt(data.stream().filter(item -> item.getDictLabel().equals("计划提报时间")).collect(Collectors.toList()).get(0).getDictValue());
        log.info("******** setting1 == " + setting1);
        int setting2 = Integer.parseInt(data.stream().filter(item -> item.getDictLabel().equals("计划完成时间")).collect(Collectors.toList()).get(0).getDictValue());
        log.info("******** setting2 == " + setting2);
        int setting3 = Integer.parseInt(data.stream().filter(item -> item.getDictLabel().equals("计划交付(实施)时间")).collect(Collectors.toList()).get(0).getDictValue());
        log.info("******** setting3 == " + setting3);
        list.forEach(item -> {
            //需求提报时间：根据数据阈值中设置的提报时间进行判断，进行预警
            int diff1 = DateUtils.differentDaysByMillisecond(item.getPlanDemandSubTime(), now);
            //采购完成时间：根据数据阈值中设置的采购完成时间进行判断，进行预警
            int diff2 = DateUtils.differentDaysByMillisecond(item.getPlanPurchaseFinishTime(), now);
            //计划交付时间：根据数据阈值中设置的交付时间进行判断，进行预警
            int diff3 = DateUtils.differentDaysByMillisecond(item.getPlanDeliverTime(), now);
            if (ObjectUtils.isEmpty(item.getRealDemandCommitTime()) && diff1 > 0 && diff1 <= setting1) {
                log.info("******** 需求提报预警: " + item.getProjectName());
                PmDemand pmDemand = new PmDemand();
                pmDemand.setDemandId(item.getDemandId());
                pmDemand.setWarnStatus(WarnStatus.DEMAND_COMMT_WARNING.getCode());
                updateList.add(pmDemand);
            } else {
                if (ObjectUtils.isEmpty(item.getRealDemandCommitTime()) && item.getPlanDemandSubTime().getTime() < System.currentTimeMillis()) {
                    log.info("******** 需求提报逾期: " + item.getProjectName());
                    PmDemand pmDemandUpdate = new PmDemand();
                    pmDemandUpdate.setDemandId(item.getDemandId());
                    pmDemandUpdate.setWarnStatus(WarnStatus.DEMAND_COMMT_EX.getCode());
                    updateList.add(pmDemandUpdate);
                } else {
                    if (ObjectUtils.isEmpty(item.getRealPurchaseFinishTime()) && diff2 > 0 && diff2 <= setting2) {
                        log.info("******** 采购完成预警: " + item.getProjectName());
                        PmDemand pmDemand = new PmDemand();
                        pmDemand.setDemandId(item.getDemandId());
                        pmDemand.setWarnStatus(WarnStatus.PURCHASE_FINISH_WARNING.getCode());
                        updateList.add(pmDemand);
                    } else {
                        if (ObjectUtils.isEmpty(item.getRealPurchaseFinishTime()) && item.getPlanPurchaseFinishTime().getTime() < System.currentTimeMillis()) {
                            log.info("******** 采购完成逾期: " + item.getProjectName());
                            PmDemand pmDemandUpdate = new PmDemand();
                            pmDemandUpdate.setDemandId(item.getDemandId());
                            pmDemandUpdate.setWarnStatus(WarnStatus.PURCHASE_FINISH_EX.getCode());
                            updateList.add(pmDemandUpdate);
                        } else {
                            if (ObjectUtils.isEmpty(item.getRealDeliverTime()) && diff3 > 0 && diff3 <= setting3) {
                                log.info("******** 计划交付预警: " + item.getProjectName());
                                PmDemand pmDemand = new PmDemand();
                                pmDemand.setDemandId(item.getDemandId());
                                pmDemand.setWarnStatus(WarnStatus.PLAN_DELIVER_WARNING.getCode());
                                updateList.add(pmDemand);
                            } else {
                                if (ObjectUtils.isEmpty(item.getRealDeliverTime()) && item.getPlanDeliverTime().getTime() < System.currentTimeMillis()) {
                                    log.info("******** 计划交付逾期: " + item.getProjectName());
                                    PmDemand pmDemandUpdate = new PmDemand();
                                    pmDemandUpdate.setDemandId(item.getDemandId());
                                    pmDemandUpdate.setWarnStatus(WarnStatus.PLAN_DELIVER_EX.getCode());
                                    updateList.add(pmDemandUpdate);
                                } else {
                                    PmDemand pmDemand = new PmDemand();
                                    pmDemand.setDemandId(item.getDemandId());
                                    pmDemand.setWarnStatus(WarnStatus.NORMAL.getCode());
                                    updateList.add(pmDemand);
                                }
                            }
                        }
                    }
                }
            }
        });

        if (!ObjectUtils.isEmpty(updateList)) {
            this.pmDemandService.updateBatchById(updateList);
        }
        log.info("******** 项目更新预警状态任务  结束");
    }

}
