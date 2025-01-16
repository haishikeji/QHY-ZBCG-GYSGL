package com.px.plan.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.px.plan.doman.PlanQuarter;
import com.px.plan.doman.vo.requestVo.PlanQuarterStandardVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户与角色关联表 数据层
 *
 * @author 品讯科技
 */
@Mapper
public interface PlanQuarterMapper extends BaseMapper<PlanQuarter> {

    /**
     * 查询季度计划记录集合
     *
     * @param PlanQuarter 季度计划请求对象
     * @return 季度计划记录集合
     */
    public List<PlanQuarter> selectPlanQuarterList(PlanQuarter PlanQuarter);

    /**
     * 查询季度计划记录集合(审核单位)
     *
     * @param PlanQuarter 季度计划请求对象
     * @return 季度计划记录集合
     */
    public List<PlanQuarter> selectPlanQuarterExamineList(PlanQuarter PlanQuarter);

    /**
     * 创建季度计划
     *
     * @param PlanQuarter 季度计划请求对象
     */
    public Integer insertPlanQuarter(PlanQuarter PlanQuarter);

    /**
     * 批量删除季度计划
     *
     * @param planIds 需要删除的季度计划ID
     * @return 结果
     */
    public Integer deletePlanQuarterByIds(Long[] planIds);

    /**
     * 查询项目名称是否存在
     *
     * @param projectName 项目名称
     * @return 结果
     */
    public Integer countProjectName(String projectName);

    /**
     * 查询季度计划
     *
     * @param planPracticalId 季度计划ID
     * @return 结果
     */
    public PlanQuarter getById(Long planPracticalId);

    /**
     * 根据年度计划id查询季度计划
     *
     * @param planYearId 年度计划ID
     * @return 结果
     */
    public PlanQuarter getByPlanYearId(Long planYearId);

    /**
     * 查询项目名称是否存在(除自己以外)
     *
     * @param projectName 项目名称
     * @return 结果
     */
    public Integer countProjectNameOther(@Param("projectName") String projectName, @Param("planPracticalId") String planPracticalId);

    /**
     * 修改季度计划
     *
     * @param ofYears 季度计划
     * @return 结果
     */
    public Integer updateInfoById(PlanQuarter ofYears);

    public Integer deletePlanQuarterById(Long planPracticalId);

    public Integer commit(Long planPracticalId);

    public Integer review(PlanQuarterStandardVo vo);

    Integer sendLetter(Long planPracticalId);

    List<PlanQuarter> query(PlanQuarterStandardVo vo);

    @Select("SELECT plan_practical_id  FROM plan_practical WHERE plan_practical_id not in " +
            " (SELECT IFNULL(plan_id,-1) FROM pm_demand)  AND del_flay=0")
    List<Integer> idNotDemand();
}
