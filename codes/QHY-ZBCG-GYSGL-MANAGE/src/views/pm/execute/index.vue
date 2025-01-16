<template>
    <div class="app-container search-table-box aidex-table cust-list-page">
        <el-card
            shadow="never"
            ref="queryRef"
            style="margin-bottom: 12px;"
            class="search_card"
            v-show="showSearch"
        >
            <div class="filter-container">
                <div class="search_box">
                    <el-form :model="queryParams" ref="queryForm" label-width="0px">
                        <el-row :gutter="16">
                            <el-col :md="4">
                                <el-form-item prop="projectName">
                                    <el-input
                                        v-model="queryParams.projectName"
                                        placeholder="请输入项目名称进行查询"
                                        clearable
                                        @change="handleRefresh"
                                    />
                                </el-form-item>
                            </el-col>
                            <el-col :md="4">
                                <el-form-item prop="purchaseServices">
                                    <el-select
                                        v-model="queryParams.purchaseServices"
                                        placeholder="请选择采购服务站"
                                        style="width: 100%"
                                        clearable
                                        @change="handleRefresh"
                                    >
                                        <el-option
                                            v-for="item in editDroppdown.purchaseServices"
                                            :key="item"
                                            :label="item.label"
                                            :value="item.value"
                                        ></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :md="4">
                                <el-form-item prop="projectStatus">
                                    <el-select
                                        v-model="queryParams.projectStatus"
                                        placeholder="请选择项目状态"
                                        style="width: 100%"
                                        clearable
                                        @change="handleRefresh"
                                    >
                                        <el-option
                                            v-for="dict in sys_project_status"
                                            :key="dict.value"
                                            :label="dict.label"
                                            :value="dict.value"
                                        />
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :md="4">
                                <el-form-item prop="projectStatus">
                                    <el-select
                                        v-model="queryParams.warnStatus"
                                        placeholder="请选择预警状态"
                                        style="width: 100%"
                                        clearable
                                        @change="handleRefresh"
                                    >
                                        <el-option
                                            v-for="dict in project_warn_status"
                                            :key="dict.value"
                                            :label="dict.label"
                                            :value="dict.value"
                                        />
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :md="6">
                                <el-form-item>
                                    <el-date-picker
                                        style="width: 100%"
                                        v-model="dateRange"
                                        value-format="YYYY-MM-DD"
                                        type="daterange"
                                        range-separator="-"
                                        start-placeholder="开始日期"
                                        end-placeholder="结束日期"
                                    ></el-date-picker>
                                </el-form-item>
                            </el-col>
                            <el-col :md="2" align="right" style="margin-left: auto;">
                                <el-form-item class="search_btn_box">
                                    <el-button
                                        class="filter-item w-100"
                                        type="primary"
                                        @click="handleRefresh"
                                    >搜索</el-button>
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </el-form>
                </div>
            </div>
        </el-card>

        <el-card shadow="never" class="table_card">

            <el-table
                stripe
                ref="tableRef"
                v-loading="loading"
                :data="records"
                highlight-current-row
                style="width: 100%;"
                :height="tableHeight"
            >
                <el-table-column type="index" label="序号" width="55" align="center" fixed="left" />
                <el-table-column
                    label="采购单位"
                    align="center"
                    prop="purchaseDeptName"
                    fixed="left"
                    width="100"
                    show-overflow-tooltip
                />
                <el-table-column
                    label="项目名称"
                    align="center"
                    prop="projectName"
                    fixed="left"
                    width="200"
                />
                <el-table-column label="项目编号" align="center" prop="projectNumber" width="120" />
                <el-table-column label="预算金额(万元)" align="center" prop="evaluation" width="120" />
                <el-table-column label="项目类型" align="center" prop="projectType" width="120" >
                    <template #default="scope">
                        <dict-tag :options="editDroppdown.projectTypes" :value="scope.row.projectType" />
                    </template>
                </el-table-column>
                <el-table-column label="采购机构" align="center" prop="purchaseServicesName" width="120" >
                    <template #default="scope">
                        {{ scope.row.purchaseServicesName || scope.row.purchaseServices }}
                    </template>
                </el-table-column>
                <el-table-column label="采购方式" align="center" prop="purchaseMode" width="150" >
                    <template #default="scope">
                        {{ scope.row.purchaseModeName || scope.row.purchaseMode }}
                    </template>
                </el-table-column>
                <el-table-column label="任务下达时间" align="center" prop="taskReleaseTime" width="160" />
                <el-table-column label="文件评审时间" align="center" prop="documentReviewTime" width="160" />
                <el-table-column label="文件审批时间" align="center" prop="procurementOfficeApprovalTime" width="160" />
                <el-table-column label="招标公告发布时间" align="center" prop="announcementReleaseTime" width="160" />
                <el-table-column label="开标时间" align="center" prop="tenderOpenTime" width="160" />
                <el-table-column label="中标公示时间" align="center" prop="bidAnnouncementTime" width="160" />
                <el-table-column label="备注" align="center" prop="remarks" width="300" show-overflow-tooltip />


                <!-- <el-table-column label="需求概况" align="center" prop="demandOverview" width="200" show-overflow-tooltip />
                <el-table-column label="是否为超限额计划" align="center" prop="isExcess" width="150" >
                    <template #default="scope">
                        <dict-tag :options="[{ label: '未超额', value: '0', elTagType: 'primary', elTagClass: '' }, 
                            { label: '超额', value: '1', listClass: 'danger', elTagType: 'error', elTagClass: '' }]" :value="scope.row.isExcess" />
                    </template>
                </el-table-column>
                <el-table-column label="采购服务站" align="center" prop="purchaseServices" width="150" />
                <el-table-column label="计划提报需求时间" align="center" prop="planDemandSubTime" width="150" />
                <el-table-column label="计划完成采购时间" align="center" prop="planPurchaseFinishTime" width="150" />
                <el-table-column label="计划交付(实施)时间" align="center" prop="planDeliverTime" width="150" /> -->
                <!-- <el-table-column label="项目属性" align="center" prop="projectAttr" width="200" >
                    <template #default="scope">
                        <dict-tag :options="editDroppdown.projectAttributes" :value="scope.row.projectAttr" />
                    </template>
                </el-table-column> -->
                <!-- <el-table-column label="项目属性" align="center" prop="projectAttrName" width="200" />
                <el-table-column label="计划调整情况及理由" align="center" prop="delayReason" width="200" /> -->
                <!-- <el-table-column label="预警状态" align="center" prop="warnStatusName" width="80" fixed="right">
                    <template #default="scope">
                        <dict-color-tag :options="[
                            { label: '正常', value: '0', bgColor: '#EEF6F6', color: '#007871' },
                            { label: '预警', value: '1', bgColor: '#FFF6F6', color: '#CA1919' }
                        ]" :value="scope.row.warnStatus"></dict-color-tag>
                    </template>
                </el-table-column> -->
                <el-table-column label="预警状态" align="center" prop="warnStatusName" width="120" fixed="right">
                    <template #default="scope">
                        <div v-if="scope.row.warnStatus === '0'" style="padding: 1px 6px; border-radius: 20px;" :style="{ 'background': 'rgb(238, 246, 246)', 'color': 'rgb(0, 120, 113)' }">{{ scope.row.warnStatusName }}</div>
                        <div v-else style="padding: 1px 6px; border-radius: 20px;" :style="{ 'background': '#FFF6F6', 'color': '#CA1919' }">{{ scope.row.warnStatusName }}</div>
                    </template>
                </el-table-column>
                <el-table-column label="项目状态" align="center" prop="projectStatusName" width="130" fixed="right"></el-table-column>
                <el-table-column
                    label="操作"
                    align="center"
                    width="290"
                    fixed="right"
                    class-name="small-padding fixed-width"
                >
                    <template #default="scope">
                        <el-button
                            type="default"
                            size="small"
                            @click="execModalDisplayChange('info', { id: scope.row.demandId, projectStatus: scope.row.projectStatus })"
                        >查看</el-button>
                        <el-button
                            v-if="scope.row.projectStatus === '9'"
                            size="small"
                            @click="handleShowAgentModal(scope.row)"
                        >选取代理</el-button>
                        <el-button
                            v-if="scope.row.projectStatus === '10'"
                            size="small"
                            @click="execModalDisplayChange('opinion', { id: scope.row.demandId })"
                        >上传意见反馈单</el-button>
                        <el-button
                            v-if="scope.row.projectStatus === '11'"
                            size="small"
                            @click="execModalDisplayChange('examine', { id: scope.row.demandId })"
                        >上传招标文件审批表</el-button>
                        <el-button
                            v-if="scope.row.projectStatus === '12'"
                            size="small"
                            @click="execModalDisplayChange('executeSend', { id: scope.row.demandId })"
                        >采购公告发布</el-button>
                        <el-button
                            v-if="scope.row.projectStatus === '13'"
                            size="small"
                            @click="execModalDisplayChange('questionBefore', { id: scope.row.demandId })"
                        >标前质疑投诉</el-button>
                        <el-button
                            v-if="scope.row.projectStatus === '14' && scope.row.hasExpert === '0'"
                            size="small"
                            @click="execModalDisplayChange('expert', { id: scope.row.demandId })"
                        >填写专家信息</el-button>
                        <el-button
                            v-if="scope.row.projectStatus === '14'"
                            size="small"
                            @click="execModalDisplayChange('bidInfo', { id: scope.row.demandId })"
                        >开评标</el-button>
                        <el-button
                            v-if="scope.row.projectStatus === '15'"
                            size="small"
                            @click="execModalDisplayChange('bidInfoBase', { id: scope.row.demandId })"
                        >评标结果公告</el-button>
                        <el-button
                            v-if="scope.row.projectStatus === '16'"
                            size="small"
                            @click="execModalDisplayChange('questionNew', { id: scope.row.demandId })"
                        >质疑处理</el-button>
                        <el-button
                            v-if="scope.row.projectStatus === '16'"
                            size="small"
                            @click="execModalDisplayChange('executeSend', { id: scope.row.demandId })"
                        >采购公告发布</el-button>
                        <el-button
                            v-if="scope.row.projectStatus === '17'"
                            size="small"
                            @click="execModalDisplayChange('question', { id: scope.row.demandId })"
                        >标后质疑</el-button>
                        <el-button
                            v-if="scope.row.projectStatus === '17'"
                            size="small"
                            @click="execModalDisplayChange('executeUpload', { id: scope.row.demandId })"
                        >上传中标通知书</el-button>



                        <!-- <el-button
                            v-if="scope.row.projectStatus === '11'"
                            size="small"
                            @click="execModalDisplayChange('executeSend', { id: scope.row.demandId })"
                        >发布招标公告</el-button>
                        <el-button
                            v-if="scope.row.projectStatus === '13'"
                            size="small"
                            @click="execModalDisplayChange('bidInfoBase', { id: scope.row.demandId })"
                        >发布中标公告</el-button> -->
                        <!-- <el-divider direction="vertical"></el-divider> -->
                    </template>
                </el-table-column>
                <template v-slot:empty>
                    <svg-icon icon-class="search-none" style="font-size: 64px;" />
                    <p>暂无数据</p>
                </template>
            </el-table>

            <pagination
                v-show="queryParams.total > 0"
                :total="queryParams.total"
                v-model:page="queryParams.pageNum"
                v-model:limit="queryParams.pageSize"
                @pagination="handleQuery"
            />
        </el-card>
        
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
                <InfoComponent v-if="globalModal.info.show" :recordId="globalModal.info.id" :project-status="globalModal.info.projectStatus" :tab-panes="globalModal.info.projectStatus === '9' ? [ '1', '2', '3' ] : [ '1', '2', '3', '4' ]" step="purchaseExecution"></InfoComponent>
            </div>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="globalModal.info.show = false">关闭</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>
  
