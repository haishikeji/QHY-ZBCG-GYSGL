package com.px.web.controller.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.px.common.config.PurchaseConfig;
import com.px.common.utils.StringUtils;

/**
 * 首页
 *
 * @author 品讯科技
 */
@RestController
@Api(tags = "首页")
public class SysIndexController
{
    /** 系统基础配置 */
    @Autowired
    private PurchaseConfig purchaseConfig;

    /**
     * 访问首页，提示语
     */
    @RequestMapping("/")
    @ApiOperation("访问首页，提示语")
    public String index()
    {
        return StringUtils.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", purchaseConfig.getName(), purchaseConfig.getVersion());
    }
}
