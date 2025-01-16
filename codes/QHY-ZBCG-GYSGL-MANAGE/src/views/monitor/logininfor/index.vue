<template>
  <div class="app-container search-table-box aidex-table">
    <el-card shadow="never" ref="queryRef" style="margin-bottom: 12px;" class="search_card" v-show="showSearch">
      <div class="filter-container">
        <div class="search_box">
          <el-form :model="queryParams" ref="queryForm" label-width="80px">
            <el-row :gutter="16">
              <el-col :md="6">
                <el-form-item label="登录地址" prop="ipaddr">
                  <el-input
                    v-model="queryParams.ipaddr"
                    placeholder="请输入登录地址"
                    clearable
                    @keyup.enter="handleQuery"
                  />
                </el-form-item>
              </el-col>
              <el-col :md="6">
                <el-form-item label="用户名称" prop="userName">
                  <el-input
                    v-model="queryParams.userName"
                    placeholder="请输入用户名称"
                    clearable
                    @keyup.enter="handleQuery"
                  />
                </el-form-item>
              </el-col>
              <el-col :md="6">
                <el-form-item label="登录状态" prop="status">
                  <el-select
                    v-model="queryParams.status"
                    placeholder="登录状态"
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
                <el-form-item label="登录时间">
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
              <el-button disabled type="text">登录日志</el-button>
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
                v-hasPermi="['monitor:logininfor:remove']"
              >删除</el-button>
              <el-button
                class="filter-item"
                style="margin-left: 8px;"
                type="danger"
                icon="Delete"
								:disabled="multiple"
								v-if="!multiple"
                @click="handleClean"
                v-hasPermi="['monitor:logininfor:remove']"
              >清空</el-button>
							<el-button
							  class="filter-item"
							  style="margin-left: 8px;"
							  type="danger"
							  icon="Unlock"
							  @click="handleUnlock"
							  v-hasPermi="['monitor:logininfor:unlock']"
							>解锁</el-button>
              <el-button
                class="filter-item"
                style="margin-left: 8px;"
                type="warning"
                icon="Download"
                @click="handleExport"
                v-hasPermi="['monitor:logininfor:export']"
              >导出</el-button>
              <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
            </div>
          </el-col>
        </el-row>
      </template>

      <el-table stripe ref="logininforRef" v-loading="loading" :data="logininforList" @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange" highlight-current-row style="width: 100%;" :height="tableHeight">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="访问编号" align="center" prop="infoId" />
        <el-table-column label="用户名称" align="center" prop="userName" :show-overflow-tooltip="true" sortable="custom" :sort-orders="['descending', 'ascending']" />
        <el-table-column label="地址" align="center" prop="ipaddr" :show-overflow-tooltip="true" />
        <el-table-column label="登录地点" align="center" prop="loginLocation" :show-overflow-tooltip="true" />
        <el-table-column label="操作系统" align="center" prop="os" :show-overflow-tooltip="true" />
        <el-table-column label="浏览器" align="center" prop="browser" :show-overflow-tooltip="true" />
        <el-table-column label="登录状态" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="sys_common_status" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="描述" align="center" prop="msg" />
        <el-table-column label="访问时间" align="center" prop="loginTime" sortable="custom" :sort-orders="['descending', 'ascending']" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.loginTime) }}</span>
          </template>
        </el-table-column>
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
    </el-card>
  </div>
</template>

<script setup name="Logininfor">
import { list, delLogininfor, cleanLogininfor, unlockLogininfor } from "@/api/monitor/logininfor";

const { proxy } = getCurrentInstance();
const { sys_common_status } = proxy.useDict("sys_common_status");

const advanced = ref(false);
const tableHeight = proxy.getInitTableHeight();
const logininforList = ref([]);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const selectName = ref("");
const total = ref(0);
const dateRange = ref([]);
const defaultSort = ref({ prop: "loginTime", order: "descending" });

/** 隐藏搜索按钮操作 */
watch(showSearch, (value) => {
  let oldHeight = proxy.$refs.queryRef.$el.offsetHeight
  if (value) {
    oldHeight = oldHeight - 12
  } else {
     oldHeight = oldHeight + 12
  }
  nextTick(() => (
    tableHeight.value = proxy.$refs.logininforRef.$el.offsetHeight - (proxy.$refs.queryRef.$el.offsetHeight-oldHeight)
  ))
})

/** 展开按钮操作 */
function toggleAdvanced() {
  const oldHeight = proxy.$refs.queryRef.$el.offsetHeight
  advanced.value = !advanced.value
  nextTick(() => (
    tableHeight.value = proxy.$refs.logininforRef.$el.offsetHeight - (proxy.$refs.queryRef.$el.offsetHeight-oldHeight)
  ));
}

// 查询参数
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  ipaddr: undefined,
  userName: undefined,
  status: undefined,
  orderByColumn: undefined,
  isAsc: undefined
});

/** 查询登录日志列表 */
function getList() {
  loading.value = true;
  list(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
    logininforList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
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
  proxy.$refs["logininforRef"].sort(defaultSort.value.prop, defaultSort.value.order);
}
/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.infoId);
  multiple.value = !selection.length;
  single.value = selection.length != 1;
  selectName.value = selection.map(item => item.userName);
}
/** 排序触发事件 */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}
/** 删除按钮操作 */
function handleDelete(row) {
  const infoIds = row.infoId || ids.value;
  proxy.$modal.confirm('是否确认删除访问编号为"' + infoIds + '"的数据项?').then(function () {
    return delLogininfor(infoIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}
/** 清空按钮操作 */
function handleClean() {
  proxy.$modal.confirm("是否确认清空所有登录日志数据项?").then(function () {
    return cleanLogininfor();
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("清空成功");
  }).catch(() => {});
}
/** 解锁按钮操作 */
function handleUnlock() {
  const username = selectName.value;
  proxy.$modal.confirm('是否确认解锁用户"' + username + '"数据项?').then(function () {
    return unlockLogininfor(username);
  }).then(() => {
    proxy.$modal.msgSuccess("用户" + username + "解锁成功");
  }).catch(() => {});
}
/** 导出按钮操作 */
function handleExport() {
  proxy.download("monitor/logininfor/export", {
    ...queryParams.value,
  }, `config_${new Date().getTime()}.xlsx`);
}

getList();
</script>