<script setup name="PmAssignmentIndex">
import DictColorTag from "@/components/DictColorTag";
import GlobalDelete from "@/components/GlobalDelete";

import { queryList } from "@/api/pm/execute";

import InfoComponent from "../components/common/info";
import AgentModal from "./components/agent/index";
import EnclosureModal from "./components/enclosure/index";
import ExpertModal from "./components/expert/index";
import ExecuteSendModal from "./components/notice/send";
import ExecuteUploadModal from "./components/notice/upload";
import BidInfoModal from "./components/bidinfo/index";
import BidInfoBaseModal from "./components/bidinfo/base";
import QuestionModal from "./components/question/index";

import OpinionModal from "./components/opinion/index";
import ExamineModal from "./components/examine/index";
import QuestionBeforeModal from "./components/question/index-before";
import QuestionNewModal from "./components/question/index-new";

import CommonTableMinix from "@/mixins/table/table";
import CommonModalMinix from "@/mixins/modal/index";

import {
    queryYearPlanDorpdown,
    purchaseServices
} from "@/api/plan/year";

const { proxy } = getCurrentInstance();
const { sys_project_status, project_warn_status } = proxy.useDict("sys_project_status","project_warn_status");

const advanced = ref(false);
const tableHeight = proxy.getInitTableHeight(50);
const loading = ref(true);
const showSearch = ref(true);
const dateRange = ref([]);
let purchaseServicesOpt = ref([])


