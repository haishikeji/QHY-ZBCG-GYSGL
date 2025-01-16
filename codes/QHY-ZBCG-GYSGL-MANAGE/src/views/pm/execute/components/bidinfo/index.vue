<template>
    <el-dialog title="开评标" v-model="myVisible" width="700px" align-center :close-on-click-modal="false" :modal-append-to-body="false" custom-class="cust-modal">
        <div style="padding: 30px 20px">
            <el-form :model="editForm.form" :rules="editForm.rules" ref="editFormRef" label-width="100px" class="demo-ruleForm">
                <el-row v-for="(row, index) in editForm.form.pmBidOpeningList" :key="index" :gutter="0" >
                    <el-col :span="10">
                        <el-form-item label="投标人名称" :prop="'pmBidOpeningList.' + index + '.bidderName'"
                            :rules="[{ required: true, message: '请填写投标人名称', trigger: 'blur', } ]">
                            <el-input v-model="row.bidderName" placeholder="请输入投标人名称" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="10">
                        <el-form-item label="评分" :prop="'pmBidOpeningList.' + index + '.score'"
                            :rules="[{ required: true, message: '请填写评分', trigger: 'blur', } ]">
                            <el-input v-model="row.score" type="number" placeholder="请输入评分" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="4" style="text-align: right">
                        <el-button v-if="index === 0" type="warning" @click="handleAddJoinPerson">新增</el-button>
                        <el-button v-if="index > 0" type="danger" @click="handleRemoveJoinPerson(index)">删除</el-button>
                    </el-col>
                </el-row>
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
import { queryAgencyList, execInsertBidOpeningBatch } from "@/api/pm/execute";
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
        demandId: undefined,
        pmBidOpeningList: [
            {
                bidderName: undefined,
                score: undefined,
            }
        ]
    },
    rules: {
    },
})

function handleAddJoinPerson() {
    editForm.value.form.pmBidOpeningList.push({
        bidderName: undefined,
        score: undefined,
    })
}

function handleRemoveJoinPerson(index) {
    editForm.value.form.pmBidOpeningList.splice(index, 1);
}


/** 表单重置 */
function handleFormReset() {
    editForm.value.form = {
        demandId: undefined,
        pmBidOpeningList: [
            {
                bidderName: undefined,
                score: undefined,
            }
        ]
    };
    proxy.resetForm("editFormRef");
}

const execLoadingRef = ref(false)
/** 提交按钮 */
function handleExecOper() {
    execLoadingRef.value = true
    proxy.$refs["editFormRef"].validate((valid) => {
        if (valid) {
            execInsertBidOpeningBatch({ ...editForm.value.form, demandId: props.recordId }).then((response) => {
                proxy.$modal.msgSuccess("填写开标信息成功");
                emit('on-refresh')
                handleClose();
            }).catch((err) => {
                console.log(err);
                execLoadingRef.value = false
            });
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