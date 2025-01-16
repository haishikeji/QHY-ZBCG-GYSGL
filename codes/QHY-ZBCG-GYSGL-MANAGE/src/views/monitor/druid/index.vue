<template>
   <div>
      <i-frame v-model:src="url"></i-frame>
   </div>
</template>

<script setup>
import iFrame from '@/components/iFrame'
const { proxy } = getCurrentInstance();

import { ref } from 'vue';

const url = ref(proxy.globalConfig.base_url + '/druid/login.html');
</script>
