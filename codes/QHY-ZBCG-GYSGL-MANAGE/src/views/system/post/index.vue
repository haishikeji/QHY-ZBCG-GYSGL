<template>
  <div class="app-container search-table-box aidex-table">
    <el-card shadow="never" ref="queryRef" style="margin-bottom: 12px;" class="search_card" v-show="showSearch">
      <div class="filter-container">
        <div class="search_box">
          <el-form :model="queryParams" ref="queryForm" label-width="80px">
            <el-row :gutter="16">
              <el-col :md="6">
                <el-form-item label="岗位编码" prop="postCode">
                  <el-input
                    v-model="queryParams.postCode"
                    placeholder="请输入岗位编码"
                    clearable
                    @keyup.enter="handleQuery"
                  />
                </el-form-item>
              </el-col>
              <el-col :md="6">
                <el-form-item label="岗位名称" prop="postName">
                  <el-input
                    v-model="queryParams.postName"
                    placeholder="请输入岗位名称"
                    clearable
                    @keyup.enter="handleQuery"
                  />
                </el-form-item>
              </el-col>
              <el-col :md="6">
                <el-form-item label="状态" prop="status">
                  <el-select v-model="queryParams.status" placeholder="岗位状态" @change="handleQuery" style="width: 100%" clearable>
                    <el-option
                      v-for="dict in sys_normal_disable"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    />
                  </el-select>
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
              <el-button disabled type="text">岗位信息</el-button>
            </div>
          </el-col>
          <el-col :span="16">
            <div class="btn_box" align="right" style="float: right;">
              <el-button
                class="filter-item"
                style="margin-left: 8px;"
                type="primary"
                icon="Plus"
                @click="handleAdd"
                v-hasPermi="['system:post:add']"
              >新增</el-button>
              <el-button
                class="filter-item"
                style="margin-left: 8px;"
                type="danger"
                icon="Delete"
                v-if="!multiple"
                :disabled="multiple"
                @click="handleDelete"
                v-hasPermi="['system:post:remove']"
              >删除</el-button>
              <el-button
                class="filter-item"
                style="margin-left: 8px;"
                icon="Download"
                @click="handleExport"
                v-hasPermi="['system:post:export']"
              >导出</el-button>
              <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
            </div>
          </el-col>
        </el-row>
      </template>

      <el-table stripe ref="tableRef" v-loading="loading" :data="postList" @selection-change="handleSelectionChange" highlight-current-row style="width: 100%;" :height="tableHeight">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="岗位编号" align="center" prop="postId" />
        <el-table-column label="岗位编码" align="center" prop="postCode" />
        <el-table-column label="岗位名称" align="center" prop="postName" />
        <el-table-column label="岗位排序" align="center" prop="postSort" />
        <el-table-column label="状态" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button
              type="text"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['system:post:edit']"
            >修改</el-button>
            <el-divider direction="vertical"></el-divider>
            <el-button
              type="text"
              style="color: red;"
              @click="handleDelete(scope.row)"
              v-hasPermi="['system:post:remove']"
            >删除</el-button>
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
        small
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />

      <!-- 添加或修改岗位对话框 -->
      <el-dialog :title="title" v-model="open" width="520px" append-to-body>
        <div class="dialog_box">
          <el-form ref="postRef" :model="form" :rules="rules" label-position="top">
            <el-row :gutter="24">
              <el-col :span="24">
                <el-form-item label="岗位名称" prop="postName">
                  <el-input v-model="form.postName" placeholder="请输入岗位名称" />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="岗位编码" prop="postCode">
                  <el-input v-model="form.postCode" placeholder="请输入编码名称" />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="岗位顺序" prop="postSort">
                  <el-input-number :step="10" style="width: 100%;" v-model="form.postSort" controls-position="right" :min="10" />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="岗位状态" prop="status">
                  <el-radio-group v-model="form.status">
                    <el-radio-button
                      v-for="dict in sys_normal_disable"
                      :key="dict.value"
                      :label="dict.value"
                    >{{ dict.label }}</el-radio-button>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="备注" prop="remark">
                  <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入内容" />
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
    </el-card>
  </div>
</template>

<script setup name="Post">
import { listPost, addPost, delPost, getPost, updatePost } from "@/api/system/post";

const { proxy } = getCurrentInstance();
const { sys_normal_disable } = proxy.useDict("sys_normal_disable");

const tableHeight = proxy.getInitTableHeight();
const postList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    postCode: undefined,
    postName: undefined,
    status: undefined
  },
  rules: {
    postName: [{ required: true, message: "岗位名称不能为空", trigger: "blur" }],
    postCode: [{ required: true, message: "岗位编码不能为空", trigger: "blur" }],
    postSort: [{ required: true, message: "岗位顺序不能为空", trigger: "blur" }],
  }
});

const { queryParams, form, rules } = toRefs(data);

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

/** 查询岗位列表 */
function getList() {
  loading.value = true;
  listPost(queryParams.value).then(response => {
    postList.value = response.rows;
    total.value = response.total;
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
    postId: undefined,
    postCode: undefined,
    postName: undefined,
    postSort: 0,
    status: "0",
    remark: undefined
  };
  proxy.resetForm("postRef");
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
  ids.value = selection.map(item => item.postId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}
/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加岗位";
}
/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const postId = row.postId || ids.value;
  getPost(postId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改岗位";
  });
}
/** 提交按钮 */
function submitForm() {
  proxy.$refs["postRef"].validate(valid => {
    if (valid) {
      if (form.value.postId != undefined) {
        updatePost(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addPost(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}
/** 删除按钮操作 */
function handleDelete(row) {
  const postIds = row.postId || ids.value;
  proxy.$modal.confirm('是否确认删除岗位编号为"' + postIds + '"的数据项？').then(function() {
    return delPost(postIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}
/** 导出按钮操作 */
function handleExport() {
  proxy.download("system/post/export", {
    ...queryParams.value
  }, `post_${new Date().getTime()}.xlsx`);
}

getList();
</script>
