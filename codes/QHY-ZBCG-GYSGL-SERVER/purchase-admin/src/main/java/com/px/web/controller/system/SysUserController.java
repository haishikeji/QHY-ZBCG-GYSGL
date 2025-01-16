package com.px.web.controller.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.common.core.domain.model.LoginUser;
import com.px.pm.service.IPmDemandService;
import com.px.system.domain.vo.requestVo.SysUserPageRequestVo;
import com.px.system.domain.vo.requestVo.SysUserRequestVo;
import com.px.system.domain.vo.responseVo.SysDeptResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.px.common.annotation.Log;
import com.px.common.constant.UserConstants;
import com.px.common.core.controller.BaseController;
import com.px.common.core.domain.AjaxResult;
import com.px.common.core.domain.entity.SysDept;
import com.px.common.core.domain.entity.SysRole;
import com.px.common.core.domain.entity.SysUser;
import com.px.common.enums.BusinessType;
import com.px.common.utils.SecurityUtils;
import com.px.common.utils.StringUtils;
import com.px.common.utils.poi.ExcelUtil;
import com.px.system.service.ISysDeptService;
import com.px.system.service.ISysPostService;
import com.px.system.service.ISysRoleService;
import com.px.system.service.ISysUserService;

/**
 * 用户信息
 *
 * @author 品讯科技
 */
@RestController
@RequestMapping("/system/user")
@Api(tags = "用户信息")
public class SysUserController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysPostService postService;
    @Autowired
    private ISysDeptService iSysDeptService;
    @Autowired
    private IPmDemandService demandService;

//    /**
//     * 获取用户列表
//     */
//    @PreAuthorize("@ss.hasPermi('system:user:list')")
//    @GetMapping("/list")
//    @ApiOperation("获取用户列表")
//    public TableDataInfo list(SysUser user)
//    {
//        startPage();
//        List<SysUser> list = userService.selectUserList(user);
//        return getDataTable(list);
//    }

    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @PostMapping("/list")
    @ApiOperation("获取用户列表")
    public AjaxResult list(@RequestBody SysUserPageRequestVo sysUserPageRequestVo) {
        LambdaQueryWrapper<SysUser> lw = new LambdaQueryWrapper<SysUser>();
        lw.eq(SysUser::getDelFlag, 0);
        if (!StringUtils.isBlank(sysUserPageRequestVo.getNickName())) {
            lw.like(SysUser::getNickName, "%" + sysUserPageRequestVo.getNickName() + "%");
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (!String.valueOf(loginUser.getUserId()).equals("1")) {
            List<Long> deptList = new ArrayList<>();
            //获取到子孙级部门id
            Long deptId = loginUser.getDeptId();
            if (deptService.hasChildByDeptId(loginUser.getDeptId())) {
                List<Long> children = deptService.getDeptChildren(deptId);
                System.err.println(children.size());
                deptList.addAll(children);
            }
            deptList.add(deptId);
            lw.in(SysUser::getDeptId, deptList);
        } else if (!StringUtils.isBlank(sysUserPageRequestVo.getDeptId())) {
            lw.eq(SysUser::getDeptId, sysUserPageRequestVo.getDeptId());
        }
        IPage<SysUser> page = userService.page(new Page<>(sysUserPageRequestVo.getPageNum(), sysUserPageRequestVo.getPageSize()), lw);
        if (!ObjectUtils.isEmpty(page.getRecords())) {
            page.setRecords(page.getRecords().stream().map(dto -> {
                // 采购单位名称
                Map<String, Object> stringObjectMap = iSysDeptService.selectDeptById(dto.getDeptId());
                if (!ObjectUtils.isEmpty(stringObjectMap)) {
                    SysDeptResponseVo sysDeptResponseVo = (SysDeptResponseVo) stringObjectMap.get("sysDept");
                    SysDept sysDept = new SysDept();
                    sysDept.setDeptName(sysDeptResponseVo.getDeptName());
                    dto.setDept(sysDept);
                }
                return dto;
            }).collect(Collectors.toList()));
        }
        if (!ObjectUtils.isEmpty(page) && !ObjectUtils.isEmpty(page.getRecords())) {
            List<SysUser> dto1 = page.getRecords().stream().map(o -> {
                Map<String, Object> stringObjectMap = deptService.selectDeptById(o.getDeptId());
                Object sysDept = stringObjectMap.get("dto");
                if (!ObjectUtils.isEmpty(sysDept)) {
                    SysDept dto = (SysDept) sysDept;
                    o.setDept(dto);
                    o.setDeptName(dto.getDeptName());
                }
                return o;
            }).collect(Collectors.toList());
            page.setRecords(dto1);
        }
        return success(page);
    }

    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:user:export')")
    @PostMapping("/export")
    @ApiOperation("用户管理")
    public void export(HttpServletResponse response, SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.exportExcel(response, list, "用户数据");
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/importData")
    @ApiOperation("用户管理")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return success(message);
    }

    @PostMapping("/importTemplate")
    @ApiOperation("用户数据")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.importTemplateExcel(response, "用户数据");
    }

    /**
     * 根据用户编号获取详细信息
     */
    @GetMapping(value = {"/", "/{userId}"})
    @ApiOperation("根据用户编号获取详细信息")
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        userService.checkUserDataScope(userId);
        AjaxResult ajax = AjaxResult.success();
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        ajax.put("posts", postService.selectPostAll());
        if (StringUtils.isNotNull(userId)) {
            SysUser sysUser = userService.selectUserById(userId);
            ajax.put(AjaxResult.DATA_TAG, sysUser);
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            ajax.put("roleIds", sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toList()));
        }
        return ajax;
    }