const data = reactive({
    records: [],
    form: {
        projectAttr: [],
    },
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        total: 0,
    },
    rules: {
        projectName: [
            { required: true, message: "项目名称不能为空", trigger: "blur" },
        ],
        projectType: [
            { required: true, message: "项目类型不能为空", trigger: "change" },
        ],
        demandOverview: [
            { required: true, message: "需求概况不能为空", trigger: "blur" },
        ],
        evaluation: [
            { required: true, message: "预算金额不能为空", trigger: "blur" },
        ],
        purchaseMode: [
            { required: true, message: "采购方式不能为空", trigger: "change" },
        ],
        purchaseServices: [
            { required: true, message: "采购服务站不能为空", trigger: "change" },
        ],
        planDemandSubTime: [
            { required: true, message: "计划提报需求时间不能为空", trigger: "change" },
        ],
        planPurchaseFinishTime: [
            { required: true, message: "计划完成采购时间不能为空", trigger: "change" },
        ],
        planDeliverTime: [
            { required: true, message: "计划交付时间不能为空", trigger: "change" },
        ],
        projectAttr: [
            { type: 'array', required: true, message: "项目属性不能为空", trigger: "change" },
        ],
    },
});

const { handleQuery, handleRefresh, handleResetQuery, toggleAdvanced } = CommonTableMinix(proxy, data, {
  fetch: () => queryList(
    proxy.addDateRange(queryParams.value, dateRange.value)
  ),
}, {
  loading: loading,
  showSearch: showSearch,
  tableHeight: tableHeight,
  advanced: advanced,
  beforeReset: () => {
    dateRange.value = [];
  }
})


