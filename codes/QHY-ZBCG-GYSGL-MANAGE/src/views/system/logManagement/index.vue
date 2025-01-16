<template>
  <div class="app-container search-table-box aidex-table">
    <el-card shadow="never" ref="queryRef" style="margin-bottom: 12px;" class="search_card" v-show="showSearch">
      <div class="filter-container">
        <div class="search_box">
          <el-form :model="queryParams" ref="queryForm" label-width="80px">
            <el-row :gutter="16">
              <el-col :md="6">
                <el-form-item label="系统模块" prop="title">
                  <el-input
                    v-model="queryParams.title"
                    placeholder="请输入内容进行模糊查询"
                    clearable
                    @keyup.enter="handleQuery"
                  />
                </el-form-item>
              </el-col>
              <el-col :md="6">
                <el-form-item label="操作人员" prop="operName">
                  <el-input
                    v-model="queryParams.operName"
                    placeholder="请输入操作人员"
                    clearable
                    @keyup.enter="handleQuery"
                  />
                </el-form-item>
              </el-col>
              <el-col :md="6">
                <el-form-item label="告警状态" prop="businessType">
                  <el-select
                    v-model="queryParams.businessType"
                    placeholder="请选择告警状态"
                    clearable
                    @change="handleQuery"
                    style="width: 100%"
                  >
                    <el-option
                      v-for="dict in sys_oper_type"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :md="6" v-if="advanced">
                <el-form-item label="状态" prop="status">
                  <el-select
                    v-model="queryParams.status"
                    placeholder="操作状态"
                    clearable
                    @change="handleQuery"
                    style="width: 100%"
                  >
                    <el-option
                      v-for="dict in sys_common_status"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :md="6" v-if="advanced">
                <el-form-item label="操作时间">
                  <el-date-picker
                    v-model="dateRange"
                    value-format="YYYY-MM-DD"
                    type="daterange"
                    range-separator="-"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    style="width: 100%"
                  ></el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :md="6" align="right" style="margin-left: auto;">
                <el-form-item class="search_btn_box">
                  <el-button class="filter-item" type="primary" @click="handleQuery">搜索</el-button>
                  <el-button class="filter-item" style="margin-left: 8px;" @click="resetQuery">重置</el-button>
                  <a @click="toggleAdvanced" style="margin:0 4px 0 8px ;vertical-align: middle;">
                    {{ advanced ? '收起' : '展开' }}
                    <el-icon v-if="!advanced"><ArrowDown /></el-icon>
                    <el-icon v-if="advanced"><ArrowUp /></el-icon>
                  </a>
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
              <el-button disabled type="text">操作日志</el-button>
            </div>
          </el-col>
          <el-col :span="16">
            <div class="btn_box" align="right" style="float: right;">
              <el-button
                class="filter-item"
                style="margin-left: 8px;"
                type="danger"
                icon="Delete"
                :disabled="multiple"
                v-if="!multiple"
                @click="handleDelete"
                v-hasPermi="['monitor:operlog:remove']"
              >删除</el-button>
              <el-button
                class="filter-item"
                style="margin-left: 8px;"
                type="danger"
                icon="Delete"
                @click="handleClean"
                v-hasPermi="['monitor:operlog:remove']"
              >清空</el-button>
              <el-button
                class="filter-item"
                style="margin-left: 8px;"
                icon="Download"
                @click="handleExport"
                v-hasPermi="['monitor:operlog:export']"
              >导出</el-button>
              <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
            </div>
          </el-col>
        </el-row>
      </template>

      <el-table stripe ref="operlogRef" v-loading="loading" :data="operlogList" @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange" highlight-current-row style="width: 100%;" :height="tableHeight">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="操作时间" align="center" prop="operTime" sortable="custom" :sort-orders="['descending', 'ascending']" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.operTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作账号" align="center" prop="operName" :show-overflow-tooltip="true" sortable="custom" :sort-orders="['descending', 'ascending']" width="100" />
        <el-table-column label="操作模块" align="center" prop="title" />
        <el-table-column label="操作人IP" align="center" prop="operIp" width="130" :show-overflow-tooltip="true" />
        <el-table-column label="操作内容" align="center" prop="businessType">
          <template #default="scope">
            <dict-tag :options="sys_oper_type" :value="scope.row.businessType" />
          </template>
        </el-table-column>
        <el-table-column label="告警状态" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="sys_common_status" :value="scope.row.status" />
          </template>
        </el-table-column>
        <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button
              type="text"
              @click="handleView(scope.row, scope.index)"
              v-hasPermi="['monitor:operlog:query']"
            >详细</el-button>
          </template>
        </el-table-column> -->
        <template v-slot:empty>
          <svg-icon icon-class="search-none" style="font-size: 64px;" />
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

      <!-- 操作日志详细 -->
      <el-dialog title="操作日志详细" v-model="open" width="720px" append-to-body>
        <div class="dialog_box">
          <el-form :model="form" label-position="top">
            <el-row>
               <el-col :span="12">
                  <el-form-item label="操作模块">{{ form.title }} / {{ typeFormat(form) }}</el-form-item>
                  <el-form-item
                    label="登录信息"
                  >{{ form.operName }} / {{ form.operIp }} / {{ form.operLocation }}</el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="请求地址">{{ form.operUrl }}</el-form-item>
                  <el-form-item label="请求方式">{{ form.requestMethod }}</el-form-item>
               </el-col>
               <el-col :span="24">
                  <el-form-item label="操作方法">{{ form.method }}</el-form-item>
               </el-col>
               <el-col :span="24">
                  <el-form-item label="请求参数">{{ form.operParam }}</el-form-item>
               </el-col>
               <el-col :span="24">
                  <el-form-item label="返回参数">{{ form.jsonResult }}</el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="操作状态">
                     <div v-if="form.status === 0">正常</div>
                     <div v-else-if="form.status === 1">失败</div>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="操作时间">{{ parseTime(form.operTime) }}</el-form-item>
               </el-col>
               <el-col :span="24">
                  <el-form-item label="异常信息" v-if="form.status === 1">{{ form.errorMsg }}</el-form-item>
               </el-col>
            </el-row>
          </el-form>
        </div>
        <template #footer>
          <div class="dialog-footer">
             <el-button @click="open = false">关 闭</el-button>
          </div>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup name="Operlog">
