package com.px.web.controller.statisticalAnalysis;

import com.px.common.annotation.Log;
import com.px.common.constant.ModularConstans;
import com.px.common.core.controller.BaseController;
import com.px.common.core.domain.AjaxResult;
import com.px.common.enums.BusinessType;
import com.px.common.utils.StringUtils;
import com.px.pm.doman.vo.requestVo.PmDemandReqVo;
import com.px.pm.doman.vo.responseVo.StatisticalChartsResVo;
import com.px.pm.service.IPmDemandService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 统计分析页面控制层
 * (需求更新后进行的整理)
 *
 * @author 品讯科技
 */
@RestController
@RequestMapping("/analysis")
public class AnalysisController extends BaseController {

    @Resource
    private IPmDemandService pmDemandService;

    @ApiOperation(value = "采购管理部门“执行率”、“规划执行率”、“完成率”、“滞后率”、“偏差率”、“流废标率”统计")
    @PostMapping("/demandZGWZPL")
    @Log(title = ModularConstans.statisticalAnalysis, businessType = BusinessType.QUERY)
    public AjaxResult demandZGWZPL(@RequestBody PmDemandReqVo pmDemandReqVo) {
        if (StringUtils.isNull(pmDemandReqVo.getPurchaseDeptId())) {
            return error("登录账号的单位 不能为空！");
        }
        Map<String, String> maps = pmDemandService.demandZGWZPL(pmDemandReqVo);
        return success(maps);
    }

    @ApiOperation(value = "需求提报情况")
    @PostMapping("/demandSubCount")
    //@PreAuthorize("@ss.hasPermi('statistical:demandSubCount')")
    @Log(title = ModularConstans.statisticalAnalysis, businessType = BusinessType.QUERY)
    public AjaxResult demandSubCount(@RequestBody PmDemandReqVo pmDemandReqVo) {
        if (StringUtils.isNull(pmDemandReqVo.getPurchaseDeptId())) {
            return error("登录账号的单位 不能为空！");
        }
        List<StatisticalChartsResVo> resVos = pmDemandService.demandSubCount(pmDemandReqVo);
        return success(resVos);
    }

    @ApiOperation(value = "项目类型情况统计")
    @PostMapping("/countByProjectType")
    //@PreAuthorize("@ss.hasPermi('statistical:countByProjectType')")
    @Log(title = ModularConstans.statisticalAnalysis, businessType = BusinessType.QUERY)
    public AjaxResult countByProjectType(@RequestBody PmDemandReqVo pmDemandReqVo) {
        if (StringUtils.isNull(pmDemandReqVo.getPurchaseDeptId())) {
            return error("登录账号的单位 不能为空！");
        }
        List<StatisticalChartsResVo> resVos = pmDemandService.countByProjectType(pmDemandReqVo);
        return AjaxResult.success(resVos);
    }

    @ApiOperation(value = "项目属性情况统计")
    @PostMapping("/countByProjectAttr")
    //@PreAuthorize("@ss.hasPermi('statistical:countByProjectAttr')")
    @Log(title = ModularConstans.statisticalAnalysis, businessType = BusinessType.QUERY)
    public AjaxResult countByProjectAttr(@RequestBody PmDemandReqVo pmDemandReqVo) {
        //按照项目属性统计所有的项目
        if (StringUtils.isNull(pmDemandReqVo.getPurchaseDeptId())) {
            return error("登录账号的单位 不能为空！");
        }

        return AjaxResult.success(pmDemandService.countByProjectAttr(pmDemandReqVo));
    }

    @ApiOperation(value = "任务属性分布")
    @PostMapping("/countByTaskAttr")
    //@PreAuthorize("@ss.hasPermi('statistical:countByProjectAttr')")
    @Log(title = ModularConstans.statisticalAnalysis, businessType = BusinessType.QUERY)
    public AjaxResult countByTaskAttr(@RequestBody PmDemandReqVo pmDemandReqVo) {
        //按照项目属性统计所有的项目
        if (StringUtils.isNull(pmDemandReqVo.getPurchaseDeptId())) {
            return error("登录账号的单位 不能为空！");
        }

        return AjaxResult.success(pmDemandService.countByTaskAttr(pmDemandReqVo));
    }

