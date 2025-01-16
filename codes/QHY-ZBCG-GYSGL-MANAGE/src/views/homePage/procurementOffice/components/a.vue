<template>
    <div class="home-page-office">
        <div v-for="(item, index) in datas" :key="index" class="contactAll">
            <div class="all_item">
                <div class="allItem_left">
                    <div class="projectNameTotle">{{ item.projectName }}</div>
                    <div class="projectInformat">
                        <div class="informatLeft" style="display: flex">
                            <div class="type">
                                <span v-if="item.projectType == '0'">装备类</span>
                                <span v-if="item.projectType == '1'">物资类</span>
                                <span v-if="item.projectType == '2'">服务类</span>
                                <span v-if="item.projectType == '3'">工程类</span>
                            </div>
                            <div class="prompt" v-if="item.tipsMessage !== ''">
                                {{ item.tipsMessage }}
                            </div>
                        </div>
                        <div class="numTotal">
                            {{ item.evaluation }} <span class="unit">万元</span>
                        </div>
                    </div>
                    <div class="spanMarks">
                        {{ item.demandOverview }}
                    </div>
                </div>
                <div class="allItem_right">
                    <el-button v-if="!item.demandId" class="submit"
                        type="default"
                        size="small"
                        @click="execModalDisplayChange('interpellatio', { id: item.planPracticalId || item.planYearId, type: item.planPracticalId ? 1 : 0 })"
                    >查看</el-button>
                    <!-- v-else -->
                    <el-button v-else class="submit"
                        type="default"
                        size="small"
                        @click="execModalDisplayChange('info', { id: item.demandId, projectStatus: item.projectStatus })"
                    >查看</el-button>
                    <el-button class="submit"
                        v-if="item.projectStatus === '9'"
                        size="small"
                        @click="handleShowAgentModal(item)"
                    >选取代理</el-button>
                    <el-button class="submit"
                        v-if="item.projectStatus === '10'"
                        size="small"
                        @click="execModalDisplayChange('opinion', { id: item.demandId })"
                    >上传意见反馈单</el-button>
                    <el-button class="submit"
                        v-if="item.projectStatus === '11'"
                        size="small"
                        @click="execModalDisplayChange('examine', { id: item.demandId })"
                    >上传招标文件审批表</el-button>
                    <el-button class="submit"
                        v-if="item.projectStatus === '12'"
                        size="small"
                        @click="execModalDisplayChange('executeSend', { id: item.demandId })"
                    >采购公告发布</el-button>
                    <el-button class="submit"
                        v-if="item.projectStatus === '13'"
                        size="small"
                        @click="execModalDisplayChange('questionBefore', { id: item.demandId })"
                    >标前质疑投诉</el-button>
                    <el-button class="submit"
                        v-if="item.projectStatus === '14' && item.hasExpert === '0'"
                        size="small"
                        @click="execModalDisplayChange('expert', { id: item.demandId })"
                    >填写专家信息</el-button>
                    <el-button class="submit"
                        v-if="item.projectStatus === '14'"
                        size="small"
                        @click="execModalDisplayChange('bidInfo', { id: item.demandId })"
                    >开评标</el-button>
                    <el-button class="submit"
                        v-if="item.projectStatus === '15'"
                        size="small"
                        @click="execModalDisplayChange('bidInfoBase', { id: item.demandId })"
                    >评标结果公告</el-button>
                    <el-button class="submit"
                        v-if="item.projectStatus === '16'"
                        size="small"
                        @click="execModalDisplayChange('questionNew', { id: item.demandId })"
                    >质疑处理</el-button>
                    <el-button class="submit"
                        v-if="item.projectStatus === '16'"
                        size="small"
                        @click="execModalDisplayChange('executeSend', { id: item.demandId })"
                    >采购公告发布</el-button>
                    <el-button class="submit"
                        v-if="item.projectStatus === '17'"
                        size="small"
                        @click="execModalDisplayChange('question', { id: item.demandId })"
                    >标后质疑</el-button>
                    <el-button class="submit"
                        v-if="item.projectStatus === '17'"
                        size="small"
                        @click="execModalDisplayChange('executeUpload', { id: item.demandId })"
                    >上传中标通知书</el-button>
                    <!-- <el-button v-if="step === 'interpellatio'" class="submit"
                        type="warning"
                        size="small"
                        @click="handleUrgeShow({ id: item.planPracticalId || item.planYearId, type: item.planPracticalId ? 1 : 0 })"
                    >发函催告</el-button> -->
                </div>
            </div>
        </div>

        
        <AgentModal v-model="agentModal.show" :record-id="agentModal.id" @on-refresh="handleQuery"></AgentModal>
        <EnclosureModal v-model="globalModal.enclosure.show" :record-id="globalModal.enclosure.id" @on-refresh="handleQuery"></EnclosureModal>
        <ExecuteSendModal v-model="globalModal.executeSend.show" :record-id="globalModal.executeSend.id" @on-refresh="handleQuery"></ExecuteSendModal>
        <ExecuteUploadModal v-model="globalModal.executeUpload.show" :record-id="globalModal.executeUpload.id" @on-refresh="handleQuery"></ExecuteUploadModal>
        <ExpertModal v-if="globalModal.expert.show" v-model="globalModal.expert.show" :record-id="globalModal.expert.id" @on-refresh="handleQuery"></ExpertModal>
        <BidInfoModal v-model="globalModal.bidInfo.show" :record-id="globalModal.bidInfo.id" @on-refresh="handleQuery"></BidInfoModal>
        <BidInfoBaseModal v-if="globalModal.bidInfoBase.show && globalModal.bidInfoBase.id" v-model="globalModal.bidInfoBase.show" 
            :record-id="globalModal.bidInfoBase.id" @on-refresh="handleQuery"></BidInfoBaseModal>
        <QuestionModal v-model="globalModal.question.show" :record-id="globalModal.question.id" @on-refresh="handleQuery"></QuestionModal>
        
        <OpinionModal v-model="globalModal.opinion.show" :record-id="globalModal.opinion.id" @on-refresh="handleQuery"></OpinionModal>
        <ExamineModal v-model="globalModal.examine.show" :record-id="globalModal.examine.id" @on-refresh="handleQuery"></ExamineModal>
        <QuestionBeforeModal v-model="globalModal.questionBefore.show" :record-id="globalModal.questionBefore.id" @on-refresh="handleQuery"></QuestionBeforeModal>
        <QuestionNewModal v-model="globalModal.questionNew.show" :record-id="globalModal.questionNew.id" @on-refresh="handleQuery"></QuestionNewModal>
        
        <el-dialog title="查看详情" v-model="globalModal.info.show" width="1100px" align-center :close-on-click-modal="false" class="plan-cust-info-modal">
            <div style="padding: 10px 20px 30px 20px">
                <InfoComponent v-if="globalModal.info.show" :recordId="globalModal.info.id" :tab-panes="globalModal.info.projectStatus === '9' ? [ '1', '2', '3' ] : [ '1', '2', '3', '4' ]" step="purchaseExecution"></InfoComponent>
            </div>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="globalModal.info.show = false">关闭</el-button>
                </div>
            </template>
        </el-dialog>

        <el-dialog
            title="查看详情"
            v-model="globalModal.interpellatio.show"
            width="980px"
            align-center
            :close-on-click-modal="false"
            class="plan-quarter-info-modal"
        >
            <div style="padding: 30px 20px">
                <PlanQuarterInfo v-if="globalModal.interpellatio.show" ref="planQuarterInfo" :recordId="globalModal.interpellatio.id" :paltYpe="globalModal.interpellatio.type" ></PlanQuarterInfo>
            </div>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="globalModal.interpellatio.show = false">关闭</el-button>
                    <!-- <el-button v-if="infoModal.operType === 'applyModify'" type="primary" @click="handleQuarterPlanApplyUpdate">提交申请</el-button> -->
                </div>
            </template>
        </el-dialog>

        

        <el-dialog title="发函催告" v-model="urgeModal.show" width="390px" align-center :close-on-click-modal="false" class="plan-examine-urge-modal" style="margin-top: auto !important;">
            <div class="dialog_body">
                <el-input
                    type="textarea"
                    v-model="urgeModal.letterStr"
                    :rows="15"
                    placeholder="请填写催告内容"
                    clearable
                />
            </div>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="urgeModal.show = false">取 消</el-button>
                    <el-button type="primary" @click="handleUrgeAction">确 定</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>
  
