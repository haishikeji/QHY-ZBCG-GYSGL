<template>
  <div class="app-container search-table-box aidex-table">
    <el-card shadow="never" style="height: calc(100% - 70px)">
      <div class="content">
        <div class="left">
          <div style="padding: 12px 22px 10px; margin-bottom: 10px">
            <el-input
              style="width: 435px; height: 40px"
              v-model="queryParams.professionalName"
              placeholder="请输入"
              clearable
            />
            <!-- prefix-icon="Search" -->
            <el-button
              @click="handleQuery"
              class="search_btn"
              icon="Search"
              style="margin-left: 10px"
              >搜索</el-button
            >
          </div>
          <div class="" style="display: flex; justify-content: space-between">
            <div class="">
              <el-button v-hasPermi="['base:professional:insertProfessional']"
                @click="addZy"
                class="addZy_btn"
                style="margin-left: 20px"
                >新增专业</el-button
              >
              <el-button v-hasPermi="['base:professional:insertProfessional']" class="addXj_btn" @click="addXj">新增下级</el-button>
              <el-button v-hasPermi="['base:professional:downloaExcel']" class="addXj_btn" @click="addXjData(item, $event)"
                >下载模板</el-button
              >

              <el-button v-hasPermi="['base:professional:deleteIds']" class="deletePl_btn" @click="handleRemoveArr"
                >批量删除</el-button
              >
            </div>
            <div class="">
              <el-button v-hasPermi="['base:professional:importBaseProfessional']" class="loadInBtn" @click="uploadIn"
                >导入专业</el-button
              >
              <el-button v-hasPermi="['base:professional:exportBaseProfessional']" class="loadOutBtn" @click="uploadOut"
                >导出专业</el-button
              >
            </div>
          </div>
        </div>
        <div class="mainCont" style="display: flex">
          <div class="main_left">
            <el-tree
              show-checkbox
              ref="treeData"
              @node-click="menuNodeClick"
              :check-on-click-node="false"
              :default-checked-keys="checkdKeys"
              :default-expanded-keys="defaultExpandedKey"
              @check="handleCkeckChange"
              :check-strictly="true"
              :current-node-key="value"
              style="
                background: #f5f5f5;
                margin-top: 20px;
                padding-bottom: 100px;
                height: 500px;
                overflow-y: scroll;
              "
              :data="menuOptions"
              node-key="id"
              default-expand-all
              empty-text="加载中，请稍候"
              :props="{
                label: 'professionalName',
                children: 'baseProfessionalVoList',
              }"
            ></el-tree>
          </div>
          <div class="right">
            <div class="basicInformation">
              <div
                class=""
                style="
                  width: 100px;
                  height: 20px;
                  mrgin-top: 100px;
                  border-left: 5px solid #006aea;
                  display: inline-block;
                  position: relative;
                  top: 20px;
                  left: 20px;
                "
              >
                基本信息
              </div>
              <div style="padding: 100px">
                <el-form ref="userRef" :model="form" :rules="rules">
                  <el-form-item label="一级分类" label-width="100px">
                    <el-select
                      v-model="formInformation.professionalGrade"
                      placeholder="请选择一级分类"
                      size="medium"
                    >
                      <el-option
                        v-for="dict in one_grede"
                        :key="dict.value"
                        :label="dict.label"
                        :value="dict.value"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="编码" label-width="100px">
                    <el-input
                      v-model="formInformation.professionalCode"
                      placeholder="请输入编码"
                      maxlength="30"
                    />
                  </el-form-item>
                  <el-form-item label="品目名称" label-width="100px">
                    <el-input
                      v-model="formInformation.professionalName"
                      placeholder="请输入品目名称"
                      maxlength="30"
                    />
                  </el-form-item>
                  <el-form-item label="说明" label-width="100px">
                    <el-input
                      v-model="formInformation.purchaseState"
                      placeholder="请输入说明"
                      maxlength="30"
                    />
                  </el-form-item>
                </el-form>
              </div>
            </div>
          </div>
        </div>
        <div  v-hasPermi="['base:professional:updateProfessional']"
          class=""
          style="width: 76%; text-align: right; margin: 70px; margin-left: 50px"
        >
          <el-button class="deleteBtn" @click="handleDelete">删除</el-button>
          <el-button class="resetBtn" @click="resetBtn">重置</el-button>
          <el-button class="saveBtn" @click="saveInformData">保存</el-button>
        </div>
      </div>
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
                <el-form-item label="一级分类" prop="professionalGrade">
                  <el-select
                    v-model="form.professionalGrade"
                    placeholder="请选择一级分类"
                    style="width: 100%"
                  >
                    <el-option
                      v-for="dict in one_grede"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="编码" prop="professionalCode">
                  <el-input
                    v-model="form.professionalCode"
                    placeholder="请输入编码"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="品目名称" prop="professionalName">
                  <el-input
                    v-model="form.professionalName"
                    placeholder="请输入品目名称"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="说明" prop="purchaseState">
                  <el-input
                    v-model="form.purchaseState"
                    placeholder="请输入说明"
                  />
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
      <!-- 用户导入对话框 -->
      <el-dialog
        :title="upload.title"
        v-model="upload.open"
        width="500px"
        append-to-body
      >
        <el-upload
          class="upload-model"
          ref="uploadRef"
          :limit="1"
          accept=".xlsx, .xls"
          :headers="upload.headers"
          :action="upload.url"
          :disabled="upload.isUploading"
          :on-progress="handleFileUploadProgress"
          :on-success="handleFileSuccess"
          :auto-upload="false"
          drag
        >
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <!-- <template #tip>
            <div class="el-upload__tip text-center">
                <div class="el-upload__tip">
                <el-checkbox
                    v-model="upload.updateSupport"
                />是否更新已经存在的用户数据
                </div>
                <span>仅允许导入xls、xlsx格式文件。</span>
                <el-link
                type="primary"
                :underline="false"
                style="font-size: 12px; vertical-align: baseline"
                @click="importTemplate"
                >下载模板</el-link
                >
            </div>
            </template> -->
        </el-upload>
        <template #footer>
          <div class="dialog-footer">
            <el-button type="primary" @click="submitFileForm">确 定</el-button>
            <el-button @click="upload.open = false">取 消</el-button>
          </div>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>
