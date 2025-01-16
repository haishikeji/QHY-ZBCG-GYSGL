<template>
  <el-dialog
    title="流程图"
    v-model="myVisible"
    width="1000px"
    align-center
    :close-on-click-modal="false"
    class="pm-demand-flow-modal"
  >
    <div style="padding: 10px 10px">
      <!-- <VueFlow v-model="elements" :default-zoom="1" :max-zoom="3" :min-zoom="1"  :nodes-draggable="false" :nodes-connectable="false" 
                :default-viewport="{ x: 500, y: 500 }" :pan-on-scroll="true" fit-view-on-init class="cust-flow"></VueFlow> -->
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
//样式引入
// import '@vue-flow/core/dist/style.css';
// import '@vue-flow/core/dist/theme-default.css';
// import tippy from 'tippy.js';
// import 'tippy.js/dist/tippy.css';
// import { VueFlow, useVueFlow, MarkerType, Position } from '@vue-flow/core'

// const { proxy } = getCurrentInstance();

// const props = defineProps({
//     // 是否展现
//     modelValue: {
//         type: Boolean,
//         default: false
//     },
//     // 当前记录ID
//     recordId: {
//         type: Number,
//         require: true
//     },
//     // 当前记录ID
//     step: {
//         type: String,
//         default: '需求填制'
//     }
// });

// const emit = defineEmits(['update:modelValue'])

// let myVisible = computed({
//     get: function() {
//         return props.modelValue
//     },
//     set: function(val) {
//         emit('update:modelValue', val)
//     }
// })

// let { onPaneReady, setCenter, setTransform } = useVueFlow()
// const data = [

//     {
//         id: '23',
//         label: '',
//         position: { x: 4, y: 0 },
//         style: { backgroundColor: 'rgba(16, 185, 129, 0.1)', width: '240px', height: '1370px' },
//     },
//     {
//         id: '24',
//         label: '',
//         position: { x: 255, y: 0 },
//         style: { backgroundColor: 'rgba(16, 185, 129, 0.1)', width: '240px', height: '1370px' },
//     },
//     {
//         id: '25',
//         label: '',
//         position: { x: 507, y: 0 },
//         style: { backgroundColor: 'rgba(16, 185, 129, 0.1)', width: '240px', height: '1370px' },
//     },

//     { id: '20', label: '需求单位', position: { x: 50, y: 20 }, style: { backgroundColor: 'rgba(16, 185, 129, 1)', color: 'white' }, },
//     { id: '21', label: '采购管理部门', position: { x: 300, y: 20 }, style: { backgroundColor: 'rgba(16, 185, 129, 1)', color: 'white' }, },
//     { id: '22', label: '采购办', position: { x: 550, y: 20 }, style: { backgroundColor: 'rgba(16, 185, 129, 1)', color: 'white' }, },

//     { id: '1', label: '需求填制', position: { x: 50, y: 100 }, targetPosition: Position.Left, },
//     { id: '2', label: '需求提交', position: { x: 50, y: 200 }, sourcePosition: Position.Right },
//     { id: '3', label: '需求审核', position: { x: 300, y: 200 }, class: 'light', targetPosition: Position.Left },
//     { id: '4', label: '审核是否通过', position: { x: 300, y: 300 }, sourcePosition: Position.Left },
//     { id: '5', label: '任务下达', position: { x: 300, y: 400 }, sourcePosition: Position.Right, targetPosition: Position.Left },
//     { id: '6', label: '选取代理', position: { x: 550, y: 400 }, sourcePosition: Position.Bottom, targetPosition: Position.Left },

//     { id: '7', label: '上传意见反馈单', position: { x: 550, y: 500 } },
//     { id: '8', label: '上传招标文件审批表', position: { x: 550, y: 600 } },
//     { id: '9', label: '标前质疑投诉', position: { x: 550, y: 700 } },
//     { id: '10', label: '采购公告发布', position: { x: 550, y: 800 } },
//     { id: '11', label: '填写专家信息', position: { x: 550, y: 900 } },
//     { id: '12', label: '开评标', position: { x: 550, y: 1000 } },
//     { id: '13', label: '评标结果公告', position: { x: 550, y: 1100 } },
//     { id: '14', label: '质疑处理', position: { x: 550, y: 1200 } },
//     { id: '15', label: '上传中标通知书', position: { x: 550, y: 1300 }, sourcePosition: Position.Left },

