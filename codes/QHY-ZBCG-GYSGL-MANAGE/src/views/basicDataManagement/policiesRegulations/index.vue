<template>
  <div class="app-container search-table-box aidex-table">
    <el-card shadow="never">
      <template #header>
        <el-row>
          <el-col :span="16">
            <div class="btn_box" align="left" style="float: left">
              <el-button
                class="filter-item addGys_btn"
                style="margin-left: 8px"
                icon="Plus"
                @click="handleAdd"
                v-hasPermi="['base:policy:add']"
                >新增法规</el-button
              >
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <div class="search_box">
              <el-form :model="queryParams" ref="queryForm" label-width="10px">
                <el-row :gutter="16">
                  <el-col :md="6">
                    <el-form-item label="" prop="name">
                      <el-input
                        v-model="queryParams.name"
                        placeholder="请输入法律条文或内容进行查询"
                        clearable
                        @keyup.enter="handleQuery"
                      />
                    </el-form-item>
                  </el-col>
                  <el-col :md="6">
                    <el-form-item label="" prop="companyNature">
                      <el-date-picker
                        v-model="dateRange"
                        value-format="YYYY-MM-DD"
                        type="daterange"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                      >
                      </el-date-picker>
                    </el-form-item>
                  </el-col>
                  <el-col :md="6" align="left">
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
      <div
        class=""
        style="display: flex; flex-wrap: wrap; justify-content: flex-start"
      >
        <div class="tablsDataList" :key="item.id" v-for="item in noticeList">
          <div class="noticeItem">
            <div class="itemSty">
              <div class="mainCount">
                <span class="titleLitt">法律条文名称</span>
                <p class="contLitt">{{ item.name }}</p>
                <span class="titleLitt">发文字号</span>
                <p class="contLitta">{{ item.issuedNumber }}</p>
                <div class="" style="display: flex">
                  <span class="titleLitt">发布机关</span>
                  <div class="fbjg">{{ item.issuingAuthority }}</div>
                </div>
                <div class="data">
                  <div class="data_left">
                    <span class="titleLitt">发布日期:</span>
                    <p class="contLitt_data">{{ item.releaseTime }}</p>
                  </div>
                  <div class="data_right">
                    <span class="titleLitt">收录日期:</span>
                    <p class="contLitt_data">{{ item.includedTime }}</p>
                  </div>
                </div>
              </div>
              <div class="btnList" style="min-height: 40px">
                <el-switch
                  v-hasPermi="['base:policy:update']"
                  v-model="item.state"
                  active-color="#006AEA"
                  active-value="1"
                  inactive-value="0"
                  inactive-color="#CDCDCD"
                  class="main-el-switch"
                  @click="handleStatusChange(item)"
                  width="60"
                  active-text="在用"
                  inactive-text="止用"
                >
                </el-switch>
                <el-button
                  type="text"
                  class="lookDetil_btn"
                  @click="handleSearLok(item)"
                  v-hasPermi="['base:policy:query']"
                  >查看</el-button
                >
                <el-button
                  type="text"
                  class="delete_btn"
                  @click="handleDelete(item)"
                  v-hasPermi="['system:notice:remove']"
                  >删除</el-button
                >
              </div>
            </div>
          </div>
        </div>
      </div>

      <pagination
        style="margin-top: 80px"
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />

      <!-- 添加或修改公告对话框 -->
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
                <el-form-item label="法规名称" prop="name">
                  <el-input v-model="form.name" placeholder="请输入法规名称" />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="发文字号" prop="issuedNumber">
                  <el-input
                    v-model="form.issuedNumber"
                    placeholder="请输入发文字号"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="发布机关" prop="issuingAuthority">
                  <el-input
                    v-model="form.issuingAuthority"
                    placeholder="请输入发布机关"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="发布日期" prop="releaseTime">
                  <el-date-picker
                    v-model="form.releaseTime"
                    :value-format="'YYYY-MM-DD'"
                    format="YYYY-MM-DD"
                    type="date"
                    placeholder="选择日期"
                  >
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="收录日期" prop="includedTime">
                  <el-date-picker
                    :value-format="'YYYY-MM-DD'"
                    format="YYYY-MM-DD"
                    v-model="form.includedTime"
                    type="date"
                    placeholder="选择日期"
                  >
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="法律条文内容" prop="">
                  <el-upload
                    v-model:file-list="fileList"
                    class="upload-demo w-100"
                    :action="globalConfig.base_url + '/base/policy/pdfUpload'"
                    :headers="fileHeaderParams"
                    multiple
                    :on-preview="handlePreview"
                    :on-remove="handleRemove"
                    :before-remove="beforeRemove"
                    :on-exceed="handleExceed"
                  >
                    <el-button type="primary">点击上传</el-button>
                    <template #tip>
                      <div
                        class="el-upload__tip ml10"
                        style="display: inline-block"
                      >
                        只能上传
                        <span style="color: #e54242">pdf</span>
                        格式文档
                        <!-- ，且不超过
                        <span style="color: #e54242">30M</span> -->
                      </div>
                    </template>
                  </el-upload>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
        <template #footer>
          <div class="dialog-footer">
            <el-button type="primary" @click="submitForm">确 定</el-button>
            <el-button @click="cancel">取 消</el-button>
          </div>
        </template>
      </el-dialog>
      <el-dialog
        v-model="detailOpen"
        title="查看详情"
        width="720px"
        append-to-body
      >
        <div class="">
          <iframe
            :src="nedJumpDra"
            frameborder="no"
            style="width: 100%; height: 800px"
          />
        </div>
      </el-dialog>
    </el-card>
  </div>