<script setup name="professionalLibrary">
import {
  getprofessionalList,
  addExpert,
  removeProfessiona,
  batchDelProfessiona,
  editProfessiona,
  downLoadBasic,
} from "@/api/basicDataManagement/professionalLibrary";
// import { listNotice, getNotice, delNotice, addNotice, updateNotice } from "@/api/system/notice";
import { getToken } from "@/utils/auth";
const { proxy } = getCurrentInstance();

const { sys_notice_status, one_grede } = proxy.useDict(
  "sys_notice_status",
  "one_grede"
);

const advanced = ref(false);
const tableHeight = proxy.getInitTableHeight();
const noticeList = ref([]);
const menuOptions = ref([]);
const removeArray = ref([]);
const defaultExpandedKey = ref([]);
const getDeleId=ref([])
const open = ref(false);
const addType=('')
const dialog = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const deptName = ref("");

const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const needIdData = ref("");
const getParentCodeValue = ref("");
const totalId = ref([]);
/*** 用户导入参数 */
const upload = reactive({
  // 是否显示弹出层（用户导入）
  open: false,
  roleOpen: false,
  // 弹出层标题（用户导入）
  title: "",
  // 是否禁用上传
  isUploading: false,
  // 是否更新已经存在的用户数据
  updateSupport: 0,
  // 设置上传的请求头部
  headers: { Authorization: "Bearer " + getToken() },
  // 上传的地址
  url:
    proxy.globalConfig.base_url + "/base/professional/importBaseProfessional",
});

