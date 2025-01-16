<template>
  <div>
    <div class="title">项目计划</div>
    <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
      <el-tab-pane label="项目计划" name="1">
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
              <el-form-item label="概算金额" prop="evaluation">
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
            <el-col
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
            </el-col>
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
            <el-col v-if="voInfo.remarks" :span="24">
              <el-form-item label="计划调整情况及理由" prop="remarks">
                <el-input
                  v-model="voInfo.remarks"
                  type="textarea"
                  placeholder="请输入计划调整情况及理由"
                  readonly
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div class="title mt-20" v-if="voInfo.demandFileMap">项目附件</div>
        <div class="files">
          <div
            v-for="(item, index) in voInfo.demandFileMap"
            :key="index"
            class="file-item"
            @click="handlePreviewFile(item.fileUrl)"
          >
            <img
              src="/public/images/common/icon-file-pdf.png"
              style="width: 50px; height: 53px"
            />
            <div class="text">{{ item.fileName }}</div>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="需求建档" name="2">
        <el-form :model="form" label-position="right" label-width="180">
          <el-row :gutter="24">
            <el-col :span="24">
              <el-form-item label="项目名称" prop="typeAndName">
                <el-input
                  v-model="demandVoInfo.typeAndName"
                  placeholder=""
                  readonly
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="预算金额(万元)" prop="budgetAmount">
                <el-input
                  v-model="demandVoInfo.budgetAmount"
                  placeholder="请输入预算金额"
                  type="number"
                  readonly
                >
                  <template #append>万元</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="需求提报时间" prop="demandCommitTime">
                <el-date-picker
                  v-model="demandVoInfo.demandCommitTime"
                  :value-format="'YYYY-MM-DD'"
                  type="date"
                  placeholder="请选择需求提报时间"
                  format="YYYY-MM-DD"
                  class="w-100"
                  readonly
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="备注" prop="remarks">
                <el-input
                  v-model="demandVoInfo.remarks"
                  type="textarea"
                  placeholder="请输入备注"
                  readonly
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div class="title mt-20"  v-if="voInfo.demandFileMap&&voInfo.demandFileMap.length">项目附件</div>
        <div class="files">
          <div
            v-for="(item, index) in demandVoInfo.demandFileMap"
            :key="index"
            class="file-item"
            @click="handlePreviewFile(item.fileUrl)"
          >
            <img
              src="/public/images/common/icon-file-pdf.png"
              style="width: 50px; height: 53px"
            />
            <div class="text">{{ item.fileName }}</div>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { getDetail } from "@/api/pm/demand";
import { queryYearPlanDorpdown, queryYearPlanInfo } from "@/api/plan/year";
const activeName = "1";
const nedTabData = "";
const radioArray = ref([
  {
    value: "1",
    label: "公开招标",
  },
  {
    value: "2",
    label: "邀请招标",
  },
  {
    value: "3",
    label: "竞争性谈判",
  },
  {
    value: "4",
    label: "询价",
  },
  {
    value: "5",
    label: "单一来源",
  },
]);

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
  stepInof:{
    type: String,
    default: "",
  }
});

watch(
  () => props.recordId,
  (value) => init(value),
  { immediate: true },
  activeName.value==1

);
const voInfo = ref({});
const demandVoInfo=ref({})
function init(id, tab) {
  console.log(props.stepInof,'00000000000000000000000000000000000000000000000000')
  getDetail({ demandId: props.recordId, detailType: activeName }).then(
    (res) => {
      voInfo.value = res.data;
      if(res.data.pmBookBuildingResVo){
        demandVoInfo.value=res.data.pmBookBuildingResVo
      }
      
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
  getDetail({ demandId: props.recordId, detailType: tab.props.name }).then(
    (res) => {
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