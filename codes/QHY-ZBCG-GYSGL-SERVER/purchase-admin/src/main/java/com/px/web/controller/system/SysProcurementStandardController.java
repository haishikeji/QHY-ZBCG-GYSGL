package com.px.web.controller.system;


import com.px.common.core.domain.R;
import com.px.common.core.domain.entity.SysProcurementStandard;
import com.px.system.domain.vo.requestVo.SysProcurementStandardVo;
import com.px.system.service.SysProcurementStandardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 采购标准前端控制器
 * </p>
 *
 * @author 品讯科技
 * @since 2022-11-02
 */
@Api(tags = "采购标准接口")
@RestController
@RequestMapping("system/SysProcurementStandard")
@CrossOrigin
public class SysProcurementStandardController {

    @Resource
    private SysProcurementStandardService sysProcurementStandardService;

    @PreAuthorize("@ss.hasPermi('system:dept:addSysProcurementStandar')")
    @ApiOperation("添加采购单位管理中标准信息")
    @PostMapping("save")
    public R<String> save(@RequestBody List<SysProcurementStandardVo> sysProcurementStandardVoList) {
        boolean isSuccess = false;
        SysProcurementStandard sysProcurementStandard = new SysProcurementStandard();
        for (SysProcurementStandardVo sysProcurementStandardVo : sysProcurementStandardVoList) {
            BeanUtils.copyProperties(sysProcurementStandardVo, sysProcurementStandard);
            isSuccess = sysProcurementStandardService.save(sysProcurementStandard);
            if (!isSuccess) {
                return R.fail();
            }
        }
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('system:dept:updateSysProcurementStandar')")
    @ApiOperation("修改采购单位管理中标准信息")
    @PostMapping("update")
    public R<String> update(@RequestBody List<SysProcurementStandardVo> sysProcurementStandardVoList) {
        boolean isSuccess = false;
        SysProcurementStandard sysProcurementStandard = new SysProcurementStandard();
        for (SysProcurementStandardVo sysProcurementStandardVo : sysProcurementStandardVoList) {
            BeanUtils.copyProperties(sysProcurementStandardVo, sysProcurementStandard);
            isSuccess = sysProcurementStandardService.updateById(sysProcurementStandard);
            if (!isSuccess) {
                return R.fail();
            }
        }
        return R.ok();
    }
}

