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

    let chartData = {
        xAxisData: ['2022-04-12', '2022-04-14', '2022-04-15', '2022-04-16', '2022-04-17', '2022-04-18', '2022-04-19', '2022-04-20', '2022-04-21', '2022-04-22', '2022-04-23'],
        finishNum: [28, 51, 43, 43, 48, 15, 51, 43, 43, 48, 15],
        unFinishNum: [28, 39, 36, 36, 31, 21, 39, 36, 36, 31, 21],
        finishRate: [10, 36, 24, 24, 40, 11, 36, 14, 24, 20, 41],
    }

    let _option = {
        color: ['#25C8BE', '#8191E7', '#E79E0C'],
        tooltip: {
            confine: true,
            trigger: 'item'
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
            left: 'right',
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
            data: ['已完成', '未完成', '完成率'],
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
            name: '万元',
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
            name: '数量',
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
                formatter: '{value}', // 右侧Y轴文字显示
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
            name: '预算金额(万元)',
            data: props.rows[0] || []
        }, {
            type: 'bar',
            barWidth: 15,
            itemStyle: {
                barBorderRadius: [4, 4, 0, 0],
            },
            name: '合同金额(万元)',
            data: props.rows[1] || []
        }, {
            name: '项目数量',
            type: 'line',
            yAxisIndex: 1, // 使用的 y 轴的 index，在单个图表实例中存在多个 y轴的时候有用
            smooth: true, // 平滑曲线显示
            symbol: 'circle', // 标记的图形为实心圆
            symbolSize: 8, // 标记的大小
            itemStyle: {
                normal: {
                    color: '#E79E0C',
                    borderColor: 'rgba(255, 255, 255, 0.7)', //圆点透明 边框
                    borderWidth: 2,
                },
            },
            data: props.rows[2] || []
        }, ],
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