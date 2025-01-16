<template>
  <div class="app-container search-table-box aidex-table">
    <el-card shadow="never" ref="queryRef" style="margin-bottom: 12px;" class="search_card">
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
              <el-button disabled type="text">用户信息</el-button>
            </div>
          </el-col>
        </el-row>
      </template>
    
      <el-table
        v-loading="loading"
        :data="onlineList.slice((pageNum - 1) * pageSize, pageNum * pageSize)"
        style="width: 100%;"
        highlight-current-row :height="tableHeight"
      >
        <el-table-column label="序号" width="60" type="index" align="center">
          <template #default="scope">
            <span>{{ (pageNum - 1) * pageSize + scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="会话编号" align="center" prop="tokenId" :show-overflow-tooltip="true" />
        <el-table-column label="登录名称" align="center" prop="userName" :show-overflow-tooltip="true" />
        <el-table-column label="所属部门" align="center" prop="deptName" :show-overflow-tooltip="true" />
        <el-table-column label="主机" align="center" prop="ipaddr" :show-overflow-tooltip="true" />
        <el-table-column label="登录地点" align="center" prop="loginLocation" :show-overflow-tooltip="true" />
        <el-table-column label="操作系统" align="center" prop="os" :show-overflow-tooltip="true" />
        <el-table-column label="浏览器" align="center" prop="browser" :show-overflow-tooltip="true" />
        <el-table-column label="登录时间" align="center" prop="loginTime" width="180">
          <template #default="scope">
             <span>{{ parseTime(scope.row.loginTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
             <el-button
                type="text"
                @click="handleForceLogout(scope.row)"
                v-hasPermi="['monitor:online:forceLogout']"
             >强退</el-button>
          </template>
        </el-table-column>
        <template v-slot:empty>
          <svg-icon icon-class="search-none" style="font-size: 64px;" />
          <p>暂无数据</p>
        </template>
      </el-table>
      <pagination v-show="total > 0" :total="total" v-model:page="pageNum" v-model:limit="pageSize" />
    </el-card>
  </div>
</template>

<script setup name="Online">
import { forceLogout, list as initData } from "@/api/monitor/online";

const { proxy } = getCurrentInstance();
const tableHeight = proxy.getInitTableHeight();

const onlineList = ref([]);
const loading = ref(true);
const total = ref(0);
const pageNum = ref(1);
const pageSize = ref(10);

const queryParams = ref({
  ipaddr: undefined,
  userName: undefined
});

/** 查询登录日志列表 */
function getList() {
  loading.value = true;
  initData(queryParams.value).then(response => {
    onlineList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}
/** 搜索按钮操作 */
function handleQuery() {
  pageNum.value = 1;
  getList();
}
/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryForm");
  handleQuery();
}
/** 强退按钮操作 */
function handleForceLogout(row) {
    proxy.$modal.confirm('是否确认强退名称为"' + row.userName + '"的用户?').then(function () {
  return forceLogout(row.tokenId);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

getList();
</script>
