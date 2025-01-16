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
          <el-col :span="8">
            <div class="card-header">
              <el-button
                v-hasPermi="['plan:planYears:add']"
                class="filter-item"
                type="success"
                icon="Plus"
                @click="handleAdd"
                >创建计划</el-button
              >
            </div>
          </el-col>
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
                v-hasPermi="['plan:planYears:importData']"
                class="filter-item"
                style="margin-left: 8px"
                icon="Upload"
                @click="handleImport"
                >导入项目</el-button
              >
              <el-button
                v-hasPermi="['plan:planYears:exportPlan']"
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
                      v-for="item in [
                        { label: '未超额', value: '0' },
                        { label: '超额', value: '1' },
                      ]"
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
                    value-format="YYYY-MM-DD"
                    type="daterange"
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
        <!-- <el-table-column
          label="是否为超限额计划"
          align="center"
          prop="isExcess"
          width="150"
        >
          <template #default="scope">
            <dict-tag
              :options="[
                {
                  label: '未超额',
                  value: '0',
                  elTagType: 'primary',
                  elTagClass: '',
                },
                {
                  label: '超额',
                  value: '1',
                  listClass: 'danger',
                  elTagType: 'error',
                  elTagClass: '',
                },
              ]"
              :value="scope.row.isExcess"
            />
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
        <el-table-column
          label="机关业务指导处(科)"
          align="center"
          prop="organDivisionStr"
          width="150"
        ></el-table-column>
        <el-table-column
          label="预算科目"
          align="center"
          prop="budgetAccountStr"
          width="150"
        ></el-table-column>
        <!-- <el-table-column
          label="采购方式"
          align="center"
          prop="purchaseMode"
          width="150"
        >
          <template #default="scope">
            <dict-tag
              :options="editDroppdown.planPurchaseModes"
              :value="scope.row.purchaseMode"
            />
          </template>
        </el-table-column> -->
        <el-table-column
          label="计划提报需求时间"
          align="center"
          prop="planDemandSubTime"
          width="150"
        />
        <el-table-column
          label="计划完成采购时间"
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
        <!-- <el-table-column label="项目属性" align="center" prop="projectAttr" width="200" >
                    <template #default="scope">
                        <dict-tag :options="editDroppdown.projectAttributes" :value="scope.row.projectAttr" />
                    </template>
                </el-table-column> -->
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
          width="340"
          fixed="right"
          class-name="small-padding fixed-width"
        >
          <template #default="scope">
            <el-button
              v-hasPermi="['plan:planYears:commit']"
              v-if="['0', '2'].includes(scope.row.projectStatus)"
              type="success"
              size="small"
              @click="handleCommit(scope.row)"
              >提交</el-button
            >
            <!-- <el-divider direction="vertical"></el-divider> -->
            <el-button
              v-hasPermi="['plan:planYears:view']"
              type="default"
              size="small"
              @click="handleInfoShow(scope.row)"
              >查看</el-button
            >
            <!-- <el-divider direction="vertical"></el-divider> -->
            <el-button
              v-hasPermi="['plan:planYears:update']"
              v-if="['0', '2'].includes(scope.row.projectStatus)"
              type="primary"
              size="small"
              @click="handleUpdate(scope.row)"
              >修改</el-button
            >
            <!-- <el-divider direction="vertical"></el-divider> -->
            <el-button
              v-hasPermi="['plan:planYears:delete']"
              v-if="['0', '2'].includes(scope.row.projectStatus)"
              type="danger"
              size="small"
              @click="handleDelete(scope.row)"
              >删除</el-button
            >
            <!-- <el-button
              v-if="scope.row.appUpdateDo === true || scope.row.appUpdateDo === 'true'"
              type="warning"
              size="small"
              @click="handleInfoShow(scope.row, 'applyModify')"
              >申请修改</el-button
            > -->
            <el-button
              v-hasPermi="['plan:planYears:appDelete']"
              v-if="
                scope.row.appDeleteDo === 'true' ||
                scope.row.appDeleteDo === true
              "
              type="success"
              size="small"
              @click="handleUndoApplyAction(scope.row.planYearId)"
              >申请撤销</el-button
            >

            <el-button
              v-hasPermi="['plan:planYears:commit']"
              v-if="['1', '3'].includes(scope.row.projectStatus)"
              type="success"
              size="small"
              @click="handleZdcShow(scope.row)"
              >机关业务指导处</el-button
            >
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

      <!-- 添加或修改部门对话框 -->
      <el-dialog :title="title" v-model="open" width="920px" append-to-body>
        <div class="dialog_box" style="height: 500px">
          <el-form
            ref="planYearRef"
            :model="form"
            :rules="rules"
            label-position="right"
            label-width="130"
          >
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="项目名称" prop="projectName">
                  <el-input
                    v-model="form.projectName"
                    placeholder="请输入项目名称"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="项目类型" prop="projectType">
                  <el-select v-model="form.projectType" placeholder="请选择">
                    <el-option
                      v-for="item in editDroppdown.projectTypes"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="概算金额" prop="evaluation">
                  <el-input
                    v-model="form.evaluation"
                    placeholder="请输入概算金额"
                  >
                    <template #append>万元</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <!-- <el-col :span="12">
                <el-form-item label="采购服务站" prop="purchaseServices">
                  <el-select
                    v-model="form.purchaseServices"
                    placeholder="请选择采购服务站"
                    :disabled="comDisabled"
                    @visible-change="
                      fetchSelectServicesOpt(form.projectType, form.evaluation)
                    "
                  >
                    <el-option
                      v-for="item in serviceOpt"
                      :key="item"
                      :label="item"
                      :value="item"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col> -->
              <el-col :span="12">
                <el-form-item label="机关业务指导处" prop="organDivision">
                  <el-select
                    v-model="form.organDivision"
                    placeholder="请选择机关业务指导处(科)"
                    style="width: 100%"
                    multiple
                    clearable
                    class="year-list-organ-division-select"
                  >
                    <el-option
                      v-for="item in nodes_flat({ children: deptOptions })"
                      :key="item.id"
                      :label="item.label"
                      :value="item.id"
                      style="display: none"
                      class="year-list-organ-division-select-none"
                    />
                    <el-tree
                      class="main-select-el-tree"
                      ref="selecteltree"
                      :data="deptOptions"
                      node-key="id"
                      highlight-current
                      :props="{
                        label: 'label',
                        children: 'children',
                      }"
                      @node-click="
                        (data) => {
                          handleNodeClickForm(data, index);
                        }
                      "
                      :current-node-key="value"
                      :expand-on-click-node="expandOnClickNode"
                      default-expand-all
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="预算科目" prop="budgetAccount">
                  <el-select
                    v-model="form.budgetAccount"
                    placeholder="请选择预算科目"
                  >
                    <el-option
                      v-for="item in editDroppdown.budgetAccounts"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <!-- <el-col :span="12">
                <el-form-item label="采购方式" prop="purchaseMode">
                  <el-select
                    v-model="form.purchaseMode"
                    placeholder="请选择采购方式"
                  >
                    <el-option
                      v-for="item in editDroppdown.planPurchaseModes"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col> -->
              <el-col :span="12">
                <el-form-item label="需求提报时间" prop="planDemandSubTime">
                  <el-date-picker
                    v-model="form.planDemandSubTime"
                    :value-format="'YYYY-MM'"
                    type="month"
                    placeholder="请选择需求提报时间"
                    format="YYYY-MM"
                    class="w-100"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item
                  label="完成采购时间"
                  prop="planPurchaseFinishTime"
                >
                  <el-date-picker
                    v-model="form.planPurchaseFinishTime"
                    :value-format="'YYYY-MM'"
                    type="month"
                    placeholder="请选择需求提报时间"
                    format="YYYY-MM"
                    class="w-100"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="交付实施时间" prop="planDeliverTime">
                  <el-date-picker
                    v-model="form.planDeliverTime"
                    :value-format="'YYYY-MM'"
                    type="month"
                    placeholder="请选择需求提报时间"
                    format="YYYY-MM"
                    class="w-100"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="项目属性" prop="projectAttr">
                  <el-select
                    v-model="form.projectAttr"
                    placeholder="请选择项目属性"
                    multiple
                  >
                    <el-option
                      v-for="item in editDroppdown.projectAttributes"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <!-- <el-col :span="12">
                <el-form-item label="是否为超限额计划" prop="isExcess">
                  <el-select
                    v-model="form.isExcess"
                    placeholder="请选择是否为超限额计划"
                  >
                    <el-option label="未超额" value="0"></el-option>
                    <el-option label="超额" value="1"></el-option>
                  </el-select>
                </el-form-item>
              </el-col> -->
              <el-col :span="24">
                <el-form-item label="需求概况" prop="demandOverview">
                  <el-input
                    v-model="form.demandOverview"
                    type="textarea"
                    placeholder="请输入需求概况"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="备注" prop="remarks">
                  <el-input
                    v-model="form.remarks"
                    type="textarea"
                    placeholder="请输入备注"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="项目附件" prop="projectFiles">
                  <el-upload
                    v-model:file-list="fileList"
                    class="upload-demo w-100"
                    :action="globalConfig.base_url + '/common/upload'"
                    :headers="fileHeaderParams"
                    accept=".doc,.docx,.pdf,.xls,.xlsx"
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
                        <span style="color: #e54242"
                          >doc/pdf/docx/xls/xlsx</span
                        >
                        文件
                        <!-- ，且不超过
                                                <span style="color: #E54242">500kb</span> -->
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
            <el-button @click="cancel">取 消</el-button>
            <el-button type="primary" @click="submitForm">确 定</el-button>
          </div>
        </template>
      </el-dialog>
    </el-card>

    <global-delete
      v-model="deleteFlag"
      title="删除内容"
      @on-confirm="handleDeleteYearPlan"
      @on-close="deleteFlag = false"
    ></global-delete>

    <submit-modal
      v-model="submitFlag"
      title="提交"
      @on-confirm="handleCommitYearPlan"
      @on-close="submitFlag = false"
    ></submit-modal>

    <el-dialog
      :title="infoModal.operType === 'applyModify' ? '申请修改' : '查看详情'"
      v-if="infoModal.show && infoModal.id"
      v-model="infoModal.show"
      width="900px"
      align-center
      :close-on-click-modal="false"
      class="plan-year-info-modal"
    >
      <div style="padding: 10px 20px 30px 20px">
        <PlanYearInfo
          ref="planYearInfo"
          v-if="infoModal.show && infoModal.id"
          :recordId="infoModal.id"
          :oper-type="infoModal.operType"
        ></PlanYearInfo>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="infoModal.show = false">关闭</el-button>
          <el-button
            v-if="infoModal.operType === 'applyModify'"
            type="primary"
            @click="handleYearPlanApplyUpdate"
            >提交申请</el-button
          >
        </div>
      </template>
    </el-dialog>

    <el-dialog
      title="编辑机关业务指导处"
      v-if="zdcModal.show && zdcModal.id"
      v-model="zdcModal.show"
      width="500px"
      align-center
      :close-on-click-modal="false"
      class="plan-year-info-modal"
    >
      <div style="padding: 10px 20px 30px 20px">
        <el-select
          v-model="zdcModal.organDivision"
          placeholder="请选择机关业务指导处(科)"
          style="width: 100%"
          multiple
          clearable
          class="year-list-organ-division-select"
        >
          <el-option
            v-for="item in nodes_flat({ children: deptOptions })"
            :key="item.id"
            :label="item.label"
            :value="item.id"
            style="display: none"
            class="year-list-organ-division-select-none"
          />
          <el-tree
            class="main-select-el-tree"
            ref="selecteltree"
            :data="deptOptions"
            node-key="id"
            highlight-current
            :props="{
              label: 'label',
              children: 'children',
            }"
            @node-click="
              (data) => {
                handleNodeZdcClickForm(data, index);
              }
            "
            :current-node-key="value"
            :expand-on-click-node="expandOnClickNode"
            default-expand-all
          />
        </el-select>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="zdcModal.show = false">关闭</el-button>
          <el-button type="primary" @click="handleZdcUpdate">保存</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog
      title="申请修改"
      v-model="applyUpdateModal.show"
      width="390px"
      align-center
      :close-on-click-modal="false"
      class="plan-year-apply-update-modal"
      style="margin-top: auto !important"
    >
      <div class="dialog_body">
        <img src="/images/common/icon-wraning-big.png" />
        <div class="msg">确定进行申请修改吗?</div>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="applyUpdateModal.show = false">取 消</el-button>
          <el-button type="primary" @click="handleYearPlanApplyUpdate"
            >确 定</el-button
          >
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
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitFileForm">确 定</el-button>
          <el-button @click="upload.open = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
  