import { list, delOperlog, cleanOperlog } from "@/api/monitor/operlog";

const { proxy } = getCurrentInstance();
const { sys_oper_type, sys_common_status } = proxy.useDict("sys_oper_type","sys_common_status");

const advanced = ref(false);
const tableHeight = proxy.getInitTableHeight();
const operlogList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const dateRange = ref([]);
const defaultSort = ref({ prop: "operTime", order: "descending" });

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    title: undefined,
    operName: undefined,
    businessType: undefined,
    status: undefined
  }
});

const { queryParams, form } = toRefs(data);

/** 隐藏搜索按钮操作 */
watch(showSearch, (value) => {
  let oldHeight = proxy.$refs.queryRef.$el.offsetHeight
  if (value) {
    oldHeight = oldHeight - 12
  } else {
     oldHeight = oldHeight + 12
  }
  nextTick(() => (
    tableHeight.value = proxy.$refs.operlogRef.$el.offsetHeight - (proxy.$refs.queryRef.$el.offsetHeight-oldHeight)
  ))
})

/** 展开按钮操作 */
function toggleAdvanced() {
  const oldHeight = proxy.$refs.queryRef.$el.offsetHeight
  advanced.value = !advanced.value
  nextTick(() => (
    tableHeight.value = proxy.$refs.operlogRef.$el.offsetHeight - (proxy.$refs.queryRef.$el.offsetHeight-oldHeight)
  ));
}

/** 查询登录日志 */
function getList() {
  loading.value = true;
  list(queryParams.value, dateRange.value).then(response => {
    console.log(response.data.records,'response=====');
    operlogList.value = response.data.records;
    total.value = response.data.total;
    // loading.value = false;
    loading.value = false;

  });
}
/** 操作日志类型字典翻译 */
function typeFormat(row, column) {
  return proxy.selectDictLabel(sys_oper_type.value, row.businessType);
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
  queryParams.value.pageNum = 1;
  proxy.$refs["operlogRef"].sort(defaultSort.value.prop, defaultSort.value.order);
}
/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.operId);
  multiple.value = !selection.length;
}
/** 排序触发事件 */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}
/** 详细按钮操作 */
function handleView(row) {
  open.value = true;
  form.value = row;
}
/** 删除按钮操作 */
function handleDelete(row) {
  const operIds = row.operId || ids.value;
  proxy.$modal.confirm('是否确认删除日志编号为"' + operIds + '"的数据项?').then(function () {
    return delOperlog(operIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}
/** 清空按钮操作 */
function handleClean() {
  proxy.$modal.confirm("是否确认清空所有操作日志数据项?").then(function () {
    return cleanOperlog();
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("清空成功");
  }).catch(() => {});
}
/** 导出按钮操作 */
function handleExport() {
  proxy.download("monitor/operlog/export",{
    ...queryParams.value,
  }, `config_${new Date().getTime()}.xlsx`);
}

getList();
</script>
