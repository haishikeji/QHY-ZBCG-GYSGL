<template>
    <div v-if="currVal">
      <div class="cust-dict-color-tag" :style="{ 'background': currVal.bgColor, 'color': currVal.color }">{{ currVal.label }}</div>
    </div>
</template>

<script setup>
const props = defineProps({
    // 数据
    options: {
        type: Array,
        default: function() {
        return []
        },
    },
    // 当前的值
    value: [ Number, String ],
})

const currVal = computed(() => {
    let _filters = (props.options || []).filter(f => f.value === props.value);
    return _filters.length ? _filters[0] : {};
})

</script>

<style scoped>
.cust-dict-color-tag {
    padding: 1px 6px;
    border-radius: 20px;
}
</style>