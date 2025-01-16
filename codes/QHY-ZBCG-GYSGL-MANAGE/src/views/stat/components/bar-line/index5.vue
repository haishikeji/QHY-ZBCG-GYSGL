<template>
  <div class="echart" ref="bar1Dom" style="width: 100%; height: 100%;"></div>
</template>

<script setup>
import * as echarts from 'echarts'

const props = defineProps({
    // 当前的值
    xAxis: {
        type: Array,
        require: true
    },
    rows: {
        type: Array,
        require: true
    }
});

const bar1Dom = ref(null)
const echartRef = ref(null)
onMounted(() => {
  nextTick(() => {
    echartRef.value = echarts.init(bar1Dom.value, 'macarons')

    let _option = {
        color: ['#E79E0C', '#25C8BE', '#8191E7'],
        tooltip: {
            confine: true,
        },
        grid: {
            left: '2%',
            right: '4%',
            bottom: '10%',
            top: '20%',
            containLabel: true,
        },
        legend: {
            icon: 'rect',
            orient: 'horizontal',
            top: '2',
            right: '210',
            itemWidth: 12,
            itemHeight: 12,
            formatter: ['{a|{name}}'].join('\n'),
            textStyle: {
                fontSize: 12,
                color: '#6A93B9',
                height: 8,
                rich: {
                    a: {
                        verticalAlign: 'bottom',
                    },
                },
            },
            data: ['任务数量'],
        },
        xAxis: {
            type: 'category',
            data: props.xAxis || [],
            axisLine: {
                lineStyle: {
                    color: 'rgba(255,255,255,0.45)',
                },
            },
            axisLabel: {
                fontSize: 12,
                color: '#6A93B9',
            },
            axisTick: {
                show: false,
            },
        },
        yAxis: [{
            type: 'value',
            min: 0,
            minInterval: 1,
            splitArea: {
                show: false,
            },
            axisLine: {
                show: false,
            },
            axisTick: {
                show: false,
            },
            splitLine: {
                lineStyle: {
                    color: 'rgba(255, 255, 255, 0.15)',
                    // type: 'dashed', // dotted 虚线
                },
            },
            axisLabel: {
                fontSize: 12,
                color: '#6A93B9',
                fontFamily: 'Bebas',
            },
        }, {
            type: 'value',
            axisLine: {
                show: false,
            },
            axisTick: {
                show: false,
            },
            splitLine: {
                show: false,
            },
            axisLabel: {
                fontSize: 12,
                formatter: '{value}%', // 右侧Y轴文字显示
                fontFamily: 'Bebas',
                color: '#6A93B9',
            },
            splitArea: {
                show: false,
            },
        }],
        series: [{
            type: 'bar',
            barWidth: 15,
            itemStyle: {
                barBorderRadius: [4, 4, 0, 0],
            },
            name: '任务数量',
            data: props.rows[0] || []
        }],
    };

    echartRef.value.setOption(!props.xAxis.length ? {
        title: {
            text: '暂无数据',
            x: 'center',
            y: 'center',
            textStyle: {
                fontSize: 14,
                fontWeight: 'normal',
            }
            }
    } : _option);
  })
})

</script>

<style lang="scss" scoped>
</style>