<template>
  <div class="app-container search-table-box aidex-table">
    <el-card shadow="never">
      <template #header>
        <el-row>
          <el-col :span="8">
            <div class="btn_box" align="left" style="float: left">
              <el-button
                class="filter-item addGys_btn"
                style="margin-left: 8px"
                icon="Plus"
                @click="handleAdd"
                v-hasPermi="['base:agency:add']"
                >新增机构</el-button
              >
              <el-button
                class="filter-item cqzj_btn"
                style="margin-left: 8px"
                @click="handlePutJg"
                v-hasPermi="['base:agency:pageProjectInfo']"
                >抽取记录</el-button
              >
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <div class="search_box">
              <el-form :model="queryParams" ref="queryForm" label-width="10px">
                <el-row :gutter="16">
                  <el-col :md="5">
                    <el-form-item label="" prop="companyName">
                      <el-input
                        v-model="queryParams.companyName"
                        placeholder="请输入机构名称进行查询"
                        clearable
                        @keyup.enter="handleQuery"
                      />
                    </el-form-item>
                  </el-col>
                  <el-col :md="5">
                    <el-form-item label="" prop="companyNature">
                      <el-select
                        v-model="queryParams.companyNature"
                        placeholder="请选择单位性质"
                        style="width: 100%"
                        @change="handleQuery"
                        clearable
                      >
                        <el-option
                          v-for="dict in sys_company_nature"
                          :key="dict.value"
                          :label="dict.label"
                          :value="dict.label"
                        />
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :md="5">
                    <el-form-item label="" prop="status">
                      <el-select
                        v-model="queryParams.status"
                        placeholder="请选择机构状态"
                        style="width: 100%"
                        @change="handleQuery"
                        clearable
                      >
                        <el-option
                          v-for="dict in statusList"
                          :key="dict.value"
                          :label="dict.label"
                          :value="dict.value"
                        />
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :md="5">
                    <el-form-item label="" prop="status">
                      <el-select
                        v-model="queryParams.companyType"
                        placeholder="请选择采购品种"
                        style="width: 100%"
                        multiple
                        @change="handleQuery"
                        clearable
                      >
                        <el-option
                          v-for="dict in sys_project_type"
                          :key="dict.value"
                          :label="dict.label"
                          :value="dict.value"
                        />
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :md="3" align="right" style="margin-left: auto">
                    <el-form-item class="search_btn_box">
                      <el-button
                        class="filter-item search_btn"
                        type="primary"
                        icon="Search"
                        @click="handleQuery"
                        >搜索</el-button
                      >
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-form>
            </div>
          </el-col>
        </el-row>
      </template>
      <el-card shadow="never" class="table_card">
        <el-table
          stripe
          ref="tableRef"
          v-loading="loading"
          :data="noticeList"
          @selection-change="handleSelectionChange"
          highlight-current-row
          style="width: 100%"
          height="65vh"
        >
          <el-table-column
            label="序号"
            align="center"
            type="index"
            width="55"
            fixed="left"
          />
          <el-table-column
            label="单位名称"
            align="center"
            prop="companyName"
            width="170"
            fixed="left"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="采购品种"
            align="center"
            prop="companyTypeName"
            width="180"
          >
            <!-- <template #default="scope">
              <span v-if="scope.row.companyType == '0'">装备类</span>
              <span v-if="scope.row.companyType == '1'">物资类</span>
              <span v-if="scope.row.companyType == '2'">服务类</span>
              <span v-if="scope.row.companyType == '3'">工程类</span>
            </template> -->
          </el-table-column>

          <!-- <el-table-column
            label="采购品种"
            align="center"
            prop="companyType"
            width="180"
          >
            <template #default="scope">
              <dict-tag
                :options="queryParams.companyType"
                :value="scope.row.companyType"
              />
            </template>
          </el-table-column> -->

          <el-table-column
            label="注册时间"
            align="center"
            prop="registTime"
            width="130"
          >
            <template #default="scope">
              <span>{{ parseTime(scope.row.registTime, "{y}-{m}-{d}") }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="单位性质"
            align="center"
            prop="companyNature"
            width="140"
          />
          <el-table-column
            label="办公地址"
            align="center"
            prop="companyAddr"
            width="150"
          />
          <el-table-column
            label="业务联系人"
            align="center"
            prop="companyContacts"
            width="140"
          />
          <el-table-column
            label="联系电话"
            align="center"
            prop="contactsPhone"
            width="170"
          />
          <el-table-column
            label="主营范围"
            align="center"
            prop="businessScope"
            min-width="400"
            show-overflow-tooltip
          />
          <el-table-column
            label="状态"
            align="center"
            prop="status"
            width="100"
          >
            <template #default="scope">
              <span
                :style="{
                  color: scope.row.status == '0' ? '#006AEA' : '#DB0505',
                }"
              >
                {{ scope.row.status == "0" ? "白名单" : "黑名单" }}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="400"
            class-name="small-padding fixed-width"
            fixed="right"
          >
            <template #default="scope">
              <el-button
                type="text"
                class="lookDetil_btn"
                @click="handleLookDetail(scope.row)"
                v-hasPermi="['base:agency:query']"
                >查看</el-button
              >
              <el-button
                type="text"
                class="edit_btn"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['base:agency:edit']"
                >编辑</el-button
              >
              <el-button
                v-if="scope.row.status == '1'"
                type="text"
                class="removeHmd_btn"
                @click="handleYcHmd(scope.row)"
                v-hasPermi="['base:agency:operationBlacklist']"
                >移除黑名单</el-button
              >
              <el-button
                v-else
                type="text"
                class="joinHmd_btn"
                @click="handleJoinHmd(scope.row)"
                v-hasPermi="['system:agency:operationBlacklist']"
                >加入黑名单</el-button
              >
              <el-button
                type="text"
                class="delete_btn"
                @click="handleDelete(scope.row)"
                v-hasPermi="['base:agency:remove']"
                >删除</el-button
              >
            </template>
          </el-table-column>
          <template v-slot:empty>
            <svg-icon icon-class="search-none" style="font-size: 64px" />
            <p>暂无数据</p>
          </template>
        </el-table>
      </el-card>

      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />

      <!-- 查看详情 -->
      <el-dialog :title="title" v-model="open" width="720px" append-to-body>
        <div class="dialog_box">
          <el-form
            ref="noticeRef"
            :model="form"
            :rules="rules"
            label-position="top"
          >
            <el-row :gutter="24">
              <el-col :span="24">
                <el-form-item label="单位名称" prop="companyName">
                  <el-input
                    v-model="form.companyName"
                    placeholder="请输入单位名称"
                    :disabled="true"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="采购品种" prop="companyType">
                  <el-select
                    multiple
                    v-model="form.companyType"
                    placeholder="请选择采购品种"
                    :disabled="true"
                  >
                    <el-option
                      v-for="item in sys_project_type"
                      :key="item.label"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="注册时间" prop="registTime">
                  <el-date-picker
                    v-model="form.registTime"
                    type="date"
                    placeholder="选择日期"
                    value-format="YYYY-MM-DD "
                    :disabled="true"
                  >
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="单位性质" prop="companyNature">
                  <el-select
                    v-model="form.companyNature"
                    placeholder="请选择单位性质"
                    :disabled="true"
                  >
                    <el-option
                      v-for="item in sys_company_nature"
                      :key="item.label"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="办公地址" prop="companyAddr">
                  <el-input
                    v-model="form.companyAddr"
                    placeholder="请输入办公地址"
                    :disabled="true"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="业务联系人" prop="companyContacts">
                  <el-input
                    v-model="form.companyContacts"
                    placeholder="请输入业务联系人"
                    :disabled="true"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="联系电话" prop="contactsPhone">
                  <el-input
                    v-model="form.contactsPhone"
                    placeholder="请填写电话号码"
                    :disabled="true"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="主营范围" prop="businessScope">
                  <el-input
                    v-model="form.businessScope"
                    placeholder="请输入主营范围"
                    :disabled="true"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
        <template #footer>
          <div class="dialog-footer" style="text-align: center">
            <!-- <el-button type="primary" @click="submitForm">确 定</el-button> -->
            <el-button @click="cancel">关闭</el-button>
          </div>
        </template>
      </el-dialog>
      <!-- add&&edit -->
      <el-dialog :title="title" v-model="dialog" width="720px" append-to-body>
        <div class="dialog_box">
          <el-form
            ref="noticeEditRef"
            :model="editForm"
            :rules="editRules"
            label-position="top"
          >
            <el-row :gutter="24">
              <el-col :span="24">
                <el-form-item label="单位名称" prop="companyName">
                  <el-input
                    v-model="editForm.companyName"
                    placeholder="请输入单位名称"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="采购品种" prop="companyType">
                  <el-select
                    multiple
                    v-model="editForm.companyType"
                    placeholder="请选择采购品种"
                  >
                    <el-option
                      v-for="item in sys_project_type"
                      :key="item.label"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="注册时间" prop="registTime">
                  <el-date-picker
                    v-model="editForm.registTime"
                    type="date"
                    placeholder="选择日期"
                    value-format="YYYY-MM-DD"
                  >
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="单位性质" prop="companyNature">
                  <el-select
                    v-model="editForm.companyNature"
                    placeholder="请选择单位性质"
                  >
                    <el-option
                      v-for="item in sys_company_nature"
                      :key="item.label"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="办公地址" prop="companyAddr">
                  <el-input
                    v-model="editForm.companyAddr"
                    placeholder="请输入办公地址"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="业务联系人" prop="companyContacts">
                  <el-input
                    v-model="editForm.companyContacts"
                    placeholder="请输入业务联系人"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="联系电话" prop="contactsPhone">
                  <el-input
                    v-model="editForm.contactsPhone"
                    placeholder="请填写电话号码"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="主营范围" prop="businessScope">
                  <el-input
                    v-model="editForm.businessScope"
                    placeholder="请输入主营范围"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
        <template #footer>
          <div class="dialog-footer">
            <el-button type="primary" @click="submitEditForm">确 定</el-button>
            <el-button @click="cancel">取 消</el-button>
          </div>
        </template>
      </el-dialog>
      <!-- 招标代理机构抽取 -->
      <el-dialog
        title="抽取招标代理机构"
        width="1100"
        v-model="dialogDlJgListDialog"
      >
        <el-row :gutter="16" style="margin: 30px 30px 0 12px">
          <el-col :md="6">
            <el-form-item label="" prop="name">
              <el-input
                v-model="projectQueryParams.name"
                placeholder="请输入项目名称进行查询"
              />
            </el-form-item>
          </el-col>
          <e-col :md="6">
            <el-button type="primary" icon="Search" @click="handleSearch"
              >查询</el-button
            >
          </e-col>
        </el-row>
        <el-row :gutter="16">
          <!-- <el-button
            class="cqzj_btn"
            style="margin: 0 0 30px 45px"
            @click="qcStart"
            >开始抽取</el-button
          > -->
        </el-row>
        <el-table
          stripe
          ref="tableRef"
          v-loading="loading"
          :data="projectList"
          @selection-change="handleSelectionChange"
          highlight-current-row
          style="height: 450px; padding: 0 20px"
        >
          <el-table-column
            label="序号"
            align="center"
            type="index"
            width="55"
          />
          <el-table-column
            label="项目名称"
            align="center"
            prop="projectName"
            :show-overflow-tooltip="true"
            width="200"
          />
          <el-table-column
            label="采购单位"
            align="center"
            prop="purchaseDeptName"
            width="100"
          />
          <el-table-column
            label="项目属性"
            align="center"
            prop="projectAttrStr"
            width="120"
          />
          <el-table-column
            label="采购品种"
            align="center"
            prop="projectTypeStr"
            width="100"
          />
          <el-table-column
            label="概算金额(万元)"
            align="center"
            prop="evaluation"
            width="120"
          ></el-table-column>
          <el-table-column
            label="抽取招标代理机构"
            align="center"
            prop="agencyName"
            width="200"
          />
          <el-table-column
            label="抽取时间"
            align="center"
            prop="extractAgencyTime"
            width="160"
          />
          <template v-slot:empty>
            <svg-icon icon-class="search-none" style="font-size: 64px" />
            <p>暂无数据</p>
          </template>
        </el-table>
        <pagination
          v-show="total > 0"
          :total="total"
          v-model:page="projectQueryParams.pageNum"
          v-model:limit="projectQueryParams.pageSize"
          @pagination="handlePutJg"
        />
        <template #footer>
          <div class="dialog-footer">
            <el-button type="primary" @click="submitEditForm">确 定</el-button>
            <el-button @click="cancel">取 消</el-button>
          </div>
        </template>
      </el-dialog>
      <!-- 开始抽取 -->
      <el-dialog
        title="开始抽取"
        v-model="dialogCqData"
        width="420px"
        append-to-body
      >
        <div class="dialog_box">
          <el-form
            ref="noticeRefs"
            :model="form"
            :rules="rules"
            label-position="center"
          >
            <el-row :gutter="24">
              <el-col :span="24">
                <el-form-item label="项目名称" prop="demandId">
                  <el-select
                    v-model="queryParamsExtraction.demandId"
                    placeholder="请选择项目名称"
                    style="width: 100%"
                    @change="handleQueryExt"
                    clearable
                  >
                    <el-option
                      v-for="dict in projectList"
                      :key="dict.demandId"
                      :label="dict.purchaseDeptName"
                      :value="dict.demandId"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
        <template #footer>
          <div class="dialog-footer">
            <el-button type="primary" @click="submitFormExtraction"
              >确 定</el-button
            >
            <el-button @click="cancel">取 消</el-button>
          </div>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>
