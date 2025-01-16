package com.px.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.base.domain.BaseAgency;
import com.px.base.service.BaseAgencyService;
import com.px.common.enums.*;
import com.px.common.utils.StringUtils;
import com.px.common.utils.bean.BeanUtils;
import com.px.plan.doman.ProvisionalPlan;
import com.px.plan.mapper.ProvisionalPlanMapper;
import com.px.pm.doman.*;
import com.px.pm.doman.vo.responseVo.*;
import com.px.pm.mapper.PmDemandHisMapper;
import com.px.pm.service.*;
import com.px.system.domain.SysFileInfo;
import com.px.system.domain.SysFileRef;
import com.px.system.domain.vo.responseVo.SysDeptResponseVo;
import com.px.system.service.ISysDeptService;
import com.px.system.service.ISysDictDataService;
import com.px.system.service.SysFileRefService;
import com.px.system.service.SysFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 采购需求表历史表(PmDemandHis)è¡¨æå¡å®ç°ç±»
 *
 * @author 品讯科技
 * @since 2023-03-06 16:43:50
 */
@Service
public class PmDemandHisServiceImpl extends ServiceImpl<PmDemandHisMapper, PmDemandHis> implements PmDemandHisService {
    @Autowired
    private IPmDemandService iPmDemandService;
    @Autowired
    private SysFileRefService sysFileRefService;
    @Autowired
    private SysFileService fileService;
    @Autowired
    private PmAuditDeptRefService pmAuditDeptRefService;
    @Autowired
    private PmBidFailureService pmBidFailureService;
    @Autowired
    private PmBidOpeningService pmBidOpeningService;
    @Autowired
    private PmBidWinningService pmBidWinningService;
    @Autowired
    private PmBidWinningOpeningRefService pmBidWinningOpeningRefService;
    @Autowired
    private PmBookBuildingService pmBookBuildingService;
    @Autowired
    private PmCallQuestionService pmCallQuestionService;
    @Autowired
    private PmDemandExpertRefService pmDemandExpertRefService;
    @Autowired
    private PmExpertFeedbackService pmExpertFeedbackService;
    @Autowired
    private PmProcurementDocumentsReviewService pmProcurementDocumentsReviewService;
    @Autowired
    private PmReleaseAnnouncementService pmReleaseAnnouncementService;

    @Autowired
    private PmBidFailureHisService pmBidFailureHisService;
    @Autowired
    private PmBidOpeningHisService pmBidOpeningHisService;
    @Autowired
    private PmBidWinningHisService pmBidWinningHisService;
    @Autowired
    private PmBidWinningOpeningRefHisService pmBidWinningOpeningRefHisService;
    @Autowired
    private PmBookBuildingHisService pmBookBuildingHisService;
    @Autowired
    private PmCallQuestionHisService pmCallQuestionHisService;
    @Autowired
    private PmDemandExpertRefHisService pmDemandExpertRefHisService;
    @Autowired
    private PmDemandHisService pmDemandHisService;
    @Autowired
    private PmExpertFeedbackHisService pmExpertFeedbackHisService;
    @Autowired
    private PmProcurementDocumentsReviewHisService pmProcurementDocumentsReviewHisService;
    @Autowired
    private PmReleaseAnnouncementHisService pmReleaseAnnouncementHisService;

    @Autowired
    private ProvisionalPlanMapper provisionalPlanMapper;
    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private BaseAgencyService baseAgencyService;