    @ApiOperation(value = "需采转化情况统计")
    @PostMapping("/countByPurchaseChange")
    //@PreAuthorize("@ss.hasPermi('statistical:countByPurchaseChange')")
    @Log(title = ModularConstans.statisticalAnalysis, businessType = BusinessType.QUERY)
    public AjaxResult countByPurchaseChange(@RequestBody PmDemandReqVo pmDemandReqVo) {
        if (StringUtils.isNull(pmDemandReqVo.getPurchaseDeptId())) {
            return error("登录账号的单位 不能为空！");
        }
        List<StatisticalChartsResVo> resVos = pmDemandService.countByPurchaseChange(pmDemandReqVo);
        return AjaxResult.success(resVos);
    }

    @ApiOperation(value = "执行滞后分析")
    @PostMapping("/countProjectExceed")
    //@PreAuthorize("@ss.hasPermi('statistical:countProjectExceed')")
    @Log(title = ModularConstans.statisticalAnalysis, businessType = BusinessType.QUERY)
    public AjaxResult countProjectExceed(@RequestBody PmDemandReqVo pmDemandReqVo) {
        if (StringUtils.isNull(pmDemandReqVo.getPurchaseDeptId())) {
            return error("登录账号的单位 不能为空！");
        }
        return pmDemandService.countProjectExceed(pmDemandReqVo);
    }

    @ApiOperation(value = "滞后项目时长分析")
    @PostMapping("/exceedMarketAnalysis")
    //@PreAuthorize("@ss.hasPermi('statistical:exceedMarketAnalysis')")
    @Log(title = ModularConstans.statisticalAnalysis, businessType = BusinessType.QUERY)
    public AjaxResult exceedMarketAnalysis(@RequestBody PmDemandReqVo pmDemandReqVo) {
        //按照滞后时长统计项目数量
        //滞后时长包括：滞后1个月以内的采购任务、滞后1至3个月采购任务、滞后3至6个月采购任务、滞后6个月至1年采购任务、滞后1年以上采购任务
        if (StringUtils.isNull(pmDemandReqVo.getPurchaseDeptId())) {
            return error("登录账号的单位 不能为空！");
        }
        return pmDemandService.exceedMarketAnalysis(pmDemandReqVo);
    }

    @ApiOperation(value = "滞后主要直接原因")
    @PostMapping("/exceedReason")
    //@PreAuthorize("@ss.hasPermi('statistical:exceedReason')")
    @Log(title = ModularConstans.statisticalAnalysis, businessType = BusinessType.QUERY)
    public AjaxResult exceedReason(@RequestBody PmDemandReqVo pmDemandReqVo) {
        //按照滞后时长统计项目数量
        //滞后时长包括：滞后1个月以内的采购任务、滞后1至3个月采购任务、滞后3至6个月采购任务、滞后6个月至1年采购任务、滞后1年以上采购任务
        if (StringUtils.isNull(pmDemandReqVo.getPurchaseDeptId())) {
            return error("登录账号的单位 不能为空！");
        }
        return pmDemandService.exceedReason(pmDemandReqVo);
    }

    @ApiOperation(value = "重大规划采购任务专项计划管理情况")
    @PostMapping("/countMajorProject")
    //@PreAuthorize("@ss.hasPermi('statistical:countMajorProject')")
    @Log(title = ModularConstans.statisticalAnalysis, businessType = BusinessType.QUERY)
    public AjaxResult countMajorProject(@RequestBody PmDemandReqVo pmDemandReqVo) {
        if (StringUtils.isNull(pmDemandReqVo.getPurchaseDeptId())) {
            return error("登录账号的单位 不能为空！");
        }
        return pmDemandService.countMajorProject(pmDemandReqVo);
    }

    @ApiOperation(value = "采购任务受领")
    @PostMapping("/countPurTask")
    //@PreAuthorize("@ss.hasPermi('statistical:countPurTask')")
    @Log(title = ModularConstans.statisticalAnalysis, businessType = BusinessType.QUERY)
    public AjaxResult countPurTask(@RequestBody PmDemandReqVo pmDemandReqVo) {
        if (StringUtils.isNull(pmDemandReqVo.getPurchaseDeptId())) {
            return error("登录账号的单位 不能为空！");
        }
        return pmDemandService.countPurTask(pmDemandReqVo);
    }