<script setup name="biddingAgency">
import {
  getBiddingList,
  operationBidding,
  addBidding,
  removeItem,
  editBidding,
  getProjectList,
  getExtraction,
} from "@/api/basicDataManagement/biddingAgency";
// import { listNotice, getNotice, delNotice, addNotice, updateNotice } from "@/api/system/notice";

const { proxy } = getCurrentInstance();
const {
  sys_notice_status,
  sys_notice_type,
  sys_company_nature,
  sys_purchase_type,
  sys_project_type,
} = proxy.useDict(
  "sys_notice_status",
  "sys_notice_type",
  "sys_company_nature",
  "sys_purchase_type",
  "sys_project_type"
);

const advanced = ref(false);
const tableHeight = proxy.getInitTableHeight();
const noticeList = ref([]);
const projectList = ref([]);
const getsXCqData = ref([]);
const open = ref(false);
const dialog = ref(false);
const dialogDlJgListDialog = ref(false);
const dialogCqData = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const needRowData = ref({});
const companyTypeData = ref("");
const companyNatureData = ref("");
const statusList = ref([
  {
    value: 0,
    label: "白名单",
  },
  {
    value: 1,
    label: "黑名单",
  },
]);

const data = reactive({
  form: {},
  editForm: {},
  zbdljgFrom: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    companyName: undefined,
    companyNature: undefined,
    status: undefined,
    companyType: undefined,
  },
  queryParamsExtraction: {
    demandId: undefined,
  },
  projectQueryParams: {
    pageNum: 1,
    pageSize: 10,
    name: undefined,
  },
  rules: {
    noticeTitle: [
      { required: true, message: "公告标题不能为空", trigger: "blur" },
    ],
    noticeType: [
      { required: true, message: "公告类型不能为空", trigger: "change" },
    ],
  },
});

