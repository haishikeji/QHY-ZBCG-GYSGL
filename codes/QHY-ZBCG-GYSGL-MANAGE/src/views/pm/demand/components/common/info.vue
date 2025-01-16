<template>
  <div>
    <!-- <div class="title">项目计划</div> -->
    <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
      <el-tab-pane v-if="tabPanes.includes('1')" label="项目计划" name="1">
        <el-form :model="form" label-position="right" label-width="120">
          <el-row :gutter="24">
            <el-col :span="12">
              <el-form-item label="项目名称" prop="projectName">
                <el-input
                  v-model="voInfo.projectName"
                  placeholder=""
                  readonly
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="项目类型" prop="projectType">
                <el-select v-model="voInfo.projectType" placeholder="" disabled>
                  <el-option
                    v-for="item in droppdown.projectTypes"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="预算金额" prop="evaluation">
                <el-input v-model="voInfo.evaluation" placeholder="" readonly>
                  <template #append>万元</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="采购服务站" prop="purchaseServices">
                <el-select
                  v-model="voInfo.purchaseServices"
                  placeholder=""
                  disabled
                >
                  <el-option
                    v-for="item in droppdown.purchaseServices"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="采购方式" prop="purchaseMode">
                <el-select
                  v-model="voInfo.purchaseMode"
                  placeholder=""
                  disabled
                >
                  <el-option
                    v-for="item in droppdown.planPurchaseModes"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="需求提报时间" prop="planDemandSubTime">
                <el-date-picker
                  v-model="voInfo.planDemandSubTime"
                  :value-format="'YYYY-MM-DD'"
                  type="date"
                  placeholder=""
                  format="YYYY-MM-DD"
                  class="w-100"
                  readonly
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="完成采购时间" prop="planPurchaseFinishTime">
                <el-date-picker
                  v-model="voInfo.planPurchaseFinishTime"
                  :value-format="'YYYY-MM-DD'"
                  type="date"
                  placeholder=""
                  format="YYYY-MM-DD"
                  class="w-100"
                  readonly
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="交付实施时间" prop="planDeliverTime">
                <el-date-picker
                  v-model="voInfo.planDeliverTime"
                  :value-format="'YYYY-MM-DD'"
                  type="date"
                  placeholder=""
                  format="YYYY-MM-DD"
                  class="w-100"
                  readonly
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="需求概况" prop="demandOverview">
                <el-input
                  v-model="voInfo.demandOverview"
                  type="textarea"
                  placeholder=""
                  readonly
                />
              </el-form-item>
            </el-col>
            <!-- <el-col
              v-if="operType === 'applyModify' || voInfo.adjustReason"
              :span="24"
            >
              <el-form-item label="计划调整及理由" prop="adjustReason">
                <el-input
                  v-model="voInfo.adjustReason"
                  type="textarea"
                  placeholder=""
                  :readonly="operType !== 'applyModify'"
                />
              </el-form-item>
            </el-col> -->
            <el-col v-if="voInfo.remarks" :span="24">
              <el-form-item label="备注" prop="remarks">
                <el-input
                  v-model="voInfo.remarks"
                  type="textarea"
                  placeholder=""
                  readonly
                />
              </el-form-item>
            </el-col>
            <!-- <el-col v-if="voInfo.remarks" :span="24">
              <el-form-item label="计划调整情况及理由" prop="remarks">
                <el-input
                  v-model="voInfo.remarks"
                  type="textarea"
                  placeholder="请输入计划调整情况及理由"
                  readonly
                />
              </el-form-item>
            </el-col> -->
          </el-row>
        </el-form>
        <div class="title mt-20">项目附件</div>
        <div class="files">
          <div
            v-for="(item, index) in voInfo.demandFileInfos"
            :key="index"
            class="file-item"
            @click="handlePreviewFile(item.fileUrl)"
          >
            <img
              src="/images/common/icon-file-pdf.png"
              style="width: 50px; height: 53px"
            />
            <div class="text">{{ item.fileName }}</div>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane v-if="tabPanes.includes('2')&&props.projectIdType==5||props.projectIdType==6||props.projectIdType==7||props.projectIdType==8||props.projectIdType==9||props.projectIdType==10||props.projectIdType==11||props.projectIdType==12||props.projectIdType==13||props.projectIdType==14||props.projectIdType==15||props.projectIdType==16||props.projectIdType==17" label="需求建档" name="2">
        <el-form v-if="voInfo.pmBookBuildingResVo" :model="form" label-position="right" label-width="180">
          <el-row :gutter="24">
            <el-col :span="24">
              <el-form-item label="项目名称" prop="projectName">
                <el-input
                  v-model="voInfo.projectName"
                  placeholder=""
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="预算金额(万元)" prop="budgetAmount">
                <el-input
                  v-model="voInfo.pmBookBuildingResVo.budgetAmount"
                  placeholder="请输入预算金额"
                  type="number"
                  disabled
                >
                  <template #append>万元</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="需求提报时间" prop="demandCommitTime">
                <el-date-picker
                  v-model="voInfo.pmBookBuildingResVo.demandCommitTime"
                  :value-format="'YYYY-MM-DD'"
                  type="date"
                  placeholder="请选择需求提报时间"
                  format="YYYY-MM-DD"
                  class="w-100"
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="备注" prop="remarks">
                <el-input
                  v-model="voInfo.pmBookBuildingResVo.remarks"
                  type="textarea"
                  placeholder="请输入备注"
                  disabled
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div v-if="voInfo.pmBookBuildingResVo" class="title mt-20">项目附件</div>
        <div v-if="voInfo.pmBookBuildingResVo" class="files">
          <div
            v-for="(item, index) in voInfo.pmBookBuildingResVo.fileInfos"
            :key="index"
            class="file-item"
            @click="handlePreviewFile(item.fileUrl)"
          >
            <img
              src="/images/common/icon-file-pdf.png"
              style="width: 50px; height: 53px"
            />
            <div class="text">{{ item.fileName }}</div>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane v-if="tabPanes.includes('3')&&props.projectIdType==9||props.projectIdType==10||props.projectIdType==11||props.projectIdType==12||props.projectIdType==13||props.projectIdType==14||props.projectIdType==15||props.projectIdType==16||props.projectIdType==17" label="任务下达" name="3">
        <div v-if="voInfo.taskReleaseFileInfos" class="title mt-20">附件</div>
        <div v-if="voInfo.taskReleaseFileInfos" class="files">
          <div
            v-for="(item, index) in voInfo.taskReleaseFileInfos"
            :key="index"
            class="file-item"
            @click="handlePreviewFile(item.fileUrl)"
          >
            <img
              src="/images/common/icon-file-pdf.png"
              style="width: 50px; height: 53px"
            />
            <div class="text">{{ item.fileName }}</div>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="中标信息" name="4" v-if="props.projectIdType>=13">
        <div v-if="voInfo.pmPurchaseExecutionResVo">
          <div v-if="voInfo.pmPurchaseExecutionResVo.baseAgency" class="title">
              招标代理机构信息
          </div>
          <el-form v-if="voInfo.pmPurchaseExecutionResVo.baseAgency" label-position="right" label-width="110">
            <el-row :gutter="24">
              <el-col :span="8">
                  <el-form-item label="单位名称">
                      <el-input v-model="voInfo.pmPurchaseExecutionResVo.baseAgency.companyName" readonly />
                  </el-form-item>
              </el-col>
              <el-col :span="8">
                  <el-form-item label="采购品种">
                      <el-input v-model="voInfo.pmPurchaseExecutionResVo.baseAgency.companyTypeName" readonly />
                  </el-form-item>
              </el-col>
              <el-col :span="8">
                  <el-form-item label="注册时间">
                      <el-input v-model="voInfo.pmPurchaseExecutionResVo.baseAgency.registTime" readonly />
                  </el-form-item>
              </el-col>
              <el-col :span="8">
                  <el-form-item label="单位性质">
                      <el-input v-model="voInfo.pmPurchaseExecutionResVo.baseAgency.companyNatureName" readonly />
                  </el-form-item>
              </el-col>
              <el-col :span="8">
                  <el-form-item label="业务联系人">
                      <el-input v-model="voInfo.pmPurchaseExecutionResVo.baseAgency.companyContacts" readonly />
                  </el-form-item>
              </el-col>
              <el-col :span="8">
                  <el-form-item label="联系电话">
                      <el-input v-model="voInfo.pmPurchaseExecutionResVo.baseAgency.contactsPhone" readonly />
                  </el-form-item>
              </el-col>
              <el-col :span="24">
                  <el-form-item label="办公地址">
                      <el-input v-model="voInfo.pmPurchaseExecutionResVo.baseAgency.companyAddr" readonly />
                  </el-form-item>
              </el-col>
              <el-col :span="24">
                  <el-form-item label="主营范围">
                      <el-input v-model="voInfo.pmPurchaseExecutionResVo.baseAgency.businessScope" type="textarea" readonly />
                  </el-form-item>
              </el-col>
            </el-row>
          </el-form>

          <div class="title" style="margin-bottom: 4px;">
              发布公告
          </div>
          <el-table stripe :data="voInfo.pmPurchaseExecutionResVo.pmReleaseAnnouncementResVoList" highlight-current-row style="width: 100%;" class="table-min-height" >
              <el-table-column type="index" label="序号" width="55" align="center" fixed="left" />
              <el-table-column label="发布公告平台" align="center" prop="announcementPlatform" width="160" />
              <el-table-column label="发布时间" align="center" prop="releaseTime" width="160" />
              <el-table-column label="开标地点" align="center" prop="bidOpenPlace" min-width="300" show-overflow-tooltip />
              <el-table-column label="报名截止时间" align="center" prop="enrollCloseTime" width="160" />
              <el-table-column label="开标时间" align="center" prop="tenderOpenTime" width="160" />
              <el-table-column label="备注" align="center" prop="remarks" min-width="300" show-overflow-tooltip />
          </el-table>


          <div class="title mt20" style="margin-bottom: 4px;">
              专家信息
          </div>
          <el-table stripe :data="voInfo.pmPurchaseExecutionResVo.pmBaseExpertResVoList" highlight-current-row style="width: 100%;" class="table-min-height" >
              <el-table-column type="index" label="序号" width="55" align="center" fixed="left" />
              <el-table-column label="选取时间" align="center" prop="accessTime" />
              <el-table-column label="专家数量" align="center" prop="expertCount" />
              <el-table-column label="操作" align="center" width="100">
                <template #default="scope">
                  <el-link type="primary" @click="execModalDisplayChange('expertList', { id: scope.row.demandId, accessTime: scope.row.accessTime })">查看详情</el-link>
                </template>
              </el-table-column>
          </el-table>


          <div class="title mt20" style="margin-bottom: 4px;">
              开标信息
          </div>
          <el-table stripe :data="voInfo.pmPurchaseExecutionResVo.pmBidOpeningResVoList" highlight-current-row style="width: 100%;" class="table-min-height" >
              <el-table-column type="index" label="序号" width="55" align="center" fixed="left" />
              <el-table-column label="开标时间" align="center" prop="openBidTime" />
              <el-table-column label="参与数量" align="center" prop="partakeCount" />
              <el-table-column label="操作" align="center" width="100">
                <template #default="scope">
                  <el-link type="primary" @click="execModalDisplayChange('scoreRank', { id: scope.row.demandId, openBidTime: scope.row.openBidTime })">查看详情</el-link>
                </template>
              </el-table-column>
          </el-table>


          <div class="title mt20" style="margin-bottom: 4px;">
              中标信息
          </div>
          <el-table stripe :data="voInfo.pmPurchaseExecutionResVo.pmBidWinningResVoList" highlight-current-row style="width: 100%;" class="table-min-height" >
              <el-table-column type="index" label="序号" width="55" align="center" fixed="left" />
              <el-table-column label="中标单位信息" align="center" prop="bidWinningName" />
              <el-table-column label="中标金额(万元)" align="center" prop="bidWinningAmount" />
              <el-table-column label="中标公告时间" align="center" prop="bidAnnouncementTime" />
              <el-table-column label="中标公告截止时间" align="center" prop="bidAnnouncementCloseTime" />
          </el-table>

          <div class="title mt20">质疑情况</div>
          <div class="files">
            <div
              v-for="(item, index) in voInfo.pmPurchaseExecutionResVo.callRequestFileInfos"
              :key="index"
              class="file-item"
              @click="handlePreviewFile(item.fileUrl)"
            >
              <img
                src="/images/common/icon-file-pdf.png"
                style="width: 50px; height: 53px"
              />
              <div class="text">{{ item.fileName }}</div>
            </div>
          </div>


          <div class="title mt20" style="margin-bottom: 4px;">
              流标情况
          </div>
          <el-table stripe :data="voInfo.pmPurchaseExecutionResVo.pmBidFailureResVoList" highlight-current-row style="width: 100%;" class="table-min-height" >
              <el-table-column type="index" label="序号" width="55" align="center" fixed="left" />
              <el-table-column label="流标原因" align="center" prop="bidFailureReason" />
              <el-table-column label="流标时间" align="center" prop="bidFailureTime" width="160" />
          </el-table>

          <div class="title mt20">项目附件</div>
          <div class="files">
            <div
              v-for="(item, index) in voInfo.pmPurchaseExecutionResVo.fileInfos"
              :key="index"
              class="file-item"
              @click="handlePreviewFile(item.fileUrl)"
            >
              <img
                src="/images/common/icon-file-pdf.png"
                style="width: 50px; height: 53px"
              />
              <div class="text">{{ item.fileName }}</div>
            </div>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="合同信息" name="5" v-if="props.projectIdType==16||props.projectIdType==17">
        <el-form v-if="voInfo.pmContractInfoResVo" label-position="right" label-width="110">
          <el-row :gutter="24">
            <el-col :span="12">
                <el-form-item label="合同编号" prop="contractNumber">
                    <el-input v-model="voInfo.pmContractInfoResVo.contractNumber" placeholder="请输入合同编号" readonly />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="合同名称" prop="contractName">
                    <el-input v-model="voInfo.pmContractInfoResVo.contractName" placeholder="请输入合同名称" readonly />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="合同金额" prop="contractAmount">
                    <el-input v-model="voInfo.pmContractInfoResVo.contractAmount" placeholder="请输入合同金额" readonly>
                        <template #append>万元</template>
                    </el-input>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="合同起止日期" prop="date">
                    <el-date-picker
                        v-model="voInfo.pmContractInfoResVo.date"
                        :value-format="'YYYY-MM-DD'"
                        type="daterange"
                        placeholder="请选择合同起止日期"
                        format="YYYY-MM-DD"
                        class="w-100" readonly
                    />
                </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        
        <div v-if="voInfo.pmContractInfoResVo" class="title mt-20">附件</div>
        <div v-if="voInfo.pmContractInfoResVo" class="files">
          <div
            v-for="(item, index) in voInfo.pmContractInfoResVo.fileInfos"
            :key="index"
            class="file-item"
            @click="handlePreviewFile(item.fileUrl)"
          >
            <img
              src="/images/common/icon-file-pdf.png"
              style="width: 50px; height: 53px"
            />
            <div class="text">{{ item.fileName }}</div>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="建设情况" name="6" v-if="props.projectIdType==17">
        <el-form v-if="voInfo.pmProjectConstructionResVo" label-position="right" label-width="110">
          <el-row :gutter="24">
            <el-col :span="12">
                <el-form-item label="开始时间" prop="startTime">
                  <el-date-picker
                      v-model="voInfo.pmProjectConstructionResVo.startTime"
                      :value-format="'YYYY-MM-DD'"
                      type="date"
                      placeholder="请选择开始时间"
                      format="YYYY-MM-DD"
                      class="w-100" readonly
                  />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="初步验收时间" prop="preliminaryInspectionTime">
                  <el-date-picker
                      v-model="voInfo.pmProjectConstructionResVo.preliminaryInspectionTime"
                      :value-format="'YYYY-MM-DD'"
                      type="date"
                      placeholder="请选择初步验收时间"
                      format="YYYY-MM-DD"
                      class="w-100" readonly
                  />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="终验时间" prop="finalInspectionTime">
                  <el-date-picker
                      v-model="voInfo.pmProjectConstructionResVo.finalInspectionTime"
                      :value-format="'YYYY-MM-DD'"
                      type="date"
                      placeholder="请选择终验时间"
                      format="YYYY-MM-DD"
                      class="w-100" readonly
                  />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="结束时间" prop="endTime">
                  <el-date-picker
                      v-model="voInfo.pmProjectConstructionResVo.endTime"
                      :value-format="'YYYY-MM-DD'"
                      type="date"
                      placeholder="请选择结束时间"
                      format="YYYY-MM-DD"
                      class="w-100" readonly
                  />
                </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        
        <div v-if="voInfo.pmProjectConstructionResVo" class="title mt-20">附件</div>
        <div v-if="voInfo.pmProjectConstructionResVo && voInfo.pmProjectConstructionResVo.fileInfos && voInfo.pmProjectConstructionResVo.fileInfos.length" class="files">
          <div
            v-for="(item, index) in voInfo.pmProjectConstructionResVo.fileInfos"
            :key="index"
            class="file-item"
            @click="handlePreviewFile(item.fileUrl)"
          >
            <img
              src="/images/common/icon-file-pdf.png"
              style="width: 50px; height: 53px"
            />
            <div class="text">{{ item.fileName }}</div>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
    <ExpertList v-if="globalModal.expertList.show && globalModal.expertList.id" v-model="globalModal.expertList.show" :record-id="globalModal.expertList.id" 
      :access-time="globalModal.expertList.accessTime"></ExpertList>
    <ScoreRank v-if="globalModal.scoreRank.show && globalModal.scoreRank.id" v-model="globalModal.scoreRank.show" :record-id="globalModal.scoreRank.id" 
      :access-time="globalModal.scoreRank.openBidTime"></ScoreRank>
  </div>
