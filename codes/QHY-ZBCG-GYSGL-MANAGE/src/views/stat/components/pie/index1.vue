<template>
  <div class="echart" ref="bar1Dom" style="width: 100%; height: 100%;"></div>
</template>

<script setup>
import * as echarts from 'echarts'

const props = defineProps({
    rows: {
        type: Array,
        require: true
    },
    colors: {
        type: Array,
        default: function() {
            return [ '#f8990c', '#57ab42', '#ae6722', '#c84ea0', '#5b52e3', '#0082ed' ]
        }
    }
});

let richColor = {}
props.colors.forEach((item, idx) => {
  richColor[`color${idx}`] = { 
    fontSize: 12,
    color: item
  }
})

const bar1Dom = ref(null)
const echartRef = ref(null)
onMounted(() => {
  nextTick(() => {
    echartRef.value = echarts.init(bar1Dom.value, 'macarons')

    let _option = {
        color: props.colors,
        // tooltip: {
        //     trigger: 'item'
        // },
        tooltip: {
          trigger: "item",
        //   formatter: "{b}: {c}",
        },
        // legend: {
        //     type: 'scroll',
        //     orient: 'vertical',
        //     scrollDataIndex:5,
        //     animation : false,
        //     right: 10,
        //     top: 'center',
        // },
        series: [
            {
                name: '',
                type: 'pie',
                radius: ['40%', '70%'],
                avoidLabelOverlap: false,
                itemStyle: {
                    normal: {
                        avoidLabelOverlap: false,
                        label: {
                            formatter: params => {
                                return `{color${ params.dataIndex }|${ params.name }: ${ params.value }}`
                            },
                            rich: richColor
                        },
                        // labelLine: {
                        //     lineStyle: {
                        //         width: 3
                        //     }
                        // },
                        // label: {
                        //     show: true,
                        //     position: 'outside',
                        //     // color: '#666',
                        //     // formatter: function(params) {
                        //     //     if (params.name !== '') {
                        //     //         return `{${params.name}|{c}}\n{serie|{a}}`
                        //     //         // params.name + ':\r\r' + '' + params.data.value + '\n\n' + (params.data.value || 0) + '万元';
                        //     //     } else {
                        //     //         return '';
                        //     //     }
                        //     // },
                        // },
                        labelLine: {
                            length: 15,
                            length2: 30,
                            show: true,
                            color:'#00ffff'
                        }
                    }
                },
                // label: {
                //     show: true,
                //     color: '#333',
                //     formatter:'{b}：{d}%', // 用来换行
                // },
                emphasis: {
                    label: {
                        show: true,
                        fontSize: 16,
                        fontWeight: 'bold'
                    }
                },
                labelLine: {
                    show: false
                },
                data: props.rows || []
            }
        ]
    };

    echartRef.value.setOption(_option);
  })
})

</script>

<style lang="scss" scoped>
</style>