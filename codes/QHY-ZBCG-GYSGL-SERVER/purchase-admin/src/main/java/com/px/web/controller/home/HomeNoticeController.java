package com.px.web.controller.home;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.common.annotation.Log;
import com.px.common.constant.ModularConstans;
import com.px.common.core.controller.BaseController;
import com.px.common.core.domain.AjaxResult;
import com.px.common.core.domain.entity.SysUser;
import com.px.common.enums.BusinessType;
import com.px.common.utils.SecurityUtils;
import com.px.common.utils.StringUtils;
import com.px.home.domain.HomeNotice;
import com.px.home.domain.vo.HomeNoticeVo;
import com.px.home.domain.vo.HomeToDoQueryResVo;
import com.px.home.service.HomeNoticeService;
import com.px.plan.doman.PlanQuarter;
import com.px.plan.doman.PlanYears;
import com.px.plan.doman.vo.requestVo.PlanYearsStandardVo;
import com.px.plan.service.PlanQuarterService;
import com.px.plan.service.PlanYearsService;
import com.px.pm.doman.PmDemand;
import com.px.pm.service.IPmDemandService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 首页公告控制层
 *
 * @author 品讯科技
 */
@RestController
@RequestMapping("/home/homeNotice")
public class HomeNoticeController extends BaseController {

    @Resource
    private HomeNoticeService homeNoticeService;
    @Autowired
    private PlanYearsService planYearsService;
    @Autowired
    private PlanQuarterService planQuarterService;
    @Autowired
    private IPmDemandService demandService;

    @ApiOperation(value = "查询首页公告")
    @PostMapping("/selectHomeNotice")
//    @PreAuthorize("@ss.hasPermi('home:homeNotice:selecthomeNotice')")
    @Log(title = ModularConstans.notice, businessType = BusinessType.QUERY)
    public AjaxResult selectHomeNotice(@RequestBody HomeNoticeVo homeNoticeVo) {
        LambdaQueryWrapper<HomeNotice> lw = new LambdaQueryWrapper<HomeNotice>();
        if (!StringUtils.isBlank(homeNoticeVo.getNoticeName())) {
            lw.like(HomeNotice::getNoticeName, "%" + homeNoticeVo.getNoticeName() + "%");
        }
        if (!StringUtils.isBlank(homeNoticeVo.getAnnouncementClassification())) {
            lw.eq(HomeNotice::getAnnouncementClassification, homeNoticeVo.getAnnouncementClassification());
        }
        if (!StringUtils.isBlank(homeNoticeVo.getHomepageClassification())) {
            lw.eq(HomeNotice::getHomepageClassification, homeNoticeVo.getHomepageClassification());
        }
        if (!ObjectUtils.isEmpty(homeNoticeVo.getBeginTime())) {
            lw.ge(HomeNotice::getNoticeTime, homeNoticeVo.getBeginTime());
        }
        if (!ObjectUtils.isEmpty(homeNoticeVo.getEndTime())) {
            lw.le(HomeNotice::getNoticeTime, homeNoticeVo.getEndTime());
        }
        IPage<HomeNotice> page = homeNoticeService.page(new Page<HomeNotice>(homeNoticeVo.getPageNum(), homeNoticeVo.getPageSize()), lw);
        return success(page);
    }

    @ApiOperation(value = "搜索项目列表")
    @PostMapping("/listPlanYears")
    public AjaxResult listPlanYears(@RequestBody PlanYearsStandardVo yearsStandardVo) {
        if (ObjectUtils.isEmpty(yearsStandardVo.getProjectName())) {
            return error("查询项目名称不能为空！");
        }
        LambdaQueryWrapper<PlanYears> lw = new LambdaQueryWrapper<PlanYears>();
        lw.like(PlanYears::getProjectName, "%" + yearsStandardVo.getProjectName() + "%");
        lw.eq(PlanYears::getDelFlay, 0);
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            //查出不在季度计划表中的id
            List<Integer> ids = planYearsService.idNotInQuarter();
            if (!ObjectUtils.isEmpty(ids)) {
                lw.in(PlanYears::getPlanYearId, ids);
            }
            lw.eq(PlanYears::getPurchaseDeptId, SecurityUtils.getDeptId());
        }
        List<PlanYears> list = planYearsService.list(lw);

