<template>
    <el-dialog :title="dialogTitle" v-model="myVisible" width="800px" align-center :close-on-click-modal="false" class="pm-construction-edit">
        <div class="dialog_box">
            <el-form
                ref="editFormRef"
                :model="editForm.form"
                :rules="editForm.rules"
                label-position="right"
                label-width="110"
            >
                <el-row :gutter="24">
                    <el-col :span="12">
                        <el-form-item label="开始时间" prop="startTime">
                            <el-date-picker
                                v-model="editForm.form.startTime"
                                :value-format="'YYYY-MM-DD'"
                                type="date"
                                placeholder="请选择开始时间"
                                format="YYYY-MM-DD"
                                class="w-100"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="初步验收时间" prop="preliminaryInspectionTime">
                            <el-date-picker
                                v-model="editForm.form.preliminaryInspectionTime"
                                :value-format="'YYYY-MM-DD'"
                                type="date"
                                placeholder="请选择初步验收时间"
                                format="YYYY-MM-DD"
                                class="w-100"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="终验时间" prop="finalInspectionTime">
                            <el-date-picker
                                v-model="editForm.form.finalInspectionTime"
                                :value-format="'YYYY-MM-DD'"
                                type="date"
                                placeholder="请选择终验时间"
                                format="YYYY-MM-DD"
                                class="w-100"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="结束时间" prop="endTime">
                            <el-date-picker
                                v-model="editForm.form.endTime"
                                :value-format="'YYYY-MM-DD'"
                                type="date"
                                placeholder="请选择结束时间"
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
                                :headers="fileHeaderParams"
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
                <el-button type="success" :loading="execLoadingRef" @click="handleSubmitForm">确定</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import { getToken } from "@/utils/auth";
import { execProjectConstruction, fetchInfo } from "@/api/pm/construction";

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

const dialogTitle = ref('建设文档填制');

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
        startTime: undefined,
        preliminaryInspectionTime: undefined,
        finalInspectionTime: undefined,
        endTime: undefined,
        sysFileRefs: []
    },
    rules: {
        startTime: [
            { required: true, message: "开始时间不能为空", trigger: "blur" },
        ],
        preliminaryInspectionTime: [
            { required: true, message: "初步验收时间不能为空", trigger: "blur" },
        ],
        finalInspectionTime: [
            { required: true, message: "终验时间不能为空", trigger: "blur" },
        ],
        endTime: [
            { required: true, message: "结束时间不能为空", trigger: "blur" },
        ]
    },
})

/** 表单重置 */
function handleFormReset() {
    editForm.value.form = {
    };
    proxy.resetForm("editFormRef");
}

/** 修改按钮操作 */
function queryInfo() {
    handleFormReset();
    fileList.value = [];
    fetchInfo({ demandId: props.recordId, detailType: 6 }).then((response) => {
        fileList.value = (response.data?.fileInfos || []).map(m => {
            return {
                name: m.fileName,
                url: m.fileUrl,
                ...m
            }
        })
        editForm.value.form = response.data;
        dialogTitle.value = "建设文档填制";
    });
}

watch(() => props.recordId, value => queryInfo())

const execLoadingRef = ref(false)
/** 提交按钮 */
function handleSubmitForm() {
    execLoadingRef.value = true
    proxy.$refs["editFormRef"].validate((valid) => {
        let _files = fileList.value.map(m => {
            if (m.response) {
                let { fileId } = m.response.data;
                return { fileId: fileId, }
            } else if (m.fileId) {
                return { fileId: m.fileId, }
            }
        })
        if (valid) {
            execProjectConstruction({ demandId: props.recordId, ...editForm.value.form, sysFileRefs:_files }).then((response) => {
                proxy.$modal.msgSuccess("建设文档填制成功");
                emit('on-refresh')
                handleClose();
            }).catch(err => {
                proxy.$modal.msgError("建设文档填制失败");
                console.log(err);
                handleClose();
            });
        } else {
            execLoadingRef.value = false
        }
    });
}

// 文件上传
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

function handleClose() {
    execLoadingRef.value = false
    myVisible.value = false
}

defineExpose({
    handleClose
})
</script>

<style lang="scss">
.pm-construction-edit {
    .el-dialog__headerbtn {
        top: 0 !important;
    }
}
</style>