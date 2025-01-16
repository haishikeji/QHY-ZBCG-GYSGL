package com.px.web.controller.base;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.base.domain.BaseNoticeType;
import com.px.base.service.BaseNoticeTypeService;
import com.px.base.vo.BaseNoticeTypePageReqVo;
import com.px.common.annotation.Log;
import com.px.common.constant.ModularConstans;
import com.px.common.core.controller.BaseController;
import com.px.common.core.domain.AjaxResult;
import com.px.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Api(tags = ModularConstans.noticeType)
@RestController
@RequestMapping("/base/notice/type")
public class BaseNoticeTypeController extends BaseController {


    @Autowired
    private BaseNoticeTypeService baseNoticeTypeService;

    @ApiOperation(value = "分页查询公告类型信息")
    @PostMapping("/page")
//    @PreAuthorize("@ss.hasPermi('base:noticeType:list')")   首页调用去掉权限
    @Log(title = ModularConstans.noticeType, businessType = BusinessType.QUERY)
    public AjaxResult page(@NotEmpty(message = "数据为空")
                               @RequestBody BaseNoticeTypePageReqVo vo) {
        LambdaQueryWrapper<BaseNoticeType> lw = new LambdaQueryWrapper<>();
        if(!StringUtils.isBlank(vo.getName())){
            lw.like(BaseNoticeType::getName,vo.getName());
        }
        IPage<BaseNoticeType> page = baseNoticeTypeService.page(new Page<>(vo.getPageNum(), vo.getPageSize()), lw);
        return success(page);
    }

    @ApiOperation(value = "修改公告类型信息")
    @PostMapping("/updateInfo")
    @PreAuthorize("@ss.hasPermi('base:noticeType:edit')")
    @Log(title = ModularConstans.noticeType, businessType = BusinessType.UPDATE)
    public AjaxResult updateInfo(@NotEmpty(message = "数据为空")
                                 @RequestBody BaseNoticeType vo) {
        vo.setUpdateTime(new Date());
        vo.setUpdated(getUserId().toString());
        return toAjax(baseNoticeTypeService.updateById(vo));
    }

    @ApiOperation(value = "新增公告类型信息")
    @PostMapping("/insert")
    @PreAuthorize("@ss.hasPermi('base:noticeType:add')")
    @Log(title = ModularConstans.noticeType, businessType = BusinessType.INSERT)
    public AjaxResult insert(@NotEmpty(message = "数据为空")
                                 @RequestBody BaseNoticeType vo) {
        vo.setCreated(getUserId().toString());
        vo.setCreateTime(new Date());
        vo.setUpdated(vo.getCreated());
        vo.setUpdateTime(vo.getCreateTime());
        return toAjax(baseNoticeTypeService.save(vo));
    }

    @ApiOperation(value = "删除公告类型信息")
    @PostMapping("/remove")
    @PreAuthorize("@ss.hasPermi('base:noticeType:remove')")
    @Log(title = ModularConstans.noticeType, businessType = BusinessType.DELETE)
    public AjaxResult remove(@NotEmpty(message = "主键id不能为空")
                             @RequestBody List<Long> ids) {
        return toAjax(baseNoticeTypeService.removeBatchByIds(ids));
    }


    @ApiOperation(value = "查询公告类型信息-下拉")
    @PostMapping("/list")
    @Log(title = ModularConstans.noticeType, businessType = BusinessType.QUERY)
    public AjaxResult list() {
        return success(baseNoticeTypeService.list());
    }
}