<script setup name="home-page-office-a">
import InfoComponent from "@/views/pm/components/common/info";
import AgentModal from "@/views/pm/execute/components/agent/index";
import EnclosureModal from "@/views/pm/execute/components/enclosure/index";
import ExpertModal from "@/views/pm/execute/components/expert/index";
import ExecuteSendModal from "@/views/pm/execute/components/notice/send";
import ExecuteUploadModal from "@/views/pm/execute/components/notice/upload";
import BidInfoModal from "@/views/pm/execute/components/bidinfo/index";
import BidInfoBaseModal from "@/views/pm/execute/components/bidinfo/base";
import QuestionModal from "@/views/pm/execute/components/question/index";

import OpinionModal from "@/views/pm/execute/components/opinion/index";
import ExamineModal from "@/views/pm/execute/components/examine/index";
import QuestionBeforeModal from "@/views/pm/execute/components/question/index-before";
import QuestionNewModal from "@/views/pm/execute/components/question/index-new";

import PlanQuarterInfo from "@/views/homePage/demandUnit/components/common/info";

import CommonModalMinix from "@/mixins/modal/index";
import { execQuarterPlanSendLetter, } from "@/api/plan/quarter";
import { execYearPlanSendLetter, } from "@/api/plan/year";

const props = defineProps({
    datas: {
        type: Array,
        default: () => []
    },
    step: String
});

