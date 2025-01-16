package com.px.web.controller.home;

import com.px.common.annotation.Log;
import com.px.common.constant.ModularConstans;
import com.px.common.core.controller.BaseController;
import com.px.common.core.domain.AjaxResult;
import com.px.common.enums.BusinessType;
import com.px.plan.doman.vo.responseVo.PlanQuarterResponseVo;
import com.px.plan.service.PlanYearsService;
import com.px.pm.doman.PmDemand;
import com.px.pm.doman.vo.responseVo.PmDemandResVo;
import com.px.pm.service.IPmDemandService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * 系统首页控制层
 *
 * @author 品讯科技
 */
@RestController
@RequestMapping("/home/systemHomepage")
public class SystemHomepageController extends BaseController {
    @Resource
    private PlanYearsService planYearsService;
    @Resource
    private IPmDemandService pmDemandService;

    @ApiOperation(value = "需求单位首页计划管理总数")
    @GetMapping("/demandUnit/planManagement/total")
    public AjaxResult demandUnitPlanManagementTotal() {
        return planYearsService.demandUnitPlanManagementTotal();
    }

    @ApiOperation(value = "需求单位首页需求管理总数")
    @GetMapping("/demandUnit/demandManagement/total")
    public AjaxResult demandUnitDemandManagementTotal() {
        return planYearsService.demandUnitDemandManagementTotal();
    }

    @ApiOperation(value = "需求单位首页合同管理总数")
    @GetMapping("/demandUnit/contractManagement/total")
    public AjaxResult demandUnitContractManagementTotal() {
        return planYearsService.demandUnitContractManagementTotal();
    }

    @ApiOperation(value = "需求单位首页建设管理总数")
    @GetMapping("/demandUnit/constructionManagement/total")
    public AjaxResult demandUnitConstructionManagementTotal() {
        return planYearsService.demandUnitConstructionManagementTotal();
    }

    @ApiOperation(value = "采购管理部门首页计划管理总数")
    @GetMapping("/purchasingManagement/planManagement/total")
    public AjaxResult purchasingManagementPurchasingManagementTotal() {
        return planYearsService.purchasingManagementPurchasingManagementTotal();
    }

    @ApiOperation(value = "采购管理部门首页需求管理总数")
    @GetMapping("/purchasingManagement/demandManagement/total")
    public AjaxResult purchasingManagementDemandManagementTotal() {
        return planYearsService.purchasingManagementDemandManagementTotal();
    }

    @ApiOperation(value = "采购管理部门首页任务下达管理总数")
    @GetMapping("/purchasingManagement/taskRelease/total")
    public AjaxResult purchasingManagementTaskReleaseTotal() {
        return planYearsService.purchasingManagementTaskReleaseTotal();
    }

    @ApiOperation(value = "采购办首页采购执行管理总数")
    @GetMapping("/procurementOffice/procurementExecution/total")
    public AjaxResult procurementOfficeProcurementExecutionTotal() {
        return planYearsService.procurementOfficeProcurementExecutionTotal();
    }

    @ApiOperation(value = "采购办首已中标项目总数")
    @GetMapping("/procurementOffice/winningTheBid/total")
    public AjaxResult procurementOfficeWinningTheBidTotal() {
        return planYearsService.procurementOfficeWinningTheBidTotal();
    }

    @ApiOperation(value = "采购办首未中标项目理总数")
    @GetMapping("/procurementOffice/failureToWinTheBid/total")
    public AjaxResult procurementOfficeFailureToWinTheBidTotal() {
        return planYearsService.procurementOfficeFailureToWinTheBidTotal();
    }

    @ApiOperation(value = "需求单位首页待办事项（全部）")
    @GetMapping("/demandUnit/will/total")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult demandUnitWillManipulateTotal() {
        return planYearsService.demandUnitWillManipulateTotal();
    }

    @ApiOperation(value = "需求单位首页查看预警项目")
    @GetMapping("/demandUnit/will/alertProject")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult demandUnitWillAlertProject() {
        return planYearsService.demandUnitWillAlertProject();
    }

    @ApiOperation(value = "需求单位首页待办事项（数量）")
    @GetMapping("/demandUnit/will/totalNum")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult demandUnitWillManipulateTotalNum() {
        return planYearsService.demandUnitWillManipulateTotalNum();
    }


    @ApiOperation(value = "需求单位首页待办事项（计划待提交）")
    @GetMapping("/demandUnit/will/planWaitCommit")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult planWaitCommit() {
        List<PlanQuarterResponseVo> vos = planYearsService.planWaitCommit();
        return AjaxResult.success(vos);
    }

    @ApiOperation(value = "需求单位首页待办事项（需求待提交）")
    @GetMapping("/demandUnit/will/demandWaitCommit")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult demandUnit() {
        return AjaxResult.success(pmDemandService.listToVoList(pmDemandService.demandWaitCommit()));
    }

    @ApiOperation(value = "需求单位首页待办事项（合同待填制）")
    @GetMapping("/demandUnit/will/contractWaitFilled")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult contractWaitFilled() {
        return AjaxResult.success(pmDemandService.listToVoList(pmDemandService.contractWaitFilled()));
    }

    @ApiOperation(value = "需求单位首页待办事项（项目建设待完成）")
    @GetMapping("/demandUnit/will/projectWaitFinish")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult projectWaitFinish() {
        return AjaxResult.success(pmDemandService.listToVoList(pmDemandService.projectWaitFinish()));
    }

    @ApiOperation(value = "采购管理部门首页待办事项（全部）")
    @GetMapping("/purchasingManagement/will/total")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult purchasingManagementWillManipulateTotal() {
        return planYearsService.purchasingManagementWillManipulateTotal();
    }