    @ApiOperation(value = "任务受领数量/预算金额（万元）")
    @PostMapping("/purchaseProjectNumberAnalysis")
    //@PreAuthorize("@ss.hasPermi('statistical:purchaseProjectNumberAnalysis')")
    @Log(title = ModularConstans.statisticalAnalysis, businessType = BusinessType.QUERY)
    public AjaxResult purchaseProjectNumberAnalysis(@RequestBody PmDemandReqVo pmDemandReqVo) {
        // 需求单位可查看本单位及其下属单位的统计数据
        if (StringUtils.isNull(pmDemandReqVo.getPurchaseDeptId())) {
            return error("登录账号的单位 不能为空！");
        }
        if (StringUtils.isNull(pmDemandReqVo.getTimeType())) {
            return error("统计时间类型不能为空， 1：年度，2：季度，3：月份！");
        }
        List<Map<String, String>> map = pmDemandService.purchaseProjectNumberAnalysis(pmDemandReqVo);
        return success(map);
    }

    @ApiOperation(value = "采购方式分布")
    @PostMapping("/countByPurchaseMode")
    //@PreAuthorize("@ss.hasPermi('statistical:countByPurchaseMode')")
    @Log(title = ModularConstans.statisticalAnalysis, businessType = BusinessType.QUERY)
    public AjaxResult countByPurchaseMode(@RequestBody PmDemandReqVo pmDemandReqVo) {
        //按照采购方式统计所有的项目
        if (StringUtils.isNull(pmDemandReqVo.getPurchaseDeptId())) {
            return error("登录账号的单位 不能为空！");
        }
        List<StatisticalChartsResVo> resVos = pmDemandService.countByPurchaseMode(pmDemandReqVo);
        return AjaxResult.success(resVos);
    }

    @ApiOperation(value = "任务金额分布")
    @PostMapping("/countByEvaluation")
    //@PreAuthorize("@ss.hasPermi('statistical:purchaseProjectExecute')")
    @Log(title = ModularConstans.statisticalAnalysis, businessType = BusinessType.QUERY)
    public AjaxResult countByEvaluation(@RequestBody PmDemandReqVo pmDemandReqVo) {
        //按照概算金额统计所有的项目
        //概算金额包括：100万以下采购任务、100至500万元采购任务、500至1000万元采购任务、1000至5000万元采购任务、5000至1亿元采购任务、1亿元及以上采购任务
        if (StringUtils.isNull(pmDemandReqVo.getPurchaseDeptId())) {
            return error("登录账号的单位 不能为空！");
        }
        List<StatisticalChartsResVo> resVos = pmDemandService.countByEvaluation(pmDemandReqVo);
        return AjaxResult.success(resVos);
    }

    @ApiOperation(value = "任务执行进度")
    @PostMapping("/purchaseProjectExecute")
    //@PreAuthorize("@ss.hasPermi('statistical:purchaseProjectExecute')")
    @Log(title = ModularConstans.statisticalAnalysis, businessType = BusinessType.QUERY)
    public AjaxResult purchaseProjectExecute(@RequestBody PmDemandReqVo pmDemandReqVo) {
        // 需求单位可查看本单位及其下属单位的统计数据
        if (StringUtils.isNull(pmDemandReqVo.getPurchaseDeptId())) {
            return error("登录账号的单位 不能为空！");
        }
        List<StatisticalChartsResVo> resVos = pmDemandService.purchaseProjectExecute(pmDemandReqVo);
        return AjaxResult.success(resVos);
    }

