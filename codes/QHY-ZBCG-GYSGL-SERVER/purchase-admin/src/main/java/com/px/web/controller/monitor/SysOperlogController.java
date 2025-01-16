package com.px.web.controller.monitor;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.system.domain.vo.requestVo.SysOperLogRequestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.px.common.annotation.Log;
import com.px.common.core.controller.BaseController;
import com.px.common.core.domain.AjaxResult;
import com.px.common.enums.BusinessType;
import com.px.common.utils.poi.ExcelUtil;
import com.px.system.domain.SysOperLog;
import com.px.system.service.ISysOperLogService;

/**
 * 操作日志记录
 *
 * @author 品讯科技
 */
@RestController
@RequestMapping("/monitor/operlog")
public class SysOperlogController extends BaseController
{
    @Autowired
    private ISysOperLogService operLogService;

//    @PreAuthorize("@ss.hasPermi('monitor:operlog:list')")
//    @GetMapping("/list")
//    public TableDataInfo list(SysOperLog operLog)
//    {
//        startPage();
//        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
//        return getDataTable(list);
//    }

    @PreAuthorize("@ss.hasPermi('monitor:operlog:list')")
    @PostMapping("/list")
    public AjaxResult list(@RequestBody SysOperLogRequestVo vo)
    {
        LambdaQueryWrapper<SysOperLog> lw = new LambdaQueryWrapper<>();
        if(!ObjectUtils.isEmpty(vo.getStartTime())){
            lw.ge(SysOperLog::getOperTime,vo.getStartTime());
        }
        if(!ObjectUtils.isEmpty(vo.getStartTime())){
            lw.le(SysOperLog::getOperTime,vo.getEntTime());
        }
        if(!ObjectUtils.isEmpty(vo.getBusinessType())){
            lw.ge(SysOperLog::getBusinessType,vo.getBusinessType());
        }
        if(!ObjectUtils.isEmpty(vo.getStatus())){
            lw.ge(SysOperLog::getStatus,vo.getStatus());
        }
        IPage<SysOperLog> page = operLogService.page(new Page<>(vo.getPageNum(), vo.getPageSize()), lw);
        return success(page);
    }

    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('monitor:operlog:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysOperLog operLog)
    {
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        ExcelUtil<SysOperLog> util = new ExcelUtil<SysOperLog>(SysOperLog.class);
        util.exportExcel(response, list, "操作日志");
    }

    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/{operIds}")
    public AjaxResult remove(@PathVariable Long[] operIds)
    {
        return toAjax(operLogService.deleteOperLogByIds(operIds));
    }

    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/clean")
    public AjaxResult clean()
    {
        operLogService.cleanOperLog();
        return success();
    }
}