<script setup name="PlanYearIndex">
import { getToken } from "@/utils/auth";
import DictColorTag from "@/components/DictColorTag";
import GlobalDelete from "@/components/GlobalDelete";

import PlanYearInfo from "./components/common/info";
import SubmitModal from "./components/submit";
import { getConfig, refreshCache } from "@/api/system/config";

import dayjs from "dayjs";
// import {
//   deptTreeSelectAndF as deptTreeSelect,
// } from "@/api/system/user";
import { deptOrganMap as deptTreeSelect } from "@/api/system/dept";

import {
  queryYearPlanList,
  execYearPlanAdd,
  execYearPlanModify,
  execYearPlanDelete,
  execYearPlanCommit,
  queryYearPlanInfo,
  execYearPlanApplyUpdate,
  queryYearPlanDorpdown,
  execYearDownLoaddate,
  purchaseServices,
  queryYearData,
  adPurchaseServices,
  execAppDelete,
  execUpdateZDC,
} from "@/api/plan/year";

const { proxy } = getCurrentInstance();
const { sys_yes_no } = proxy.useDict("sys_yes_no");

const advanced = ref(false);
const tableHeight = proxy.getInitTableHeight();
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
let purchaseServicesOpt = ref([]);

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
  url: proxy.globalConfig.base_url + "/plan/planYears/importData",
});