    /**
     * 流废标退回
     * @param demandId
     * @param updateBy
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean returnBidFailure(Long demandId,String updateBy) throws Exception {
        PmDemand pmDemand = iPmDemandService.getById(demandId);
        if(ObjectUtils.isEmpty(pmDemand)){
            throw new Exception("参数错误");
        }
        //把相关表数据存入历史表，清空相关表数据

        //1需求建档表
        LambdaQueryWrapper<PmBookBuilding> pmBookBuildingLambdaQueryWrapper = new LambdaQueryWrapper<>();
        pmBookBuildingLambdaQueryWrapper.eq(PmBookBuilding::getDemandId,demandId);
        PmBookBuilding pmBookBuildingDb = this.pmBookBuildingService.getOne(pmBookBuildingLambdaQueryWrapper);
        if(pmBookBuildingDb != null) {
            PmBookBuildingHis pmBookBuildingHis = new PmBookBuildingHis();
            BeanUtils.copyProperties(pmBookBuildingDb,pmBookBuildingHis);

            LambdaQueryWrapper<PmBookBuildingHis> pmBookBuildingHisLambdaQueryWrapper = new LambdaQueryWrapper<>();
            pmBookBuildingHisLambdaQueryWrapper.eq(PmBookBuildingHis::getDemandId,demandId)
                    .orderByDesc(PmBookBuildingHis::getReturnOrderNumber).last(" limit 1");
            PmBookBuildingHis pmBookBuildingHisDb = this.pmBookBuildingHisService.getOne(pmBookBuildingHisLambdaQueryWrapper);
            if(pmBookBuildingHisDb != null){
                pmBookBuildingHis.setReturnOrderNumber(pmBookBuildingHisDb.getReturnOrderNumber() + 1);
            } else {
                pmBookBuildingHis.setReturnOrderNumber(1);
            }
            if(this.pmBookBuildingHisService.save(pmBookBuildingHis)){
                //文件
                List<SysFileRef> fileRefs = getSysFileRefList(pmBookBuildingDb.getId(), SysFileRefEnum.PM_BOOK_BUILDING.getType());
                if (!ObjectUtils.isEmpty(fileRefs)) {
                    for (SysFileRef sysFileRef : fileRefs) {
                        sysFileRef.setType(Integer.parseInt(SysFileRefEnum.PM_BOOK_BUILDING.getType() + "00" + pmBookBuildingHis.getReturnOrderNumber()));
                    }
                    sysFileRefService.updateBatchById(fileRefs);
                }
                this.pmBookBuildingService.removeById(pmBookBuildingDb.getId());
            }

        }

        //2专家意见反馈表
        LambdaQueryWrapper<PmExpertFeedback> pmExpertFeedbackLambdaQueryWrapper = new LambdaQueryWrapper<>();
        pmExpertFeedbackLambdaQueryWrapper.eq(PmExpertFeedback::getDemandId,demandId);
        PmExpertFeedback pmExpertFeedbackDb = this.pmExpertFeedbackService.getOne(pmExpertFeedbackLambdaQueryWrapper);
        if(pmExpertFeedbackDb != null) {
            PmExpertFeedbackHis pmExpertFeedbackHis = new PmExpertFeedbackHis();
            BeanUtils.copyProperties(pmExpertFeedbackDb,pmExpertFeedbackHis);

            LambdaQueryWrapper<PmExpertFeedbackHis> pmExpertFeedbackHisLambdaQueryWrapper = new LambdaQueryWrapper<>();
            pmExpertFeedbackHisLambdaQueryWrapper.eq(PmExpertFeedbackHis::getDemandId,demandId)
                    .orderByDesc(PmExpertFeedbackHis::getReturnOrderNumber).last(" limit 1");
            PmExpertFeedbackHis pmExpertFeedbackHisDb = this.pmExpertFeedbackHisService.getOne(pmExpertFeedbackHisLambdaQueryWrapper);
            if(pmExpertFeedbackHisDb != null){
                pmExpertFeedbackHis.setReturnOrderNumber(pmExpertFeedbackHisDb.getReturnOrderNumber() + 1);
            } else {
                pmExpertFeedbackHis.setReturnOrderNumber(1);
            }
            if(this.pmExpertFeedbackHisService.save(pmExpertFeedbackHis)){
                //文件
                List<SysFileRef> fileRefs = getSysFileRefList(pmExpertFeedbackDb.getId(), SysFileRefEnum.PM_EXPERT_FEEDBACK.getType());
                if (!ObjectUtils.isEmpty(fileRefs)) {
                    for (SysFileRef sysFileRef : fileRefs) {
                        sysFileRef.setType(Integer.parseInt(SysFileRefEnum.PM_EXPERT_FEEDBACK.getType() + "00" + pmExpertFeedbackHis.getReturnOrderNumber()));
                    }
                    sysFileRefService.updateBatchById(fileRefs);
                }
                this.pmExpertFeedbackService.removeById(pmExpertFeedbackDb.getId());
            }

        }

        //3采购文件编制审核表
        LambdaQueryWrapper<PmProcurementDocumentsReview> pmProcurementDocumentsReviewLambdaQueryWrapper = new LambdaQueryWrapper<>();
        pmProcurementDocumentsReviewLambdaQueryWrapper.eq(PmProcurementDocumentsReview::getDemandId,demandId);
        PmProcurementDocumentsReview pmProcurementDocumentsReviewDb = this.pmProcurementDocumentsReviewService.getOne(pmProcurementDocumentsReviewLambdaQueryWrapper);
        if(pmProcurementDocumentsReviewDb != null) {
            PmProcurementDocumentsReviewHis pmProcurementDocumentsReviewHis = new PmProcurementDocumentsReviewHis();
            BeanUtils.copyProperties(pmProcurementDocumentsReviewDb,pmProcurementDocumentsReviewHis);

            LambdaQueryWrapper<PmProcurementDocumentsReviewHis> pmProcurementDocumentsReviewHisLambdaQueryWrapper = new LambdaQueryWrapper<>();
            pmProcurementDocumentsReviewHisLambdaQueryWrapper.eq(PmProcurementDocumentsReviewHis::getDemandId,demandId)
                    .orderByDesc(PmProcurementDocumentsReviewHis::getReturnOrderNumber).last(" limit 1");
            PmProcurementDocumentsReviewHis pmProcurementDocumentsReviewHisDb = this.pmProcurementDocumentsReviewHisService.getOne(pmProcurementDocumentsReviewHisLambdaQueryWrapper);
            if(pmProcurementDocumentsReviewHisDb != null){
                pmProcurementDocumentsReviewHis.setReturnOrderNumber(pmProcurementDocumentsReviewHisDb.getReturnOrderNumber() + 1);
            } else {
                pmProcurementDocumentsReviewHis.setReturnOrderNumber(1);
            }
            if(this.pmProcurementDocumentsReviewHisService.save(pmProcurementDocumentsReviewHis)){
                //文件
                List<SysFileRef> fileRefs = getSysFileRefList(pmProcurementDocumentsReviewDb.getId(), SysFileRefEnum.PM_BID_FILE.getType());
                if (!ObjectUtils.isEmpty(fileRefs)) {
                    for (SysFileRef sysFileRef : fileRefs) {
                        sysFileRef.setType(Integer.parseInt(SysFileRefEnum.PM_BID_FILE.getType() + "00" + pmProcurementDocumentsReviewHis.getReturnOrderNumber()));
                    }
                    sysFileRefService.updateBatchById(fileRefs);
                }

                this.pmProcurementDocumentsReviewService.removeById(pmProcurementDocumentsReviewDb.getId());
            }

        }

        //4发布公告表
        LambdaQueryWrapper<PmReleaseAnnouncement> pmReleaseAnnouncementLambdaQueryWrapper = new LambdaQueryWrapper<>();
        pmReleaseAnnouncementLambdaQueryWrapper.eq(PmReleaseAnnouncement::getDemandId,demandId);
        List<PmReleaseAnnouncement> pmReleaseAnnouncementDbList = this.pmReleaseAnnouncementService.list(pmReleaseAnnouncementLambdaQueryWrapper);
        if(!ObjectUtils.isEmpty(pmReleaseAnnouncementDbList)) {

            LambdaQueryWrapper<PmReleaseAnnouncementHis> pmReleaseAnnouncementHisLambdaQueryWrapper = new LambdaQueryWrapper<>();
            pmReleaseAnnouncementHisLambdaQueryWrapper.eq(PmReleaseAnnouncementHis::getDemandId,demandId)
                    .orderByDesc(PmReleaseAnnouncementHis::getReturnOrderNumber).last(" limit 1");
            PmReleaseAnnouncementHis pmReleaseAnnouncementHisDb = this.pmReleaseAnnouncementHisService.getOne(pmReleaseAnnouncementHisLambdaQueryWrapper);

            for(PmReleaseAnnouncement pmReleaseAnnouncementDb : pmReleaseAnnouncementDbList) {
                PmReleaseAnnouncementHis pmReleaseAnnouncementHis = new PmReleaseAnnouncementHis();
                BeanUtils.copyProperties(pmReleaseAnnouncementDb,pmReleaseAnnouncementHis);

                if(pmReleaseAnnouncementHisDb != null){
                    pmReleaseAnnouncementHis.setReturnOrderNumber(pmReleaseAnnouncementHisDb.getReturnOrderNumber() + 1);
                } else {
                    pmReleaseAnnouncementHis.setReturnOrderNumber(1);
                }
                if(this.pmReleaseAnnouncementHisService.save(pmReleaseAnnouncementHis)){
                    //文件
                    List<SysFileRef> fileRefs = getSysFileRefList(pmReleaseAnnouncementDb.getId(), SysFileRefEnum.PM_ANNOUNCEMENT_FILE.getType());
                    if (!ObjectUtils.isEmpty(fileRefs)) {
                        for (SysFileRef sysFileRef : fileRefs) {
                            sysFileRef.setType(Integer.parseInt(SysFileRefEnum.PM_ANNOUNCEMENT_FILE.getType() + "00" + pmReleaseAnnouncementHis.getReturnOrderNumber()));
                        }
                        sysFileRefService.updateBatchById(fileRefs);
                    }
                    this.pmReleaseAnnouncementService.removeById(pmReleaseAnnouncementDb.getId());
                }
            }
        }

         //5质疑表
        LambdaQueryWrapper<PmCallQuestion> pmCallQuestionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        pmCallQuestionLambdaQueryWrapper.eq(PmCallQuestion::getDemandId,demandId);
        List<PmCallQuestion> pmCallQuestionDbList = this.pmCallQuestionService.list(pmCallQuestionLambdaQueryWrapper);
        if(!ObjectUtils.isEmpty(pmCallQuestionDbList)) {
            LambdaQueryWrapper<PmCallQuestionHis> pmCallQuestionHisLambdaQueryWrapper = new LambdaQueryWrapper<>();
            pmCallQuestionHisLambdaQueryWrapper.eq(PmCallQuestionHis::getDemandId,demandId)
                    .orderByDesc(PmCallQuestionHis::getReturnOrderNumber).last(" limit 1");
            PmCallQuestionHis pmCallQuestionHisDb = this.pmCallQuestionHisService.getOne(pmCallQuestionHisLambdaQueryWrapper);

            for(PmCallQuestion pmCallQuestionDb : pmCallQuestionDbList) {
                PmCallQuestionHis pmCallQuestionHis = new PmCallQuestionHis();
                BeanUtils.copyProperties(pmCallQuestionDb,pmCallQuestionHis);

                if(pmCallQuestionHisDb != null){
                    pmCallQuestionHis.setReturnOrderNumber(pmCallQuestionHisDb.getReturnOrderNumber() + 1);
                } else {
                    pmCallQuestionHis.setReturnOrderNumber(1);
                }
                if(this.pmCallQuestionHisService.save(pmCallQuestionHis)){
                    String type = pmCallQuestionDb.getType();
                    Integer sysFileRefType = null;
                    if("0".equals(type)){
                        sysFileRefType = SysFileRefEnum.PM_PRE_BID_CALL_QUESTION.getType();
                    } else {
                        sysFileRefType = SysFileRefEnum.PM_BID_CALL_QEUSTION_FILE.getType();
                    }

                    //文件
                    List<SysFileRef> fileRefs = getSysFileRefList(pmCallQuestionDb.getId(), sysFileRefType);
                    if (!ObjectUtils.isEmpty(fileRefs)) {
                        for (SysFileRef sysFileRef : fileRefs) {
                            sysFileRef.setType(Integer.parseInt(sysFileRefType + "00" + pmCallQuestionHis.getReturnOrderNumber()));
                        }
                        sysFileRefService.updateBatchById(fileRefs);
                    }
                    this.pmCallQuestionService.removeById(pmCallQuestionDb.getId());
                }
            }
        }

        //6项目专家关联表
        LambdaQueryWrapper<PmDemandExpertRef> pmDemandExpertRefLambdaQueryWrapper = new LambdaQueryWrapper<>();
        pmDemandExpertRefLambdaQueryWrapper.eq(PmDemandExpertRef::getDemandId,demandId);
        List<PmDemandExpertRef> pmDemandExpertRefDbList = this.pmDemandExpertRefService.list(pmDemandExpertRefLambdaQueryWrapper);
        if(!ObjectUtils.isEmpty(pmDemandExpertRefDbList)) {

            LambdaQueryWrapper<PmDemandExpertRefHis> pmDemandExpertRefHisLambdaQueryWrapper = new LambdaQueryWrapper<>();
            pmDemandExpertRefHisLambdaQueryWrapper.eq(PmDemandExpertRefHis::getDemandId,demandId)
                    .orderByDesc(PmDemandExpertRefHis::getReturnOrderNumber).last(" limit 1");
            PmDemandExpertRefHis pmDemandExpertRefHisDb = this.pmDemandExpertRefHisService.getOne(pmDemandExpertRefHisLambdaQueryWrapper);

            for(PmDemandExpertRef pmDemandExpertRefDb : pmDemandExpertRefDbList) {
                PmDemandExpertRefHis pmDemandExpertRefHis = new PmDemandExpertRefHis();
                BeanUtils.copyProperties(pmDemandExpertRefDb,pmDemandExpertRefHis);

                if(pmDemandExpertRefHisDb != null){
                    pmDemandExpertRefHis.setReturnOrderNumber(pmDemandExpertRefHisDb.getReturnOrderNumber() + 1);
                } else {
                    pmDemandExpertRefHis.setReturnOrderNumber(1);
                }
                if(this.pmDemandExpertRefHisService.save(pmDemandExpertRefHis)){
                    this.pmDemandExpertRefService.removeById(pmDemandExpertRefDb.getId());
                }
            }
        }

        //6开标信息表
        LambdaQueryWrapper<PmBidOpening> pmBidOpeningLambdaQueryWrapper = new LambdaQueryWrapper<>();
        pmBidOpeningLambdaQueryWrapper.eq(PmBidOpening::getDemandId,demandId);
        List<PmBidOpening> pmBidOpeningDbList = this.pmBidOpeningService.list(pmBidOpeningLambdaQueryWrapper);
        if(!ObjectUtils.isEmpty(pmBidOpeningDbList)) {
            LambdaQueryWrapper<PmBidOpeningHis> pmBidOpeningHisLambdaQueryWrapper = new LambdaQueryWrapper<>();
            pmBidOpeningHisLambdaQueryWrapper.eq(PmBidOpeningHis::getDemandId,demandId)
                    .orderByDesc(PmBidOpeningHis::getReturnOrderNumber).last(" limit 1");
            PmBidOpeningHis pmBidOpeningHisDb = this.pmBidOpeningHisService.getOne(pmBidOpeningHisLambdaQueryWrapper);

            for(PmBidOpening pmBidOpeningDb : pmBidOpeningDbList) {
                PmBidOpeningHis pmBidOpeningHis = new PmBidOpeningHis();
                BeanUtils.copyProperties(pmBidOpeningDb,pmBidOpeningHis);

                if(pmBidOpeningHisDb != null){
                    pmBidOpeningHis.setReturnOrderNumber(pmBidOpeningHisDb.getReturnOrderNumber() + 1);
                } else {
                    pmBidOpeningHis.setReturnOrderNumber(1);
                }
                if(this.pmBidOpeningHisService.save(pmBidOpeningHis)){
                    this.pmBidOpeningService.removeById(pmBidOpeningDb.getId());
                }
            }
        }

        //7中标基本情况表
        LambdaQueryWrapper<PmBidWinning> pmBidWinningLambdaQueryWrapper = new LambdaQueryWrapper<>();
        pmBidWinningLambdaQueryWrapper.eq(PmBidWinning::getDemandId,demandId);
        List<PmBidWinning> pmBidWinningDbList = this.pmBidWinningService.list(pmBidWinningLambdaQueryWrapper);
        if(!ObjectUtils.isEmpty(pmBidWinningDbList)) {
            LambdaQueryWrapper<PmBidWinningHis> pmBidWinningHisLambdaQueryWrapper = new LambdaQueryWrapper<>();
            pmBidWinningHisLambdaQueryWrapper.eq(PmBidWinningHis::getDemandId,demandId)
                    .orderByDesc(PmBidWinningHis::getReturnOrderNumber).last(" limit 1");
            PmBidWinningHis pmBidWinningHisDb = this.pmBidWinningHisService.getOne(pmBidWinningHisLambdaQueryWrapper);

            for(PmBidWinning pmBidWinningDb : pmBidWinningDbList) {
                PmBidWinningHis pmBidWinningHis = new PmBidWinningHis();
                BeanUtils.copyProperties(pmBidWinningDb,pmBidWinningHis);

                if(pmBidWinningHisDb != null){
                    pmBidWinningHis.setReturnOrderNumber(pmBidWinningHisDb.getReturnOrderNumber() + 1);
                } else {
                    pmBidWinningHis.setReturnOrderNumber(1);
                }
                if(this.pmBidWinningHisService.save(pmBidWinningHis)){
                    //文件
                    List<SysFileRef> fileRefs = getSysFileRefList(pmBidWinningDb.getId(), SysFileRefEnum.PM_BID_WINNING_FILE.getType());
                    if (!ObjectUtils.isEmpty(fileRefs)) {
                        for (SysFileRef sysFileRef : fileRefs) {
                            sysFileRef.setType(Integer.parseInt(SysFileRefEnum.PM_BID_WINNING_FILE.getType() + "00" + pmBidWinningHis.getReturnOrderNumber()));
                        }
                        sysFileRefService.updateBatchById(fileRefs);
                    }

                    this.pmBidWinningService.removeById(pmBidWinningDb.getId());
                }
            }
        }

        //8中标开标关联表
        LambdaQueryWrapper<PmBidWinningOpeningRef> pmBidWinningOpeningRefLambdaQueryWrapper = new LambdaQueryWrapper<>();
        pmBidWinningOpeningRefLambdaQueryWrapper.eq(PmBidWinningOpeningRef::getDemandId,demandId);
        List<PmBidWinningOpeningRef> pmBidWinningOpeningRefDbList = this.pmBidWinningOpeningRefService.list(pmBidWinningOpeningRefLambdaQueryWrapper);
        if(!ObjectUtils.isEmpty(pmBidWinningOpeningRefDbList)) {
            LambdaQueryWrapper<PmBidWinningOpeningRefHis> pmBidWinningOpeningRefHisLambdaQueryWrapper = new LambdaQueryWrapper<>();
            pmBidWinningOpeningRefHisLambdaQueryWrapper.eq(PmBidWinningOpeningRefHis::getDemandId,demandId)
                    .orderByDesc(PmBidWinningOpeningRefHis::getReturnOrderNumber).last(" limit 1");
            PmBidWinningOpeningRefHis pmBidWinningOpeningRefHisDb = this.pmBidWinningOpeningRefHisService.getOne(pmBidWinningOpeningRefHisLambdaQueryWrapper);

            for(PmBidWinningOpeningRef pmBidWinningOpeningRefDb : pmBidWinningOpeningRefDbList) {
                PmBidWinningOpeningRefHis pmBidWinningOpeningRefHis = new PmBidWinningOpeningRefHis();
                BeanUtils.copyProperties(pmBidWinningOpeningRefDb,pmBidWinningOpeningRefHis);

                if(pmBidWinningOpeningRefHisDb != null){
                    pmBidWinningOpeningRefHis.setReturnOrderNumber(pmBidWinningOpeningRefHisDb.getReturnOrderNumber() + 1);
                } else {
                    pmBidWinningOpeningRefHis.setReturnOrderNumber(1);
                }
                if(this.pmBidWinningOpeningRefHisService.save(pmBidWinningOpeningRefHis)){
                    this.pmBidWinningOpeningRefService.removeById(pmBidWinningOpeningRefDb.getId());
                }
            }
        }

        //9流标基本情况表
        LambdaQueryWrapper<PmBidFailure> pmBidFailureLambdaQueryWrapper = new LambdaQueryWrapper<>();
        pmBidFailureLambdaQueryWrapper.eq(PmBidFailure::getDemandId,demandId);
        List<PmBidFailure> pmBidFailureDbList = this.pmBidFailureService.list(pmBidFailureLambdaQueryWrapper);
        if(!ObjectUtils.isEmpty(pmBidFailureDbList)) {
            LambdaQueryWrapper<PmBidFailureHis> pmBidFailureHisLambdaQueryWrapper = new LambdaQueryWrapper<>();
            pmBidFailureHisLambdaQueryWrapper.eq(PmBidFailureHis::getDemandId,demandId)
                    .orderByDesc(PmBidFailureHis::getReturnOrderNumber).last(" limit 1");
            PmBidFailureHis pmBidFailureHisDb = this.pmBidFailureHisService.getOne(pmBidFailureHisLambdaQueryWrapper);

            for(PmBidFailure pmBidFailureDb : pmBidFailureDbList) {
                PmBidFailureHis pmBidFailureHis = new PmBidFailureHis();
                BeanUtils.copyProperties(pmBidFailureDb,pmBidFailureHis);

                if(pmBidFailureHisDb != null){
                    pmBidFailureHis.setReturnOrderNumber(pmBidFailureHisDb.getReturnOrderNumber() + 1);
                } else {
                    pmBidFailureHis.setReturnOrderNumber(1);
                }
                if(this.pmBidFailureHisService.save(pmBidFailureHis)){
                    //文件
                    List<SysFileRef> fileRefs = getSysFileRefList(pmBidFailureDb.getId(), SysFileRefEnum.PM_BID_FAILURE_FILE.getType());
                    if (!ObjectUtils.isEmpty(fileRefs)) {
                        for (SysFileRef sysFileRef : fileRefs) {
                            sysFileRef.setType(Integer.parseInt(SysFileRefEnum.PM_BID_FAILURE_FILE.getType() + "00" + pmBidFailureHis.getReturnOrderNumber()));
                        }
                        sysFileRefService.updateBatchById(fileRefs);
                    }

                    List<SysFileRef> fileRefs2 = getSysFileRefList(pmBidFailureDb.getId(), SysFileRefEnum.PM_BID_FAILURE_CALL_QUESTION.getType());
                    if (!ObjectUtils.isEmpty(fileRefs2)) {
                        for (SysFileRef sysFileRef : fileRefs2) {
                            sysFileRef.setType(Integer.parseInt(SysFileRefEnum.PM_BID_FAILURE_CALL_QUESTION.getType() + "00" + pmBidFailureHis.getReturnOrderNumber()));
                        }
                        sysFileRefService.updateBatchById(fileRefs2);
                    }

                    this.pmBidFailureService.removeById(pmBidFailureDb.getId());
                }
            }
        }

        //需求表
        PmDemandHis pmDemandHis = new PmDemandHis();
        BeanUtils.copyProperties(pmDemand,pmDemandHis);

        LambdaQueryWrapper<PmDemandHis> pmDemandHisLambdaQueryWrapper = new LambdaQueryWrapper<>();
        pmDemandHisLambdaQueryWrapper.eq(PmDemandHis::getDemandId,demandId)
                .orderByDesc(PmDemandHis::getReturnOrderNumber).last(" limit 1");
        PmDemandHis pmDemandHisDb= this.pmDemandHisService.getOne(pmDemandHisLambdaQueryWrapper);
        if(pmDemandHisDb != null){
            pmDemandHis.setReturnOrderNumber(pmDemandHisDb.getReturnOrderNumber() + 1);
        } else {
            pmDemandHis.setReturnOrderNumber(1);
        }
        if(this.pmDemandHisService.save(pmDemandHis)){
            UpdateWrapper<PmDemand> pmDemandUpdateWrapper = new UpdateWrapper<>();
            pmDemandUpdateWrapper.lambda()
                    .set(PmDemand::getRealDemandCommitTime,null)
                    .set(PmDemand::getRealPurchaseFinishTime,null)
                    .set(PmDemand::getRealDeliverTime,null)
                    .set(PmDemand::getAuditTime,null)
                    .set(PmDemand::getAgencyId,null)
                    .set(PmDemand::getExtractAgencyTime,null)
                    .set(PmDemand::getPurchaseTaskDocumentNumber,null)
                    .set(PmDemand::getTaskReleaseTime,null)
                    .set(PmDemand::getHasExpert,0)
                    .set(PmDemand::getProjectStatus,PmProjectStatus.WASTE_BID_RETURN.getCode())
                    .set(PmDemand::getUpdateBy,updateBy)
                    .set(PmDemand::getUpdateTime,new Date())
                    .eq(PmDemand::getDemandId,demandId);
            iPmDemandService.update(pmDemandUpdateWrapper);

            List<SysFileRef> fileRefs = getSysFileRefList(demandId, SysFileRefEnum.PM_DEMAND_EXAMINE.getType());
            if (!ObjectUtils.isEmpty(fileRefs)) {
                for (SysFileRef sysFileRef : fileRefs) {
                    sysFileRef.setType(Integer.parseInt(SysFileRefEnum.PM_DEMAND_EXAMINE.getType() + "00" + pmDemandHis.getReturnOrderNumber()));
                }
                sysFileRefService.updateBatchById(fileRefs);
            }

            List<SysFileRef> fileRefs2 = getSysFileRefList(demandId, SysFileRefEnum.PM_TAST_RELEASE.getType());
            if (!ObjectUtils.isEmpty(fileRefs2)) {
                for (SysFileRef sysFileRef : fileRefs2) {
                    sysFileRef.setType(Integer.parseInt(SysFileRefEnum.PM_TAST_RELEASE.getType() + "00" + pmDemandHis.getReturnOrderNumber()));
                }
                sysFileRefService.updateBatchById(fileRefs2);
            }

            LambdaQueryWrapper<PmAuditDeptRef> lw = new LambdaQueryWrapper<>();
            lw.eq(PmAuditDeptRef::getRefId, demandId);
            lw.eq(PmAuditDeptRef::getRefType, "2");
            pmAuditDeptRefService.remove(lw);
        }

        return true;
    }

    private List<SysFileRef> getSysFileRefList(Long redId, Integer type) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("red_id", redId);
        map.put("type", type);
        List<SysFileRef> fileRefs = sysFileRefService.listByMap(map);
        return fileRefs;
    }

    /**
     * 查询采购需求
     *
     * @param demandId 采购需求主键
     * @return 采购需求
     */
    @Override
    public PmDemandResVo selectPmDemandHisByDemandId(Long demandId, String detailType,Integer returnOrderNumber) {
        LambdaQueryWrapper<PmDemandHis> pmDemandHisLambdaQueryWrapper = new LambdaQueryWrapper<>();
        pmDemandHisLambdaQueryWrapper.eq(PmDemandHis::getDemandId,demandId).eq(PmDemandHis::getReturnOrderNumber,returnOrderNumber);
        PmDemandHis pmDemandHis = this.pmDemandHisService.getOne(pmDemandHisLambdaQueryWrapper);

        if (pmDemandHis != null) {
            PmDemandResVo vo = new PmDemandResVo();
            vo.setProjectName(pmDemandHis.getProjectName());
            vo.setDemandId(demandId);
            if ("1".equals(detailType)) { //项目计划

                BeanUtils.copyProperties(pmDemandHis, vo);

                //project_type,evaluation 从原来计划里取
                ProvisionalPlan provisionalPlan = provisionalPlanMapper.seletById(pmDemandHis.getPlanId());
                if(provisionalPlan !=null){
                    vo.setProjectType(provisionalPlan.getProjectType());
                    vo.setEvaluation(provisionalPlan.getEvaluation().doubleValue());
                }

                SysDeptResponseVo sysDeptResponseVo = (SysDeptResponseVo) deptService.selectDeptById(vo.getPurchaseDeptId()).get("sysDept");
                if (sysDeptResponseVo != null) {
                    vo.setPurchaseDeptName(sysDeptResponseVo.getDeptName());
                }

                String purchaseServicesName = dictDataService.selectDictLabel("purchase_services", vo.getPurchaseServices());
                if (StringUtils.isNotEmpty(purchaseServicesName)) {
                    vo.setPurchaseServicesName(purchaseServicesName);
                }

                //采购需求附件
                List<SysFileInfo> demandFileInfos = getSysFileInfoList(vo.getDemandId(), SysFileRefEnum.PM_DEMAND.getType());
                if (!ObjectUtils.isEmpty(demandFileInfos)) {
                    vo.setDemandFileInfos(demandFileInfos);
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
                //项目属性
                for (ProjectAttribute value : ProjectAttribute.values()) {
                    if (vo.getProjectAttr() != null && vo.getProjectAttr().equals(value.getCode())) {
                        vo.setProjectAttrName(value.getInfo());
                        break;
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


            } else if ("2".equals(detailType)) { //需求建档

                //采购需求审核文件
                List<SysFileInfo> auditFileInfos = getSysFileInfoList(vo.getDemandId(), Integer.parseInt(SysFileRefEnum.PM_DEMAND_EXAMINE.getType() + "00" + pmDemandHis.getReturnOrderNumber()));
                if (!ObjectUtils.isEmpty(auditFileInfos)) {
                    vo.setAuditFileInfos(auditFileInfos);
                }

                LambdaQueryWrapper<PmBookBuildingHis> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(PmBookBuildingHis::getDemandId, demandId).eq(PmBookBuildingHis::getReturnOrderNumber,returnOrderNumber);
                PmBookBuildingHis pmBookBuildingHis = pmBookBuildingHisService.getOne(lambdaQueryWrapper);
                if (pmBookBuildingHis != null) {
                    PmBookBuildingResVo responseVo = new PmBookBuildingResVo();
                    BeanUtils.copyProperties(pmBookBuildingHis, responseVo);

                    List<SysFileInfo> fileInfos = getSysFileInfoList(pmBookBuildingHis.getId(), Integer.parseInt(SysFileRefEnum.PM_BOOK_BUILDING.getType() + "00" + pmBookBuildingHis.getReturnOrderNumber()));
                    if (!ObjectUtils.isEmpty(fileInfos)) {
                        if (!ObjectUtils.isEmpty(auditFileInfos)) {
                            fileInfos.addAll(auditFileInfos);
                        }
                        responseVo.setFileInfos(fileInfos);
                    }
                    vo.setPmBookBuildingResVo(responseVo);
                }
            } else if ("3".equals(detailType)) { //任务下达
                vo.setTaskReleaseTime(pmDemandHis.getTaskReleaseTime());
                //任务下达文件
                List<SysFileInfo> taskReleaseFileInfos = getSysFileInfoList(vo.getDemandId(), Integer.parseInt(SysFileRefEnum.PM_TAST_RELEASE.getType() + "00" + pmDemandHis.getReturnOrderNumber()));
                if (!ObjectUtils.isEmpty(taskReleaseFileInfos)) {
                    vo.setTaskReleaseFileInfos(taskReleaseFileInfos);
                }
            } else if ("4".equals(detailType)) { //中标信息
                PmPurchaseExecutionResVo pmPurchaseExecutionResVo = new PmPurchaseExecutionResVo();

                //代理商信息
                BaseAgency baseAgency = this.baseAgencyService.getById(pmDemandHis.getAgencyId());
                pmPurchaseExecutionResVo.setBaseAgency(baseAgency);
                vo.setPurchaseTaskDocumentNumber(pmDemandHis.getPurchaseTaskDocumentNumber());

                //专家意见反馈
                LambdaQueryWrapper<PmExpertFeedbackHis> pmExpertFeedbackLambdaQueryWrapper = new LambdaQueryWrapper<>();
                pmExpertFeedbackLambdaQueryWrapper.eq(PmExpertFeedbackHis::getDemandId,demandId).eq(PmExpertFeedbackHis::getReturnOrderNumber,returnOrderNumber);
                PmExpertFeedbackHis pmExpertFeedbackHis = pmExpertFeedbackHisService.getOne(pmExpertFeedbackLambdaQueryWrapper);
                if(!ObjectUtils.isEmpty(pmExpertFeedbackHis)){
                    PmExpertFeedbackResVo pmExpertFeedbackResVo = new PmExpertFeedbackResVo();
                    BeanUtils.copyProperties(pmExpertFeedbackHis,pmExpertFeedbackResVo);

                    List<SysFileInfo> fileInfos = getSysFileInfoList(pmExpertFeedbackResVo.getId(), Integer.parseInt(SysFileRefEnum.PM_EXPERT_FEEDBACK.getType() + "00" + pmExpertFeedbackHis.getReturnOrderNumber()));
                    if (!ObjectUtils.isEmpty(fileInfos)) {
                        pmExpertFeedbackResVo.setFileInfos(fileInfos);
                    }
                    pmPurchaseExecutionResVo.setPmExpertFeedbackResVo(pmExpertFeedbackResVo);
                }

                //采购文件编制审核
                LambdaQueryWrapper<PmProcurementDocumentsReviewHis> pmProcurementDocumentsReviewLambdaQueryWrapper = new LambdaQueryWrapper<>();
                pmProcurementDocumentsReviewLambdaQueryWrapper.eq(PmProcurementDocumentsReviewHis::getDemandId,demandId).eq(PmProcurementDocumentsReviewHis::getReturnOrderNumber,returnOrderNumber);
                PmProcurementDocumentsReviewHis pmProcurementDocumentsReviewHis = pmProcurementDocumentsReviewHisService.getOne(pmProcurementDocumentsReviewLambdaQueryWrapper);
                if(!ObjectUtils.isEmpty(pmProcurementDocumentsReviewHis)){
                    PmProcurementDocumentsReviewResVo pmProcurementDocumentsReviewResVo = new PmProcurementDocumentsReviewResVo();
                    BeanUtils.copyProperties(pmProcurementDocumentsReviewHis,pmProcurementDocumentsReviewResVo);

                    List<SysFileInfo> fileInfos = getSysFileInfoList(pmProcurementDocumentsReviewResVo.getId(), Integer.parseInt(SysFileRefEnum.PM_BID_FILE.getType() + "00" + pmProcurementDocumentsReviewHis.getReturnOrderNumber()));
                    if (!ObjectUtils.isEmpty(fileInfos)) {
                        pmProcurementDocumentsReviewResVo.setFileInfos(fileInfos);
                    }
                    pmPurchaseExecutionResVo.setPmProcurementDocumentsReviewResVo(pmProcurementDocumentsReviewResVo);
                }

                //采购公告列表
                LambdaQueryWrapper<PmReleaseAnnouncementHis> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(PmReleaseAnnouncementHis::getDemandId, demandId).eq(PmReleaseAnnouncementHis::getReturnOrderNumber,returnOrderNumber);
                List<PmReleaseAnnouncementHis> pmReleaseAnnouncementHisList = this.pmReleaseAnnouncementHisService.list(lambdaQueryWrapper);
                if (!ObjectUtils.isEmpty(pmReleaseAnnouncementHisList)) {
                    List<PmReleaseAnnouncementResVo> pmReleaseAnnouncementResVoList = new ArrayList<>();
                    List<SysFileInfo> fileInfosAll = new ArrayList<>();
                    for (PmReleaseAnnouncementHis pmReleaseAnnouncementHis : pmReleaseAnnouncementHisList) {
                        PmReleaseAnnouncementResVo pmReleaseAnnouncementResVo = new PmReleaseAnnouncementResVo();
                        BeanUtils.copyProperties(pmReleaseAnnouncementHis, pmReleaseAnnouncementResVo);

                        List<SysFileInfo> fileInfos = getSysFileInfoList(pmReleaseAnnouncementResVo.getId(), Integer.parseInt(SysFileRefEnum.PM_ANNOUNCEMENT_FILE.getType() + "00" + pmReleaseAnnouncementHis.getReturnOrderNumber()));
                        if (!ObjectUtils.isEmpty(fileInfos)) {
                            fileInfosAll.addAll(fileInfos);
                        }
                        pmReleaseAnnouncementResVoList.add(pmReleaseAnnouncementResVo);
                    }
                    pmPurchaseExecutionResVo.setPmReleaseAnnouncementFileInfos(fileInfosAll);
                    pmPurchaseExecutionResVo.setPmReleaseAnnouncementResVoList(pmReleaseAnnouncementResVoList);
                }

                //标前质疑投诉
                LambdaQueryWrapper<PmCallQuestionHis> pmCallQuestionLambdaQueryWrapper = new LambdaQueryWrapper<>();
                pmCallQuestionLambdaQueryWrapper.eq(PmCallQuestionHis::getDemandId,demandId).eq(PmCallQuestionHis::getReturnOrderNumber,returnOrderNumber)
                        .eq(PmCallQuestionHis::getType,'0').orderByDesc(PmCallQuestionHis::getCallQuestionTime);
                List<PmCallQuestionHis>  pmCallQuestionHisList = pmCallQuestionHisService.list(pmCallQuestionLambdaQueryWrapper);
                if(!ObjectUtils.isEmpty(pmCallQuestionHisList)){
                    List<PmCallQuestionResVo> pmCallQuestionResVoList = new ArrayList<>();
                    List<SysFileInfo> fileInfosAll = new ArrayList<>();
                    for(PmCallQuestionHis pmCallQuestionHis : pmCallQuestionHisList){
                        PmCallQuestionResVo pmCallQuestionResVo = new PmCallQuestionResVo();
                        BeanUtils.copyProperties(pmCallQuestionHis,pmCallQuestionResVo);
                        List<SysFileInfo> fileInfos = getSysFileInfoList(pmCallQuestionResVo.getId(), Integer.parseInt(SysFileRefEnum.PM_PRE_BID_CALL_QUESTION.getType() + "00" + pmCallQuestionHis.getReturnOrderNumber()));
                        if (!ObjectUtils.isEmpty(fileInfos)) {
                            fileInfosAll.addAll(fileInfos);
                        }
                        pmCallQuestionResVoList.add(pmCallQuestionResVo);
                    }
                    pmPurchaseExecutionResVo.setPmCallQuestionPreResVoList(pmCallQuestionResVoList);
                    pmPurchaseExecutionResVo.setPmCallQuestionPreFileInfos(fileInfosAll);
                }

                //专家信息列表
                List<PmBaseExpertResVo> pmBaseExpertResVoList = this.pmDemandExpertRefHisService.getPmBaseExpertResVoList(demandId,returnOrderNumber);
                pmPurchaseExecutionResVo.setPmBaseExpertResVoList(pmBaseExpertResVoList);

                //开标信息列表
                List<PmBidOpeningResVo> pmBidOpeningResVoList = this.pmBidOpeningHisService.getPmBidOpeningResVoList(demandId,returnOrderNumber);
                pmPurchaseExecutionResVo.setPmBidOpeningResVoList(pmBidOpeningResVoList);

                //评标结果公告列表
                LambdaQueryWrapper<PmBidWinningHis> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper1.eq(PmBidWinningHis::getDemandId, demandId).eq(PmBidWinningHis::getReturnOrderNumber,returnOrderNumber)
                        .ne(PmBidWinningHis::getStatus, BidWinningStatus.BE_REPLACED.getCode())
                        .orderByDesc(PmBidWinningHis::getBidAnnouncementTime);
                List<PmBidWinningHis> pmBidWinningHisList = this.pmBidWinningHisService.list(lambdaQueryWrapper1);
                if (!ObjectUtils.isEmpty(pmBidWinningHisList)) {
                    List<PmBidWinningResVo> pmBidWinningResVoList = new ArrayList<>();
                    List<SysFileInfo> fileInfosAll = new ArrayList<>();
                    for (PmBidWinningHis pmBidWinningHis : pmBidWinningHisList) {
                        PmBidWinningResVo pmBidWinningResVo = new PmBidWinningResVo();
                        BeanUtils.copyProperties(pmBidWinningHis, pmBidWinningResVo);
                        pmBidWinningResVoList.add(pmBidWinningResVo);

                        List<SysFileInfo> fileInfos = getSysFileInfoList(pmBidWinningResVo.getId(), Integer.parseInt(SysFileRefEnum.PM_BID_WINNING_FILE.getType() + "00" + pmBidWinningHis.getReturnOrderNumber()));
                        if (!ObjectUtils.isEmpty(fileInfos)) {
                            fileInfosAll.addAll(fileInfos);
                        }

                    }
                    pmPurchaseExecutionResVo.setPmBidWinningResVoList(pmBidWinningResVoList);
                    pmPurchaseExecutionResVo.setPmBidWinningFileInfos(fileInfosAll);
                }

                //标后质疑
                LambdaQueryWrapper<PmCallQuestionHis> pmCallQuestionLambdaQueryWrapper2 = new LambdaQueryWrapper<>();
                pmCallQuestionLambdaQueryWrapper2.eq(PmCallQuestionHis::getDemandId,demandId).eq(PmCallQuestionHis::getReturnOrderNumber,returnOrderNumber)
                        .eq(PmCallQuestionHis::getType,'1').orderByDesc(PmCallQuestionHis::getCallQuestionTime);
                List<PmCallQuestionHis>  pmCallQuestionHisList2 = pmCallQuestionHisService.list(pmCallQuestionLambdaQueryWrapper2);
                if(!ObjectUtils.isEmpty(pmCallQuestionHisList2)){
                    List<PmCallQuestionResVo> pmCallQuestionResVoList = new ArrayList<>();
                    List<SysFileInfo> fileInfosAll = new ArrayList<>();
                    for(PmCallQuestionHis pmCallQuestionHis : pmCallQuestionHisList2){
                        PmCallQuestionResVo pmCallQuestionResVo = new PmCallQuestionResVo();
                        BeanUtils.copyProperties(pmCallQuestionHis,pmCallQuestionResVo);
                        List<SysFileInfo> fileInfos = getSysFileInfoList(pmCallQuestionResVo.getId(), Integer.parseInt(SysFileRefEnum.PM_BID_CALL_QEUSTION_FILE.getType() + "00" + pmCallQuestionHis.getReturnOrderNumber()));
                        if (!ObjectUtils.isEmpty(fileInfos)) {
                            fileInfosAll.addAll(fileInfos);
                        }
                        pmCallQuestionResVoList.add(pmCallQuestionResVo);
                    }
                    pmPurchaseExecutionResVo.setPmCallQuestionPostResVoList(pmCallQuestionResVoList);
                    pmPurchaseExecutionResVo.setPmCallRequestPostFileInfos(fileInfosAll);
                }

                //流标情况
                LambdaQueryWrapper<PmBidFailureHis> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper2.eq(PmBidFailureHis::getDemandId, demandId).eq(PmBidFailureHis::getReturnOrderNumber,returnOrderNumber)
                        .orderByDesc(PmBidFailureHis::getBidFailureTime);
                List<PmBidFailureHis> pmBidFailureHisList = this.pmBidFailureHisService.list(lambdaQueryWrapper2);
                if (!ObjectUtils.isEmpty(pmBidFailureHisList)) {
                    List<PmBidFailureResVo> pmBidFailureResVoList = new ArrayList<>();
                    List<PmCallQuestionResVo> pmCallQuestionBidFailureResVoList = new ArrayList<>();
                    List<SysFileInfo> pmBidFailureFileInfos = new ArrayList<>();
                    List<SysFileInfo> pmCallRequestBidFailureFileInfos = new ArrayList<>();
                    for (PmBidFailureHis pmBidFailureHis : pmBidFailureHisList) {
                        PmBidFailureResVo pmBidFailureResVo = new PmBidFailureResVo();
                        BeanUtils.copyProperties(pmBidFailureHis, pmBidFailureResVo);
                        pmBidFailureResVoList.add(pmBidFailureResVo);

                        //流标文件
                        List<SysFileInfo> fileInfos = getSysFileInfoList(pmBidFailureResVo.getId(), Integer.parseInt(SysFileRefEnum.PM_BID_FAILURE_FILE.getType() + "00" + pmBidFailureHis.getReturnOrderNumber()));
                        if (!ObjectUtils.isEmpty(fileInfos)) {
                            pmBidFailureFileInfos.addAll(fileInfos);
                        }

                        if("1".equals(pmBidFailureHis.getCallQuestion())){ //有质疑
                            PmCallQuestionResVo pmCallQuestionResVo = new PmCallQuestionResVo();
                            pmCallQuestionResVo.setCallQuestion(pmBidFailureHis.getCallQuestion());
                            pmCallQuestionResVo.setCallQuestionTime(pmBidFailureHis.getCallQuestionTime());
                            pmCallQuestionBidFailureResVoList.add(pmCallQuestionResVo);

                            //流废标质疑文件
                            List<SysFileInfo> fileInfos2 = getSysFileInfoList(pmBidFailureHis.getId(), Integer.parseInt(SysFileRefEnum.PM_BID_FAILURE_CALL_QUESTION.getType() + "00" + pmBidFailureHis.getReturnOrderNumber()));
                            if (!ObjectUtils.isEmpty(fileInfos2)) {
                                pmCallRequestBidFailureFileInfos.addAll(fileInfos2);
                            }
                        }
                    }

                    pmPurchaseExecutionResVo.setPmBidFailureResVoList(pmBidFailureResVoList);
                    pmPurchaseExecutionResVo.setPmBidFailureFileInfos(pmBidFailureFileInfos);

                    //流废标处置
                    pmPurchaseExecutionResVo.setPmCallQuestionBidFailureResVoList(pmCallQuestionBidFailureResVoList);
                    pmPurchaseExecutionResVo.setPmCallRequestBidFailureFileInfos(pmCallRequestBidFailureFileInfos);
                }

                //中标通知书
                List<SysFileInfo> pmBidWinningNotificationFileInfos = getSysFileInfoList(vo.getDemandId(), Integer.parseInt(SysFileRefEnum.PM_BID_WINNING_NOTIFICATION.getType() + "00" + pmDemandHis.getReturnOrderNumber()));
                pmPurchaseExecutionResVo.setFileInfos(pmBidWinningNotificationFileInfos);

                vo.setPmPurchaseExecutionResVo(pmPurchaseExecutionResVo);

            } else if ("5".equals(detailType)) { //合同信息
                return null;
            } else if ("6".equals(detailType)) { //建设情况
                return null;
            }

            return vo;
        } else {
            return null;
        }
    }

    private List<SysFileInfo> getSysFileInfoList(Long redId, Integer type) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("red_id", redId);
        map.put("type", type);
        List<SysFileRef> fileRefs = sysFileRefService.listByMap(map);
        if (!ObjectUtils.isEmpty(fileRefs)) {
            List<SysFileInfo> fileInfos = new ArrayList<>();
            for (SysFileRef ref : fileRefs) {
                SysFileInfo fileInfo = fileService.getById(ref.getFileId());
                fileInfos.add(fileInfo);
            }

            return fileInfos;
        } else {
            return null;
        }

    }

}
