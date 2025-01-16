<template>
   <i-frame v-model:src="url"></i-frame>
</template>

<script setup>
import iFrame from '@/components/iFrame'
const { proxy } = getCurrentInstance();

const url = ref(proxy.globalConfig.base_url + "/swagger-ui/index.html")
</script>