    @ApiOperation(value = "新完成采购任务数量")
    @PostMapping("/purchaseTaskFinish")
    //@PreAuthorize("@ss.hasPermi('statistical:purchaseTaskFinish')")
    @Log(title = ModularConstans.statisticalAnalysis, businessType = BusinessType.QUERY)
    public AjaxResult purchaseTaskFinish(@RequestBody PmDemandReqVo pmDemandReqVo) {
        // 需求单位可查看本单位及其下属单位的统计数据
        if (StringUtils.isNull(pmDemandReqVo.getPurchaseDeptId())) {
            return error("登录账号的单位 不能为空！");
        }
        if (StringUtils.isNull(pmDemandReqVo.getTimeType())) {
            return error("统计时间类型不能为空， 1：年度，2：季度，3：月份！");
        }
        List<Map<String, Integer>> map = pmDemandService.purchaseTaskFinish(pmDemandReqVo);
        return success(map);
    }

    @ApiOperation(value = "各阶段采购任务数量分布")
    @PostMapping("/countEveryStatusNum")
    //@PreAuthorize("@ss.hasPermi('statistical:countEveryStatusNum')")
    @Log(title = ModularConstans.statisticalAnalysis, businessType = BusinessType.QUERY)
    public AjaxResult countEveryStatusNum(@RequestBody PmDemandReqVo pmDemandReqVo) {
        if (StringUtils.isNull(pmDemandReqVo.getPurchaseDeptId())) {
            return error("登录账号的单位 不能为空！");
        }
        return pmDemandService.countEveryStatusNumThisYear(pmDemandReqVo);
    }

    @ApiOperation(value = "滞后直接原因")
    @PostMapping("/exceedReasonZj")
    //@PreAuthorize("@ss.hasPermi('statistical:exceedReasonZj')")
    @Log(title = ModularConstans.statisticalAnalysis, businessType = BusinessType.QUERY)
    public AjaxResult exceedReasonZj(@RequestBody PmDemandReqVo pmDemandReqVo) {
        //按照滞后时长统计项目数量
        //滞后时长包括：滞后1个月以内的采购任务、滞后1至3个月采购任务、滞后3至6个月采购任务、滞后6个月至1年采购任务、滞后1年以上采购任务
        if (StringUtils.isNull(pmDemandReqVo.getPurchaseDeptId())) {
            return error("登录账号的单位 不能为空！");
        }
        return pmDemandService.exceedReasonZj(pmDemandReqVo);
    }

    @ApiOperation(value = "重大规划采购任务专项计划管理情况")
    @PostMapping("/countMajorProjectCGB")
    //@PreAuthorize("@ss.hasPermi('statistical:countMajorProjectCGB')")
    @Log(title = ModularConstans.statisticalAnalysis, businessType = BusinessType.QUERY)
    public AjaxResult countMajorProjectCGB(@RequestBody PmDemandReqVo pmDemandReqVo) {
        if (StringUtils.isNull(pmDemandReqVo.getPurchaseDeptId())) {
            return error("登录账号的单位 不能为空！");
        }
        return pmDemandService.countMajorProjectCGB(pmDemandReqVo);
    }

    @ApiOperation(value = "新增执行滞后采购任务数量分析")
    @PostMapping("/countMajorProjectNumCGB")
    //@PreAuthorize("@ss.hasPermi('statistical:countMajorProjectNumCGB')")
    @Log(title = ModularConstans.statisticalAnalysis, businessType = BusinessType.QUERY)
    public AjaxResult countMajorProjectNumCGB(@RequestBody PmDemandReqVo pmDemandReqVo) {
        if (StringUtils.isNull(pmDemandReqVo.getPurchaseDeptId())) {
            return error("登录账号的单位 不能为空！");
        }
        return pmDemandService.countMajorProjectNumCGB(pmDemandReqVo);
    }

    @ApiOperation(value = "滞留环节")
    @PostMapping("/exceedLink")
    //@PreAuthorize("@ss.hasPermi('statistical:exceedLink')")
    @Log(title = ModularConstans.statisticalAnalysis, businessType = BusinessType.QUERY)
    public AjaxResult exceedLink(@RequestBody PmDemandReqVo pmDemandReqVo) {
        //    超过计划完成采购时间的各状态数量及占比统计，包括：采购需求对接、采购文件编制审核、标签质疑投诉、待开标/流废标处置、标后质疑
        if (StringUtils.isNull(pmDemandReqVo.getPurchaseDeptId())) {
            return error("登录账号的单位 不能为空！");
        }
        return pmDemandService.exceedLink(pmDemandReqVo);
    }


}