</template>

<script setup>
import { fetchInfo } from "@/api/pm/assignment";
import { queryYearPlanDorpdown } from "@/api/plan/year";

import { getDetail as fetchDemandInfo } from "@/api/pm/demand";
import { fetchInfo as fetchAssignmentInfo } from "@/api/pm/assignment";
import { fetchInfo as fetchExecuteInfo } from "@/api/pm/execute";
import { fetchInfo as fetchContractInfo } from "@/api/pm/contract";
import { fetchInfo as fetchConstructionInfo } from "@/api/pm/construction";


import ExpertList from "./expertList";
import ScoreRank from "./scoreRank";
import CommonModalMinix from "@/mixins/modal/index";

const { proxy } = getCurrentInstance();

const activeName = ref("1");

const props = defineProps({
  // 当前的值
  recordId: {
    type: Number,
    require: true,
  },
  operType: {
    type: String,
    default: "",
  },
  projectIdType:{
    type: String,
    require: true,
  },
  tabPanes: {
    type: Array,
    default: function() {
      return [ '1', '2', '3' ]
    },
  },
  step: {
    type: String,
    default: "demand",
  },
});

const records = ref([
  {}
])
const change = () => {
  alert(222)
}


watch(() => props.recordId, (value) => init(value), { immediate: true });

