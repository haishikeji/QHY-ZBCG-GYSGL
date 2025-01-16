<template>
    <el-dialog title="专家信息" v-model="myVisible" width="1200px" align-center :close-on-click-modal="false" :modal-append-to-body="false" custom-class="cust-modal">
        <div style="padding: 30px 20px">
          <el-table stripe :data="voRecords" highlight-current-row style="width: 100%;" class="table-min-height" >
              <el-table-column type="index" label="序号" width="55" align="center" fixed="left" />
              <el-table-column label="专家姓名" align="center" prop="expertName" />
              <el-table-column label="证件号" align="center" prop="idNumber" width="170" />
              <el-table-column label="性别" align="center" prop="expertGender" />
              <el-table-column label="出生年月" align="center" prop="expertDateBirth" />
              <el-table-column label="联系方式" align="center" prop="expertTel" width="150" />
              <el-table-column label="专业类型" align="center" prop="majorTypeName" width="150" show-overflow-tooltip />
              <el-table-column label="单位信息" align="center" prop="unitInformation" />
              <el-table-column label="采购品种" align="center" prop="varietyPurchaseName" width="150" show-overflow-tooltip />
              <el-table-column label="职称" align="center" prop="professionalTitleName" />
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
import { queryBaseExpertList } from "@/api/pm/execute";
const { proxy } = getCurrentInstance();
const { major_type, professional_title } = proxy.useDict("major_type", "professional_title");
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
    queryBaseExpertList({ demandId: props.recordId, accessTime: props.accessTime }).then(res => {
        if (res.data && res.data.length) {
            res.data.map(m => {
                let _filters = professional_title.value.filter(f => f.value === m.professionalTitle);
                if (_filters.length) {
                    m.professionalTitleName = _filters[0].label;
                }
            })
        }
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