//    /**
//     * 新增用户
//     */
//    @PreAuthorize("@ss.hasPermi('system:user:add')")
//    @Log(title = "用户管理", businessType = BusinessType.INSERT)
//    @PostMapping
//    @ApiOperation("新增用户")
//    public AjaxResult add(@Validated @RequestBody SysUser user)
//    {
//        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user)))
//        {
//            return error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
//        }
//        else if (StringUtils.isNotEmpty(user.getPhonenumber())
//                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
//        {
//            return error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
//        }
//        else if (StringUtils.isNotEmpty(user.getEmail())
//                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
//        {
//            return error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
//        }
//        user.setCreateBy(getUsername());
//        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
//        return toAjax(userService.insertUser(user));
//    }


    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增用户")
    public AjaxResult add(@Validated @RequestBody SysUserRequestVo sysUserRequestVo) {
        SysUser user = new SysUser();
        BeanUtils.copyProperties(sysUserRequestVo, user);
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user))) {
            return error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        user.setCreateBy(getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return toAjax(userService.saveUser(user));
    }

//    /**
//     * 修改用户
//     */
//    @PreAuthorize("@ss.hasPermi('system:user:edit')")
//    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
//    @PutMapping
//    @ApiOperation("修改用户")
//    public AjaxResult edit(@Validated @RequestBody SysUser user)
//    {
//        userService.checkUserAllowed(user);
//        userService.checkUserDataScope(user.getUserId());
//        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user)))
//        {
//            return error("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
//        }
//        else if (StringUtils.isNotEmpty(user.getPhonenumber())
//                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
//        {
//            return error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
//        }
//        else if (StringUtils.isNotEmpty(user.getEmail())
//                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
//        {
//            return error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
//        }
//        user.setUpdateBy(getUsername());
//        return toAjax(userService.updateUser(user));
//    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改用户")
    public AjaxResult edit(@Validated @RequestBody SysUserRequestVo sysUserRequestVo) {
        SysUser user = new SysUser();
        BeanUtils.copyProperties(sysUserRequestVo, user);
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user))) {
            return error("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUsers(user));
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    @ApiOperation("删除用户")
    public AjaxResult remove(@PathVariable Long[] userIds) {
        if (ArrayUtils.contains(userIds, getUserId())) {
            return error("当前用户不能删除");
        }
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    @ApiOperation("重置密码")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(getUsername());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    @ApiOperation("状态修改")
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUserStatus(user));
    }

    /**
     * 根据用户编号获取授权角色
     */
    @GetMapping("/authRole/{userId}")
    @ApiOperation("根据用户编号获取授权角色")
    public AjaxResult authRole(@PathVariable("userId") Long userId) {
        AjaxResult ajax = AjaxResult.success();
        SysUser user = userService.selectUserById(userId);
        List<SysRole> roles = roleService.selectRolesByUserId(userId);
        ajax.put("user", user);
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        return ajax;
    }

    /**
     * 用户授权角色
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PutMapping("/authRole")
    @ApiOperation("用户授权角色")
    public AjaxResult insertAuthRole(Long userId, Long[] roleIds) {
        userService.checkUserDataScope(userId);
        userService.insertUserAuth(userId, roleIds);
        return success();
    }

    /**
     * 获取部门树列表
     */
    @GetMapping("/deptTree")
    @ApiOperation("获取部门树列表")
    public AjaxResult deptTree(SysDept dept) {
        return success(deptService.selectDeptTreeList(dept));
    }

    /**
     * 获取部门列表(包含自己的祖级)
     */
    @GetMapping("/deptTreeF")
    @ApiOperation("获取部门树列表")
    public AjaxResult deptTreeF(SysDept dept) {
        return success(deptService.selectDeptTreeListF(dept));
    }
}