const editRules = ref({
  companyName: [{ required: true, message: "请填写单位名称", trigger: "blur" }],
  companyType: [
    {
      type: "array",
      required: true,
      message: "请选择采购品种",
      trigger: "change",
    },
  ],
  // companyNature: [
  //   { required: true, message: "请选择单位性质", trigger: "change" },
  // ],
  // companyAddr: [
  //   { required: true, message: "请填写办公地址", trigger: "blur" },
  // ],
  // registTime: [
  //   { required: true, message: "请选择注册时间", trigger: "change" },
  // ],
});

const {
  queryParams,
  projectQueryParams,
  queryParamsExtraction,
  form,
  rules,
  editForm,
  zbdljgFrom,
} = toRefs(data);

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
function getList() {
  loading.value = true;
  getBiddingList(queryParams.value).then((response) => {
    noticeList.value = response.data.records;
    total.value = response.data.total;
    loading.value = false;
  });
}
function handleLookDetail(detail) {
  console.log(detail);
  title.value = "查看招标代理机构";
  open.value = true;
  form.value.companyName = detail.companyName;
  form.value.registTime = detail.registTime;
  // (editForm.value.registTime = row.registTime),
  form.value.companyNature = detail.companyNature;
  form.value.companyAddr = detail.companyAddr;
  form.value.companyContacts = detail.companyContacts;
  form.value.contactsPhone = detail.contactsPhone;
  form.value.businessScope = detail.businessScope;
  sys_purchase_type.value.forEach((item) => {
    console.log(item, "ooo", detail.companyType);
    if (item.value == detail.companyType) {
      companyTypeData.value = item.label;
    }
  });
  sys_company_nature.value.forEach((item) => {
    if (item.value == detail.companyNature) {
      companyNatureData.value = item.label;
    }
  });
  // for (var i = 0; i < sys_purchase_type.value.length; i++) {
  //   if (row.companyType == sys_purchase_type.value[i].value) {
  //     editForm.value.companyType = sys_purchase_type.value[i].label;
  //   }
  // }
  form.value.companyType = detail.companyType.split(",");
  // form.value.registTime = detail.registTime;
  console.log(form.value.registTime, detail.registTime, "2323");
}
function handleRemove(row) {
  const params = {
    id: row.id,
    status: row.status,
  };
  // proxy.$modal
  //   .confirm("是否确认删除公告编号为的数据项？")
  //   .then(function () {
  //     return operationBidding(params);
  //   })
  //   .then(() => {
  //     getList();
  //     proxy.$modal.msgSuccess("删除成功");
  //   })
  //   .catch(() => {});
}

