<template>
  <div class="app-container search-table-box aidex-table cust-list-page">
    <el-card
      shadow="never"
      ref="queryRef"
      style="margin-bottom: 12px"
      class="search_card"
      v-show="showSearch"
    >
      <template #header>
        <el-row>
          <el-col :span="8"> </el-col>
          <el-col :span="16">
            <div class="btn_box" align="right" style="float: right">
              <el-button
                v-hasPermi="['plan:planYears:importData']"
                class="filter-item"
                style="margin-left: 8px"
                icon="Download"
                @click="downLoadExport"
                >下载模板</el-button
              >
              <el-button
                v-hasPermi="['plan:planYears:exportPlanExamine']"
                class="filter-item"
                style="margin-left: 8px"
                icon="Download"
                @click="handleExport"
                >导出项目</el-button
              >
            </div>
          </el-col>
        </el-row>
      </template>
      <div class="filter-container">
        <div class="search_box">
          <el-form :model="queryParams" ref="queryForm" label-width="0px">
            <el-row :gutter="16">
              <el-col :md="4">
                <el-form-item prop="deptId">
                  <el-select
                    v-model="queryParams.deptId"
                    placeholder="请选择单位"
                    style="width: 100%"
                    clearable
                    @change="handleQuery"
                  >
                    <el-option
                      v-for="(item, index) in unitDropdown.unitList"
                      :key="index"
                      :label="item.deptName"
                      :value="item.deptId"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :md="4">
                <el-form-item prop="projectName">
                  <el-input
                    v-model="queryParams.projectName"
                    placeholder="请输入项目名称进行查询"
                    clearable
                    @keyup.enter="handleQuery"
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
                    @change="handleQuery"
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
              <!-- <el-col :md="4">
                                <el-form-item prop="isExcess">
                                    <el-select
                                        v-model="queryParams.isExcess"
                                        placeholder="请选择是否为超限额"
                                        style="width: 100%"
                                        clearable
                                        @change="handleQuery"
                                    >
                                        <el-option
                                            v-for="item in [{ label: '未超额', value: '0' }, { label: '超额', value: '1' }]"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value"
                                        ></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col> -->
              <el-col :md="4">
                <el-form-item prop="projectStatus">
                  <el-select
                    v-model="queryParams.projectStatus"
                    placeholder="请选择项目状态"
                    style="width: 100%"
                    clearable
                    @change="handleQuery"
                  >
                    <el-option
                      v-for="item in editDroppdown.projectStatus"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :md="6">
                <el-form-item>
                  <el-date-picker
                    style="width: 100%"
                    v-model="dateRange"
                    value-format="YYYY-MM"
                    type="monthrange"
                    range-separator="-"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                  ></el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :md="2" align="right" style="margin-left: auto">
                <el-form-item class="search_btn_box">
                  <el-button
                    class="filter-item w-100"
                    type="primary"
                    @click="handleQuery"
                    >搜索</el-button
                  >
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
        :data="configList"
        @selection-change="handleSelectionChange"
        highlight-current-row
        style="width: 100%"
        :height="tableHeight"
      >
        <el-table-column
          type="index"
          label="序号"
          width="55"
          align="center"
          fixed="left"
        />
        <el-table-column
          label="采购单位"
          align="center"
          prop="purchaseDeptName"
          fixed="left"
          width="100"
        />
        <el-table-column
          label="项目名称"
          align="center"
          prop="projectName"
          fixed="left"
          width="200"
        />
        <el-table-column
          label="项目类型"
          align="center"
          prop="projectType"
          width="120"
        >
          <template #default="scope">
            <dict-tag
              :options="editDroppdown.projectTypes"
              :value="scope.row.projectType"
            />
          </template>
        </el-table-column>
        <el-table-column
          label="需求概况"
          align="center"
          prop="demandOverview"
          width="200"
          show-overflow-tooltip
        />
        <el-table-column
          label="概算金额(万元)"
          align="center"
          prop="evaluation"
          width="120"
        />
        <!-- <el-table-column label="是否为超限额计划" align="center" prop="isExcess" width="150" >
                    <template #default="scope">
                        <dict-tag :options="[{ label: '未超额', value: '0', elTagType: 'primary', elTagClass: '' }, 
                            { label: '超额', value: '1', listClass: 'danger', elTagType: 'error', elTagClass: '' }]" :value="scope.row.isExcess" />
                    </template>
                </el-table-column> -->
        <el-table-column
          label="采购服务站"
          align="center"
          prop="purchaseServicesStr"
          width="150"
        >
          <template #default="scope">
            {{
              scope.row.purchaseServicesStr ||
              scope.row.purchaseServicesName ||
              scope.row.purchaseServices
            }}
          </template>
        </el-table-column>
        <!-- <el-table-column label="采购方式" align="center" prop="purchaseMode" width="150" >
                    <template #default="scope">
                        <dict-tag :options="editDroppdown.planPurchaseModes" :value="scope.row.purchaseMode" />
                    </template>
                </el-table-column> -->
        <el-table-column
          label="计划提报需求时间"
          align="center"
          prop="planDemandSubTime"
          width="150"
        />
        <el-table-column
          label="计划提完成采购时间"
          align="center"
          prop="planPurchaseFinishTime"
          width="150"
        />
        <el-table-column
          label="计划交付(实施)时间"
          align="center"
          prop="planDeliverTime"
          width="150"
        />
        <el-table-column
          label="项目属性"
          align="center"
          prop="projectAttrName"
          width="200"
        >
          <template #default="scope">
            {{ scope.row.projectAttrName || scope.row.projectAttr }}
          </template>
        </el-table-column>
        <el-table-column
          label="备注"
          align="center"
          prop="remarks"
          width="300"
          show-overflow-tooltip
        />
        <el-table-column
          label="项目状态"
          align="center"
          prop="projectStatus"
          width="120"
          fixed="right"
        >
          <template #default="scope">
            <el-popover
              placement="right"
              :width="400"
              trigger="hover"
              v-if="scope.row.projectStatus == 1"
              @before-enter="() => showPopover(scope.row.planYearId)"
            >
              <template #reference>
                <dict-color-tag
                  :options="projectStatusConvert"
                  :value="scope.row.projectStatus"
                ></dict-color-tag>
              </template>
              <el-table :data="datapopover">
                <el-table-column width="80" type="index" label="序号" />
                <el-table-column
                  min-width="100"
                  property="deptName"
                  label="单位名称"
                />
                <el-table-column
                  width="100"
                  property="statusStr"
                  label="状态"
                />
              </el-table>
            </el-popover>

            <dict-color-tag
              :options="projectStatusConvert"
              :value="scope.row.projectStatus"
              v-else
            ></dict-color-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="380"
          fixed="right"
          class-name="small-padding fixed-width"
        >
          <template #default="scope">
            <el-button
              v-hasPermi="['plan:planYears:view']"
              type="default"
              size="small"
              @click="handleInfoShow(scope.row)"
              >查看</el-button
            >
            <el-button
              v-hasPermi="['plan:planYears:handleApp']"
              v-if="
                scope.row.appDelete === 'true' || scope.row.appDelete === true
              "
              type="success"
              size="small"
              @click="handleUndoApplyAction(scope.row.planYearId)"
              >申请撤销</el-button
            >
            <!-- <el-divider direction="vertical"></el-divider> -->
            <el-button
              v-hasPermi="['plan:planYears:reviewTo']"
              v-if="scope.row.flag == true"
              type="primary"
              size="small"
              @click="handleExamineShow(scope.row)"
              >审核</el-button
            >
            <!-- <el-divider direction="vertical"></el-divider> -->
            <!-- <el-button v-hasPermi="['plan:planYears:sendLetter']"
                            v-if="['3'].includes(scope.row.projectStatus)"
                            type="warning"
                            size="small"
                            @click="handleUrgeShow(scope.row)"
                        >发函催告</el-button> -->
            <el-button
              v-if="['3'].includes(scope.row.projectStatus)"
              type="success"
              size="small"
              @click="handleSupplementStationOpen(scope.row)"
              >填写采购服务站</el-button
            >
            <el-button
              v-if="['3'].includes(scope.row.projectStatus)"
              v-hasPermi="['plan:planYears:reviewReturn']"
              type="warning"
              size="small"
              @click="handleExamineRefuseShow(scope.row.planYearId)"
              >退回</el-button
            >
            <el-button
              v-hasPermi="['plan:planYears:delete']"
              type="danger"
              size="small"
              @click="handleDelete(scope.row)"
              >删除</el-button
            >
            <!-- <el-button
                            v-if="scope.row.appUpdate === true || scope.row.appUpdate === 'true'"
                            type="success"
                            size="small"
                            @click="handleApplyUpdateAction(scope.row.planPracticalId)"
                        >申请修改</el-button> -->
          </template>
        </el-table-column>
        <template v-slot:empty>
          <svg-icon icon-class="search-none" style="font-size: 64px" />
          <p>暂无数据</p>
        </template>
      </el-table>

      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </el-card>

    <el-dialog
      title="查看详情"
      v-model="infoModal.show"
      width="900px"
      align-center
      :close-on-click-modal="false"
      class="plan-year-info-modal"
    >
      <div style="padding: 10px 20px 30px 20px">
        <PlanYearInfo
          v-if="infoModal.id && infoModal.show"
          :recordId="infoModal.id"
        ></PlanYearInfo>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="infoModal.show = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog
      title="审核"
      v-model="examineModal.show"
      width="1100px"
      align-center
      :close-on-click-modal="false"
      class="plan-year-info-modal"
    >
      <div style="padding: 30px 20px">
        <div class="examine-steps">
          <div class="step-item" :class="{ actived: examineModal.step === 1 }">
            <div class="step-icon">
              <img
                :src="`/images/plan/icon-step-${
                  examineModal.step === 1 ? 'actived' : 'normal'
                }.png`"
              />
              <span>1</span>
            </div>
            <div class="text">查看审核内容</div>
          </div>
          <div class="step-line"></div>
          <div class="step-item" :class="{ actived: examineModal.step === 2 }">
            <div class="step-icon">
              <img
                :src="`/images/plan/icon-step-${
                  examineModal.step === 2 ? 'actived' : 'normal'
                }.png`"
              />
              <span>2</span>
            </div>
            <div class="text">查看审核内容</div>
          </div>
        </div>
        <PlanYearInfo
          v-if="examineModal.id"
          v-show="examineModal.step === 1"
          :recordId="examineModal.id"
          oper-type="examine"
          @on-project-type-change="handleExamineProjectTypeChange"
        ></PlanYearInfo>
        <div v-if="examineModal.step === 2">
          <el-upload
            v-model:file-list="fileList"
            class="upload-demo w-100"
            :action="globalConfig.base_url + '/common/upload'"
            :headers="fileHeaderParams"
            accept=".doc,.docx,.pdf,.xls,.xlsx"
            name="file"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :before-remove="beforeRemove"
            :on-exceed="handleExceed"
          >
            <el-button type="primary">点击上传</el-button>
            <template #tip>
              <div class="el-upload__tip ml10" style="display: inline-block">
                只能上传
                <span style="color: #e54242">doc/pdf/docx/xls/xlsx</span> 文件
                <!-- ，且不超过
                                <span style="color: #E54242">500kb</span> -->
              </div>
            </template>
          </el-upload>
        </div>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button
            v-if="examineModal.step === 1"
            @click="examineModal.show = false"
            >关闭</el-button
          >
          <el-button
            v-if="examineModal.step === 2"
            @click="examineModal.step = 1"
            >上一步</el-button
          >
          <el-button
            v-if="examineModal.step === 1"
            type="primary"
            @click="examineModal.step = 2"
            >下一步</el-button
          >
          <el-button
            v-if="examineModal.step === 2"
            type="warning"
            @click="handleExamineRefuseShow(examineModal.id)"
            >退回</el-button
          >
          <el-button
            v-if="examineModal.step === 2"
            type="success"
            @click="handlePassAction"
            >审核通过</el-button
          >
        </div>
      </template>
    </el-dialog>

    <el-dialog
      title="退回"
      v-model="refuseModal.show"
      width="580px"
      align-center
      :close-on-click-modal="false"
      class="plan-year-info-modal"
    >
      <div style="padding: 30px 20px">
        <div></div>
        <el-input
          type="textarea"
          v-model="refuseModal.refuseReason"
          :rows="15"
          placeholder="请填写退回原因"
          clearable
        />
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="refuseModal.show = false">关闭</el-button>
          <el-button type="primary" @click="handleRefuseAction"
            >确定退回</el-button
          >
        </div>
      </template>
    </el-dialog>

    <el-dialog
      title="填写服务站"
      v-model="supplementStationModal.show"
      width="380px"
      align-center
      :close-on-click-modal="false"
      class="plan-year-info-modal"
    >
      <div style="padding: 30px 20px">
        <el-select
          v-model="supplementStationModal.purchaseServices"
          placeholder="请选择采购服务站"
          style="width: 100%"
          clearable
        >
          <el-option
            v-for="item in editDroppdown.purchaseServices"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="supplementStationModal.show = false"
            >关闭</el-button
          >
          <el-button type="primary" @click="handleSupplementStationAction"
            >确定</el-button
          >
        </div>
      </template>
    </el-dialog>

    <global-delete
      v-model="deleteFlag"
      title="删除内容"
      subtitle="删除后该计划所有数据将强制删除"
      @on-confirm="handleDeleteYearPlan"
      @on-close="deleteFlag = false"
    ></global-delete>

    <el-dialog
      title="发函催告"
      v-model="urgeModal.show"
      width="390px"
      align-center
      :close-on-click-modal="false"
      class="plan-year-examine-urge-modal"
      style="margin-top: auto !important"
    >
      <div class="dialog_body">
        <!-- <img src="/images/common/icon-wraning-big.png" >
                <div class="msg">
                    确定进行一键催告吗?
                </div> -->
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
  
<script setup name="PlanYearExamine">
import { getToken } from "@/utils/auth";
import DictColorTag from "@/components/DictColorTag";
import {
  execYearDownLoaddate,
  adPurchaseServices,
  execYearExamineAppUpdate,
} from "@/api/plan/year";
import PlanYearInfo from "./components/common/info";
import SubmitModal from "./components/submit";
import { getConfig, refreshCache } from "@/api/system/config";
import { listDeptByUser } from "@/api/system/dept";

import GlobalDelete from "@/components/GlobalDelete";

import {
  queryYearPlanExamineList,
  queryYearPlanInfo,
  execYearPlanReviewReturn,
  execYearPlanDelete,
  execYearPlanReviewTo,
  execYearPlanSendLetter,
  queryYearPlanDorpdown,
  queryYearData,
  purchaseServices,
  execHandleAppDelete,
} from "@/api/plan/year";

const { proxy } = getCurrentInstance();

const advanced = ref(false);
const tableHeight = proxy.getInitTableHeight(-20);
const configList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const dateRange = ref([]);
const datapopover = ref([]);
const poperShow = ref(false);
let purchaseServicesOpt = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 隐藏搜索按钮操作 */
watch(showSearch, (value) => {
  let oldHeight = proxy.$refs.queryRef.$el.offsetHeight;
  if (value) {
    oldHeight = oldHeight - 12;
  } else {
    oldHeight = oldHeight + 12;
  }
  nextTick(
    () =>
      (tableHeight.value =
        proxy.$refs.tableRef.$el.offsetHeight -
        (proxy.$refs.queryRef.$el.offsetHeight - oldHeight))
  );
});

function fetchPurchaseServicesOpt(name) {
  const data = {
    projectType: "",
    evaluation: 0,
  };
  purchaseServices(data).then((response) => {
    purchaseServicesOpt.value = response.data;
  });
}

function showPopover(id) {
  const data = {
    refId: id,
    refType: "0",
  };
  queryYearData(data).then((response) => {
    console.log("response", response);
    datapopover.value = response.data;
  });
}

function downLoadExport() {
  execYearDownLoaddate({}).then((response) => {
    exportFile(response, "年度计划审核模板");
  });
}

function exportFile(obj, name = Date.now(), suffix = "xlsx") {
  const url = window.URL.createObjectURL(
    new Blob([obj], { type: "application/vnd.ms-excel" })
  );
  const aDOM = document.createElement("a");
  aDOM.style.display = "none";
  aDOM.href = url;
  const fileName = name + "." + suffix;
  aDOM.setAttribute("download", fileName);
  document.body.appendChild(aDOM);
  aDOM.click();
  document.body.removeChild(aDOM);
}

/** 展开按钮操作 */
function toggleAdvanced() {
  const oldHeight = proxy.$refs.queryRef.$el.offsetHeight;
  advanced.value = !advanced.value;
  nextTick(
    () =>
      (tableHeight.value =
        proxy.$refs.tableRef.$el.offsetHeight -
        (proxy.$refs.queryRef.$el.offsetHeight - oldHeight))
  );
}
/** 查询参数列表 */
function getList() {
  loading.value = true;
  queryYearPlanExamineList(
    proxy.addDateRange(queryParams.value, dateRange.value)
  ).then((response) => {
    configList.value = response.data.records;
    total.value = response.data.total;
    loading.value = false;
  });
}
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}
/** 表单重置 */
function reset() {
  form.value = {
    planYearId: undefined,
    purchaseServices: undefined,
    projectName: undefined,
    projectType: undefined,
    demandOverview: undefined,
    evaluation: undefined,
    purchaseMode: undefined,
    planDemandSubTime: undefined,
    planPurchaseFinishTime: undefined,
    planDeliverTime: undefined,
    projectAttr: undefined,
    remarks: undefined,
  };
  fileList.value = [];
  proxy.resetForm("planYearRef");
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = [];
  proxy.resetForm("queryForm");
  handleQuery();
}
/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.configId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

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
    0: { bgColor: "#EEF6F6", color: "#007871" },
    1: { bgColor: "#EEF6F6", color: "#007871" },
    2: { bgColor: "#FFF6F6", color: "#CA1919" },
    3: { bgColor: "#F3F9FF", color: "#006AEA" },
  };
  return (editDroppdown.value?.projectStatus || []).map((m) => {
    return { ...m, ..._projectStatusColor[m.value] };
  });
});

/** 导出按钮操作 */
function handleExport() {
  proxy.download1(
    "plan/planYears/exportPlanExamine",
    { ...queryParams.value, pageSize: 99999 },
    `年度计划审核_${new Date().getTime()}.xlsx`
  );
}

function handleApplyUpdateAction(id) {
  if (!id) return;
  return proxy.$modal
    .confirm(`是否通过该申请修改?`, {
      distinguishCancelAndClose: true,
      confirmButtonText: "通过",
      cancelButtonText: "拒绝",
    })
    .then(
      () => {
        proxy.$modal.loading("审核中, 请稍候...");
        execYearExamineAppUpdate({ planPracticalId: id, projectAttr: 1 })
          .then((response) => {
            proxy.$modal.closeLoading();
            proxy.$modal.msgSuccess("申请修改已通过");
            getList();
          })
          .catch((err) => {
            console.log(err);
            proxy.$modal.closeLoading();
          });
      },
      (action) => {
        if (action === "cancel") {
          proxy.$modal.loading("审核中, 请稍候...");
          execYearExamineAppUpdate({ planPracticalId: id, projectAttr: -1 })
            .then((response) => {
              proxy.$modal.closeLoading();
              proxy.$modal.msgSuccess("申请修改已拒绝");
              getList();
            })
            .catch((err) => {
              console.log(err);
              proxy.$modal.closeLoading();
            });
        }
      }
    );
}

const deleteFlag = ref(false);
const deleteFlagId = ref(undefined);

/** 删除按钮操作 */
function handleDelete(row) {
  deleteFlagId.value = row.planYearId;
  deleteFlag.value = true;
}

function handleDeleteYearPlan() {
  proxy.$modal.loading("数据删除中, 请稍候...");
  execYearPlanDelete({ planYearId: deleteFlagId.value })
    .then((response) => {
      proxy.$modal.closeLoading();
      proxy.$modal.msgSuccess("删除成功");
      deleteFlag.value = false;
      deleteFlagId.value = undefined;
      getList();
    })
    .catch((err) => {
      console.log(err);
      proxy.$modal.closeLoading();
    });
}

function handleUrgeAction() {
  if (!urgeModal.value.id) {
    proxy.$modal.msgError("参数不正确");
    return;
  }
  proxy.$modal.loading("正在进行发函催告, 请稍候...");
  execYearPlanSendLetter({
    planYearId: urgeModal.value.id,
    letterStr: urgeModal.value.letterStr,
  })
    .then((response) => {
      proxy.$modal.closeLoading();
      proxy.$modal.msgSuccess("发函催告成功");
      urgeModal.value = false;
      getList();
    })
    .catch((err) => {
      console.log(err);
      proxy.$modal.closeLoading();
    });
}

const infoModal = ref({
  show: false,
  id: undefined,
});

function handleInfoShow(row) {
  infoModal.value = {
    show: true,
    id: row.planYearId,
  };
}

const examineModal = ref({
  show: false,
  step: 1,
  id: undefined,
  projectType: undefined,
});

function handleExamineShow(row) {
  examineModal.value = {
    show: true,
    step: 1,
    id: row.planYearId,
    projectType: row.projectType,
  };
}

const refuseModal = ref({
  show: false,
  id: undefined,
  refuseReason: "",
});

function handleExamineRefuseShow(id) {
  examineModal.value.show = false;
  refuseModal.value = {
    show: true,
    id: id,
    refuseReason: "",
  };
}

function handleExamineProjectTypeChange(val) {
  examineModal.value.projectType = val;
}

function handleRefuseAction() {
  if (!refuseModal.value.refuseReason) {
    proxy.$modal.msgError("请输入退回原因");
    return;
  }
  proxy.$modal.loading("审核退回中, 请稍候...");
  execYearPlanReviewReturn({
    planYearId: refuseModal.value.id,
    refuseReason: refuseModal.value.refuseReason,
  })
    .then((response) => {
      proxy.$modal.closeLoading();
      proxy.$modal.msgSuccess("退回成功");
      refuseModal.value.show = false;
      getList();
    })
    .catch((err) => {
      console.log(err);
      proxy.$modal.closeLoading();
    });
}

function handlePassAction() {
  proxy.$modal.loading("审核中, 请稍候...");
  let _files = fileList.value
    .filter((f) => f.response)
    .map((m) => {
      let { fileId } = m.response.data;
      return {
        fileId: fileId,
        type: "1",
      };
    });
  execYearPlanReviewTo({
    planYearId: examineModal.value.id,
    projectType: examineModal.value.projectType,
    sysFileRefs: _files,
  })
    .then((response) => {
      proxy.$modal.closeLoading();
      proxy.$modal.msgSuccess("审核已通过");
      fileList.value = [];
      examineModal.value.show = false;
      getList();
    })
    .catch((err) => {
      console.log(err);
      proxy.$modal.closeLoading();
    });
}

const urgeModal = ref({
  show: false,
  id: undefined,
  letterStr: "",
});

function handleUrgeShow(row) {
  urgeModal.value = {
    show: true,
    id: row.planYearId,
    letterStr: "",
  };
}

const supplementStationModal = ref({
  show: false,
  id: undefined,
  purchaseServices: "",
});
function handleSupplementStationOpen(row) {
  supplementStationModal.value = {
    show: true,
    id: row.planYearId,
    purchaseServices: row.purchaseServices,
  };
}
function handleSupplementStationAction() {
  if (!supplementStationModal.value.purchaseServices) {
    proxy.$modal.msgError("请输入服务站名称");
    return;
  }
  proxy.$modal.loading("提交中, 请稍候...");
  console.log(
    "supplementStationModal.purchaseServices",
    supplementStationModal.value
  );
  adPurchaseServices({
    planYearId: supplementStationModal.value.id,
    purchaseServices: supplementStationModal.value.purchaseServices,
  })
    .then((response) => {
      proxy.$modal.closeLoading();
      proxy.$modal.msgSuccess("保存成功");
      supplementStationModal.value.show = false;
      getList();
    })
    .catch((err) => {
      console.log(err);
      proxy.$modal.closeLoading();
    });
}

const fileHeaderParams = ref({
  Authorization: "Bearer " + getToken(),
});

const fileList = ref([]);

function handleRemove(file, uploadFiles) {
  console.log(file, uploadFiles);
}

function handlePreview(uploadFile) {
  console.log(uploadFile);
}

function handleExceed(files, uploadFiles) {
  proxy.$modal.msgWarning(
    `The limit is 3, you selected ${files.length} files this time, add up to ${
      files.length + uploadFiles.length
    } totally`
  );
}

function beforeRemove(uploadFile, uploadFiles) {
  return proxy.$modal.confirm(`是否要删除文件: ${uploadFile.name} ?`).then(
    () => true,
    () => false
  );
}

function handleUndoApplyAction(id) {
  if (!id) return;
  return proxy.$modal
    .confirm(`是否通过该申请撤销?`, {
      distinguishCancelAndClose: true,
      confirmButtonText: "通过",
      cancelButtonText: "拒绝",
    })
    .then(
      () => {
        proxy.$modal.loading("审核中, 请稍候...");
        execHandleAppDelete({ planYearId: id, projectAttr: 1 })
          .then((response) => {
            proxy.$modal.closeLoading();
            proxy.$modal.msgSuccess("申请撤销已通过");
            getList();
          })
          .catch((err) => {
            console.log(err);
            proxy.$modal.closeLoading();
          });
      },
      (action) => {
        if (action === "cancel") {
          proxy.$modal.loading("审核中, 请稍候...");
          execHandleAppDelete({ planYearId: id, projectAttr: -1 })
            .then((response) => {
              proxy.$modal.closeLoading();
              proxy.$modal.msgSuccess("申请撤销已拒绝");
              getList();
            })
            .catch((err) => {
              console.log(err);
              proxy.$modal.closeLoading();
            });
        }
      }
    );
}

fetchPurchaseServicesOpt();
getList();

const unitDropdown = ref({});
function queryUnitDropDown() {
  listDeptByUser().then((response) => {
    // .filter(f => f.parentId !== 0)
    if (!response.data) response.data = [];
    response.data = [{ deptId: -1, deptName: "全部" }, ...response.data];
    unitDropdown.value.unitList = response.data;
  });
}
queryUnitDropDown();
</script>

<style lang="scss" scoped>
.app-container {
  :deep(.plan-year-info-modal) {
    padding: 0px 0px 10px 0px;
    border-radius: 10px;
    // margin-top: initial !important;
    .el-dialog__header {
      text-align: center;
      border-bottom: 0px;
    }
    .el-dialog__headerbtn {
      // top: 16px !important;
      // height: initial !important;
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

    .examine-steps {
      width: 400px;
      margin: 0 auto;
      display: flex;
      flex-direction: row;
      text-align: center;
      margin-bottom: 40px;
      .step-line {
        position: relative;
        top: 25px;
        width: 50px;
        height: 2px;
        background: #e1e1e1;
      }
      .step-item {
        cursor: pointer;
        flex: 1;
        .step-icon {
          display: inline-block;
          position: relative;
          span {
            position: absolute;
            left: 0px;
            right: 0px;
            top: 23px;
            text-align: center;
            color: #333;
            font-weight: bold;
          }
        }
        .text {
          margin-top: 10px;
          text-align: center;
        }
        &.actived {
          .step-icon {
            span {
              color: white;
            }
          }
          .text {
            color: #006aea;
          }
        }
      }
    }
  }

  :deep(.plan-year-examine-urge-modal) {
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
.aidex-table {
  .search_box {
    margin-top: 0px;
  }
  .btn_box {
    margin-top: 0px;
  }
}
</style>
  