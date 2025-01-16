package com.px.web.controller.plan;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.common.annotation.Log;
import com.px.common.constant.ModularConstans;
import com.px.common.core.controller.BaseController;
import com.px.common.core.domain.AjaxResult;
import com.px.common.core.domain.model.LoginUser;
import com.px.common.enums.BusinessType;
import com.px.common.enums.SysFileRefEnum;
import com.px.common.exception.ServiceException;
import com.px.common.exception.base.BaseException;
import com.px.common.utils.PageUtils;
import com.px.common.utils.StringUtils;
import com.px.common.utils.poi.ExcelUtil;
import com.px.framework.web.service.TokenService;
import com.px.plan.doman.ProvisionalPlan;
import com.px.plan.doman.vo.requestVo.ProvisionalPlanVo;
import com.px.plan.doman.vo.responseVo.ProvisionalPlanResVo;
import com.px.plan.service.ProvisionalPlanService;
import com.px.plan.service.impl.ProvisionalPlanServiceImpl;
import com.px.system.domain.SysFileRef;
import com.px.system.service.SysFileRefService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 临时计划
 *
 * @author 品讯科技
 */
@Api(tags = "临时计划")
@Slf4j
@RestController
@RequestMapping("/plan/provisionalPlan")
public class ProvisionalPlanController extends BaseController {

    @Autowired
    private ProvisionalPlanService provisionalPlanService;
    @Autowired
    private ProvisionalPlanServiceImpl provisionalPlanServiceImpl;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private SysFileRefService sysFileRefService;

    @ApiOperation(value = "新增临时计划")
    @PostMapping("/insertProvisionalPlan")
    @PreAuthorize("@ss.hasPermi('plan:provisionalPlan:insertProvisionalPlan')")
    @Log(title = ModularConstans.provisionalPlan, businessType = BusinessType.INSERT)
    public AjaxResult insertProvisionalPlan(@RequestBody ProvisionalPlan provisionalPlan, HttpServletRequest request) {
        //年度中已存在的不能再使用（临时审核通过后会进行同步到年度计划）
        //if (provisionalPlanService.countYearProjectName(provisionalPlan.getProjectName()) > 0) {
        //    return AjaxResult.error("该项目名称已经使用。");
        //}
        //if (provisionalPlanService.countProjectName(provisionalPlan.getProjectName()) > 0) {
        //    return AjaxResult.error("该项目名称已经使用。");
        //}
        //获取采购单位
        LoginUser loginUser = tokenService.getLoginUser(request);
        provisionalPlan.setPurchaseDeptId(String.valueOf(loginUser.getDeptId()));
        provisionalPlan.setCreated(String.valueOf(loginUser.getUserId()));
        provisionalPlan.setCreateTime(new Date());
        //默认未超额
        if (ObjectUtils.isEmpty(provisionalPlan.getIsExcess())) {
            provisionalPlan.setIsExcess("0");
        }
        // planType计划类型（0：季度计划，1：临时计划）
        provisionalPlan.setPlanType("1");
        // 项目状态(0:计划待提交，1:计划待审核，2:计划已退回，3:计划已审核)
        provisionalPlan.setProjectStatus("0");
        //     发函状态（0：未发函或已处理，1：确认发函）
        provisionalPlan.setSendLetter("0");
        // 计算是否超限额计划 *** 改为前端录入
        boolean save = provisionalPlanService.save(provisionalPlan);
        List<SysFileRef> sysFileRefs = provisionalPlan.getSysFileRefs();
        if (!ObjectUtils.isEmpty(sysFileRefs)) {
            if (save) {
                for (SysFileRef ref : sysFileRefs) {
                    ref.setRedId(provisionalPlan.getPlanPracticalId());
                    ref.setType(SysFileRefEnum.PLAN_TEMPORARY.getType());
                    ref.setCreated(provisionalPlan.getCreated());
                    ref.setCreateTime(new Date());
                    ref.setUpdated(provisionalPlan.getCreated());
                    ref.setUpdateTime(new Date());
                    sysFileRefService.save(ref);
                }
            }

        }
        return toAjax(save);
    }

