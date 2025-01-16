<template>
    <el-dialog :title="title" :model-value="modelValue" width="390px" append-to-body align-center :close-on-click-modal="false" class="global-delete" style="margin-top: auto !important;">
        <div class="dialog_body">
            <img src="/images/common/icon-delete.png" >
            <div class="msg">
                {{ subtitle || '您确定要删除此内容吗?' }}
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
    subtitle: String,
});

function handleCloseAction() {
    emit('on-close');
}
function handleConfirmAction() {
    emit('on-confirm');
}
</script>

<style lang="scss">
.global-delete {
    padding: 0px 0px 10px 0px;
    border-radius: 10px;
    // margin-top: initial !important;
    .el-dialog__header {
        text-align: center;
        border-bottom: 0px;
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
</style>
