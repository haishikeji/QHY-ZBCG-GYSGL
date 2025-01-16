<template>
    <el-dialog :title="title" :model-value="modelValue" width="390px" append-to-body align-center :close-on-click-modal="false" 
        class="plan-year-submit-modal" style="margin-top: auto !important;" @close="handleCloseAction">
        <div class="dialog_body">
            <img src="/images/common/icon-warning.png" >
            <div class="msg">
                您确定要提交吗？
            </div>
        </div>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="handleCloseAction">取 消</el-button>
                <el-button type="primary" @click="handleConfirmAction">确 定</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
const emit = defineEmits([ 'on-close', 'on-confirm' ])
const props = defineProps({
    modelValue: Boolean,
    title: String,
});

function handleCloseAction() {
    emit('on-close');
}
function handleConfirmAction() {
    emit('on-confirm');
}
</script>

<style lang="scss">
.plan-year-submit-modal {
    padding: 0px 0px 10px 0px;
    border-radius: 10px;
    // margin-top: initial !important;
    .el-dialog__header {
        text-align: center;
        border-bottom: 0px;
    }
    .el-dialog__footer {
        border-top: 0px;
    }
    .dialog_body {
        display: flex;
        flex-direction: row;
        align-items: center;
        padding: 30px;
        border-radius: 20px;
        text-align: center;
        .msg {
            display: inline-block;
            margin-left: 10px;
        }
    }
}
</style>