const voInfo = ref({});
function init(id, tab) {
  console.log(props.projectIdType,'projectIdType=====projectIdType=======');
  if(props.projectIdType==5||props.projectIdType==6||props.projectIdType==7||props.projectIdType==8){
    activeName.value='2'
  }else if(props.projectIdType==9){
    activeName.value='3'
  }else if(props.projectIdType==10||props.projectIdType==11||props.projectIdType==12||props.projectIdType==13||props.projectIdType==14||props.projectIdType==15){
    activeName.value='4'
  }else if(props.projectIdType==16){
    activeName.value='5'
  }else if(props.projectIdType==17){
    activeName.value='6'
  }
  // nedTabData.value=activeName
  let _api;
  if (props.step === 'demand') {
    _api = fetchDemandInfo;
  } else if (props.step === 'taskRelease') {
    _api = fetchAssignmentInfo;
  } else if (props.step === 'purchaseExecution') {
    _api = fetchExecuteInfo;
  } else if (props.step === 'contractInfo') {
    _api = fetchContractInfo;
  } else if (props.step === 'projectConstruction') {
    _api = fetchConstructionInfo;
  }
  console.log('props.step', props.step, _api)
  _api({ demandId: props.recordId, detailType: activeName._value }).then(
    (res) => {
      console.log(res,'res============');
      if (!res.data.pmBookBuildingResVo) {
        res.data.pmBookBuildingResVo = {};
      }
      if (!res.data.pmPurchaseExecutionResVo) {
        res.data.pmPurchaseExecutionResVo = {
          baseAgency: {},
          pmReleaseAnnouncementResVoList: [],
          pmBaseExpertResVoList: [],
          pmBidWinningResVoList: [],
          callRequestFileInfos: [],
          pmBidFailureResVoList: [],
          pmBidWinningNotificationFileInfos: [],
          fileInfos: []
        };
      }
      if (!res.data.pmContractInfoResVo) {
        res.data.pmContractInfoResVo = {
        }
      }
      if (res.data.pmContractInfoResVo.contractStartDate && res.data.pmContractInfoResVo.contractTerminationDate) {
        res.data.pmContractInfoResVo.date = [ res.data.pmContractInfoResVo.contractStartDate, res.data.pmContractInfoResVo.contractTerminationDate ]
      }
      if (!res.data.taskReleaseFileInfos) {
        res.data.taskReleaseFileInfos = []
      }
      voInfo.value = res.data;
    }
  );
}
// init()

