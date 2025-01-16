<template>
  <div class="app-container search-table-box aidex-table">
    <el-card shadow="never" ref="queryRef" style="margin-bottom: 12px;" class="search_card" v-show="showSearch">
      <div class="filter-container">
        <div class="search_box">
          <el-form :model="queryParams" ref="queryForm" label-width="80px">
            <el-row :gutter="16">
              <el-col :md="6">
                <el-form-item label="表名称" prop="tableName">
                  <el-input
                    v-model="queryParams.tableName"
                    placeholder="请输入表名称"
                    clearable
                    @keyup.enter="handleQuery"
                  />
                </el-form-item>
              </el-col>
              <el-col :md="6">
                <el-form-item label="表描述" prop="tableComment">
                  <el-input
                    v-model="queryParams.tableComment"
                    placeholder="请输入表描述"
                    clearable
                    @keyup.enter="handleQuery"
                  />
                </el-form-item>
              </el-col>
              <el-col :md="6">
                <el-form-item label="创建时间">
                  <el-date-picker
                    style="width: 100%;"
                    v-model="dateRange"
                    value-format="YYYY-MM-DD"
                    type="daterange"
                    range-separator="-"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                  ></el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :md="6" align="right" style="margin-left: auto;">
                <el-form-item class="search_btn_box">
                  <el-button class="filter-item" type="primary" @click="handleQuery">搜索</el-button>
                  <el-button class="filter-item" style="margin-left: 8px;" @click="resetQuery">重置</el-button>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </div>
    </el-card>

    <el-card shadow="never" >
      <template #header>
        <el-row>
          <el-col :span="8">
            <div class="card-header">
              <el-button disabled type="text">代码生成</el-button>
            </div>
          </el-col>
          <el-col :span="16">
            <div class="btn_box" align="right" style="float: right;">
              <el-button
                class="filter-item"
                style="margin-left: 8px;"
                type="primary"
                icon="Download"
                @click="handleGenTable"
                v-hasPermi="['tool:gen:code']"
              >生成</el-button>
              <el-button
                class="filter-item"
                style="margin-left: 8px;"
                icon="Upload"
                @click="openImportTable"
                v-hasPermi="['tool:gen:import']"
              >导入</el-button>
              <el-button
                class="filter-item"
                style="margin-left: 8px;"
                icon="Delete"
                :disabled="multiple"
                v-if="!multiple"
                @click="handleDelete"
                v-hasPermi="['tool:gen:remove']"
              >删除</el-button>
              <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
            </div>
          </el-col>
        </el-row>
      </template>

      <el-table stripe ref="tableRef" v-loading="loading" :data="tableList" @selection-change="handleSelectionChange" highlight-current-row style="width: 100%;" :height="tableHeight">
        <el-table-column type="selection" align="center" width="55"></el-table-column>
        <el-table-column label="序号" type="index" width="60" align="center">
          <template #default="scope">
            <span>{{(queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1}}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="表名称"
          align="center"
          prop="tableName"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="表描述"
          align="center"
          prop="tableComment"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="实体"
          align="center"
          prop="className"
          :show-overflow-tooltip="true"
        />
        <el-table-column label="创建时间" align="center" prop="createTime" width="160" />
        <el-table-column label="更新时间" align="center" prop="updateTime" width="160" />
        <el-table-column label="操作" align="center" width="330" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button
              type="text"
              @click="handlePreview(scope.row)"
              v-hasPermi="['tool:gen:preview']"
            >预览</el-button>
            <el-divider direction="vertical"></el-divider>
            <el-button
              type="text"
              @click="handleEditTable(scope.row)"
              v-hasPermi="['tool:gen:edit']"
            >编辑</el-button>
            <el-divider direction="vertical"></el-divider>
            <el-button
              type="text"
              @click="handleDelete(scope.row)"
              v-hasPermi="['tool:gen:remove']"
            >删除</el-button>
            <el-divider direction="vertical"></el-divider>
            <el-button
              type="text"
              @click="handleSynchDb(scope.row)"
              v-hasPermi="['tool:gen:edit']"
            >同步</el-button>
            <el-divider direction="vertical"></el-divider>
            <el-button
              type="text"
              @click="handleGenTable(scope.row)"
              v-hasPermi="['tool:gen:code']"
            >生成代码</el-button>
          </template>
        </el-table-column>
        <template v-slot:empty>
          <svg-icon icon-class="search-none" style="font-size: 64px;" />
          <p>暂无数据</p>
        </template>
      </el-table>
      <pagination
        v-show="total>0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
      <!-- 预览界面 -->
      <el-dialog :title="preview.title" v-model="preview.open" width="80%" append-to-body custom-class="scrollbar gencode-dialog">
        <el-tabs v-model="preview.activeName">
          <el-tab-pane
            v-for="(value, key) in preview.data"
            :label="key.substring(key.lastIndexOf('/')+1,key.indexOf('.vm'))"
            :name="key.substring(key.lastIndexOf('/')+1,key.indexOf('.vm'))"
            :key="value"
          >
            <el-link :underline="false" icon="DocumentCopy" v-copyText="value" v-copyText:callback="copyTextSuccess" style="float:right">&nbsp;复制</el-link>
            <pre>{{ value }}</pre>
          </el-tab-pane>
        </el-tabs>
      </el-dialog>
      <import-table ref="importRef" @ok="handleQuery" />
    </el-card>
  </div>
</template>

<script setup name="Gen">
import { listTable, previewTable, delTable, genCode, synchDb } from "@/api/tool/gen";
import router from "@/router";
import importTable from "./importTable";

const route = useRoute();
const { proxy } = getCurrentInstance();

const tableHeight = proxy.getInitTableHeight();
const tableList = ref([]);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const tableNames = ref([]);
const dateRange = ref([]);
const uniqueId = ref("");

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    tableName: undefined,
    tableComment: undefined
  },
  preview: {
    open: false,
    title: "代码预览",
    data: {},
    activeName: "domain.java"
  }
});