const data = reactive({
  form: {},
  formInformation: {},
  editForm: {},
  queryParams: {
    // pageNum: 1,
    // pageSize: 10,
    // companyName: undefined,
    // companyNature: undefined,
    // status: undefined,
    professionalName: undefined,
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

const { queryParams, form, rules, editForm, formInformation } = toRefs(data);

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
function addZy() {
  open.value = true;
  title.value = "新增专业";
  addType.value=0
}
function addXj() {
  if (!getParentCodeValue.value||getDeleId.value.length==0) {
    proxy.$modal.msgError("请先选择父级");
  } else {
    open.value = true;
    title.value = "新增下级";
    addType.value=1
  }
}
function getList() {
  loading.value = true;
  getprofessionalList(queryParams.value).then((response) => {
    menuOptions.value = response.data;
    total.value = response.data.total;
    loading.value = false;
  });
}
watch(deptName, (val) => {
  proxy.$refs["tree"].filter(val);
});
function handleLookDetail(detail) {
  open.value = true;
  form.value.companyName = detail.companyName;
  form.value.registTime = detail.registTime;
  form.value.companyNature = detail.companyNature;
  form.value.companyAddr = detail.companyAddr;
  form.value.companyContacts = detail.companyContacts;
  form.value.contactsPhone = detail.contactsPhone;
  form.value.businessScope = detail.businessScope;
}
/**文件上传中处理 */
const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true;
};
/** 文件上传成功处理 */
const handleFileSuccess = (response, file, fileList) => {
  upload.open = false;
  upload.isUploading = false;
  proxy.$refs["uploadRef"].handleRemove(file);
  proxy.$alert(
    "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
      response.msg +
      "</div>",
    "导入结果",
    { dangerouslyUseHTMLString: true }
  );
  getList();
};
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
  form.value = {};
  reset();
}
/** 表单重置 */
function reset() {
  form.value = {
    professionalGrade: undefined,
    professionalCode: undefined,
    professionalName: undefined,
    purchaseState: undefined,
    parentCode: undefined,
  };
  formInformation.value = {
    professionalGrade: undefined,
    professionalCode: undefined,
    professionalName: undefined,
    purchaseState: undefined,
  };
  proxy.resetForm("noticeRef");
  editForm.value = {
    registTime: undefined,
    companyName: undefined,
    // registTime:undefined,
    companyNature: undefined,
    companyAddr: undefined,
    companyContacts: undefined,
    contactsPhone: undefined,
    businessScope: undefined,
    companyType: "1",
    status: "0",
    cgpz: undefined,
  };
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
  dialog.value = true;
  title.value = "新增招标代理机构";
}
/**修改按钮操作 */
function handleUpdate(row) {
  reset();
  dialog.value = true;
  title.value = "修改公告";
  editForm.value = row;
}
// function handleCkeckChange(val) {
//   console.log(val,'al6++++++++++++++');
//   needIdData.value=val.professionalCode
//   formInformation.value=val
// }
function menuNodeClick(detail) {
  console.log(
    detail,
    "detail000000..............................................."
  );
  needIdData.value = detail.professionalCode;
}
function handleCheckChange() {
  // const checkedNodes = this.$refs.treeData.getCheckedNodes()
  // console.log(checkedNodes,'checkedNodescheckedNodescheckedNodes');
}
function exportFile(data, type, fileName) {
  const blob = new Blob([data], {
    // type类型后端返回来的数据中会有，根据自己实际进行修改
    // 表格下载为 application/xlsx，压缩包为 application/zip等，
    type: "application/vnd.ms-excel",
  });
  const filename = fileName;
  if (typeof window.navigator.msSaveBlob !== "undefined") {
    window.navigator.msSaveBlob(blob, filename);
  } else {
    var blobURL = window.URL.createObjectURL(blob);
    // 创建隐藏<a>标签进行下载
    var tempLink = document.createElement("a");
    tempLink.style.display = "none";
    tempLink.href = blobURL;
    tempLink.setAttribute("download", filename);
    if (typeof tempLink.download === "undefined") {
      tempLink.setAttribute("target", "_blank");
    }
    document.body.appendChild(tempLink);
    tempLink.click();
    document.body.removeChild(tempLink); // 移除dom元素
    window.URL.revokeObjectURL(blobURL); // 释放bolb内存
  }
}
function addXjData(tab, event) {
  downLoadBasic({}).then((response) => {
    const fileName = "专业库模板";
    const contentType = "application/vnd.ms-excel";
    exportFile(response, contentType, fileName);
    // proxy.$download.zip(response);
    //   let blob = new Blob([response], {
    //     type: "application/octet-stream", // 后台返回 pdf 类型的文件，如果是其他文件，可以根据MIME表来选择对应的文件类型
    //   });
    //   const fileURL = URL.createObjectURL(blob);
    //   if (window.navigator.msSaveOrOpenBlob) {
    //     navigator.msSaveBlob(blob, getUrlData);
    //   } else {
    //     var link = document.createElement("a");
    //     link.href = window.URL.createObjectURL(blob);
    //     // link.download = getUrl;
    //     link.click();
    //     //释放内存
    //     window.URL.revokeObjectURL(link.href);
    //   }
  });
}
function uploadOut() {
  proxy.download1(
    "base/professional/exportBaseProfessional",
    { ...queryParams.value, pageSize: 99999 },
    `专业库_${new Date().getTime()}.xlsx`
  );
}
// add&edit提交按钮
function submitForm() {
  console.log(getDeleId.value,'............................');
  console.log(getParentCodeValue.value, "needIdData.valueneedIdData.value");
  proxy.$refs["noticeRef"].validate((valid) => {
    if (valid) {
      if (getDeleId.value.length==0) {
        form.value.parentCode = 0;
        addExpert(form.value).then((response) => {
          if (response.code == 200) {
            form.value == "";
            proxy.$modal.msgSuccess("新增成功");
            open.value = false;
            form.value = {};
            getList();
          }
        });
      } else {
        form.value.parentCode = getParentCodeValue.value;
        console.log(form.value.parentCode, "0000+++++++++++++++++++++++");
        addExpert(form.value).then((response) => {
          if (response.code == 200) {
            form.value == "";
            proxy.$modal.msgSuccess("新增成功");
            open.value = false;
            form.value = {};
          }
          getList();
        });
      }
    }
  });
}
/** 删除按钮操作 */
function handleDelete() {
  console.log(getDeleId.value[0].id, "needIdDataneedIdDataneedIdDataneedIdData");
  if(getDeleId.value.length==0){
    proxy.$modal.msgError("请先选择需要删除的数据")
  }else{
    let params = {
    id: getDeleId.value[0].id,
  };
  console.log(params, "parasmmsmsmsmsm");
  proxy.$modal
    .confirm("是否确认删除所选数据项？")
    .then(function () {
      return removeProfessiona(params);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
  }
}
function saveInformData() {
  editProfessiona(formInformation.value).then((response) => {
    if (response.code == 200) {
      proxy.$modal.msgSuccess("操作成功");
      getList();
    }
  });
}

function uploadIn() {
  upload.open = true;
}
function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
}
// 获取已选中数据ID
function handleCkeckChange(val, checked) {
  console.log(checked, "checkedcheckedchecked");
  if (checked == false) {
    this.$refs.treeData.setCheckedKeys([]);
    formInformation.value={}
  } else {
    formInformation.value=checked.checkedNodes[0]
    const resourceArr = proxy.$refs.treeData
      .getCheckedNodes()
      .filter((item) => {
        return item;
      });
      getDeleId.value=resourceArr
    getParentCodeValue.value = resourceArr[0].professionalCode;


    totalId.value = [];
  resourceArr.forEach((item) => {
    totalId.value.push(item.id);
  });
  needIdData.value = val.id;
  formInformation.value = val;
  }
}

function resetBtn() {
  formInformation.value = "";
}

// 批量删除
function handleRemoveArr() {
  // console.log(totalId.value)
  let params = {
    deleteIds: totalId.value,
  };
  proxy.$modal
    .confirm("是否确认删除所选数据项？")
    .then(function () {
      return batchDelProfessiona(params);
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
.content {
  display: flex;
  flex-direction: column;
}
.basicInformation {
  background: #f5f5f5;
  flex: 1;
  height: 500px;
  margin-top: 20px;
}
.basicTitle {
  font-size: 26px;
  font-weight: 600;
  color: rgb(39, 131, 196);
}
.left {
  flex: 1;
}
.main_left {
  flex: 2;
}
.right {
  flex: 5;
  margin-left: 30px;
}
.formBd {
  display: flex;
  div:nth-child(1) {
    width: 120px;
  }
  padding: 10px 100px;
}
.mainCont {
  padding-left: 20px;
}
.deleteBtn {
  background-color: #fff3f3 !important;
  color: #db0505 !important;
  width: 122px;
  height: 40px;
  font-size: 14px;
  line-height: 30px;
  border: 1px solid #efb0b0;
}
.resetBtn {
  background-color: #e2e2e2 !important;
  color: #333 !important;
  width: 122px;
  height: 40px;
  font-size: 14px;
  line-height: 30px;
  border: 1px solid #cecece;
}
.saveBtn {
  background-color: #15bd9d !important;
  color: #fff !important;
  width: 122px;
  height: 40px;
  font-size: 14px;
  line-height: 30px;
  border: 1px solid #efb0b0;
}
.loadInBtn {
  background: #d6e1ed !important;
  width: 96px;
  height: 40px;
  color: #000;
}
.loadOutBtn {
  background: #d6e1ed !important;
  width: 96px;
  height: 40px;
  color: #000;
}
</style>
