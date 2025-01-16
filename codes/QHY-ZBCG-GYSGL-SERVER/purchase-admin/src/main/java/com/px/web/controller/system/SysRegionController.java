package com.px.web.controller.system;


import com.px.common.core.controller.BaseController;
import com.px.common.core.domain.AjaxResult;
import com.px.system.domain.vo.SysRegionVO;
import com.px.system.service.SysRegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @author
 * @version 1.0.0 创建于 2019-12-04
 **/
@RestController
@Api(value = "SysRegionController", tags = {"行政区划管理"})
@Slf4j
@RequestMapping("/system/region")
public class SysRegionController extends BaseController {

    @Resource
    private SysRegionService sysRegionService;

    @ApiOperation(value = "获取省列表不限权", notes = "获取省列表不限权")
    @GetMapping(value = "/getProvinceListNoneLimit", produces = "application/json;charset=utf-8")
    public AjaxResult getProvinceList(HttpServletRequest request) {
        List<SysRegionVO> list = this.sysRegionService.geAdministrativeDivisionList(null, 1);
        return success(list);
    }

    @ApiOperation(value = "获取地级市列表不限权", notes = "获取地级市列表不限权")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "provinceId", value = "省区划ID", required = true)
    })
    @GetMapping(value = "/getCityListNoneLimit", produces = "application/json;charset=utf-8")
    public AjaxResult getCityList(HttpServletRequest request,
                                  @RequestParam(value = "provinceId", required = true)
                                          Long provinceId) {
        List<SysRegionVO> list = this.sysRegionService.geAdministrativeDivisionList(provinceId, 2);
        return success(list);
    }

    @ApiOperation(value = "获取区县列表不限权", notes = "获取区县列表不限权")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "cityId", value = "地级市ID", required = true)
    })
    @GetMapping(value = "/getDistrictListNoneLimit", produces = "application/json;charset=utf-8")
    public AjaxResult getDistrictList(HttpServletRequest request,
                                      @RequestParam(value = "cityId", required = true)
                                              Long cityId) {
        List<SysRegionVO> list = this.sysRegionService.geAdministrativeDivisionList(cityId, 3);
        return success(list);
    }


    @ApiOperation(value = "获取父级编码列表，省/市/县 -test", notes = "获取父级编码列表，省/市/县 -test")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", required = true, name = "token", dataType = "String", value = "token验证信息"),
            @ApiImplicitParam(paramType = "query", name = "code", value = "行政区划编码", required = true)
    })
    @GetMapping(value = "/getParentAdministrativeDivisionCodeList", produces = "application/json;charset=utf-8")
    public AjaxResult getParentAdministrativeDivisionCodeList(HttpServletRequest request,
                                                              @RequestParam(value = "code", required = true)
                                                                      String code) {

        List<String> list = this.sysRegionService.getParentAdministrativeDivisionCodeList(code, true);
        return success(list);

    }

    @ApiOperation(value = "获取父级名称列表，省/市/县 -test", notes = "获取父级名称列表，省/市/县 -test")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", required = true, name = "token", dataType = "String", value = "token验证信息"),
            @ApiImplicitParam(paramType = "query", name = "code", value = "行政区划编码", required = true)
    })
    @GetMapping(value = "/getParentAdministrativeDivisionNames", produces = "application/json;charset=utf-8")
    public AjaxResult getParentAdministrativeDivisionNames(HttpServletRequest request,
                                                           @RequestParam(value = "code", required = true)
                                                                   String code){
            String list = this.sysRegionService.getParentAdministrativeDivisionNames(code);
            return success(list);
    }

}
