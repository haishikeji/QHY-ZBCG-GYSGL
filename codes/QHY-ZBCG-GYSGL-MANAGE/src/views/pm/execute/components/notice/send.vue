<template>
    <el-dialog title="发布招标公告" v-model="myVisible" width="700px" align-center :close-on-click-modal="false" :modal-append-to-body="false" custom-class="cust-modal">
        <div style="padding: 30px 20px">
            <el-form
                ref="editFormRef"
                :model="editForm.form"
                :rules="editForm.rules"
                label-position="right"
                label-width="110"
            >
                <el-row :gutter="24">
                    <el-col :span="12">
                        <el-form-item label="发布公告平台" prop="announcementPlatform">
                            <el-select v-model="editForm.form.announcementPlatform" filterable placeholder="请选择发布公告平台">
                                <el-option label="军队采购网" :value="1"></el-option>
                                <el-option label="全军武器装备采购网" :value="2"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="报名截止时间" prop="enrollCloseTime">
                            <el-date-picker
                                v-model="editForm.form.enrollCloseTime"
                                :value-format="'YYYY-MM-DD'"
                                type="date"
                                placeholder="请选择报名截止时间"
                                format="YYYY-MM-DD"
                                class="w-100"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="发布时间" prop="releaseTime">
                            <el-date-picker
                                v-model="editForm.form.releaseTime"
                                :value-format="'YYYY-MM-DD'"
                                type="date"
                                placeholder="请选择发布时间"
                                format="YYYY-MM-DD"
                                class="w-100"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="开标时间" prop="tenderOpenTime">
                            <el-date-picker
                                v-model="editForm.form.tenderOpenTime"
                                :value-format="'YYYY-MM-DD'"
                                type="date"
                                placeholder="请选择开标时间"
                                format="YYYY-MM-DD"
                                class="w-100"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="开标地点" prop="bidOpenPlace">
                            <el-input v-model="editForm.form.bidOpenPlace" placeholder="请输入开标地点" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="备注" prop="remarks">
                            <el-input
                                v-model="editForm.form.remarks"
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
import { execInsertPmReleaseAnnouncement } from "@/api/pm/execute";
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
        announcementPlatform: 1,
        bidOpenPlace: undefined,
        enrollCloseTime: undefined,
        releaseTime: undefined,
        remarks: undefined,
        tenderOpenTime: undefined,
        sysFileRefs: []
    },
    rules: {
        bidOpenPlace: [
            { required: true, message: '请输入开标地点', trigger: 'blur' }
        ],
        enrollCloseTime: [
            { required: true, message: '请选择报名截止时间', trigger: 'change' }
        ],
        releaseTime: [
            { required: true, message: '请输入发布时间', trigger: 'blur' }
        ],
        tenderOpenTime: [
            { required: true, message: '请选择开标时间', trigger: 'change' }
        ]
    },
})

/** 表单重置 */
function handleFormReset() {
    fileList.value = [];
    editForm.value.form = {
        announcementPlatform: 1,
        bidOpenPlace: undefined,
        enrollCloseTime: undefined,
        releaseTime: undefined,
        remarks: undefined,
        tenderOpenTime: undefined,
        sysFileRefs: []
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
            execInsertPmReleaseAnnouncement({ ...editForm.value.form, sysFileRefs: _files, demandId: props.recordId }).then((response) => {
                proxy.$modal.msgSuccess("发布公告成功");
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