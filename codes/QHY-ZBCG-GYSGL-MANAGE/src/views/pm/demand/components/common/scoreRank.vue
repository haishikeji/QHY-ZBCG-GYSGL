<template>
    <el-dialog title="开标信息" v-model="myVisible" width="700px" align-center :close-on-click-modal="false" :modal-append-to-body="false" custom-class="cust-modal">
        <div style="padding: 30px 20px">
          <el-table stripe :data="voRecords" highlight-current-row style="width: 100%;" class="table-min-height" >
              <el-table-column type="index" label="序号" width="55" align="center" fixed="left" />
              <el-table-column label="排名" align="center" prop="bidderName" >
                <template #default="scope">
                    第{{ scope.$index + 1 }}名
                </template>
                </el-table-column>
              <el-table-column label="单位" align="center" prop="bidderName" width="170" />
              <el-table-column label="分数" align="center" prop="score" >
                <template #default="scope">
                    {{ scope.row.score }}分
                </template>
                </el-table-column>
          </el-table>
        </div>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="handleClose">关闭</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import { queryBidOpeningList } from "@/api/pm/execute";
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
    },
    // 
    accessTime: {
        type: String,
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

watch(() => props.recordId, value => init(value), { immediate: true })

const voRecords = ref([])
function init(id) {
    queryBidOpeningList({ demandId: props.recordId, openBidTime: props.accessTime }).then(res => {
        voRecords.value = res.data;
    });
}

function handleClose() {
    myVisible.value = false
}

defineExpose({
    handleClose
})
</script>

<style lang="scss">
</style>