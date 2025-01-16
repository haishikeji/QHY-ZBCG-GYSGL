<template>
    <el-dialog title="选取代理" v-model="myVisible" width="500px" align-center :close-on-click-modal="false" :modal-append-to-body="false" custom-class="cust-modal">
        <div style="padding: 30px 20px">
            <el-form :model="editForm.form" :rules="editForm.rules" ref="editFormRef" label-width="100px" class="demo-ruleForm">
                <el-form-item label="采购任务文号" prop="purchaseTaskDocumentNumber">
                    <el-input v-model="editForm.form.purchaseTaskDocumentNumber" placeholder="请输入采购任务文号" />
                </el-form-item>
                <el-form-item label="选取方式" prop="mode">
                    <el-radio-group v-model="editForm.form.mode">
                        <el-radio :label="0">填写招标代理机构信息</el-radio>
                        <el-radio :label="1">抽取招标代理机构</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item v-if="editForm.form.mode === 0" label="招标代理机构" prop="agencyId">
                    <el-select v-model="editForm.form.agencyId" filterable placeholder="请选择招标代理机构">
                        <el-option v-for="(org, index) in orgs" :key="index" :label="org.companyName" :value="org.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item v-if="editForm.form.mode === 1" label=" ">
                    <span style="line-height: 22px; font-size: 13px; color: #7e7e7e;">提示：“抽取招标代理机构”将从招标代理库中随机抽取1家代理机构，点击确定后进行抽取</span>
                </el-form-item>
            </el-form>
        </div>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="handleClose">取消</el-button>
                <el-button type="success" :loading="execLoadingRef" @click="handleExecOper">确定</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import { queryAgencyList, execExtractAgency, execSelectAgency } from "@/api/pm/execute";
const { proxy } = getCurrentInstance();
const props = defineProps({
    // 是否展现
    modelValue: {
        type: Boolean,
        default: false
    },
    // 当前记录ID
    recordId: {
        type: Number,
        require: true
    }
});

const emit = defineEmits(['update:modelValue'])

let myVisible = computed({
    get: function() {
        return props.modelValue
    },
    set: function(val) {
        emit('update:modelValue', val)
    }
})

const editForm = ref({
    form: {
        mode: 0,
        agencyId: undefined,
        purchaseTaskDocumentNumber: undefined
    },
    rules: {
        mode: [
            { required: true, message: '请选择选取方式', trigger: 'change' }
        ]
    },
})

const orgs = ref([])
function queryDorpDown() {
    if (!props.recordId) return;
    queryAgencyList({ demandId: props.recordId }).then((resp) => {
        orgs.value = resp.data || []
    });
}

watch(() => props.recordId, (value) => queryDorpDown(), { immediate: true });


/** 表单重置 */
function handleFormReset() {
    editForm.value.form = {
        mode: 0,
        agencyId: undefined,
    };
    proxy.resetForm("editFormRef");
}

const execLoadingRef = ref(false)
/** 提交按钮 */
function handleExecOper() {
    execLoadingRef.value = true
    proxy.$refs["editFormRef"].validate((valid) => {
        if (valid) {
            if (editForm.value.form.mode === 0) {
                if (!editForm.value.form.agencyId) {
                    execLoadingRef.value = false
                    proxy.$modal.msgError("请选择招标代理机构");
                    return;
                }
                execSelectAgency({ demandId: props.recordId, agencyId: editForm.value.form.agencyId, purchaseTaskDocumentNumber: editForm.value.form.purchaseTaskDocumentNumber }).then((response) => {
                    proxy.$modal.msgSuccess("选取成功");
                    emit('on-refresh')
                    handleClose();
                }).catch((err) => {
                    console.log(err);
                    execLoadingRef.value = false
                });
            } else if (editForm.value.form.mode === 1) {
                execExtractAgency({ demandId: props.recordId, purchaseTaskDocumentNumber: editForm.value.form.purchaseTaskDocumentNumber }).then((response) => {
                    proxy.$modal.msgSuccess("选取成功");
                    emit('on-refresh')
                    handleClose();
                }).catch(() => {
                    console.log(err);
                    execLoadingRef.value = false
                });
            }
        } else {
            execLoadingRef.value = false
        }
    });
}

function handleClose() {
    handleFormReset();
    execLoadingRef.value = false
    myVisible.value = false
}

function resetLoadingStatus() {
    execLoadingRef.value = false
}

defineExpose({
    handleClose, resetLoadingStatus
})
</script>

<style lang="scss">
</style>