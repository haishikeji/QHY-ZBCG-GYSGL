package com.px.plan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.px.plan.doman.ProvisionalPlan;
import com.px.plan.doman.vo.requestVo.ProvisionalPlanVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProvisionalPlanMapper extends BaseMapper<ProvisionalPlan> {
    int deleteProvisionalPlanById(@Param("planPracticalId") Long planPracticalId);

    int updateProvisionalPlanById(ProvisionalPlanVo provisionalPlan);

    List<ProvisionalPlan> selectProjectName(String projectName);

    int commitProvisionalPlan(@Param("planPracticalId") Long planPracticalId);

    ProvisionalPlan seletById(@Param("planPracticalId") Long planPracticalId);

    ProvisionalPlan seletByPlanid(@Param("planPracticalId") Long planPracticalId);

    List<ProvisionalPlan> selectProvisionalPlan(ProvisionalPlanVo provisionalPlanVo);

    List<ProvisionalPlan> selectProvisionalPlanAudit(ProvisionalPlanVo provisionalPlanVo);

    int review(ProvisionalPlanVo vo);

    /**
     * 通过年度ID，查询季度、临时计划详情
     *
     * @param planYearId
     * @return
     */
    ProvisionalPlan selectProvisionalPlanByYearId(@Param("planYearId") Integer planYearId);

    List<ProvisionalPlan> query(ProvisionalPlanVo vo);

    /**
     * 查询项目名称是否存在
     * (临时计划审核后要同步到年度计划，所以名称不能与年度计划名称重复)
     *
     * @param projectName 项目名称
     * @return 结果
     */
    public Integer countYearProjectName(String projectName);

    /**
     * 查询项目名称是否存在
     * (也要验证临时计划里不能有重复的)
     *
     * @param projectName 项目名称
     * @return 结果
     */
    public Integer countProjectName(String projectName);

    /**
     * 查询项目名称是否存在(除本身外)
     * (也要验证临时计划里不能有重复的)
     *
     * @return 结果
     */
    public Integer countProjectNameOth(ProvisionalPlanVo vo);
}
