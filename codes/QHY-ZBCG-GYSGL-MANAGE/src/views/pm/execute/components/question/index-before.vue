<template>
    <el-dialog title="标前质疑投诉" v-model="myVisible" width="500px" align-center :close-on-click-modal="false" :modal-append-to-body="false" custom-class="cust-modal">
        <div style="padding: 30px 20px">
            <el-form :model="editForm.form" :rules="editForm.rules" ref="editFormRef" label-width="140px" class="demo-ruleForm">
                <el-form-item label="是否有质疑情况" prop="mode">
                    <el-radio-group v-model="editForm.form.mode">
                        <el-radio :label="1">是</el-radio>
                        <el-radio :label="0">否</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item v-if="editForm.form.mode === 1" label="质疑时间" prop="callQuestionTime" :rules="[{ required: true, message: '请选择质疑时间', trigger: 'change', } ]">
                    <el-date-picker
                        v-model="editForm.form.callQuestionTime"
                        :value-format="'YYYY-MM-DD'"
                        type="date"
                        placeholder="请选择质疑时间"
                        format="YYYY-MM-DD"
                        class="w-100"
                    ></el-date-picker>
                </el-form-item>
                <el-form-item v-if="editForm.form.mode === 1" label="上传质疑情况附件" prop="projectFiles">
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
import CommonModalMinix from "@/mixins/modal/index";
import { execHandlePreCallQuestion } from "@/api/pm/execute";
import { getToken } from "@/utils/auth";
import { beforeUpload } from "@/utils/index";
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
        mode: 0,
        callQuestionTime: undefined
    },
    rules: {
        mode: [
            { required: true, message: '请选择选取方式', trigger: 'change' }
        ]
    },
})

/** 表单重置 */
function handleFormReset() {
    fileList.value = [];
    editForm.value.form = {
        mode: 0,
        callQuestionTime: undefined
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
            execHandlePreCallQuestion({ demandId: props.recordId, callQuestion: editForm.value.form.mode, 
                callQuestionTime: editForm.value.form.callQuestionTime, sysFileRefs: _files }).then((response) => {
                proxy.$modal.msgSuccess("质疑处理成功");
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

const globalModal = ref({
    fail: { show: false },
    modify: { show: false },
})

const { execModalDisplayChange } = CommonModalMinix(proxy, globalModal)

function handleClose() {
    handleFormReset();
    execLoadingRef.value = false
    myVisible.value = false
}

function handleClose1() {
    emit('on-refresh');
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