/** 取消按钮 */
function cancel() {
  open.value = false;
  dialog.value = false;
  dialogDlJgListDialog.value = false;
  dialogCqData.value = false;
  reset();
}
/** 表单重置 */
function reset() {
  form.value = {
    companyName: undefined,
    registTime: undefined,
    companyNature: undefined,
    companyAddr: undefined,
    companyContacts: undefined,
    contactsPhone: undefined,
    businessScope: undefined,
  };
  editForm.value = {
    registTime: undefined,
    companyName: undefined,
    // registTime: undefined,
    companyNature: undefined,
    companyAddr: undefined,
    companyContacts: undefined,
    contactsPhone: undefined,
    businessScope: undefined,
    companyType: undefined,
    companyType: [],
    status: 0,
    cgpz: undefined,
    updated: 1,
  };
  zbdljgFrom.value = {
    name: undefined,
  };
  proxy.resetForm("noticeRef");
}
function handlePutJg() {
  loading.value = true;
  getProjectList(projectQueryParams.value).then((response) => {
    projectList.value = response.data.records;
    total.value = response.data.total;
    loading.value = false;
  });
  dialogDlJgListDialog.value = true;
  // dialogDlJgList.title='抽取招标代理机构'
}
function handleQueryExt(detail) {
  console.log(detail, "6666666666");
}
function submitFormExtraction(detail) {
  getExtraction(detail.detail).then((response) => {
    console.log(response, "确认抽取后");
    if (response.code == 200) {
      proxy.$modal.msgSuccess("操作成功");
      dialogCqData.value = false;
    }
  });
}
function qcStart() {
  dialogCqData.value = true;
  dialogDlJgListDialog.value = false;
  // getsXCqData.value=projectList.value
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
function handleSearch() {
  projectQueryParams.value.pageNum = 1;
  handlePutJg();
}
/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryForm");
  handleQuery();
}
/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.noticeId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}
/** 新增按钮操作 */
function handleAdd() {
  reset();
  dialog.value = true;
  title.value = "新增招标代理机构";
}
/**修改按钮操作 */
function handleUpdate(row) {
  reset();
  dialog.value = true;
  title.value = "编辑招标代理机构";
  editForm.value.id = row.id;
  (editForm.value.companyName = row.companyName),
    (editForm.value.companyType = row.companyType.split(",")),
    (editForm.value.registTime = row.registTime),
    (editForm.value.companyNature = row.companyNature),
    (editForm.value.companyAddr = row.companyAddr),
    (editForm.value.companyContacts = row.companyContacts),
    (editForm.value.contactsPhone = row.contactsPhone),
    (editForm.value.businessScope = row.businessScope),
    (editForm.value.status = 1);
  // for (var i = 0; i < sys_purchase_type.value.length; i++) {
  //   if (row.companyType == sys_purchase_type.value[i].value) {
  //     editForm.value.companyType = sys_purchase_type.value[i].label;
  //   }
  // }
  console.log(editForm.value);
}
/** 查看详情提交按钮 */
function submitForm() {
  open.value = false;
}
// add&edit提交按钮
function submitEditForm() {
  sys_purchase_type.value.forEach((item) => {
    if (item.label == editForm.value.companyType) {
      companyTypeData.value = item.value;
    }
  });
  // sys_company_nature.value.forEach((item) => {
  //   console.log(item.value, editForm.value.companyNature)
  //   if (item.value == editForm.value.companyNature) {
  //     companyNatureData.value = item.label;
  //   }
  // });
  proxy.$refs["noticeEditRef"].validate((valid) => {
    if (valid) {
      if (editForm.value.id != undefined) {
        // form.value.companyType = detail.companyType;
        let params = {
          id: editForm.value.id,
          companyName: editForm.value.companyName,
          companyType: editForm.value.companyType.join(","),
          registTime: editForm.value.registTime,
          companyNature: editForm.value.companyNature,
          companyAddr: editForm.value.companyAddr,
          companyContacts: editForm.value.companyContacts,
          contactsPhone: editForm.value.contactsPhone,
          businessScope: editForm.value.businessScope,
          status: 0,
        };
        editBidding(params).then((response) => {
          proxy.$modal.msgSuccess("修改成功");
          dialog.value = false;
          getList();
        });
      } else {
        let params = {
          id: editForm.value.id,
          companyName: editForm.value.companyName,
          companyType: editForm.value.companyType.join(","),
          registTime: editForm.value.registTime,
          companyNature: editForm.value.companyNature,
          companyAddr: editForm.value.companyAddr,
          companyContacts: editForm.value.companyContacts,
          contactsPhone: editForm.value.contactsPhone,
          businessScope: editForm.value.businessScope,
          status: 1,
        };
        console.log(params, "params=======");
        addBidding(params).then((response) => {
          proxy.$modal.msgSuccess("新增成功");
          dialog.value = false;
          getList();
        });
      }
    }
  });
}
/** 删除按钮操作 */
function handleDelete(row) {
  proxy.$modal
    .confirm("是否确认删除所选数据项？")
    .then(function () {
      return removeItem(row.id);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}

function handleYcHmd(row) {
  proxy.$modal
    .confirm("是否确认移除黑名单？")
    .then(function () {
      return operationBidding({ id: row.id, status: 0 });
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("成功移除黑名单");
    })
    .catch(() => {});
}
function handleJoinHmd(row) {
  proxy.$modal
    .confirm("是否确认加入黑名单？")
    .then(function () {
      return operationBidding({ id: row.id, status: 1 });
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("成功加入黑名单");
    })
    .catch(() => {});
}

getList();
</script>
<style lang="scss">
.cqzj_btn {
  &:hover {
    color: #fff;
  }
}
</style>