const data = reactive({
  form: {
    projectAttr: [],
    organDivision: [],
    organDivisionName: [],
  },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
  },
  rules: {
    projectName: [
      { required: true, message: "项目名称不能为空", trigger: "blur" },
    ],
    projectType: [
      { required: true, message: "项目类型不能为空", trigger: "change" },
    ],
    demandOverview: [
      { required: true, message: "需求概况不能为空", trigger: "blur" },
    ],
    evaluation: [
      { required: true, message: "概算金额不能为空", trigger: "blur" },
    ],
    // purchaseMode: [
    //   { required: true, message: "采购方式不能为空", trigger: "change" },
    // ],
    purchaseServices: [
      { required: true, message: "采购服务站不能为空", trigger: "change" },
    ],
    budgetAccount: [
      { required: true, message: "预算科目不能为空", trigger: "change" },
    ],
    organDivision: [
      { required: true, message: "机关业务指导处不能为空", trigger: "change" },
    ],
    planDemandSubTime: [
      {
        required: true,
        // message: "计划提报需求时间不能为空",
        trigger: "change",
        validator: (rule, value, callback) => {
          console.log(rule, value, data.form.planPurchaseFinishTime);
          if (!value) {
            callback(new Error("请选择计划提报需求时间"));
          } else {
            if (
              data.form.planPurchaseFinishTime &&
              dayjs(value).isSameOrAfter(
                dayjs(data.form.planPurchaseFinishTime)
              )
            ) {
              callback(new Error("计划提报需求时间必须小于计划完成采购时间"));
            } else if (
              data.form.planDeliverTime &&
              dayjs(value).isSameOrAfter(dayjs(data.form.planDeliverTime))
            ) {
              callback(new Error("计划提报需求时间必须小于计划交付时间"));
            } else {
              callback();
            }
          }
        },
      },
    ],
    planPurchaseFinishTime: [
      {
        required: true,
        // message: "计划完成采购时间不能为空",
        trigger: "change",
        validator: (rule, value, callback) => {
          if (!value) {
            callback(new Error("请选择计划完成采购时间"));
          } else {
            if (
              data.form.planDemandSubTime &&
              (dayjs(value).isSameOrBefore(
                dayjs(data.form.planDemandSubTime)
              ) ||
                dayjs(value).diff(dayjs(data.form.planDemandSubTime), "month") <
                  1)
            ) {
              callback(new Error("完成采购时间必须大于提报需求时间一个月以上"));
            } else {
              callback();
            }
          }
        },
      },
    ],
    planDeliverTime: [
      {
        required: true,
        // message: "计划交付时间不能为空",
        trigger: "change",
        validator: (rule, value, callback) => {
          if (!value) {
            callback(new Error("请选择计划交付时间"));
          } else {
            if (
              data.form.planPurchaseFinishTime &&
              value !== data.form.planPurchaseFinishTime &&
              dayjs(value).isSameOrBefore(
                dayjs(data.form.planPurchaseFinishTime)
              )
            ) {
              callback(new Error("计划交付时间必须大于采购时间"));
            } else {
              callback();
            }
          }
        },
      },
    ],

    // planDemandSubTime   planPurchaseFinishTime   planDeliverTime
    projectAttr: [
      {
        type: "array",
        required: true,
        message: "项目属性不能为空",
        trigger: "change",
      },
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

function showDialog(name) {
  open.value = true;
  title.value = name;
}

function fetchPurchaseServicesOpt(projectType = "", evaluation = 0) {
  const data = {
    projectType,
    evaluation,
  };
  purchaseServices(data).then((response) => {
    purchaseServicesOpt.value = response.data;
  });
}

let serviceOpt = ref([]);
function fetchSelectServicesOpt(projectType = "", evaluation = 0) {
  const data = {
    projectType,
    evaluation,
  };
  purchaseServices(data).then((response) => {
    serviceOpt.value = response.data;
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

function downLoadExport() {
  execYearDownLoaddate({}).then((response) => {
    exportFile(response, "年度计划模板");
  });
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
  queryYearPlanList(
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
    isExcess: undefined,
    demandOverview: undefined,
    evaluation: undefined,
    purchaseMode: undefined,
    planDemandSubTime: undefined,
    planPurchaseFinishTime: undefined,
    planDeliverTime: undefined,
    projectAttr: undefined,
    remarks: undefined,
    projectAttr: [],
    organDivision: [],
    organDivisionName: [],
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

const datapopover = ref([]);
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

const deptOptions = ref([]);
const editDroppdown = ref({});
function queryDorpDown() {
  queryYearPlanDorpdown({}).then((response) => {
    let _projectTypes = [];
    Object.entries(response.data?.projectTypesAds || {}).map((entry) => {
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
    let _budgetAccounts = [];
    Object.entries(response.data?.budgetAccount || {}).map((entry) => {
      _budgetAccounts.push({ value: entry[1], label: entry[0] });
    });
    editDroppdown.value = {
      projectTypes: _projectTypes,
      projectStatus: _projectStatus,
      projectAttributes: _projectAttributes,
      purchaseServices: _purchaseServices,
      planPurchaseModes: _planPurchaseModes,
      budgetAccounts: _budgetAccounts,
    };
  });

  deptTreeSelect().then((response) => {
    let _vals = [];
    Object.keys(response.data).map((key) => {
      let _temps = response.data[key] || [];
      _vals.push({
        id: 100,
        label: key,
        children: _temps.map((m) => {
          return {
            id: m.id,
            label: m.organName,
          };
        }),
      });
    });
    deptOptions.value = _vals;
  });
}
queryDorpDown();

function nodes_flat(rootNode) {
  const result = [];
  const queue = [];
  queue.push(rootNode);
  while (true) {
    let count = queue.length;
    if (count === 0) break;
    while (count > 0) {
      let node = queue.shift();
      result.push(node);
      node.children?.forEach((child) => queue.push(child));
      count--;
    }
  }
  return result;
}

const needDeptIdData = ref("");
const needDeptNameData = ref("");
// /** 节点单击事件 */
// function handleNodeClick(data) {
//   form.value.organDivision = data.label;
//   needDeptIdData.value = data.id;
//   needDeptNameData.value = data.label;
// }
// function handleNodeClickForm(data) {
//   queryParams.value.deptId = data.label;
//   searchFormData.value = data.id;
// }

function handleNodeClickForm(data, index) {
  if (data.id == 100) {
    return;
  }
  if (!form.value?.organDivision) form.value.organDivision = [];
  if (!form.value?.organDivisionName) form.value.organDivisionName = [];
  if (form.value?.organDivision?.includes(data.id)) {
    proxy.$modal.msgError("该选项已被选择");
    return;
  }
  form.value.organDivision.push(data.id);
  form.value.organDivisionName.push(data.label);
  proxy.$refs["planYearRef"].validateField("organDivision");
}
// function handleCqClickForm(data){
//     getCqNodeCode.value.push(data.professionalCode)
//     console.log(getCqNodeCode.value,'getCqNod......................................valuegetCqNodeCode.valuegetCqNodeCode.value');
//     editForm.value.form.baseExpertList[0].majorType.push( data.label)
//     editForm.value.form.professional.push( data.label)
// }

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

/** 新增按钮操作 */
function handleAdd() {
  reset();
  fileList.value = [];
  showDialog("创建年度计划");
}
/** 修改按钮操作 */
function handleUpdate(row, hasApplay) {
  reset();
  fileList.value = [];
  queryYearPlanInfo({ planYearId: row.planYearId }).then((response) => {
    if (response.data.projectAttr) {
      response.data.projectAttr = response.data.projectAttr.split(",");
    }
    if (response.data.organDivision) {
      response.data.organDivision = response.data.organDivision
        .split(",")
        .map((m) => {
          return parseInt(m);
        });
    }
    // if (response.data.projectAttr) {
    //   response.data.organDivisionName = response.data.organDivisionName.split(",");
    // }
    // organDivision: [],
    // organDivisionName: [],
    fileList.value = (response.data?.fileInfos || []).map((m) => {
      return {
        name: m.fileName,
        url: m.fileUrl,
        ...m,
      };
    });

    form.value = response.data;
    let title = ref("");
    if (hasApplay) {
      title.value = "申请修改年度计划";
      form.value.applyUpdate = true;
    } else {
      title.value = "修改年度计划";
    }
    showDialog(title);
  });
}
/** 提交按钮 */
function submitForm() {
  proxy.$refs["planYearRef"].validate((valid) => {
    let _files = fileList.value.map((m) => {
      if (m.response) {
        let { fileId } = m.response.data;
        return {
          fileId: fileId,
          type: "1",
        };
      } else if (m.fileId) {
        return {
          fileId: m.fileId,
          type: "1",
        };
      }
    });
    if (valid) {
      if (form.value.planYearId != undefined) {
        if (form.value.applyUpdate) {
          // proxy.$modal.loading("申请修改中, 请稍候...");
          execYearPlanApplyUpdate({
            ...form.value,
            sysFileRefs: _files,
            projectAttr: form.value.projectAttr.join(","),
          }).then((response) => {
            // proxy.$modal.closeLoading();
            proxy.$modal.msgSuccess("申请修改成功");
            open.value = false;
            delete form.value.applyUpdate;
            getList();
          });
        } else {
          execYearPlanModify({
            ...form.value,
            sysFileRefs: _files,
            projectAttr: form.value.projectAttr.join(","),
            organDivision: form.value.organDivision.join(","),
          }).then((response) => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          });
        }
      } else {
        execYearPlanAdd({
          ...form.value,
          sysFileRefs: _files,
          projectAttr: form.value.projectAttr.join(","),
          organDivision: form.value.organDivision.join(","),
        }).then((response) => {
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
  deleteFlagId.value = row.planYearId;
  deleteFlag.value = true;
}
/** 提交按钮操作 */
function handleCommit(row) {
  submitFlagId.value = row.planYearId;
  submitFlag.value = true;
}
/** 导出按钮操作 */
function handleExport() {
  proxy.download1(
    "plan/planYears/exportPlan",
    { ...queryParams.value, pageSize: 99999 },
    `年度计划_${new Date().getTime()}.xlsx`
  );
}
/** 刷新缓存按钮操作 */
function handleRefreshCache() {
  refreshCache().then(() => {
    proxy.$modal.msgSuccess("刷新缓存成功");
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

const deleteFlag = ref(false);
const deleteFlagId = ref(undefined);
const submitFlag = ref(false);
const submitFlagId = ref(undefined);

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

function handleCommitYearPlan() {
  proxy.$modal.loading("提交中, 请稍候...");
  execYearPlanCommit({ planYearId: submitFlagId.value })
    .then((response) => {
      proxy.$modal.closeLoading();
      proxy.$modal.msgSuccess("提交成功");
      submitFlag.value = false;
      submitFlagId.value = undefined;
      getList();
    })
    .catch((err) => {
      console.log(err);
      proxy.$modal.closeLoading();
    });
}

const applyUpdateModal = ref({
  show: false,
  id: undefined,
});
function handleApplyUpdateShow(row) {
  applyUpdateModal.value = {
    show: true,
    id: row.planYearId,
  };
}

const planYearInfo = ref(null);
function handleYearPlanApplyUpdate() {
  let _reqParams = planYearInfo.value.fetchReqParams();
  if (!_reqParams.adjustReason) {
    proxy.$modal.msgError("请填写计划调整及理由");
    return;
  }
  proxy.$modal.loading("申请修改中, 请稍候...");
  execYearPlanApplyUpdate({ planYearId: _reqParams.id, ..._reqParams })
    .then((response) => {
      proxy.$modal.closeLoading();
      proxy.$modal.msgSuccess("申请修改成功");
      infoModal.value.show = false;
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
  operType: undefined,
});

function handleInfoShow(row, operType) {
  infoModal.value = {
    show: true,
    id: row.planYearId,
    operType: operType,
  };
}

function handleZdcUpdate() {
  if (!zdcModal.value.organDivision) {
    proxy.$modal.msgError("请选择机关业务指导处");
    return;
  }
  proxy.$modal.loading("修改中, 请稍候...");
  execUpdateZDC({
    planYearId: zdcModal.value.id,
    organDivision: zdcModal.value.organDivision.join(","),
  })
    .then((response) => {
      proxy.$modal.closeLoading();
      proxy.$modal.msgSuccess("修改成功");
      zdcModal.value.show = false;
      getList();
    })
    .catch((err) => {
      console.log(err);
      proxy.$modal.closeLoading();
    });
}
const zdcModal = ref({
  show: false,
  id: undefined,
  organDivision: [],
});

function handleZdcShow(row) {
  zdcModal.value = {
    show: true,
    id: row.planYearId,
    organDivision: row.organDivision
      ? row.organDivision.split(",").map((m) => {
          return parseInt(m);
        })
      : [],
  };
}

function handleNodeZdcClickForm(data, index) {
  if (data.id == 100) {
    return;
  }
  if (!zdcModal.value?.organDivision) form.value.organDivision = [];
  if (!zdcModal.value?.organDivisionName) form.value.organDivisionName = [];
  if (zdcModal.value?.organDivision?.includes(data.id)) {
    proxy.$modal.msgError("该选项已被选择");
    return;
  }
  zdcModal.value.organDivision.push(data.id);
  zdcModal.value.organDivisionName.push(data.label);
}

/** 导入按钮操作 */
function handleImport() {
  upload.title = "年度计划导入";
  upload.open = true;
}
/** 下载模板操作 */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `user_template_${new Date().getTime()}.xlsx`
  );
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
/** 提交上传文件 */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
}

const comDisabled = computed(() => {
  return !(form.value.evaluation && form.value.projectType);
});

fetchPurchaseServicesOpt();
getList();

function handleUndoApplyAction(id) {
  if (!id) return;
  return proxy.$modal
    .confirm(`您确定提交申请撤销吗？撤销后该条数据将删除?`)
    .then(
      () => {
        proxy.$modal.loading("撤销中, 请稍候...");
        execAppDelete({ planYearId: id })
          .then((response) => {
            proxy.$modal.closeLoading();
            proxy.$modal.msgSuccess("申请撤销已提交");
            getList();
          })
          .catch((err) => {
            console.log(err);
            proxy.$modal.closeLoading();
          });
      },
      () => false
    );
}
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
      top: 0px !important;
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
  }

  :deep(.plan-year-apply-update-modal) {
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
  
<style lang="scss">
.year-list-organ-division-select-none {
  display: none !important;
}
</style>