</template>
<script setup name="policiesRegulations">
import { getToken } from "@/utils/auth";
import {
  getRegulationList,
  deletePolice,
  addPolice,
  lookPolice,
  changePoliceStatu,
  changePoliceNotStatu,
} from "@/api/basicDataManagement/policiesRegulations";
// import { listNotice, getNotice, delNotice, addNotice, updateNotice } from "@/api/system/notice";

const { proxy } = getCurrentInstance();
const { sys_notice_status, sys_notice_type } = proxy.useDict(
  "sys_notice_status",
  "sys_notice_type"
);

const advanced = ref(false);
const tableHeight = proxy.getInitTableHeight();
const noticeList = ref([]);
const open = ref(false);
const detailOpen = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const dateRange = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const value = ref(false);
const title = ref("");
const nedJumpDra = ref("");
const nedgetStateValue = ref("");
const fileList = ref([]);
// const state = ref(0);
const fileHeaderParams = ref({
  Authorization: "Bearer " + getToken(),
});

const data = reactive({
  form: {},

  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: undefined,
    startTime: undefined,
    entTime: undefined,
    fileName: undefined,
    fileUrl: undefined,
    esid: undefined,
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
function getList() {
  loading.value = true;
  if (dateRange.value && dateRange.value.length) {
    queryParams.value.startTime = dateRange.value[0];
    queryParams.value.endTime = dateRange.value[1];
  } else {
    queryParams.value.startTime = "";
    queryParams.value.endTime = "";
  }
  console.log(queryParams.value, "请求参数123");

  getRegulationList(queryParams.value, dateRange.value).then((response) => {
    noticeList.value = response.data.records;
    total.value = response.data.total;
    loading.value = false;
  });
}
function handleStatusChange(item) {
  console.log(item.state, "item;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
  // changePoliceNotStatu({ id: item.id }).then((response) => {
  //   if (response.code == 200) {
  //     proxy.$modal.msgSuccess("修改成功");
  //     // getList();
  //   }
  // });
  if (item.state == 0) {
    proxy.$modal
      .confirm("是否确认修改为止用状态？")
      .then(function () {
        return changePoliceNotStatu({ id: item.id });
      })
      .then(() => {
        getList();
        proxy.$modal.msgSuccess("成功修改为止用状态");
      })
      .catch(() => {});
  } else {
    proxy.$modal
      .confirm("是否确认修改为在用状态？")
      .then(function () {
        return changePoliceStatu({ id: item.id });
      })
      .then(() => {
        getList();
        proxy.$modal.msgSuccess("成功修改为在用状态");
      })
      .catch(() => {});
  }
}
function handleSearLok(item) {
  console.log(
    item,
    "item------------------------------------------------------------------------------------------------"
  );
  detailOpen.value = true;
  let params = item.id;
  lookPolice(params).then((res) => {
    nedJumpDra.value = res.data.fileUrl;
    // nedState.value = res.data.state;
  });
}
function handlePreview() {}
function handleRemove() {}
function beforeRemove(uploadFile, uploadFiles) {
  return proxy.$modal.confirm(`是否要删除文件: ${uploadFile.name} ?`).then(
    () => true,
    () => false
  );
}

function handleExceed(files, uploadFiles) {
  proxy.$modal.msgWarning(
    `The limit is 3, you selected ${files.length} files this time, add up to ${
      files.length + uploadFiles.length
    } totally`
  );
}
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}
/** 表单重置 */
function reset() {
  form.value = {
    name: undefined,
    issuedNumber: undefined,
    issuingAuthority: undefined,
    releaseTime: undefined,
    includedTime: undefined,
    fileName: undefined,
    fileUrl: undefined,
    esId: undefined,
  };
  fileList.value = undefined;
  proxy.resetForm("noticeRef");
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
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
  open.value = true;
  title.value = "新增法规";
}
/**修改按钮操作 */
function handleUpdate(row) {
  reset();
  const noticeId = row.noticeId || ids.value;
  getNotice(noticeId).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = "修改公告";
  });
}
/** 提交按钮 */
function submitForm() {
  proxy.$refs["noticeRef"].validate((valid) => {
    let _files = fileList.value
      .filter((f) => f.response)
      .map((m) => {
        let { esId, fileName, url } = m.response;
        form.value.fileName = fileName;
        form.value.fileUrl = url;
        form.value.esId = esId;
      });
    if (valid) {
      if (form.value.id != undefined) {
        updateNotice(form.value, _files).then((response) => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addPolice(form.value, _files).then((response) => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}
/** 删除按钮操作 */
function handleDelete(item) {
  let idArray = [];
  idArray.push(item.id);
  proxy.$modal
    .confirm("是否确认所选数据项？")
    .then(function () {
      return deletePolice(idArray);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}

getList();
</script>
<style lang="scss">
.tablsDataList {
}
.noticeItem {
  flex-wrap: wrap;
  margin-left: 10px;
}
.itemSty {
  flex: 1;
  background: #f8f8f8;
  border-radius: 10px;
  margin: 5px;
  width: 294px;
  &:hover {
    background: #fff;
    box-shadow: 1px 1px 10px rgba(0, 0, 0, 0.2);
  }
}
.titleLitt {
  color: #999999;
  font-size: 12px;
}
.contLitt {
  color: #333333;
  font-size: 16px;
}
.contLitta {
  color: #333333;
  font-size: 16px;
  display: block;
  height: 20px;
}
.contLitt_data {
  color: #333333;
  font-size: 12px;
}
.fbjg {
  background: #e8eef4;
  color: #003f8a;
  font-size: 12px;
  line-height: 20px;
  padding: 0 10px;
  margin-left: 5px;
}
.data {
  margin-top: 15px;
  display: flex;
  justify-content: space-between;
}
.mainCount {
  border-bottom: 1px solid #e5e5e5;
  padding: 25px;
}
.btnList {
  margin: 10px;
  text-align: right;
  padding-bottom: 10px;
}

.main-el-switch {
  float: left;
  .el-switch__label.is-active {
    color: #fff !important;
    line-height: 30px;
  }
  position: relative;
  .el-switch__core {
    height: 24px;
    border-radius: 12px;
    &:after {
      left: 4px;
      top: 3px;
    }
  }
  &.el-switch {
    &.is-checked {
      .el-switch__core {
        &:after {
          margin-left: -20px;
          left: 100%;
        }
      }
    }
  }
  &.is-checked {
    .el-switch__label--left {
      opacity: 0;
    }
    .el-switch__label--right {
      opacity: 1;
    }
  }
  .el-switch__label {
    position: absolute;
    top: 0;
  }
  .el-switch__label--left {
    right: 0;
    color: #999;
    z-index: 1;
    margin-right: 8px;
  }
  .el-switch__label--right {
    left: 0;
    color: #fff;
    opacity: 0;
    margin-left: 8px;
  }
}
</style>
