<template>
  <div id="app" :class="size">
    <router-view />
  </div>
</template>
<script setup >
  // import store from '@/store'
  // const size = store.getters.size
  import useAppStore from "@/store/modules/app";
  const appStore = useAppStore();
  const size = computed(() => appStore.size);
</script>
<style lang="scss" scoped>
.files {
  .file-item {
    .text {
      word-break: break-word;
    }
  }
}
</style>
<style lang="scss">
.el-table__inner-wrapper {
  .el-scrollbar__view {
    min-height: 80px;
    .el-table__body {
      min-height: 35px;
    }
  }
}
.cust-close-dialog {
  .el-dialog__headerbtn {
    top: 0px !important;
  }
}
</style>