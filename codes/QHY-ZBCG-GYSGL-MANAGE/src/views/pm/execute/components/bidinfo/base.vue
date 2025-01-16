<template>
    <el-dialog title="发布中标公告" v-model="myVisible" width="700px" align-center :close-on-click-modal="false" :modal-append-to-body="false" custom-class="cust-modal">
        <div style="padding: 30px 20px">
            <el-tabs v-model="tab.current" type="card" @tab-click="handleTabChange">
                <el-tab-pane label="预中标" name="adopt"></el-tab-pane>
                <el-tab-pane label="流废标" name="fail"></el-tab-pane>
            </el-tabs>
            <el-form v-if="tab.current === 'adopt'" :model="editForm.form" :rules="editForm.rules" ref="editFormRef" label-width="140px" class="demo-ruleForm">
                <el-row :key="index" :gutter="24" >
                    <el-col :span="16">
                        <el-form-item label="中标单位信息">
                            <div>
                                <div v-for="(unit, index) in dropdown.unitList" :key="index">第{{index+1}}名 - {{ unit.bidderName }} - {{ unit.score }}分</div>
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                    </el-col>
                    <el-col :span="16">
                        <el-form-item label="选择中标金额或备注" prop="chooseMode">
                            <!-- <el-select v-model="editForm.form.pmBidOpeningIdList" filterable multiple placeholder="请选择中标单位信息">
                                <el-option v-for="(item, index) in dropdown.unitList" :key="index" :label="item.bidderName" :value="item.id"></el-option>
                            </el-select> -->
                            <el-radio-group v-model="editForm.form.chooseMode" class="ml-4">
                                <el-radio label="1" size="large">中标金额</el-radio>
                                <el-radio label="2" size="large">备注</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                    </el-col>
                    <el-col v-if="editForm.form.chooseMode === '1'" :span="16">
                        <el-form-item label="中标金额" prop="bidWinningAmount" :rules="[{ required: true, message: '请输入中标金额', trigger: 'blur', } ]">
                            <el-input v-model="editForm.form.bidWinningAmount" placeholder="请输入中标金额" type="number" >
                                <template #append>万元</template>
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col v-if="editForm.form.chooseMode === '2'" :span="16">
                        <el-form-item label="备注" prop="remarks" :rules="[{ required: true, message: '请输入备注', trigger: 'blur', } ]">
                            <el-input v-model="editForm.form.remarks" placeholder="请输入备注" ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                    </el-col>
                    <!-- <el-col :span="16">
                        <el-form-item label="中标金额" prop="bidWinningAmount" :rules="[{ required: true, message: '请填写中标金额', trigger: 'blur', } ]">
                            <el-input v-model="editForm.form.bidWinningAmount" type="number" placeholder="请输入中标金额" >
                                <template #append>万元</template>
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                    </el-col> -->
                    <el-col :span="16">
                        <el-form-item label="中标公告时间" prop="bidAnnouncementTime" :rules="[{ required: true, message: '请选择中标公告时间', trigger: 'change', } ]">
                            <el-date-picker
                                v-model="editForm.form.bidAnnouncementTime"
                                :value-format="'YYYY-MM-DD'"
                                type="date"
                                placeholder="请选择中标公告时间"
                                format="YYYY-MM-DD"
                                class="w-100"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                    </el-col>
                    <el-col :span="16">
                        <el-form-item label="中标公告截止时间" prop="bidAnnouncementCloseTime" :rules="[{ required: true, message: '请选择中标公告截止时间', trigger: 'change', } ]">
                            <el-date-picker
                                v-model="editForm.form.bidAnnouncementCloseTime"
                                :value-format="'YYYY-MM-DD'"
                                type="date"
                                placeholder="请选择中标公告截止时间"
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
            <el-form v-if="tab.current === 'fail'" :model="editForm.form" :rules="editForm.rules" ref="editFormRef" label-width="100px" class="demo-ruleForm">
                <el-row :gutter="24" >
                    <el-col :span="16">
                        <el-form-item label="流标原因" prop="bidFailureReason" :rules="[{ required: true, message: '请输入流标原因', trigger: 'change', } ]">
                            <el-input v-model="editForm.form.bidFailureReason" type="textarea" placeholder="请输入流标原因" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="16">
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
import { execInsertPmBidWinning, execInsertPmBidFailure, fetchPullDownBidOpeningList } from "@/api/pm/execute";
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
        chooseMode: '1',
        pmBidOpeningIdList: [],
        bidFailureReason: undefined,
        bidFailureTime: undefined,
    },
    rules: {
    },
})

const tab = ref({
    current: 'adopt'
})
function handleTabChange(tab) {
    fileList.value = [];
    setTimeout(() => {
        handleFormReset();
    }, 10)
}

function handleAddJoinPerson() {
    editForm.value.form.pmBidOpeningList.push({
        bidderName: undefined,
        score: undefined,
    })
}

function handleRemoveJoinPerson(index) {
    editForm.value.form.pmBidOpeningList.splice(index, 1);
}

const dropdown = ref({})
function queryDropDown() {
    fetchPullDownBidOpeningList({ demandId: props.recordId }).then((response) => {
        dropdown.value.unitList = (response.data || []).splice(0, 3);
    });
}
queryDropDown()


/** 表单重置 */
function handleFormReset() {
    editForm.value.form = {
        demandId: undefined,
        chooseMode: '1',
        pmBidOpeningIdList: [],
        bidFailureReason: undefined,
        bidFailureTime: undefined,
    };
    proxy.resetForm("editFormRef");
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
            editForm.value.form.pmBidOpeningIdList = dropdown.value.unitList.map(m => m.id)
            if (tab.value.current === 'adopt') {
                execInsertPmBidWinning({ ...editForm.value.form, sysFileRefs: _files, demandId: props.recordId }).then((response) => {
                    proxy.$modal.msgSuccess("设置中标状态成功");
                    emit('on-refresh')
                    handleClose();
                }).catch((err) => {
                    console.log(err);
                    execLoadingRef.value = false
                });
            } else if (tab.value.current === 'fail') {
                execInsertPmBidFailure({ ...editForm.value.form, sysFileRefs: _files, demandId: props.recordId }).then((response) => {
                    proxy.$modal.msgSuccess("设置流标状态成功");
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