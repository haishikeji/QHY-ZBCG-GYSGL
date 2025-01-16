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
    legends: {
        type: Array,
        require: []
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
    echartRef.value = markRaw(echarts.init(bar1Dom.value, 'macarons'))

    let _option = {
        color: ['#4992FF', '#72C799', '#E79E0C'],
        tooltip: {
            trigger: "axis",
            axisPointer: { type: "shadow" }
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
            data: props.legends || [],
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
            name: '',
            type: 'bar',
            barWidth: 15,
            itemStyle: {
                barBorderRadius: [4, 4, 0, 0],
            },
            name: props.legends[0] || '',
            data: props.rows[0] || []
        }, {
            name: '',
            type: 'line',
            yAxisIndex: 1, // 使用的 y 轴的 index，在单个图表实例中存在多个 y轴的时候有用
            smooth: true, // 平滑曲线显示
            symbol: 'circle', // 标记的图形为实心圆
            symbolSize: 8, // 标记的大小
            itemStyle: {
                normal: {
                    color: '#72C799',
                    borderColor: 'rgba(255, 255, 255, 0.7)', //圆点透明 边框
                    borderWidth: 2,
                },
            },
            name: props.legends[1] || '',
            data: props.rows[1] || [],
            tooltip: {
                valueFormatter: function (value) {
                    return value + '%';
                }
            }, 
        }, {
            name: '',
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
            name: props.legends[2] || '',
            data: props.rows[2] || [],
            tooltip: {
                valueFormatter: function (value) {
                    return value + '%';
                }
            }, 
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