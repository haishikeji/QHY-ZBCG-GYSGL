<template>
    <div>
        <div class="title">
            项目计划
        </div>
        <el-form :model="form" label-position="right" label-width="130" >
            <el-row :gutter="24">
                <el-col :span="12">
                    <el-form-item label="项目名称" prop="projectName">
                        <el-input v-model="voInfo.projectName" placeholder="" readonly />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="项目类型" prop="projectType">
                        <el-select
                            v-model="voInfo.projectType"
                            placeholder="" :disabled="operType !== 'examine'" @change="(val) => emit('on-project-type-change', val)"
                        >
                            <el-option
                                v-for="item in droppdown.projectTypes"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="概算金额" prop="evaluation">
                        <el-input v-model="voInfo.evaluation" placeholder="" readonly>
                            <template #append>万元</template>
                        </el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="采购服务站" prop="purchaseServices">
                        <el-select
                            v-model="voInfo.purchaseServices"
                            placeholder="" disabled
                        >
                            <el-option
                                v-for="item in droppdown.purchaseServices"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="机关业务指导处" prop="organDivision">
                    <el-select
                        v-model="voInfo.organDivision"
                        placeholder="请选择机关业务指导处(科)"
                        style="width: 100%"
                        multiple
                        disabled
                        class="year-list-organ-division-select"
                    >
                        <el-option
                            v-for="item in nodes_flat({ children: deptOptions })"
                            :key="item.id"
                            :label="item.label"
                            :value="item.id"
                            style="display: none" class="year-list-organ-division-select-none"
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
                            @node-click="(data) => { handleNodeClickForm(data, index) }"
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
                        v-model="voInfo.budgetAccount"
                        placeholder="请选择预算科目" disabled
                    >
                        <el-option
                        v-for="item in droppdown.budgetAccounts"
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
                            v-model="voInfo.purchaseMode"
                            placeholder="" disabled
                        >
                            <el-option
                                v-for="item in droppdown.planPurchaseModes"
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
                            v-model="voInfo.planDemandSubTime" :value-format="'YYYY-MM'"
                            type="month"
                            placeholder=""
                            format="YYYY-MM"
                            class="w-100" readonly
                        />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="完成采购时间" prop="planPurchaseFinishTime">
                        <el-date-picker
                            v-model="voInfo.planPurchaseFinishTime" :value-format="'YYYY-MM'"
                            type="month"
                            placeholder=""
                            format="YYYY-MM"
                            class="w-100" readonly
                        />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="交付实施时间" prop="planDeliverTime">
                        <el-date-picker
                            v-model="voInfo.planDeliverTime" :value-format="'YYYY-MM'"
                            type="month"
                            placeholder=""
                            format="YYYY-MM"
                            class="w-100" readonly
                        />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="项目属性" prop="projectAtt">
                        <el-select
                            v-model="voInfo.projectAttr"
                            placeholder="" disabled multiple
                        >
                            <el-option
                                v-for="item in droppdown.projectAttributes"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <!-- <el-col :span="12">
                    <el-form-item label="是否为超限额计划" prop="isExcess">
                        <el-select v-model="voInfo.isExcess" placeholder="请选择是否为超限额计划" disabled>
                            <el-option label="未超额" value="0" ></el-option>
                            <el-option label="超额" value="1" ></el-option>
                        </el-select>
                    </el-form-item>
                </el-col> -->
                <el-col :span="24">
                    <el-form-item label="需求概况" prop="demandOverview">
                        <el-input
                            v-model="voInfo.demandOverview"
                            type="textarea"
                            placeholder="" readonly
                        />
                    </el-form-item>
                </el-col>
                <el-col v-if="operType === 'applyModify' || voInfo.adjustReason" :span="24">
                    <el-form-item label="计划调整及理由" prop="adjustReason">
                        <el-input
                            v-model="voInfo.adjustReason"
                            type="textarea"
                            placeholder="" :readonly="operType !== 'applyModify'"
                        />
                    </el-form-item>
                </el-col>
                <el-col v-if="voInfo.remarks" :span="24">
                    <el-form-item label="备注" prop="remarks">
                        <el-input
                            v-model="voInfo.remarks"
                            type="textarea"
                            placeholder="" readonly
                        />
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <div v-if="voInfo.fileInfos && voInfo.fileInfos.length" class="title mt-20">
            项目附件
        </div>
        <div class="files">
            <div v-for="(item, index) in voInfo.fileInfos" :key="index" class="file-item" @click="handlePreviewFile(item.fileUrl)">
                <img src="/images/common/icon-file-pdf.png" style="width: 50px; height: 53px;">
                <div class="text">{{ item?.fileName }}</div>
            </div>
        </div>
        <div v-if="voInfo.refuseReason && voInfo.projectStatus === '2'" class="title mt-20">
            退回原因
        </div>
        <el-input v-if="voInfo.refuseReason && voInfo.projectStatus === '2'"
            type="textarea"
            :value="voInfo.refuseReason"
            :rows="15"
            placeholder="" readonly
        />
    </div>
