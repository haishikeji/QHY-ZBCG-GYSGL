package com.px.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.px.base.domain.BaseExpert;
import com.px.base.domain.BaseProfessional;
import com.px.base.domain.BaseUnitInformation;
import com.px.base.domain.vo.BaseExpertVo;
import com.px.base.mapper.BaseExpertMapper;
import com.px.base.service.BaseExpertService;
import com.px.base.service.BaseProfessionalService;
import com.px.common.core.domain.AjaxResult;
import com.px.common.core.domain.entity.SysUser;
import com.px.common.core.domain.model.LoginUser;
import com.px.common.enums.*;
import com.px.common.exception.ServiceException;
import com.px.common.utils.PageUtils;
import com.px.common.utils.SecurityUtils;
import com.px.common.utils.StringUtils;
import com.px.common.utils.bean.BeanUtils;
import com.px.pm.doman.PmDemand;
import com.px.pm.doman.PmDemandExpertRef;
import com.px.pm.doman.vo.responseVo.PmDemandResVo;
import com.px.pm.mapper.PmDemandExpertRefMapper;
import com.px.pm.service.IPmDemandService;
import com.px.pm.service.PmDemandExpertRefService;
import com.px.system.domain.vo.SysRegionVO;
import com.px.system.domain.vo.responseVo.SysDeptResponseVo;
import com.px.system.service.ISysDeptService;
import com.px.system.service.ISysDictDataService;
import com.px.system.service.ISysDictTypeService;
import com.px.system.service.SysRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BaseExpertServiceImpl extends ServiceImpl<BaseExpertMapper, BaseExpert> implements BaseExpertService {

    @Autowired
    private BaseExpertMapper baseExpertMapper;
    @Autowired
    private PmDemandExpertRefService pmDemandExpertRefService;
    @Autowired
    private PmDemandExpertRefMapper expertRefMapper;
    @Autowired
    private IPmDemandService pmDemandService;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private ISysDictDataService dictDataService;
    @Autowired
    private ISysDictTypeService dictTypeService;
    @Autowired
    private BaseProfessionalService baseProfessionalService;
    @Autowired
    private SysRegionService sysRegionService;

    @Override
    public int insertExpert(BaseExpertVo baseExpertVo) {
        // base_unit_information 单位信息表
        if (StringUtils.isNotNull(baseExpertVo.getUnitInformation())) {
            List<BaseUnitInformation> baseUnitInformationList = baseExpertMapper.selectByUnitInformation(baseExpertVo.getUnitInformation());
            if (baseUnitInformationList.size() <= 0) {
                BaseUnitInformation baseUnitInformation = new BaseUnitInformation();
                baseUnitInformation.setUnitName(baseExpertVo.getUnitInformation());
                baseUnitInformation.setCreated(baseExpertVo.getExpertName());
                baseUnitInformation.setCreateTime(new Date());
                Integer i = baseExpertMapper.insertBaseUnitInformation(baseUnitInformation);
            }
        }
        // 区域list转字符串
        if (StringUtils.isNotNull(baseExpertVo.getLocalAreaList())) {
//            String stringFromList = String.join(",", baseExpertVo.getLocalAreaList());
            baseExpertVo.setLocalArea(baseExpertVo.getLocalAreaList().get(baseExpertVo.getLocalAreaList().size() - 1));
        }
        return baseExpertMapper.insertExpert(baseExpertVo);
    }

    @Override
    public int updateSupplierType(BaseExpert baseExpert) {
        LambdaUpdateWrapper<BaseExpert> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        //eq是指你查询的条件，set是指你修改的值
        lambdaUpdateWrapper
                .eq(BaseExpert::getId, baseExpert.getId())
                .set(BaseExpert::getStatus, baseExpert.getStatus());
        return baseExpertMapper.update(null, lambdaUpdateWrapper);
    }

    @Override
    public AjaxResult selectReviewProject(BaseExpertVo baseExpertVo) {
        // 通过专家ID查询 与专家库关联的采购执行管理ID
        List<Integer> demandIdList = pmDemandExpertRefService.selectByExpertId(baseExpertVo.getId());
        if (ObjectUtils.isEmpty(demandIdList)) {
            return AjaxResult.success(new PageInfo<>(demandIdList));
        }
        List<PmDemand> pmDemandList = pmDemandService.selectByDemandIdList(demandIdList);
        List<PmDemandResVo> pmDemandResponseVoList = changTo(pmDemandList);
        PageHelper.startPage(baseExpertVo.getPageNum().intValue(), baseExpertVo.getPageSize().intValue());
        PageInfo<PmDemandResVo> pageInfo = new PageInfo<>(pmDemandResponseVoList);
        return AjaxResult.success(pageInfo);
    }

    @Override
    public AjaxResult selectExtractionExpert(BaseExpertVo baseExpertVo) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        LambdaQueryWrapper<PmDemand> dp = new LambdaQueryWrapper<>();
        //PmDemandExpertRef项目专家关联表
        List<PmDemandExpertRef> list1 = expertRefMapper.selectList(new LambdaQueryWrapper<>());
        List<Long> collect = list1.stream().map(PmDemandExpertRef::getDemandId).collect(Collectors.toList());
        if (!SysUser.isAdmin(loginUser.getUserId())) {
            //指定部门--以及子单位的超额数据
            dp.in(PmDemand::getDemandId, pmDemandService.isQueryZD(Math.toIntExact(loginUser.getDeptId())));
        }
        // 查询抽取过专家的项目列表
        List<PmDemand> pmDemandList = pmDemandService.list(dp);
        List<PmDemandResVo> pmDemandResponseVoList = changTo(pmDemandList);
        // 遍历项目 查询专家姓名
        if (!ObjectUtils.isEmpty(pmDemandResponseVoList) && pmDemandResponseVoList.size() > 0) {
            for (PmDemandResVo pmDemandResVo : pmDemandResponseVoList) {
                Long demandId = pmDemandResVo.getDemandId();
                // 查询项目专家关联表 专家ID，关联时间
                List<PmDemandExpertRef> pmDemandExpertRefList = baseExpertMapper.getExpertIdList(demandId);
                // 专家ID列表
                List<Long> expertList = pmDemandExpertRefList.stream().map(PmDemandExpertRef::getExpertId).collect(Collectors.toList());
                // 获取抽取时间（列表中取其中一个时间）

                if (!ObjectUtils.isEmpty(expertList)) {
                    Date accessTime = pmDemandExpertRefList.get(0).getAccessTime();
                    Map<String, String> map = baseExpertMapper.getExpertNameList(expertList);
                    if (!ObjectUtils.isEmpty(map)) {
                        // 专家名称
                        String expertNameStr = map.get("expertName");
                        pmDemandResVo.setExpertNameStr(expertNameStr);
                    }
                    pmDemandResVo.setAccessTime(accessTime);
                }
            }
        }
        Page pages = PageUtils.getPages(baseExpertVo.getPageNum().intValue(), baseExpertVo.getPageSize().intValue(), pmDemandResponseVoList);
        PageInfo<PmDemandResVo> pageInfo = new PageInfo<>(pmDemandResponseVoList);
        if (!ObjectUtils.isEmpty(pages) && pages.getRecords().size() > 0) {
            pageInfo.setList(pages.getRecords());
            pageInfo.setSize(Math.toIntExact(pages.getSize()));
            pageInfo.setPageNum(Math.toIntExact(pages.getCurrent()));
            pageInfo.setTotal(pages.getTotal());
            pageInfo.setPages(Integer.parseInt(pages.getPages() + ""));
        }
        return AjaxResult.success(pageInfo);
    }

    /**
     * 自定义去重
     *
     * @param list
     */
    public static List<String> method(List<String> list) {
        // 新集合
        List<String> newList = new ArrayList<>(list.size());
        list.forEach(i -> {
            // 如果新集合中不存在则插入
            if (!newList.contains(i)) {
                newList.add(i);
            }
        });
        return newList;
    }

    @Override
    public List<BaseUnitInformation> getBaseUnitInformationList() {
        List<BaseUnitInformation> baseUnitInformationList = baseExpertMapper.selectByUnitInformation(null);
        return baseUnitInformationList;
    }

    @Transactional
    @Override
    public String importBaseExpert(List<BaseExpertVo> expertVos, LoginUser loginUser) {
        //获取字典数据
        HashMap<String, HashMap<String, String>> planEnums = dictTypeService.getAbouteExpertEnums();
        //专家类型
        HashMap<String, String> expertTypeMap = planEnums.get("expertTypes");
        //职称
        HashMap<String, String> professionalTitleMap = planEnums.get("professionalTitles");

        if (ObjectUtils.isEmpty(expertVos) || expertVos.size() < 1) {
            throw new ServiceException("导入专家信息不能为空！");
        }
        //转实体类
        List<BaseExpert> experts = new ArrayList<>();
        for (BaseExpertVo expertVo : expertVos) {
            BaseExpert expert = new BaseExpert();
            BeanUtils.copyProperties(expertVo, expert);
            expert.setMajorType(expertVo.getMajorTypeName());
            experts.add(expert);
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        ////deptService
        for (BaseExpert expert : experts) {
            try {
                //专家类型，专家状态
                if (!StringUtils.isEmpty(expertTypeMap.get(expert.getExpertType()))) {
                    expert.setExpertType(expertTypeMap.get(expert.getExpertType()));
                    expert.setStatus("0");
                } else {
                    ++failureNum;
                    failureMsg.append(failureNum + "、专家类型“" + expert.getExpertType() + "”不存在     <br/>");
                }
                if (expert.getIdNumber().length() > 18) {
                    ++failureNum;
                    failureMsg.append(failureNum + "、身份证号“" + expert.getIdNumber() + "”长度过长     <br/>");
                }
                //查询身份证号是否已使用
                LambdaQueryWrapper<BaseExpert> baseExpertWrapper = new LambdaQueryWrapper<>();
                baseExpertWrapper.eq(BaseExpert::getIdNumber, expert.getIdNumber());
                List<BaseExpert> idNumberList = baseExpertMapper.selectList(baseExpertWrapper);
                //身份证号已存在的不可添加
                if (!ObjectUtils.isEmpty(idNumberList)) {
                    ++failureNum;
                    failureMsg.append(failureNum + "、身份证号“" + expert.getIdNumber() + "”已被使用 <br/>");
                }
                //专业类型
                LambdaQueryWrapper<BaseProfessional> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(BaseProfessional::getProfessionalName, expert.getMajorType());
                List<BaseProfessional> list = baseProfessionalService.list(wrapper);
                //专家信息中存的是专业的code
                if (!ObjectUtils.isEmpty(list)) {
                    expert.setMajorType(list.get(0).getProfessionalCode());
                } else {
                    ++failureNum;
                    failureMsg.append(failureNum + "、专业类型“" + expert.getExpertType() + "”不存在     <br/>");
                }
                //职称
                if (!StringUtils.isEmpty(professionalTitleMap.get(expert.getProfessionalTitle()))) {
                    expert.setProfessionalTitle(professionalTitleMap.get(expert.getProfessionalTitle()));
                } else {
                    ++failureNum;
                    failureMsg.append(failureNum + "、职称“" + expert.getProfessionalTitle() + "”不存在    <br/>");
                }
                if (StringUtils.isNotNull(expert.getUnitInformation())) {
                    List<BaseUnitInformation> baseUnitInformationList = baseExpertMapper.selectByUnitInformation(expert.getUnitInformation());
                    if (baseUnitInformationList.size() <= 0) {
                        BaseUnitInformation baseUnitInformation = new BaseUnitInformation();
                        baseUnitInformation.setUnitName(expert.getUnitInformation());
                        baseUnitInformation.setCreated(loginUser.getUserId().toString());
                        baseUnitInformation.setCreateTime(new Date());
                        Integer i = baseExpertMapper.insertBaseUnitInformation(baseUnitInformation);
                        expert.setUnitInformation(i + "");
                    }
                }
                //地区
                if (StringUtils.isEmpty(expert.getLocalArea()) || expert.getLocalArea().split("/").length < 2) {
                    ++failureNum;
                    failureMsg.append(failureNum + "、地区“" + expert.getLocalArea() + "”不存在    <br/>");
                } else {
                    //大部分地区是三级
                    String[] localAreas = expert.getLocalArea().split("/");
                    if (localAreas.length == 3) {
                        List<SysRegionVO> sysRegionVO = sysRegionService.selectInfoByName(localAreas[0]);
                        List<SysRegionVO> sysRegionVO1 = sysRegionService.selectInfoByName(localAreas[1]);
                        List<SysRegionVO> sysRegionVO2 = sysRegionService.selectInfoByName(localAreas[2]);
                        if (ObjectUtils.isEmpty(sysRegionVO2) || ObjectUtils.isEmpty(sysRegionVO1) || ObjectUtils.isEmpty(sysRegionVO)) {
                            ++failureNum;
                            failureMsg.append(failureNum + "、地区“" + expert.getLocalArea() + "”信息有误  <br/>");
                        } else {
                            //是否成立上下级关系
                            boolean isLea = false;
                            for (SysRegionVO vo : sysRegionVO2) {
                                for (SysRegionVO regionVO : sysRegionVO1) {
                                    if (vo.getPid().equals(regionVO.getId())) {
                                        for (SysRegionVO sysRegionVO3 : sysRegionVO) {
                                            if (regionVO.getPid().equals(sysRegionVO3.getId())) {
                                                //保存行政编码
                                                isLea = true;
                                                expert.setLocalArea(vo.getCode());
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                            if (isLea == false) {
                                ++failureNum;
                                failureMsg.append(failureNum + "、地区“" + expert.getLocalArea() + "”非上下级关系    <br/>");
                            }
                        }

                        //香港等少数地区是二级
                    } else if (localAreas.length == 2) {
                        List<SysRegionVO> sysRegionVO = sysRegionService.selectInfoByName(localAreas[0]);
                        List<SysRegionVO> sysRegionVO1 = sysRegionService.selectInfoByName(localAreas[1]);
                        if (ObjectUtils.isEmpty(sysRegionVO1) || ObjectUtils.isEmpty(sysRegionVO)) {
                            ++failureNum;
                            failureMsg.append(failureNum + "、地区“" + expert.getLocalArea() + "”信息有误  <br/>");
                        } else {
                            //是否成立上下级关系
                            boolean isLea = false;
                            for (SysRegionVO regionVO : sysRegionVO) {
                                for (SysRegionVO vo : sysRegionVO1) {
                                    if (vo.getPid().equals(regionVO.getId())) {
                                        //保存行政编码
                                        isLea = true;
                                        expert.setLocalArea(vo.getCode());
                                        break;
                                    }
                                }
                            }
                            if (isLea == false) {
                                ++failureNum;
                                failureMsg.append(failureNum + "、地区“" + expert.getLocalArea() + "”非上下级关系    <br/>");
                            }
                        }
                    }
                }
                expert.setCreated(String.valueOf(loginUser.getUserId()));
                expert.setCreateTime(new Date());
                expert.setUpdated(String.valueOf(loginUser.getUserId()));
                expert.setUpdateTime(new Date());
                baseExpertMapper.insert(expert);
                successNum++;
                successMsg.append(successNum + "、专家【" + expert.getExpertName() + "】导入成功!    <br/>");
            } catch (Exception exc) {
                String msg = "      **专家【" + expert.getExpertName() + "】导入失败 <br/>";
                failureMsg.append(msg);
                log.error(msg, exc);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "导入失败！共 " + failureNum + " 条数据格式不正确：   <br/>");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "导入成功！共 " + successNum + " 条。<br/>");
        }
        return successMsg.toString();
    }


    private List<PmDemandResVo> changTo(List<PmDemand> pmDemandList) {
        List<PmDemandResVo> pmDemandResponseVoList = new ArrayList<>();
        //获取字典数据
        HashMap<String, HashMap<String, String>> planEnums = dictTypeService.getAboutEnums();
        HashMap<String, String> projectAttributes = planEnums.get("projectAttributes");

        if (pmDemandList != null && pmDemandList.size() > 0) {
            for (PmDemand pmDemand1 : pmDemandList) {
                PmDemandResVo vo = new PmDemandResVo();
                BeanUtils.copyProperties(pmDemand1, vo);
                if (vo.getPurchaseDeptId() != null) {
                    Map<String, Object> deptMap = deptService.selectDeptById(vo.getPurchaseDeptId());
                    if (deptMap != null) {
                        SysDeptResponseVo sysDeptResponseVo = (SysDeptResponseVo) deptMap.get("sysDept");
                        if (sysDeptResponseVo != null) {
                            vo.setPurchaseDeptName(sysDeptResponseVo.getDeptName());
                        }
                    }
                }
                String purchaseServicesName = dictDataService.selectDictLabel("purchase_services", vo.getPurchaseServices());
                if (StringUtils.isNotEmpty(purchaseServicesName)) {
                    vo.setPurchaseServicesName(purchaseServicesName);
                }
                //项目类型
                String projectTypeName = dictDataService.selectDictLabel("sys_project_type", vo.getProjectType());
                if (StringUtils.isNotEmpty(projectTypeName)) {
                    vo.setProjectTypeName(projectTypeName);
                }
                //是否为超限额计划
                for (IsExcess value : IsExcess.values()) {
                    if (vo.getIsExcess() != null && vo.getIsExcess().equals(value.getCode())) {
                        vo.setIsExcessName(value.getInfo());
                        break;
                    }
                }
                //采购方式
                for (PlanPurchaseMode value : PlanPurchaseMode.values()) {
                    if (vo.getPurchaseMode() != null && vo.getPurchaseMode().equals(value.getCode())) {
                        vo.setPurchaseModeName(value.getInfo());
                        break;
                    }
                }
                //项目属性是拼接的
                if (!ObjectUtils.isEmpty(vo.getProjectAttr())) {
                    if (vo.getProjectAttr().length() > 1) {
                        StringBuilder builder = new StringBuilder();
                        String[] split = vo.getProjectAttr().split(",");
                        for (String s : split) {
                            for (Map.Entry<String, String> entry : projectAttributes.entrySet()) {
                                if (s.equals(entry.getValue())) {
                                    if (!vo.getProjectAttr().endsWith(s)) {
                                        builder.append(entry.getKey() + ",");
                                    } else {
                                        builder.append(entry.getKey());
                                    }
                                    break;
                                }
                            }
                        }
                        vo.setProjectAttrName(builder.toString());
                    } else {
                        for (Map.Entry<String, String> entry : projectAttributes.entrySet()) {
                            if (vo.getProjectAttr().equals(entry.getValue())) {
                                vo.setProjectAttrName(entry.getKey());
                                break;
                            }
                        }
                    }
                }
                //预警状态
                for (WarnStatus value : WarnStatus.values()) {
                    if (vo.getWarnStatus() != null && vo.getWarnStatus().equals(value.getCode())) {
                        vo.setWarnStatusName(value.getInfo());
                        break;
                    }
                }
                //项目状态
                for (PmProjectStatus value : PmProjectStatus.values()) {
                    if (vo.getProjectStatus() != null && vo.getProjectStatus().equals(value.getCode())) {
                        vo.setProjectStatusName(value.getInfo());
                        break;
                    }
                }
                pmDemandResponseVoList.add(vo);
            }
        }
        return pmDemandResponseVoList;
    }
}
