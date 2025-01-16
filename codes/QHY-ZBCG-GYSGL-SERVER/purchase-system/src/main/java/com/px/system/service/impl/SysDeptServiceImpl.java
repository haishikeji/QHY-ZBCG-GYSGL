package com.px.system.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.px.common.core.domain.AjaxResult;
import com.px.common.core.domain.entity.SysProcurementStandard;
import com.px.common.core.domain.model.LoginUser;
import com.px.common.exception.base.BaseException;
import com.px.system.domain.vo.responseVo.SysDeptResponseVo;
import com.px.system.mapper.SysDeptMapper;
import com.px.system.mapper.SysProcurementStandardMapper;
import com.px.system.mapper.SysRoleMapper;
import com.px.system.service.ISysDeptService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.px.common.annotation.DataScope;
import com.px.common.constant.UserConstants;
import com.px.common.core.domain.TreeSelect;
import com.px.common.core.domain.entity.SysDept;
import com.px.common.core.domain.entity.SysRole;
import com.px.common.core.domain.entity.SysUser;
import com.px.common.core.text.Convert;
import com.px.common.exception.ServiceException;
import com.px.common.utils.SecurityUtils;
import com.px.common.utils.StringUtils;
import com.px.common.utils.spring.SpringUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * 部门管理 服务实现
 *
 * @author 品讯科技
 */
@Service
public class SysDeptServiceImpl implements ISysDeptService {

