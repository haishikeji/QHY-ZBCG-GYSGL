package com.px.web.controller.pm;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.common.annotation.Log;
import com.px.common.constant.ModularConstans;
import com.px.common.core.domain.entity.SysUser;
import com.px.common.core.domain.model.LoginUser;
import com.px.common.enums.BusinessType;
import com.px.common.utils.PageUtils;
import com.px.common.utils.StringUtils;
import com.px.framework.web.service.TokenService;
import com.px.pm.doman.PmDemand;
import com.px.pm.doman.PmDemandHis;
import com.px.pm.doman.vo.requestVo.PmBookBuildingReqVo;
import com.px.pm.doman.vo.requestVo.PmDemandReqVo;
import com.px.pm.doman.vo.responseVo.PmDemandResVo;
import com.px.pm.service.IPmDemandService;
import com.px.pm.service.PmDemandHisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import com.px.common.core.controller.BaseController;
import com.px.common.core.domain.AjaxResult;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.util.*;

/**
 * 采购需求Controller
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Api(tags = "采购需求")
@RestController
@RequestMapping("/pm/demand")
public class PmDemandController extends BaseController {
    @Autowired
    private IPmDemandService pmDemandService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PmDemandHisService pmDemandHisService;

    /**
     * 查询采购需求列表
     */
    @ApiOperation(value = "查询采购需求列表", notes = "参数非必传")
    @PostMapping("/list")
    @PreAuthorize("@ss.hasPermi('pm:demand:list')")
    @Log(title = ModularConstans.demand, businessType = BusinessType.QUERY)
    public AjaxResult list(@RequestBody PmDemandReqVo pmDemandReqVo) {
        pmDemandReqVo.setDeptId(getDeptId());
        pmDemandReqVo.setIsAdmin(SysUser.isAdmin(getUserId()));
        pmDemandReqVo.setUserId(getUserId());
        IPage<PmDemandResVo> page = pmDemandService.selectPmDemandList(pmDemandReqVo, 0);
        return success(page);

    }

    /**
     * 查看详情
     */
    @ApiOperation(value = "查看详情", notes = "必传demandId和详情类型(1项目计划，2需求建档，3任务下达,4中标信息，5合同信息，6建设情况),其他字段不传")
    @PostMapping("/view")