//     // { id: '7', label: '上传招标文件', position: { x: 550, y: 500 } },
//     // { id: '8', label: '发布公告', position: { x: 550, y: 600 } },
//     // { id: '9', label: '填写专家信息', position: { x: 550, y: 700 } },
//     // { id: '10', label: '填写开标信息', position: { x: 550, y: 800 } },
//     // { id: '11', label: '填写中标信息', position: { x: 550, y: 900 } },
//     // { id: '12', label: '质疑处理', position: { x: 550, y: 1000 } },
//     // { id: '13', label: '上传中标通知书', position: { x: 550, y: 1100 }, targetPosition: Position.Top, sourcePosition: Position.Left },
//     { id: '16', label: '合同填制', position: { x: 50, y: 1100 }, targetPosition: Position.Right },
//     { id: '17', label: '建设文档填制', position: { x: 50, y: 1200 } },
//     { id: '18', label: '项目归档', position: { x: 50, y: 1300 } },

//     { id: 'e1-1', source: '1', target: '2'},
//     { id: 'e1-2', source: '2', target: '3', type: 'smoothstep', markerEnd: MarkerType.ArrowClosed },
//     { id: 'e1-3', source: '3', target: '4', type: 'smoothstep', markerEnd: MarkerType.ArrowClosed },
//     { id: 'e1-4', source: '4', target: '5', label: '通过', type: 'smoothstep', markerEnd: MarkerType.ArrowClosed },
//     { id: 'e1-5', source: '4', target: '1', label: '退回', type: 'smoothstep', markerEnd: MarkerType.ArrowClosed },

//     { id: 'e1-6', source: '5', target: '6', type: 'smoothstep', markerEnd: MarkerType.ArrowClosed },
//     { id: 'e1-7', source: '6', target: '7', type: 'smoothstep', markerEnd: MarkerType.ArrowClosed },
//     { id: 'e1-8', source: '7', target: '8', type: 'smoothstep', markerEnd: MarkerType.ArrowClosed },
//     { id: 'e1-9', source: '8', target: '9', type: 'smoothstep', markerEnd: MarkerType.ArrowClosed },
//     { id: 'e1-10', source: '9', target: '10', type: 'smoothstep', markerEnd: MarkerType.ArrowClosed },
//     { id: 'e1-11', source: '10', target: '11', type: 'smoothstep', markerEnd: MarkerType.ArrowClosed },
//     { id: 'e1-12', source: '11', target: '12', type: 'smoothstep', markerEnd: MarkerType.ArrowClosed },
//     { id: 'e1-13', source: '12', target: '13', type: 'smoothstep', markerEnd: MarkerType.ArrowClosed },
//     { id: 'e1-14', source: '13', target: '14', type: 'smoothstep', markerEnd: MarkerType.ArrowClosed },
//     { id: 'e1-15', source: '14', target: '15', type: 'smoothstep', markerEnd: MarkerType.ArrowClosed },
//     { id: 'e1-16', source: '15', target: '16', type: 'smoothstep', markerEnd: MarkerType.ArrowClosed },
//     { id: 'e1-16', source: '16', target: '17', type: 'smoothstep', markerEnd: MarkerType.ArrowClosed },
//     { id: 'e1-16', source: '17', target: '18', type: 'smoothstep', markerEnd: MarkerType.ArrowClosed },
//     { id: 'e1-16', source: '18', target: '19', type: 'smoothstep', markerEnd: MarkerType.ArrowClosed },
// ]
// let elements = ref(data)