    @ApiOperation(value = "采购管理部门首页查看预警项目")
    @GetMapping("/purchasingManagement/will/alertProject")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult purchasingManagementWillAlertProject() {
        return planYearsService.purchasingManagementWillAlertProject();
    }

    @ApiOperation(value = "采购管理部门首页待办事项（数量）")
    @GetMapping("/purchasingManagement/will/totalNum")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult purchasingManagementWillManipulateTotalNum() {
        return planYearsService.purchasingManagementWillManipulateTotalNum();
    }

    @ApiOperation(value = "采购管理部门首页待办事项（计划待审核）")
    @GetMapping("/purchasingManagement/will/planWaitExamine")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult planWaitExamine() {
        List<PlanQuarterResponseVo> resVos = planYearsService.planWaitExamine();
        return AjaxResult.success(resVos);
    }

    @ApiOperation(value = "采购管理部门首页待办事项（需求待审核）")
    @GetMapping("/purchasingManagement/will/demandWaitExamine")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult demandWaitExamine() {
        List<PmDemand> demandList = pmDemandService.demandWaitExamine();
        return AjaxResult.success(pmDemandService.listToVoList(demandList));
    }

    @ApiOperation(value = "采购管理部门首页待办事项（任务待下达）")
    @GetMapping("/purchasingManagement/will/taskWaitRelease")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult taskWaitRelease() {
        List<PmDemand> demandList = pmDemandService.taskWaitRelease();
        return AjaxResult.success(pmDemandService.listToVoList(demandList));
    }

    @ApiOperation(value = "采购办首页待办事项（全部）")
    @GetMapping("/procurementOffice/will/total")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult procurementOfficeWillManipulateTotal() {
        return pmDemandService.procurementOfficeWillManipulateTotal();
    }

    @ApiOperation(value = "采购办首页查看预警项目")
    @GetMapping("/procurementOffice/will/alertProject")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult procurementOfficeWillAlertProject() {
        return pmDemandService.procurementOfficeWillAlertProject();
    }

    @ApiOperation(value = "采购办首页待办事项（数量）")
    @GetMapping("/procurementOffice/will/totalNum")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult procurementOfficeWillManipulateTotalNum() {
        return pmDemandService.procurementOfficeWillManipulateTotalNum();
    }

    @ApiOperation(value = "采购办首页待办事项（采购需求对接）")
    @GetMapping("/procurementOffice/will/purchaseDemandDocking")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult purchaseDemandDocking() {
        List<PmDemandResVo> resVos = pmDemandService.purchaseDemandDocking();
        return AjaxResult.success(resVos);
    }

    @ApiOperation(value = "采购办首页待办事项（专家意见反馈）")
    @GetMapping("/procurementOffice/will/expertFeedback")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult expertFeedback() {
        List<PmDemandResVo> resVos = pmDemandService.expertFeedback();
        return AjaxResult.success(resVos);
    }

    @ApiOperation(value = "采购办首页待办事项（采购文件编制审核）")
    @GetMapping("/procurementOffice/will/procurementDocumentsReview")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult procurementDocumentsReview() {
        List<PmDemandResVo> resVos = pmDemandService.procurementDocumentsReview();
        return AjaxResult.success(resVos);
    }

    @ApiOperation(value = "采购办首页待办事项（采购公告发布）")
    @GetMapping("/procurementOffice/will/procurementAnnouncement")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult procurementAnnouncement() {
        List<PmDemandResVo> resVos = pmDemandService.procurementAnnouncement();
        return AjaxResult.success(resVos);
    }

    @ApiOperation(value = "采购办首页待办事项（标前质疑投诉）")
    @GetMapping("/procurementOffice/will/preBidQuery")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult preBidQuery() {
        List<PmDemandResVo> resVos = pmDemandService.preBidQuery();
        return AjaxResult.success(resVos);
    }

    @ApiOperation(value = "采购办首页待办事项（待开标）")
    @GetMapping("/procurementOffice/will/waitOpenBid")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult waitOpenBid() {
        List<PmDemandResVo> resVos = pmDemandService.waitOpenBid();
        return AjaxResult.success(resVos);
    }

    @ApiOperation(value = "采购办首页待办事项（评标结果公告）")
    @GetMapping("/procurementOffice/will/bidResultAnnouncement")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult bidResultAnnouncement() {
        List<PmDemandResVo> resVos = pmDemandService.bidResultAnnouncement();
        return AjaxResult.success(resVos);
    }

    @ApiOperation(value = "采购办首页待办事项（流废标处置）")
    @GetMapping("/procurementOffice/will/wasteBidHandle")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult wasteBidHandle() {
        List<PmDemandResVo> resVos = pmDemandService.wasteBidHandle();
        return AjaxResult.success(resVos);
    }

    @ApiOperation(value = "采购办首页待办事项（标后质疑）")
    @GetMapping("/procurementOffice/will/postBidQuery")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult postBidQuery() {
        List<PmDemandResVo> resVos = pmDemandService.postBidQuery();
        return AjaxResult.success(resVos);
    }

    @ApiOperation(value = "采购办首页待办事项（发函催告）")
    @GetMapping("/procurementOffice/will/sendLetter")
    @Log(title = ModularConstans.systemHome, businessType = BusinessType.QUERY)
    public AjaxResult willSendLetter() {
        List<PlanQuarterResponseVo> vos = planYearsService.willSendLetter();
        return AjaxResult.success(vos);
    }
}