//    @PreAuthorize("@ss.hasPermi('pm:demand:view')")  首页去除权限
    @Log(title = ModularConstans.demand, businessType = BusinessType.QUERY)
    public AjaxResult view(@RequestBody PmDemandReqVo pmDemandReqVo) {
        if (pmDemandReqVo.getDemandId() == null) {
            return AjaxResult.error("demandId不能为空");
        }
        if (StringUtils.isEmpty(pmDemandReqVo.getDetailType())) {
            return AjaxResult.error("详情的类型不能为空");
        }
        return success(pmDemandService.selectPmDemandByDemandId(pmDemandReqVo.getDemandId(), pmDemandReqVo.getDetailType()));
    }

    /**
     * 获取回退下拉列表
     */
    @ApiOperation(value = "获取回退下拉列表", notes = "必传demandId")
    @GetMapping("/getReturnList")
    @PreAuthorize("@ss.hasPermi('pm:demand:getReturnList')")
    @Log(title = ModularConstans.demand, businessType = BusinessType.QUERY)
    public AjaxResult getReturnList(@NotEmpty(message = "需求ID不能为空")
                                    @RequestParam(value = "demandId", required = true) Long demandId) {
        List<Map<String, Integer>> list = new ArrayList<>();
        LambdaQueryWrapper<PmDemandHis> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PmDemandHis::getDemandId, demandId).orderByAsc(PmDemandHis::getReturnOrderNumber);
        List<PmDemandHis> pmDemandHisList = this.pmDemandHisService.list(lambdaQueryWrapper);
        if (!ObjectUtils.isEmpty(pmDemandHisList)) {
            Map<String, Integer> map = new HashMap<>();
            map.put("当前信息", 0);
            list.add(map);
            for (PmDemandHis pmDemandHis : pmDemandHisList) {
                Map<String, Integer> map2 = new HashMap<>();
                map2.put("第" + pmDemandHis.getReturnOrderNumber() + "回退", pmDemandHis.getReturnOrderNumber());
                list.add(map2);
            }
        }

        return success(list);
    }

    /**
     * 查看历史详情
     */
    @ApiOperation(value = "查看历史详情", notes = "必传demandId和详情类型(1项目计划，2需求建档，3任务下达,4中标信息，5合同信息，6建设情况),必传returnOrderNumber(回退序号,0当前/1第一次回退/2第二次回退...),其他字段不传")
    @PostMapping("/viewHis")
    @PreAuthorize("@ss.hasPermi('pm:demand:viewHis')")
    @Log(title = ModularConstans.demand, businessType = BusinessType.QUERY)
    public AjaxResult viewHis(@RequestBody PmDemandReqVo pmDemandReqVo) {
        if (pmDemandReqVo.getDemandId() == null) {
            return AjaxResult.error("demandId不能为空");
        }
        if (ObjectUtils.isEmpty(pmDemandReqVo.getDetailType())) {
            return AjaxResult.error("详情的类型不能为空");
        }
        if (ObjectUtils.isEmpty(pmDemandReqVo.getReturnOrderNumber())) {
            return AjaxResult.error("回退序号不能为空");
        }
        if (0 == pmDemandReqVo.getReturnOrderNumber()) {
            return success(pmDemandService.selectPmDemandByDemandId(pmDemandReqVo.getDemandId(), pmDemandReqVo.getDetailType()));
        } else {
            return success(pmDemandHisService.selectPmDemandHisByDemandId(pmDemandReqVo.getDemandId(), pmDemandReqVo.getDetailType(), pmDemandReqVo.getReturnOrderNumber()));
        }

    }

    /**
     * 需求建档
     */
    @ApiOperation(value = "需求建档", notes = "必传demandId,根据项目类型必传pmDemandEngineeringResponseVo(3：工程类)、pmDemandEquipResponseVo(0：装备类)、pmDemandMaterialsResponseVo(1：物资类)、pmDemandServeResponseVo(2：服务类)其中之一")
    @PostMapping("/bookBuilding")
    @PreAuthorize("@ss.hasPermi('pm:demand:bookBuilding')")
    @Log(title = ModularConstans.demand, businessType = BusinessType.INSERT)
    public AjaxResult bookBuilding(@NotEmpty(message = "数据为空") @RequestBody PmBookBuildingReqVo pmBookBuildingReqVo, HttpServletRequest request) {
        try {
            if (pmBookBuildingReqVo.getDemandId() == null) {
                return AjaxResult.error("demandId不能为空");
            }
            PmDemand pmDemand = pmDemandService.getById(pmBookBuildingReqVo.getDemandId());
            if (ObjectUtils.isEmpty(pmDemand)) {
                return error("demandId参数错误");
            }
            if (ObjectUtils.isEmpty(pmBookBuildingReqVo.getBudgetAmount())) {
                return error("预算金额不能为空");
            }
            if (pmBookBuildingReqVo.getBudgetAmount() > pmDemand.getEvaluation()) {
                return error("需求建档的预算金额不能大于计划的预算金额");
            }
            if (ObjectUtils.isEmpty(pmBookBuildingReqVo.getDemandCommitTime())) {
                return error("需求提报时间不能为空");
            }
            if (ObjectUtils.isEmpty(pmBookBuildingReqVo.getSysFileRefs())) {
                return error("需求建档的附件不能为空");
            }
            LoginUser loginUser = tokenService.getLoginUser(request);
            pmBookBuildingReqVo.setCreateBy(String.valueOf(loginUser.getUserId()));
            pmBookBuildingReqVo.setCreateTime(new Date());
            pmBookBuildingReqVo.setUpdateBy(String.valueOf(loginUser.getUserId()));
            pmBookBuildingReqVo.setUpdateTime(pmBookBuildingReqVo.getCreateTime());
            return toAjax(pmDemandService.bookBuilding(pmBookBuildingReqVo));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @ApiOperation(value = "提交采购需求", notes = "必传demandId,其他字段不传")
    @PostMapping("/commit")
    @PreAuthorize("@ss.hasPermi('pm:demand:commit')")
    @Log(title = ModularConstans.demand, businessType = BusinessType.UPDATE)
    public AjaxResult commit(@RequestBody PmDemandReqVo pmDemandReqVo, HttpServletRequest request) {
        if (pmDemandReqVo.getDemandId() == null) {
            return AjaxResult.error("demandId不能为空");
        }

        LoginUser loginUser = tokenService.getLoginUser(request);
        pmDemandReqVo.setUpdateBy(String.valueOf(loginUser.getUserId()));
        return toAjax(pmDemandService.commit(pmDemandReqVo));
    }

    /**
     * 查看流程图，获取当前阶段名字
     */
    @ApiOperation(value = "查看流程图，获取当前阶段名字", notes = "必传demandId,其他字段不传")
    @PostMapping("/viewFlowChart")
    @PreAuthorize("@ss.hasPermi('pm:demand:viewFlowChart')")
    @Log(title = ModularConstans.demand, businessType = BusinessType.QUERY)
    public AjaxResult viewFlowChart(@RequestBody PmDemandReqVo pmRequestVo) {
        if (pmRequestVo.getDemandId() == null) {
            return AjaxResult.error("demandId不能为空");
        }
        return success(pmDemandService.viewFlowChart(pmRequestVo.getDemandId()));
    }


    /**
     * 审核单位查询采购需求列表
     */
    @ApiOperation(value = "审核单位查询采购需求列表", notes = "参数非必传")
    @PostMapping("/examineList")
    @PreAuthorize("@ss.hasPermi('pm:examine:list')")
    @Log(title = ModularConstans.demandExamine, businessType = BusinessType.QUERY)
    public AjaxResult examineList(@RequestBody PmDemandReqVo pmDemandReqVo) {
        Page pages = new Page();
        pmDemandReqVo.setDeptId(getDeptId());
        pmDemandReqVo.setIsAdmin(SysUser.isAdmin(getUserId()));
        pmDemandReqVo.setUserId(getUserId());
        List<PmDemandResVo> list = pmDemandService.selectPmDemandListDe(pmDemandReqVo);
        IPage<PmDemandResVo> results = new Page<>();
        if (!ObjectUtils.isEmpty(list)) {
            pages = PageUtils.getPages(pmDemandReqVo.getPageNum().intValue(), pmDemandReqVo.getPageSize().intValue(), list);
            results = new Page<>(pages.getCurrent(), pages.getSize(), list.size());
            results.setRecords(pages.getRecords());
        }
        return success(results);
    }

    @ApiOperation(value = "审核采购需求通过", notes = "必传demandId和上传附件，其他字段不传")
    @PostMapping("/reviewTo")
    @PreAuthorize("@ss.hasPermi('pm:examine:reviewTo')")
    @Log(title = ModularConstans.demandExamine, businessType = BusinessType.UPDATE)
    public AjaxResult reviewTo(@RequestBody PmDemandReqVo pmDemandReqVo, HttpServletRequest request) {
        if (pmDemandReqVo.getDemandId() == null) {
            return AjaxResult.error("demandId不能为空");
        }
        if (pmDemandReqVo.getSysFileRefs() == null || pmDemandReqVo.getSysFileRefs().size() == 0) {
            return AjaxResult.error("上传附件不能为空");
        }
        LoginUser loginUser = tokenService.getLoginUser(request);
        pmDemandReqVo.setUpdateBy(String.valueOf(loginUser.getUserId()));
        return toAjax(pmDemandService.reviewTo(pmDemandReqVo));
    }

    @ApiOperation(value = "审核采购需求退回", notes = "必传demandId和退回原因，其他字段不传")
    @PostMapping("/reviewReturn")
    @PreAuthorize("@ss.hasPermi('pm:examine:reviewReturn')")
    @Log(title = ModularConstans.demandExamine, businessType = BusinessType.UPDATE)
    public AjaxResult reviewReturn(@RequestBody PmDemandReqVo pmDemandReqVo, HttpServletRequest request) {
        if (pmDemandReqVo.getDemandId() == null) {
            return AjaxResult.error("demandId不能为空");
        }
        if (StringUtils.isEmpty(pmDemandReqVo.getRefuseReason())) {
            return AjaxResult.error("退回原因不能为空");
        }
        LoginUser loginUser = tokenService.getLoginUser(request);
        pmDemandReqVo.setUpdateBy(String.valueOf(loginUser.getUserId()));
        return toAjax(pmDemandService.reviewReturn(pmDemandReqVo));
    }

}
