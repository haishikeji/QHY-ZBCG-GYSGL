package com.px.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.px.system.domain.SysDeptOrgan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 部门的机关指导处
 *
 * @author 品讯科技
 */
@Mapper
public interface SysDeptOrganMapper extends BaseMapper<SysDeptOrgan> {

    /**
     * 校验机关指导处名称是否唯一
     *
     * @param organName 机关指导处名称
     * @return 结果
     */
    public SysDeptOrgan checkDeptOrganNameOnlyOne(@Param("organName") String organName);
}
