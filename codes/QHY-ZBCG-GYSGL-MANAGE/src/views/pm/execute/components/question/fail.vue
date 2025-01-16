<template>
    <el-dialog title="流标" v-model="myVisible" width="500px" align-center :close-on-click-modal="false" :modal-append-to-body="false" custom-class="cust-modal">
        <div style="padding: 30px 20px">
            <el-form :model="editForm.form" :rules="editForm.rules" ref="editFormRef" label-width="100px" class="demo-ruleForm">
                <el-row :gutter="24" >
                    <el-col :span="24">
                        <el-form-item label="流标原因" prop="bidFailureReason" :rules="[{ required: true, message: '请输入流标原因', trigger: 'change', } ]">
                            <el-input v-model="editForm.form.bidFailureReason" type="textarea" placeholder="请输入流标原因" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="流标时间" prop="bidFailureTime" :rules="[{ required: true, message: '请选择流标时间', trigger: 'change', } ]">
                            <el-date-picker
                                v-model="editForm.form.bidFailureTime"
                                :value-format="'YYYY-MM-DD'"
                                type="date"
                                placeholder="请选择流标时间"
                                format="YYYY-MM-DD"
                                class="w-100"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="项目附件" prop="projectFiles">
                            <el-upload
                                v-model:file-list="fileList"
                                class="upload-demo w-100"
                                :action="globalConfig.base_url + '/common/upload'"
                                :headers="fileHeaderParams" accept=".doc,docx,.pdf"
                                multiple
                                :before-remove="beforeRemove"
                            >
                                <el-button type="primary">点击上传</el-button>
                                <template #tip>
                                    <div
                                        class="el-upload__tip ml10"
                                        style="display: inline-block"
                                    >
                                        只能上传
                                        <span style="color: #E54242">doc/pdf</span> 文件
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
                <el-button @click="handleClose">取消</el-button>
                <el-button type="success" :loading="execLoadingRef" @click="handleExecOper">确定</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import { execInsertPmBidFailure } from "@/api/pm/execute";
import { getToken } from "@/utils/auth";
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
        fileList.value = [];
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
                bidWinningId: undefined,
                bidWinningAmount: undefined,
            }
        ]
    },
    rules: {
    },
})

function handleAddJoinPerson() {
    editForm.value.form.pmBidOpeningList.push({
        bidWinningId: undefined,
        bidWinningAmount: undefined,
    })
}

function handleRemoveJoinPerson(index) {
    editForm.value.form.pmBidOpeningList.splice(index, 1);
}


/** 表单重置 */
function handleFormReset() {
    fileList.value = [];
    editForm.value.form = {
        demandId: undefined,
        pmBidOpeningList: [
            {
                bidWinningId: undefined,
                bidWinningAmount: undefined,
            }
        ]
    };
    proxy.resetForm("editFormRef");
}

const fileList = ref([]);
const fileHeaderParams = ref({
    Authorization: "Bearer " + getToken(),
});
function beforeRemove(uploadFile, uploadFiles) {
    return proxy.$modal.confirm(`是否要删除文件: ${uploadFile.name} ?`).then(
        () => true,
        () => false
    );
}

const execLoadingRef = ref(false)
/** 提交按钮 */
function handleExecOper() {
    execLoadingRef.value = true
    proxy.$refs["editFormRef"].validate((valid) => {
        if (valid) {
            let _files = fileList.value.map(m => {
                if (m.response && m.response.data) {
                    let { fileId } = m.response.data;
                    return { fileId: fileId, }
                } else if (m.fileId) {
                    return { fileId: m.fileId, }
                }
            })
            execInsertPmBidFailure({ ...editForm.value.form, sysFileRefs: _files, demandId: props.recordId }).then((response) => {
                proxy.$modal.msgSuccess("设置流标状态成功");
                emit('on-confirm')
                handleClose();
            }).catch(() => {
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