// let constantNodeTip = {
//     '需求填制': {
//         text: '需求单位根据项目类型进行需求基本情况填制，并上传需求资料',
//         files: [ '《采购需求计划明细表》' ]
//     },
//     '需求提交': {
//         text: '需求单位填制好基本情况确认无误后进行需求提报，由需求审核部门进行审核',
//         files: [ '《采购需求调查情况报告表》', '《采购需求专项论证报告》', '《采购需求审查表》' ]
//     },
//     '需求审核': {
//         text: '采购管理部门接收到需求单位提报的需求后，进行审核：审核通过后生成采购任务并由采购管理部门接收',
//         files: [ '《采购需求计划审核表》' ]
//     },
//     '审核是否通过': {
//         text: '',
//         files: [ ]
//     },
//     '任务下达': {
//         text: '采购管理部门接收到需求管理部门审核通过的任务后，将任务下达至采购办',
//         files: [ '《军队工程采购申请审批表》' ]
//     },
//     '选取代理': {
//         text: '两种选取方式：（1）一种是已通过其他方式选取了招标代理机构，直接在招标代理库里选择招标代理机构信息（2）另一种是通过点击开始抽取按钮，然后根据该项目类型（如果是装备类项目，则在装备类招标代理库中选，如果是非装备类项目则从非装备类招标代理库中选取）从招标代理库中随机抽取1家代理机构',
//         files: [ ]
//     },
//     '上传意见反馈单': {
//         text: '专家意见附件',
//         files: [ ]
//     },
//     '上传招标文件审批表': {
//         text: '招标审核附件',
//         files: [ ]
//     },
//     '采购公告发布': {
//         text: '发布招标公告，填写发布公告的平台，发布时间，开标地点，报名截止时间，开标时间，备注等信息',
//         files: [ ]
//     },
//     '标前质疑投诉': {
//         text: '上传质疑附件',
//         files: [ ]
//     },
//     '填写专家信息': {
//         text: '填写专家信息各投标人的基本信息和得分',
//         files: [ ]
//     },
//     '开评标': {
//         text: '填写开标信息',
//         files: [ ]
//     },
//     '评标结果公告': {
//         text: '填写预中标或流废标信息',
//         files: [ ]
//     },
//     '上传中标通知书': {
//         text: '上传中标通知附件',
//         files: [ ]
//     },
//     '合同填制': {
//         text: '采购办填制中标情况后，由需求单位进行合同信息填制，并上传过程资料',
//         files: [ ]
//     },
//     '建设文档填制': {
//         text: '供应商进行项目交付后，需求单位填写项目交付基本情况，并上传交付资料',
//         files: [ ]
//     },
//     '项目归档': {
//         text: '需求单位填制建设文档后，将项目所有阶段信息、文档进行整合',
//         files: [ ]
//     }

//     // '上传招标文件': {
//     //     text: '上传招标文件、招标文件确认文件',
//     //     files: [ ]
//     // },
//     // '发布公告': {
//     //     text: '发布招标公告，填写发布公告的平台，发布时间，开标地点，报名截止时间，开标时间，备注等信息',
//     //     files: [ ]
//     // },
//     // '填写开标信息': {
//     //     text: '填写各投标人的基本信息和得分',
//     //     files: [ ]
//     // },
//     // '填写中标信息': {
//     //     text: '中标信息填写中标单位信息，中标公告时间，中标公告截止时间',
//     //     files: [ ]
//     // },
//     // '质疑处理': {
//     //     text: '填写质疑情况，可更换中标人或进行流标操作',
//     //     files: [ ]
//     // },
//     // '上传中标通知书': {
//     //     text: '上传中标通知附件',
//     //     files: [ ]
//     // },
//     // '合同填制': {
//     //     text: '采购办填制中标情况后，由需求单位进行合同信息填制，并上传过程资料',
//     //     files: [ ]
//     // },
//     // '建设文档填制': {
//     //     text: '供应商进行项目交付后，需求单位填写项目交付基本情况，并上传交付资料',
//     //     files: [ ]
//     // },
//     // '项目归档': {
//     //     text: '需求单位填制建设文档后，将项目所有阶段信息、文档进行整合',
//     //     files: [ ]
//     // }
// }

// const instance = ref(null);
// onPaneReady((vueFlowInstance) => {
//     vueFlowInstance.fitView()
//     instance.value = vueFlowInstance;
//     setTimeout(() => {
//         instance.value.setTransform({ x: 100, y: 40, zoom: 1 })
//     }, 100)