</template>

<script setup>
import {
    queryYearPlanDorpdown, queryYearPlanInfo
} from "@/api/plan/year";
// import {
//     deptTreeSelectAndF as deptTreeSelect,
// } from "@/api/system/user";
import { 
  deptOrganMap as deptTreeSelect,
} from "@/api/system/dept";

const emit = defineEmits([ 'on-project-type-change' ])
const props = defineProps({
    // 当前的值
    recordId: {
        type: Number,
        require: true
    },
    operType: {
        type: String,
        default: ''
    }
});

watch(() => props.recordId, value => init(value), { immediate: true })

const voInfo = ref({})
function init(id) {
    queryYearPlanInfo({ planYearId: props.recordId }).then(res => {
        if (res.data.projectAttr) {
            res.data.projectAttr =  res.data.projectAttr.split(res.data.projectAttr.indexOf('，') !== -1 ? '，' : ',')
        }
        if (res.data.organDivision) {
            res.data.organDivision = res.data.organDivision.split(",").map(m => {
                return parseInt(m);
            });
        }
        voInfo.value = res.data;
    });
}
// init()

const deptOptions = ref([]);
const droppdown = ref({});
/** 新增按钮操作 */
function loadDropdownContent() {
    queryYearPlanDorpdown({}).then((response) => {
        let _projectTypes = [];
        if (props.operType === 'examine') {
            Object.entries(response.data?.projectTypes || {}).map((entry) => {
                _projectTypes.push({ value: entry[1], label: entry[0] });
            });
        } else {
            Object.entries(response.data?.projectTypesAds || {}).map((entry) => {
                _projectTypes.push({ value: entry[1], label: entry[0] });
            });
        }
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
        droppdown.value = {
            projectTypes: _projectTypes,
            projectStatus: _projectStatus,
            projectAttributes: _projectAttributes,
            purchaseServices: _purchaseServices,
            planPurchaseModes: _planPurchaseModes,
            budgetAccounts: _budgetAccounts
        };
    });
    
  
  deptTreeSelect().then((response) => {
    // deptOptions.value = response.data.map(m => {
    //   return {
    //     ...m,
    //     id: m.id
    //   }
    // });
    let _vals = [];
    Object.keys(response.data).map(key => {
      let _temps = response.data[key] || [];
      _vals.push({
        id: 100,
        label: key,
        children: _temps.map(m => {
          return {
            id: m.id,
            label: m.organName
          }
        })
      })
    });
    deptOptions.value = _vals;
  });
}
loadDropdownContent();


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
            node.children?.forEach(child => queue.push(child));
            count--
        }
    }
    return result;
}

function handlePreviewFile(path) {
    if (!path) return;
    window.open(path);
}

function fetchReqParams() {
    return {
        id: props.recordId,
        adjustReason: voInfo.value.adjustReason,
    }
}

defineExpose({ fetchReqParams })

</script>

<style lang="scss" scoped>
.title {
    position: relative;
    width: 100%;
    padding: 10px 10px 10px 20px;
    box-sizing: border-box;
    background: linear-gradient(270deg, rgba(218,231,246,0) 0%, #E3EBF4 100%);
    font-weight: bold;
    margin-bottom: 20px;
    &::before {
        content: ' ';
        position: absolute;
        left: 0px;
        width: 5px;
        height: 100%;
        top: 0px;
        background: #006aea;
    }
}

.files {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    padding-left: 120px;
    box-sizing: border-box;
    .file-item {
        width: 100px;
        text-align: center;
        cursor: pointer;
        .text {
            margin-top: 8px;
        }
    }
}
</style>