package com.px.system.service;


import com.px.common.core.domain.AjaxResult;
import com.px.system.domain.SysDeptOrgan;

import java.util.List;

/**
 * 部门的机关指导处 服务层
 *
 * @author 品讯科技
 */
public interface SysDeptOrganService {

    /**
     * 获取机关指导处列表
     *
     * @return
     */
    List<SysDeptOrgan> selectList(SysDeptOrgan deptOrgan);

    AjaxResult selectListMap();

    /**
     * @return
     */
    AjaxResult insertDeptOrgan(List<SysDeptOrgan> deptOrganList, Long userId);

    /**
     * 删除机关指导处
     *
     * @param deptOrgan
     * @return
     */
    AjaxResult deleteById(SysDeptOrgan deptOrgan);
}