    @ApiOperation(value = "删除临时计划", notes = "必传 plan_practical_id")
    @PostMapping("/deleteProvisionalPlan")
    @PreAuthorize("@ss.hasPermi('plan:provisionalPlan:deleteProvisionalPlan')")
    @Log(title = ModularConstans.provisionalPlan, businessType = BusinessType.DELETE)
    public AjaxResult deleteProvisionalPlan(@RequestBody ProvisionalPlan provisionalPlan) {
        if (StringUtils.isNull(provisionalPlan) || StringUtils.isNull(provisionalPlan.getPlanPracticalId())) {
            return error("临时计划id不能为空");
        }
        return toAjax(provisionalPlanService.deleteProvisionalPlanById(provisionalPlan.getPlanPracticalId()));
    }

    @ApiOperation(value = "修改临时计划", notes = "必传 planPracticalId 及修改数据")
    @PostMapping("/updateProvisionalPlan")
    @PreAuthorize("@ss.hasPermi('plan:provisionalPlan:updateProvisionalPlan')")
    @Log(title = ModularConstans.provisionalPlan, businessType = BusinessType.UPDATE)
    public AjaxResult updateProvisionalPlan(@RequestBody ProvisionalPlanVo provisionalPlan) {
        //年度中已存在的不能再使用（临时审核通过后会进行同步到年度计划）
        if (provisionalPlanService.countYearProjectName(provisionalPlan.getProjectName()) > 0) {
            return AjaxResult.error("该项目已同步至年度计划。");
        }
        if (provisionalPlanService.countProjectNameOth(provisionalPlan) > 0) {
            return AjaxResult.error("该项目名称已经使用。");
        }
        if (StringUtils.isNull(provisionalPlan) || StringUtils.isNull(provisionalPlan.getPlanPracticalId())) {
            return error("临时计划id和修改数据不能为空");
        }
        log.info(":{}", provisionalPlan);
        // 计算是否超限额计划 *** 改为前端录入
        // provisionalPlan.setIsExcess(iSysDeptService.isExcessOrNo(provisionalPlan.getProjectType(), provisionalPlan.getEvaluation(), getDeptId()));
        return toAjax(provisionalPlanService.updateProvisionalPlanById(provisionalPlan));
    }

    @ApiOperation(value = "查询临时计划")
    @PostMapping("/selectProvisionalPlan")
    @PreAuthorize("@ss.hasPermi('plan:provisionalPlan:selectProvisionalPlan')")
    @Log(title = ModularConstans.provisionalPlan, businessType = BusinessType.QUERY)
    public AjaxResult selectProvisionalPlan(@RequestBody ProvisionalPlanVo vo, HttpServletRequest request) {
        if (StringUtils.isNull(vo)
                || StringUtils.isNull(vo.getPageNum())
                || StringUtils.isNull(vo.getPageSize())) {
            throw new ServiceException("分页参数不能为空！");
        }
        LoginUser loginUser = tokenService.getLoginUser(request);
        return provisionalPlanService.selectProvisionalPlan(vo, request, loginUser);
    }