const { queryParams, preview } = toRefs(data);

onActivated(() => {
  const time = route.query.t;
  if (time != null && time != uniqueId.value) {
    uniqueId.value = time;
    queryParams.value.pageNum = Number(route.query.pageNum);
    dateRange.value = [];
    proxy.resetForm("queryForm");
    getList();
  }
})

/** 隐藏搜索按钮操作 */
watch(showSearch, (value) => {
  let oldHeight = proxy.$refs.queryRef.$el.offsetHeight
  if (value) {
    oldHeight = oldHeight - 12
  } else {
     oldHeight = oldHeight + 12
  }
  nextTick(() => (
    tableHeight.value = proxy.$refs.tableRef.$el.offsetHeight - (proxy.$refs.queryRef.$el.offsetHeight-oldHeight)
  ))
})

/** 查询表集合 */
function getList() {
  loading.value = true;
  listTable(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
    tableList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
/** 生成代码操作 */
function handleGenTable(row) {
  const tbNames = row.tableName || tableNames.value;
  if (tbNames == "") {
    proxy.$modal.msgError("请选择要生成的数据");
    return;
  }
  if (row.genType === "1") {
    genCode(row.tableName).then(response => {
      proxy.$modal.msgSuccess("成功生成到自定义路径：" + row.genPath);
    });
  } else {
    proxy.$download.zip("/tool/gen/batchGenCode?tables=" + tbNames, "ruoyi.zip");
  }
}
/** 同步数据库操作 */
function handleSynchDb(row) {
  const tableName = row.tableName;
  proxy.$modal.confirm('确认要强制同步"' + tableName + '"表结构吗？').then(function () {
    return synchDb(tableName);
  }).then(() => {
    proxy.$modal.msgSuccess("同步成功");
  }).catch(() => {});
}
/** 打开导入表弹窗 */
function openImportTable() {
  proxy.$refs["importRef"].show();
}
/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = [];
  proxy.resetForm("queryForm");
  handleQuery();
}
/** 预览按钮 */
function handlePreview(row) {
  previewTable(row.tableId).then(response => {
    preview.value.data = response.data;
    preview.value.open = true;
    preview.value.activeName = "domain.java";
  });
}
/** 复制代码成功 */
function copyTextSuccess() {
  proxy.$modal.msgSuccess("复制成功");
}
// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.tableId);
  tableNames.value = selection.map(item => item.tableName);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}
/** 修改按钮操作 */
function handleEditTable(row) {
  const tableId = row.tableId || ids.value[0];
  router.push({ path: "/tool/gen-edit/index/" + tableId, query: { pageNum: queryParams.value.pageNum } });
}
/** 删除按钮操作 */
function handleDelete(row) {
  const tableIds = row.tableId || ids.value;
  proxy.$modal.confirm('是否确认删除表编号为"' + tableIds + '"的数据项？').then(function () {
    return delTable(tableIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

getList();
</script>

<style lang="scss">
.gencode-dialog {
  .el-tabs {
    padding: 10px;
  }
}
</style>