        //项目搜索的结果中要包含季度、临时计划（过滤掉已同步到项目管理的临时、季度计划）
        LambdaQueryWrapper<PlanQuarter> ls = new LambdaQueryWrapper<PlanQuarter>();
        ls.like(PlanQuarter::getProjectName, "%" + yearsStandardVo.getProjectName() + "%");
        ls.eq(PlanQuarter::getDelFlay, 0);
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            //查出不在项目表中的季度、临时id
            List<Integer> ids = planQuarterService.idNotDemand();
            if (!ObjectUtils.isEmpty(ids)) {
                ls.in(PlanQuarter::getPlanPracticalId, ids);
            }
            ls.eq(PlanQuarter::getPurchaseDeptId, SecurityUtils.getDeptId());
        }
        List<PlanQuarter> lists = planQuarterService.list(ls);
        //年度计划融合季度、临时计划
        for (PlanYears year : list) {
            PlanQuarter quarter = new PlanQuarter();
            BeanUtils.copyProperties(year, quarter);
            lists.add(quarter);
        }
        //查询项目信息，
        LambdaQueryWrapper<PmDemand> lq = new LambdaQueryWrapper<PmDemand>();
        lq.like(PmDemand::getProjectName, "%" + yearsStandardVo.getProjectName() + "%");
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            lq.eq(PmDemand::getPurchaseDeptId, SecurityUtils.getDeptId());
        }
        List<PmDemand> demandList = demandService.list(lq);
        //放在同一个列表里返回
        List<HomeToDoQueryResVo> home = planYearsService.listToHome(lists, demandList);
        return AjaxResult.success(home);
    }

    @ApiOperation(value = "搜索项目--选择项目查看详情")
    @PostMapping("/projectDetails")
    public AjaxResult projectDetails(@RequestBody PlanYearsStandardVo yearsStandardVo) {
        if (StringUtils.isNull(yearsStandardVo.getPlanYearId())) {
            return error("项目ID不能为空！");
        }
        return planYearsService.projectDetails(yearsStandardVo);
    }

    /**
     * 查询不同类型的公告
     */
    @PreAuthorize("@ss.hasPermi('home:homeNotice:getNoticesByType')")
    @Log(title = "查询公告", businessType = BusinessType.QUERY)
    @PostMapping("getNoticesByType")
    @ApiOperation("查询不同类型的公告")
    public AjaxResult getNoticesByType(@RequestBody HomeNoticeVo homeNoticeVo) {
        LambdaQueryWrapper<HomeNotice> lw = new LambdaQueryWrapper<>();
        if (!ObjectUtils.isEmpty(homeNoticeVo.getNoticeName())) {
            lw.like(HomeNotice::getNoticeName, homeNoticeVo.getNoticeName());
        }
        if (!ObjectUtils.isEmpty(homeNoticeVo.getAnnouncementClassification())) {
            lw.eq(HomeNotice::getAnnouncementClassification, homeNoticeVo.getAnnouncementClassification());
        }
        if (!ObjectUtils.isEmpty(homeNoticeVo.getBeginTime())) {
            lw.ge(HomeNotice::getNoticeTime, homeNoticeVo.getBeginTime());
        }
        if (!ObjectUtils.isEmpty(homeNoticeVo.getEndTime())) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateFormatT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            Date parse = null;
            try {
                parse = dateFormatT.parse((dateFormat.format(homeNoticeVo.getEndTime()) + " 23:59:59:999"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            lw.le(HomeNotice::getNoticeTime, parse);
        }

        List<HomeNotice> noticeList = homeNoticeService.list(lw);
        return AjaxResult.success(noticeList);
    }


}