    @ApiOperation(value = "查询临时计划-审核单位")
    @PostMapping("/selectProvisionalPlanAudit")
    @PreAuthorize("@ss.hasPermi('plan:provisionalPlan:selectProvisionalPlanAudit')")
    @Log(title = ModularConstans.provisionalPlan, businessType = BusinessType.QUERY)
    public AjaxResult selectProvisionalPlanAudit(@RequestBody ProvisionalPlanVo vo, HttpServletRequest request) {
        List<ProvisionalPlanResVo> provisionalPlanList = new ArrayList<>();
        if (StringUtils.isNull(vo)
                || StringUtils.isNull(vo.getPageNum())
                || StringUtils.isNull(vo.getPageSize())) {
            throw new ServiceException("分页参数不能为空！");
        }
        Page pages = new Page();
        try {
            ProvisionalPlanVo standardVo = new ProvisionalPlanVo();
            standardVo.setPageNum(null);
            standardVo.setPageSize(null);
            LoginUser loginUser = tokenService.getLoginUser(request);
            standardVo.setCreated(String.valueOf(loginUser.getUserId()));
            //前端没有选择部门id，就默认登录人的id
            if (!ObjectUtils.isEmpty(vo.getDeptId())) {
                standardVo.setPurchaseDeptId(vo.getDeptId());
            } else {
                standardVo.setPurchaseDeptId(loginUser.getDeptId());
            }
            List<ProvisionalPlan> provisionalPlans = provisionalPlanService.queryPage(standardVo);
            if (!ObjectUtils.isEmpty(provisionalPlans) && !ObjectUtils.isEmpty(provisionalPlans.size())) {
                provisionalPlanList = provisionalPlanServiceImpl.changeTo(provisionalPlans);
                pages = PageUtils.getPages(vo.getPageNum().intValue(), vo.getPageSize().intValue(), provisionalPlanList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.success(pages);
    }

    @ApiOperation(value = "导出临时计划(审核单位)")
    @PostMapping("/exportProvisionalPlanAudit")
    @PreAuthorize("@ss.hasPermi('plan:provisionalPlan:exportProvisionalPlanAudit')")
    @Log(title = ModularConstans.provisionalPlan, businessType = BusinessType.EXPORT)
    public void exportProvisionalPlanAudit(HttpServletResponse response,@RequestBody ProvisionalPlanVo vo, HttpServletRequest request){
        List<ProvisionalPlanResVo> provisionalPlanList = new ArrayList<>();
        try {
            ProvisionalPlanVo standardVo = new ProvisionalPlanVo();
            standardVo.setPageNum(null);
            standardVo.setPageSize(null);
            LoginUser loginUser = tokenService.getLoginUser(request);
            standardVo.setCreated(String.valueOf(loginUser.getUserId()));
            //前端没有选择部门id，就默认登录人的id
            if (!ObjectUtils.isEmpty(vo.getDeptId())) {
                standardVo.setPurchaseDeptId(vo.getDeptId());
            } else {
                standardVo.setPurchaseDeptId(loginUser.getDeptId());
            }
            List<ProvisionalPlan> provisionalPlans = provisionalPlanService.queryPage(standardVo);
            if (!ObjectUtils.isEmpty(provisionalPlans) && !ObjectUtils.isEmpty(provisionalPlans.size())) {
                provisionalPlanList = provisionalPlanServiceImpl.changeTo(provisionalPlans);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long num = 1L;
        for(ProvisionalPlanResVo li:provisionalPlanList){
            li.setPlanPracticalId(num);
            num++;
        }
        ExcelUtil<ProvisionalPlanResVo> util = new ExcelUtil<>(ProvisionalPlanResVo.class);
        util.exportExcel(response, provisionalPlanList, "临时计划数据(审核单位)");
    }

    @ApiOperation(value = "查询临时计划详情")
    @PostMapping("/selectById")
    @PreAuthorize("@ss.hasPermi('plan:provisionalPlan:selectById')")
    @Log(title = ModularConstans.provisionalPlan, businessType = BusinessType.QUERY)
    public AjaxResult selectById(@RequestBody ProvisionalPlanVo provisionalPlanVo) {
        if (StringUtils.isNull(provisionalPlanVo) || StringUtils.isNull(provisionalPlanVo.getPlanPracticalId())) {
            throw new ServiceException("id不能为空！");
        }
        return provisionalPlanService.seletById(provisionalPlanVo.getPlanPracticalId());
    }

    @ApiOperation(value = "导入临时计划")
    @PostMapping("/importProvisionalPlan")
    @PreAuthorize("@ss.hasPermi('plan:provisionalPlan:importProvisionalPlan')")
    @Log(title = ModularConstans.provisionalPlan, businessType = BusinessType.INSERT)
    public AjaxResult importProvisionalPlan(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<ProvisionalPlan> util = new ExcelUtil<>(ProvisionalPlan.class);
        List<ProvisionalPlan> provisionalPlans = util.importExcel(file.getInputStream(), 3);
        if (StringUtils.isNull(provisionalPlans) || provisionalPlans.size() == 0) {
            throw new ServiceException("导入临时计划数据不能为空！");
        }
        //获取采购单位-
        LoginUser loginUser = getLoginUser();
        String message = provisionalPlanService.importProvisionalPlan(provisionalPlans, updateSupport, loginUser);
        return success(message);
    }

    @ApiOperation(value = "导出临时计划")
    @PostMapping("/exportProvisionalPlan")
    @PreAuthorize("@ss.hasPermi('plan:provisionalPlan:exportProvisionalPlan')")
    @Log(title = ModularConstans.provisionalPlan, businessType = BusinessType.EXPORT)
    public void exportProvisionalPlan(HttpServletResponse response, @RequestBody ProvisionalPlanVo provisionalPlanVo, HttpServletRequest request) throws Exception {
        LoginUser loginUser = tokenService.getLoginUser(request);
        List<ProvisionalPlanResVo> list = provisionalPlanService.selectProvisionalPlanExport(provisionalPlanVo, loginUser);
        ExcelUtil<ProvisionalPlanResVo> util = new ExcelUtil<>(ProvisionalPlanResVo.class);
        util.exportExcel(response, list, "临时计划数据");
    }

    @ApiOperation(value = "提交临时计划")
    @PostMapping("/commitProvisionalPlan")
    @PreAuthorize("@ss.hasPermi('plan:provisionalPlan:commitProvisionalPlan')")
    @Log(title = ModularConstans.provisionalPlan, businessType = BusinessType.UPDATE)
    public AjaxResult commitProvisionalPlan(@RequestBody ProvisionalPlanVo provisionalPlanVo) {
        if (StringUtils.isNull(provisionalPlanVo) || StringUtils.isNull(provisionalPlanVo.getPlanPracticalId())) {
            return error("提交的临时计划ID不能为空！");
        }
        return toAjax(provisionalPlanService.commitProvisionalPlan(provisionalPlanVo));
    }

    @ApiOperation(value = "审核通过")
    @PostMapping("/auditPass")
    @PreAuthorize("@ss.hasPermi('plan:provisionalPlan:auditPass')")
    @Log(title = ModularConstans.provisionalPlan, businessType = BusinessType.UPDATE)
    public AjaxResult auditPass(@RequestBody ProvisionalPlanVo provisionalPlanVo, HttpServletRequest request) {
        if (StringUtils.isNull(provisionalPlanVo) || StringUtils.isNull(provisionalPlanVo.getPlanPracticalId())) {
            return error("审核数据ID不能为空！");
        }
        LoginUser loginUser = tokenService.getLoginUser(request);
        provisionalPlanVo.setUpdated(String.valueOf(loginUser.getUserId()));
        return provisionalPlanService.auditPass(provisionalPlanVo);
    }

    @ApiOperation(value = "审核不通过")
    @PostMapping("/auditNoPass")
    @PreAuthorize("@ss.hasPermi('plan:provisionalPlan:auditNoPass')")
    @Log(title = ModularConstans.provisionalPlan, businessType = BusinessType.UPDATE)
    public AjaxResult auditNoPass(@RequestBody ProvisionalPlanVo provisionalPlanVo, HttpServletRequest request) {
        if (StringUtils.isNull(provisionalPlanVo) || StringUtils.isNull(provisionalPlanVo.getPlanPracticalId())) {
            return error("审核数据ID不能为空！");
        }
        LoginUser loginUser = tokenService.getLoginUser(request);
        provisionalPlanVo.setUpdated(String.valueOf(loginUser.getUserId()));
        return provisionalPlanService.auditNoPass(provisionalPlanVo);
    }

    @ApiOperation(value = "填写采购服务站")
    @PostMapping("/adPurchaseServices")
    //@PreAuthorize("@ss.hasPermi('plan:planYears:adPurchaseServices')")
    @Log(title = ModularConstans.provisionalPlan, businessType = BusinessType.UPDATE)
    public AjaxResult adPurchaseServices(@RequestBody ProvisionalPlanVo provisionalPlanVo) {
        return provisionalPlanService.adPurchaseServices(provisionalPlanVo);
    }

    @ApiOperation("模板下载")
    @GetMapping("/downloaExcel")
    public void downloadZip(HttpServletResponse response) {
        List<ProvisionalPlanResVo> list = new ArrayList<ProvisionalPlanResVo>();
        list.add(new ProvisionalPlanResVo(1L, "单位1", "环太平洋项目", "物资服务类",
                "徒步环太平洋", new BigDecimal(12.23), "单位1,单位2", "科目一",
                "2023-02", "2023-03", "2023-04",
                "重点采购项目,重大规划任务项目", ""));
        list.add(new ProvisionalPlanResVo(2L, "单位2", "环北冰洋项目", "工程类",
                "徒步环北冰洋", new BigDecimal(15.88), "单位1", "科目二",
                "2023-01", "2023-05", "2023-08",
                "重点采购项目", ""));
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/template/provisional_plan.xlsx");
        OutputStream outputStream = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("临时计划（模板）", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            outputStream = response.getOutputStream();
            ExcelWriter excelWriter = EasyExcel.write(outputStream)
                    .withTemplate(resourceAsStream).autoCloseStream(true).build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            excelWriter.fill(list, writeSheet);
            excelWriter.finish();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BaseException("下载文件失败");
        }
    }

}
