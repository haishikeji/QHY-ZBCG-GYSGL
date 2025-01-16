<template>
  <div ref="portaletDiv">
    <div :class="className"  ref="commandstats" :style="{height:height,width:width}" />
  </div>
</template>

<script>
import * as echarts from 'echarts'
const animationDuration = 2000

export default {
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '300px'
    }
  },
  data() {
    return {
      chart: null
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$refs.commandstats, 'macarons')

      this.chart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        grid: {
          top: 10,
          left: '2%',
          right: '2%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [{
          type: 'category',
          data: ['2022-07', '2022-08', '2022-09', '2022-10', '2022-11', '2022-12', '2023-01'],
          axisTick: {
            alignWithLabel: true
          }
        }],
        yAxis: [{
          type: 'value',
          axisTick: {
            show: false
          }
        }],
        series: [{
          name: '公安数据',
          type: 'bar',
          stack: 'vistors',
          barWidth: '60%',
          data: [79, 52, 200, 334, 390, 330, 220],
          animationDuration
        }, {
          name: '检察院数据',
          type: 'bar',
          stack: 'vistors',
          barWidth: '60%',
          data: [80, 52, 200, 334, 390, 330, 220],
          animationDuration
        }]
      })
    }
  }
}
</script>