const droppdown = ref({});
/** 新增按钮操作 */
function loadDropdownContent() {
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
    droppdown.value = {
      projectTypes: _projectTypes,
      projectStatus: _projectStatus,
      projectAttributes: _projectAttributes,
      purchaseServices: _purchaseServices,
      planPurchaseModes: _planPurchaseModes,
    };
  });
}
loadDropdownContent();
function handleClick(tab, event) {
  voInfo.value = {};
  let _api;
  if (props.step === 'demand') {
    _api = fetchDemandInfo;
  } else if (props.step === 'taskRelease') {
    _api = fetchAssignmentInfo;
  } else if (props.step === 'purchaseExecution') {
    _api = fetchExecuteInfo;
  } else if (props.step === 'contractInfo') {
    _api = fetchContractInfo;
  } else if (props.step === 'projectConstruction') {
    _api = fetchConstructionInfo;
  }
  _api({ demandId: props.recordId, detailType: tab.props.name }).then(
    (res) => {
      if (!res.data.pmBookBuildingResVo) {
        res.data.pmBookBuildingResVo = {};
      }
      if(res.data.auditFileInfos!=null){
          res.data.auditFileInfos.filter((ant)=>{
          res.data.pmBookBuildingResVo.fileInfos.push(ant)
          })
      }
      if (!res.data.pmPurchaseExecutionResVo) {
        res.data.pmPurchaseExecutionResVo = {
          baseAgency: {},
          pmReleaseAnnouncementResVoList: [],
          pmBaseExpertResVoList: [],
          pmBidWinningResVoList: [],
          callRequestFileInfos: [],
          pmBidFailureResVoList: [],
          pmBidWinningNotificationFileInfos: [],
          fileInfos: []
        };
      }
      if (!res.data.pmContractInfoResVo) {
        res.data.pmContractInfoResVo = {
        }
      }
      if (res.data.pmContractInfoResVo.contractStartDate && res.data.pmContractInfoResVo.contractTerminationDate) {
        res.data.pmContractInfoResVo.date = [ res.data.pmContractInfoResVo.contractStartDate, res.data.pmContractInfoResVo.contractTerminationDate ]
      }
      if (!res.data.taskReleaseFileInfos) {
        res.data.taskReleaseFileInfos = []
      }
      voInfo.value = res.data;
    }
  );
}
function handlePreviewFile(path) {
  if (!path) return;
  window.open(path);
}

function fetchReqParams() {
  return {
    id: props.recordId,
    adjustReason: voInfo.value.adjustReason,
  };
}

const globalModal = ref({
    expertList: { show: false },
    scoreRank: { show: false },
})

const { execModalDisplayChange } = CommonModalMinix(proxy, globalModal)

defineExpose({ fetchReqParams });
</script>

<style lang="scss" scoped>
.title {
  position: relative;
  width: 100%;
  padding: 10px 10px 10px 20px;
  box-sizing: border-box;
  background: linear-gradient(270deg, rgba(218, 231, 246, 0) 0%, #e3ebf4 100%);
  font-weight: bold;
  margin-bottom: 20px;
  &::before {
    content: " ";
    position: absolute;
    left: 0px;
    width: 5px;
    height: 100%;
    top: 0px;
    background: #006aea;
  }
}

.files {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  padding-left: 120px;
  box-sizing: border-box;
  .file-item {
    width: 100px;
    text-align: center;
    cursor: pointer;
    .text {
      margin-top: 8px;
    }
  }
}
</style>