const emit = defineEmits(['on-refresh'])

const { proxy } = getCurrentInstance();
const { sys_project_status, project_warn_status } = proxy.useDict(
    "sys_project_status",
    "project_warn_status"
);

const agentModal = ref({
  show: false,
  id: undefined,
});

function handleShowAgentModal(row) {
  if (!row) return;
  agentModal.value = {
    show: true,
    id: row.demandId
  }
}

function handleQuery() {
    emit('on-refresh')
}

const globalModal = ref({
    info: { show: false },
    enclosure: { show: false },
    executeSend: { show: false },
    executeUpload: { show: false },
    expert: { show: false },
    bidInfo: { show: false },
    bidInfoBase: { show: false },
    question: { show: false },
    opinion: { show: false },
    examine: { show: false },
    notice: { show: false },
    questionBefore: { show: false },
    questionNew: { show: false },
    interpellatio: { show: false },
})

const { execModalDisplayChange } = CommonModalMinix(proxy, globalModal)


const urgeModal = ref({
    show: false,
    id: undefined,
    type: undefined,
    letterStr: '',
})

function handleUrgeShow(row) {
    urgeModal.value = {
        show: true,
        id: row.id,
        type: row.type,
        letterStr: ''
    }
}
function handleUrgeAction() {
    if (!urgeModal.value.id) {
        proxy.$modal.msgError('参数不正确');
        return;
    }
    proxy.$modal.loading("正在进行发函催告, 请稍候...");
    if (urgeModal.value.type === 1) {
        execQuarterPlanSendLetter({ planPracticalId: urgeModal.value.id, letterStr: urgeModal.value.letterStr }).then((response) => {
            proxy.$modal.closeLoading();
            proxy.$modal.msgSuccess("发函催告成功");
            urgeModal.value = false;
            getList();
        }).catch(err => {
            console.log(err);
            proxy.$modal.closeLoading();
        });
    } else {
        execYearPlanSendLetter({ planYearId: urgeModal.value.id, letterStr: urgeModal.value.letterStr }).then((response) => {
            proxy.$modal.closeLoading();
            proxy.$modal.msgSuccess("发函催告成功");
            urgeModal.value = false;
            getList();
        }).catch(err => {
            console.log(err);
            proxy.$modal.closeLoading();
        });
    }
}

</script>

<style lang="scss" scoped>
.home-page-office {
    ::v-deep(.el-dialog__headerbtn) {
        top: 0 !important;
    }
    
    :deep(.plan-examine-urge-modal) {
        padding: 0px 0px 10px 0px;
        border-radius: 10px;
        // margin-top: initial !important;
        .el-dialog__header {
            text-align: center;
            border-bottom: 0px;
        }
        .el-dialog__headerbtn {
            top: 0px !important;
        }
        .el-dialog__footer {
            text-align: center;
            border-top: 0px;
        }
        .dialog_body {
            padding: 30px;
            border-radius: 20px;
            text-align: center;
            .msg {
                margin-top: 14px;
            }
        }
    }
}
</style>
  