function fetchPurchaseServicesOpt(name){
    const data  = {       
      "projectType": "",
      "evaluation": 0
        }
    purchaseServices(data).then((response)=>{
        purchaseServicesOpt.value = response.data
        
        // const fileName = '年度计划模板'
        //   const contentType =
        //     'application/vnd.ms-excel'
        //   exportFile(response, contentType, fileName)
    })
}
fetchPurchaseServicesOpt()

const { queryParams, records, form, rules } = toRefs(data);

const editDroppdown = ref({});
function queryDorpDown() {
    queryYearPlanDorpdown({}).then((response) => {
        let _projectTypes = [];
        Object.entries(response.data?.projectTypes || {}).map((entry) => {
            _projectTypes.push({ value: entry[1], label: entry[0] });
        });
        let _projectStatus = [];
        Object.entries(response.data?.projectStatus || {}).map((entry) => {
            _projectStatus.push({ value: entry[1], label: entry[0] });
        });
        let _projectAttributes = [];
        Object.entries(response.data?.projectAttributes || {}).map((entry) => {
            _projectAttributes.push({ value: entry[1], label: entry[0] });
        });
        let _purchaseServices = [];
        Object.entries(response.data?.purchaseServices || {}).map((entry) => {
            _purchaseServices.push({ value: entry[1], label: entry[0] });
        });
        let _planPurchaseModes = [];
        Object.entries(response.data?.planPurchaseModes || {}).map((entry) => {
            _planPurchaseModes.push({ value: entry[1], label: entry[0] });
        });
        editDroppdown.value = {
            projectTypes: _projectTypes,
            projectStatus: _projectStatus,
            projectAttributes: _projectAttributes,
            purchaseServices: _purchaseServices,
            planPurchaseModes: _planPurchaseModes,
        };
    });
}
queryDorpDown();

const projectStatusConvert = computed(() => {
    let _projectStatusColor = {
        '0': { bgColor: '#EEF6F6', color: '#007871' },
        '1': { bgColor: '#EEF6F6', color: '#007871' },
        '2': { bgColor: '#FFF6F6', color: '#CA1919' },
        '3': { bgColor: '#F3F9FF', color: '#006AEA' },
    }
    return (editDroppdown.value?.projectStatus || []).map(m => {
        return { ...m, ..._projectStatusColor[m.value] }
    })
})


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


// const infoModal = ref({
//   show: false,
//   id: undefined,
// });

// function handleShowInfoModal(row) {
//   if (!row) return;
//   infoModal.value = {
//     show: true,
//     id: row.demandId
//   }
// }

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
})

const { execModalDisplayChange } = CommonModalMinix(proxy, globalModal)


handleQuery();
</script>

<style lang="scss" scoped>
.app-container {
    :deep(.plan-cust-info-modal) {
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
            border-top: 0px;
            text-align: center;
        }
        .dialog_body {
            padding: 30px 0px;
            border-radius: 20px;
            text-align: center;
            .msg {
                margin-top: 10px;
            }
        }
    }
}
.aidex-table {
    .search_box {
        margin-top: 0px;
    }
    .btn_box {
        margin-top: 0px;
    }
}
</style>
  