    @Autowired
    private SysDeptMapper deptMapper;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private SysRoleMapper roleMapper;
    @Resource
    private SysProcurementStandardMapper sysProcurementStandardMapper;

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysDept> selectDeptList(SysDept dept) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (!String.valueOf(loginUser.getUserId()).equals("1")) {
            List<Long> deptList = new ArrayList<>();
            //获取到子孙级部门id
            Long deptId = loginUser.getDeptId();
            if (deptService.hasChildByDeptId(deptId)) {
                List<Long> children = deptService.getDeptChildren(deptId);
                System.err.println(children.size());
                deptList.addAll(children);
            }
            deptList.add(deptId);
            dept.setDeptList(deptList);
        }
        return deptMapper.selectDeptList(dept);
    }

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysDept> selectDeptListAndF(SysDept dept) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Long deptId = loginUser.getDeptId();
        if (!String.valueOf(loginUser.getUserId()).equals("1")) {
            List<Long> deptList = new ArrayList<>();
            //获取到子孙级部门id
            if (deptService.hasChildByDeptId(deptId)) {
                List<Long> children = deptService.getDeptChildren(deptId);
                System.err.println(children.size());
                deptList.addAll(children);
            }
            //查询上一级到顶级
            SysDept sysDept = deptService.selectById(deptId);
            String[] split = sysDept.getAncestors().split(",");
            if (split.length > 1) {
                for (String s : split) {
                    deptList.add(Long.valueOf(s));
                }
            }
            deptList.add(deptId);
            dept.setDeptList(deptList);
        }
        return deptMapper.selectDeptList(dept);
    }

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysDept> selectDeptListAndUp(SysDept dept) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (!String.valueOf(loginUser.getUserId()).equals("1")) {
            List<Long> deptList = new ArrayList<>();
            //获取到子孙级部门id
            Long deptId = loginUser.getDeptId();
            if (deptService.hasChildByDeptId(deptId)) {
                List<Long> children = deptService.getDeptChildren(deptId);
                System.err.println(children.size());
                deptList.addAll(children);
            }
            //查询上一级
            SysDept sysDept = deptService.selectByCid(loginUser.getDeptId());
            if (!ObjectUtils.isEmpty(sysDept)) {
                deptList.add(sysDept.getDeptId());
            }
            deptList.add(deptId);
            dept.setDeptList(deptList);
        }
        return deptMapper.selectDeptList(dept);
    }

    /**
     * 查询部门树结构信息
     *
     * @param dept 部门信息
     * @return 部门树信息集合
     */
    @Override
    public List<TreeSelect> selectDeptTreeList(SysDept dept) {
        List<SysDept> depts = SpringUtils.getAopProxy(this).selectDeptList(dept);
        return buildDeptTreeSelect(depts);
    }

    /**
     * 查询部门树结构信息
     *
     * @param dept 部门信息
     * @return 部门树信息集合
     */
    @Override
    public List<TreeSelect> selectDeptTreeListF(SysDept dept) {
        List<SysDept> depts = SpringUtils.getAopProxy(this).selectDeptListAndF(dept);
        return buildDeptTreeSelect(depts);
    }

    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    @Override
    public List<SysDept> buildDeptTree(List<SysDept> depts) {
        List<SysDept> returnList = new ArrayList<SysDept>();
        List<Long> tempList = depts.stream().map(SysDept::getDeptId).collect(Collectors.toList());
        for (SysDept dept : depts) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts) {
        List<SysDept> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    @Override
    public List<Long> selectDeptListByRoleId(Long roleId) {
        SysRole role = roleMapper.selectRoleById(roleId);
        return deptMapper.selectDeptListByRoleId(roleId, role.isDeptCheckStrictly());
    }

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Override
    public Map<String, Object> selectDeptById(Long deptId) {
        Map<String, Object> returnMap = new HashMap<>();
        SysDept sysDept = deptMapper.selectDeptById(deptId);
        SysDeptResponseVo sysDeptResponseVo = new SysDeptResponseVo();
        if (ObjectUtils.isEmpty(sysDept)) {
            throw new BaseException("deptId为" + deptId + "的单位不存在");
        }
        BeanUtils.copyProperties(sysDept, sysDeptResponseVo);
        QueryWrapper<SysProcurementStandard> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_id", deptId);
        List<SysProcurementStandard> sysProcurementStandardsList = sysProcurementStandardMapper.selectList(queryWrapper);

        returnMap.put("sysDept", sysDeptResponseVo);
        returnMap.put("sysProcurementStandardsList", sysProcurementStandardsList);
        returnMap.put("dto", sysDept);
        return returnMap;
    }

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Override
    public int selectNormalChildrenDeptById(Long deptId) {
        return deptMapper.selectNormalChildrenDeptById(deptId);
    }

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptId(Long deptId) {
        int result = deptMapper.hasChildByDeptId(deptId);
        return result > 0;
    }

    /**
     * 获取所有子节点id
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public List<Long> getChildIdByDeptId(Long deptId) {
        return deptMapper.getChildIdByDeptId(deptId);
    }

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(Long deptId) {
        int result = deptMapper.checkDeptExistUser(deptId);
        return result > 0;
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public String checkDeptNameUnique(SysDept dept) {
        Long deptId = StringUtils.isNull(dept.getDeptId()) ? -1L : dept.getDeptId();
        SysDept info = deptMapper.checkDeptNameUnique(dept.getDeptName(), dept.getParentId());
        if (StringUtils.isNotNull(info) && info.getDeptId().longValue() != deptId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验部门是否有数据权限
     *
     * @param deptId 部门id
     */
    @Override
    public void checkDeptDataScope(Long deptId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            SysDept dept = new SysDept();
            dept.setDeptId(deptId);
            List<SysDept> depts = SpringUtils.getAopProxy(this).selectDeptList(dept);
            if (StringUtils.isEmpty(depts)) {
                throw new ServiceException("没有权限访问部门数据！");
            }
        }
    }

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public AjaxResult insertDept(SysDept dept) {
        SysDept info = deptMapper.selectDeptById(dept.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus())) {
            throw new ServiceException("部门停用，不允许新增");
        }
        //授权到5级单位，创建6级单位时提示：只允许创建5级单位
        String[] ancestors = info.getAncestors().split(",");
        if (ancestors.length >= 6) {
            return AjaxResult.error("只允许创建5级单位");
        }
        dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
        Integer dept1 = deptMapper.insertDept(dept);
        return AjaxResult.success(dept.getDeptId());
    }

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int updateDept(SysDept dept) {
        SysDept newParentDept = deptMapper.selectDeptById(dept.getParentId());
        SysDept oldDept = deptMapper.selectDeptById(dept.getDeptId());
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept)) {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
        }
        int result = deptMapper.updateDept(dept);
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus()) && StringUtils.isNotEmpty(dept.getAncestors())
                && !StringUtils.equals("0", dept.getAncestors())) {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatusNormal(dept);
        }
        return result;
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private void updateParentDeptStatusNormal(SysDept dept) {
        String ancestors = dept.getAncestors();
        Long[] deptIds = Convert.toLongArray(ancestors);
        deptMapper.updateDeptStatusNormal(deptIds);
    }

    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        List<SysDept> children = deptMapper.selectChildrenDeptById(deptId);
        for (SysDept child : children) {
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }
        if (children.size() > 0) {
            deptMapper.updateDeptChildren(children);
        }
    }

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public int deleteDeptById(Long deptId) {
        return deptMapper.deleteDeptById(deptId);
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysDept> list, SysDept t) {
        // 得到子节点列表
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysDept> getChildList(List<SysDept> list, SysDept t) {
        List<SysDept> tlist = new ArrayList<SysDept>();
        Iterator<SysDept> it = list.iterator();
        while (it.hasNext()) {
            SysDept n = (SysDept) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getDeptId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDept> list, SysDept t) {
        return getChildList(list, t).size() > 0;
    }

    //判断是否为超额计划
    @Override
    public String isExcessOrNo(String projectType, BigDecimal evaluation, Long deptId) {
        String result = "0";
        BigDecimal threshold = new BigDecimal(0);
        //获取各个项目类型设定的概算金额阈值
        QueryWrapper<SysProcurementStandard> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_id", deptId);
        queryWrapper.eq("category", projectType.equals("2") ? "1" : projectType.equals("1") ? "2" : projectType);
        List<SysProcurementStandard> sysProcurementStandardsList = sysProcurementStandardMapper.selectList(queryWrapper);
        if (!ObjectUtils.isEmpty(sysProcurementStandardsList)) {
            BigDecimal maximum = sysProcurementStandardsList.get(0).getMaximum();
            if (ObjectUtils.isEmpty(maximum)) {
                return "0";
            }
            if (evaluation.compareTo(maximum) == 1) {
                //是超额计划
                result = "1";
            } else {
                result = "0";
            }
        }
        return result;

    }

    //获取到子孙级部门id
    public List<Long> getDeptChildren(Long deptId) {
        List<Long> deptList = new ArrayList<>();
        List<Long> childByDeptId = getChildIdByDeptId(deptId);
        for (Long cid : childByDeptId) {
            deptList.add(cid);
            if (hasChildByDeptId(cid)) {
                deptList.addAll(getDeptChildren(cid));
            }
        }
        return deptList;
    }

    @Override
    public SysDept selectById(Long deptId) {
        return deptMapper.selectDeptById(deptId);
    }

    @Override
    public List<SysDept> selectChildrenDeptById(Long deptId) {
        return deptMapper.selectChildrenDeptById(deptId);
    }

    @Override
    public List<SysDept> selectMaxDeptByParentId(Long deptId) {
        return deptMapper.selectMaxDeptByParentId(deptId);
    }

    @Override
    public SysDept selectByCid(Long deptId) {
        return deptMapper.selectByCid(deptId);
    }
}