//     nextTick(() => {
//         setTimeout(() => {
//             tippy('.vue-flow__node:not([data-id="4"]):not([data-id="20"]):not([data-id="21"]):not([data-id="22"]):not([data-id="23"]):not([data-id="24"]):not([data-id="25"])', {
//                 allowHTML: true,
//                 content: (reference) => {
//                     let _step = reference.querySelectorAll('div')[1].innerText;
//                     let _curr = constantNodeTip[_step];
//                     let _fileStr = _curr && _curr.files && _curr.files.length ? '附件: ' + _curr.files.join(',') : ''
//                     if (!_curr?.text) return '暂无提示信息';
//                     return `<strong style="display: block; margin-bottom: 4px; font-size: 16px;">${_step}:</strong>提醒: ${_curr?.text}<br/>${_fileStr}`
//                 }
//             });
//         }, 700)
//     })
// })

// watch(() => props.step, value => handleStepChange(value), { immediate: true })
// function handleStepChange(step) {
//     let _nodes = [ '1' ];
//     if (step === '需求填制') {
//         _nodes = [ '1' ];
//     } else if (step === '需求提交') {
//         _nodes = [ '1', '2' ];
//     } else if (step === '需求审核') {
//         _nodes = [ '1', '2', '3' ];
//     } else if (step === '审核是否通过') {
//         _nodes = [ '1', '2', '3', '4' ];
//     } else if (step === '任务下达') {
//         _nodes = [ '1', '2', '3', '4', '5' ];
//     } else if (step === '选取代理') {
//         _nodes = [ '1', '2', '3', '4', '5', '6' ];
//     } else if (step === '上传意见反馈单') {
//         _nodes = [ '1', '2', '3', '4', '5', '6', '7' ];
//     } else if (step === '上传招标文件审批表') {
//         _nodes = [ '1', '2', '3', '4', '5', '6', '7', '8' ];
//     } else if (step === '采购公告发布') {
//         _nodes = [ '1', '2', '3', '4', '5', '6', '7', '8', '9', '10' ];
//     } else if (step === '标前质疑投诉') {
//         _nodes = [ '1', '2', '3', '4', '5', '6', '7', '8', '9' ];
//     } else if (step === '填写专家信息,开评标') {
//         _nodes = [ '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12' ];
//     } else if (step === '评标结果公告') {
//         _nodes = [ '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13' ];
//     } else if (step === '质疑处理,上传中标通知书') {
//         _nodes = [ '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15' ];
//     } else if (step === '合同填制') {
//         _nodes = [ '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16' ];
//     } else if (step === '建设文档填制') {
//         _nodes = [ '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17' ];
//     } else if (step === '项目归档') {
//         _nodes = [ '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18' ];
//     }
//     data.map(m => {
//         if (m.id === _nodes[_nodes.length - 1]) {
//             m.style = { backgroundColor: '#006aea', color: '#ffffff' };
//         }
//         if (_nodes.includes(m.target) && m.id !== 'e1-5') {
//             m.animated = true;
//             m.style = { stroke: '#10b981' };
//         }
//     })
// }

// function handleClose() {
//     myVisible.value = false
// }

// defineExpose({
//     handleClose,
// })
</script>

<style lang="scss" scoped>
.cust-flow {
  margin: 10px;
  height: 600px;
}
</style>
<style lang="scss">
.cust-flow .vue-flow__node-custom {
  border: 1px solid #777;
  padding: 16px;
  border-radius: 10px;
  background: whitesmoke;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
  width: 160px;
}

[data-id="20"],
[data-id="21"],
[data-id="22"],
[data-id="23"],
[data-id="24"],
[data-id="25"] {
  .vue-flow__handle-top,
  .vue-flow__handle-bottom {
    display: none;
  }
}

.pm-demand-flow-modal {
  padding: 0px 0px 10px 0px;
  border-radius: 10px;
  // margin-top: initial !important;
  .el-dialog__header {
    text-align: center;
    border-bottom: 0px;
  }
  .el-dialog__headerbtn {
    top: 0px !important;
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
