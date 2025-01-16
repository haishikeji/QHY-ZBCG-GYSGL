<template>
    <div class="stat-page">
        <el-form :model="queryParams" ref="queryForm" label-width="60px">
            <el-row :gutter="16">
                <el-col :md="4">
                    <el-form-item prop="unitSelected" label="单位">
                        <el-select
                            v-model="unitSelected"
                            placeholder="请选择单位"
                            style="width: 100%"
                            clearable
                            @change="handleQuery"
                        >
                            <el-option v-for="(item, index) in dropdown.unitList" :key="index" :label="item.deptName" :value="item.deptId"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <!-- <el-col :md="2" align="right" style="margin-left: auto;">
                    <el-form-item class="search_btn_box">
                        <el-button
                            class="filter-item w-100"
                            type="primary"
                            @click="handleQuery"
                        >搜索</el-button>
                    </el-form-item>
                </el-col> -->
            </el-row>
        </el-form>

        <el-card shadow="never" class="table_card">
            <div class="block-title">采购任务受领</div>
            
            <el-row v-if="chartData.demandSubCount && chartData.demandSubCount.length >= 4" class=" mt20" :gutter="24">
                <el-col :span="6">
                    <div class="row-item">
                        <div class="row-item-infos">
                            <div class="info" style="flex-direction: column;">
                                <div class="info-title" style="width: initial;">{{ chartData.demandSubCount[0].columnName || '' }}</div>
                                <div class="info-num" style="text-align: center;">{{ chartData.demandSubCount[0].num || 0 }}</div>
                            </div>
                        </div>
                        <div class="row-item-img" style="position: relative;">
                             <img src="/images/stat/icon-chart.png" style="width: 167px; height: 76px; position: absolute; right: -22px; bottom: -59px; top: initial;" >
                        </div>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="row-item">
                        <div class="row-item-infos">
                            <div class="info" style="flex-direction: column;">
                                <div class="info-title" style="width: initial;">{{ chartData.demandSubCount[1].columnName || '' }}</div>
                                <div class="info-num" style="text-align: center;">{{ chartData.demandSubCount[1].num || 0 }}</div>
                            </div>
                        </div>
                        <div class="row-item-img" style="position: relative;">
                             <img src="/images/stat/icon-chart.png" style="width: 167px; height: 76px; position: absolute; right: -22px; bottom: -59px; top: initial;" >
                        </div>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="row-item">
                        <div class="row-item-img">
                            <img src="/images/stat/icon-add.png" >
                        </div>
                        <div class="row-item-infos">
                            <div class="info">
                                <div class="info-title">{{ chartData.demandSubCount[2].columnName || '' }}</div>
                                <div class="info-num">{{ chartData.demandSubCount[2].num || 0 }}</div>
                            </div>
                            <div class="info">
                                <div class="info-title">预算金额（万元）</div>
                                <div class="info-num">{{ chartData.demandSubCount[2].evaluationTotal || 0 }}</div>
                            </div>
                        </div>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="row-item">
                        <div class="row-item-img">
                            <img src="/images/stat/icon-up.png" >
                        </div>
                        <div class="row-item-infos">
                            <div class="info">
                                <div class="info-title">{{ chartData.demandSubCount[3].columnName || '' }}</div>
                                <div class="info-num">{{ chartData.demandSubCount[3].num || 0 }}</div>
                            </div>
                            <div class="info">
                                <div class="info-title">预算金额（万元）</div>
                                <div class="info-num">{{ chartData.demandSubCount[3].evaluationTotal || 0 }}</div>
                            </div>
                        </div>
                    </div>
                </el-col>
            </el-row>
            
            <el-row class=" mt20" :gutter="24">
                <el-col :span="12" style="position: relative;">
                    <div class="echart-title">
                        <div class="title">任务受领数量</div>
                        <div class="filter-items">
                            <div class="filter-item" :class="{ 'actived': filterSelected.filter1 === 'month' }" @click="handleFilterItemClick('filter1', 'month')">月</div>
                            <div class="filter-item" :class="{ 'actived': filterSelected.filter1 === 'quarter' }" @click="handleFilterItemClick('filter1', 'quarter')">季</div>
                            <div class="filter-item" :class="{ 'actived': filterSelected.filter1 === 'year' }" @click="handleFilterItemClick('filter1', 'year')">年</div>
                        </div>
                    </div>
                    <BarLineComponent v-if="chartData.purchaseProjectNumberAnalysis.xAxis" style="height: 320px;" :legends="[ '项目数量', '项目数量同比', '项目数量环比' ]"
                        :x-axis="chartData.purchaseProjectNumberAnalysis.xAxis" :rows="chartData.purchaseProjectNumberAnalysis.rows"></BarLineComponent>
                </el-col>
                <el-col :span="12" style="position: relative;">
                    <div class="echart-title">
                        <div class="title">任务受领预算金额(万元)</div>
                        <div class="filter-items">
                            <div class="filter-item" :class="{ 'actived': filterSelected.filter2 === 'month' }" @click="handleFilterItemClick('filter2', 'month')">月</div>
                            <div class="filter-item" :class="{ 'actived': filterSelected.filter2 === 'quarter' }" @click="handleFilterItemClick('filter2', 'quarter')">季</div>
                            <div class="filter-item" :class="{ 'actived': filterSelected.filter2 === 'year' }" @click="handleFilterItemClick('filter2', 'year')">年</div>
                        </div>
                    </div>
                    <BarLineComponent v-if="chartData.purchaseProjectNumberAnalysis1.xAxis" style="height: 320px;" :legends="[ '预算金额', '预算金额同比', '预算金额环比' ]" 
                        :x-axis="chartData.purchaseProjectNumberAnalysis1.xAxis" :rows="chartData.purchaseProjectNumberAnalysis1.rows"></BarLineComponent>
                </el-col>
            </el-row>
            
        </el-card>

        <el-row class=" mt20" :gutter="24">
            <el-col :span="8">
                <el-card shadow="never" class="table_card">
                    <div class="block-title">任务属性分布</div>
                    <PieComponent v-if="chartData.countByProjectAttr.length" style="height: 320px;" :rows="chartData.countByProjectAttr"
                        :colors="[ '#0082ea', '#c69d0b', '#71bbfe', '#e0618e', '#14d5bb', '#75bb39', '#da7af2' ]"></PieComponent>
                    <PieComponent v-else style="height: 320px;" :rows="[]"
                        :colors="[ '#0082ea', '#c69d0b', '#71bbfe', '#e0618e', '#14d5bb', '#75bb39', '#da7af2' ]"></PieComponent>
                </el-card>
            </el-col>
            <el-col :span="8">
                <el-card shadow="never" class="table_card">
                    <div class="block-title">采购方式分布</div>
                    <PieComponent v-if="chartData.countByPurchaseMode.length" style="height: 320px;" :rows="chartData.countByPurchaseMode"
                        :colors="[ '#0082ea', '#c69d0b', '#71bbfe', '#e0618e', '#14d5bb', '#75bb39', '#da7af2' ]"></PieComponent>
                    <PieComponent v-else style="height: 320px;" :rows="[]"
                        :colors="[ '#0082ea', '#c69d0b', '#71bbfe', '#e0618e', '#14d5bb', '#75bb39', '#da7af2' ]"></PieComponent>
                </el-card>
            </el-col>
            <el-col :span="8">
                <el-card shadow="never" class="table_card">
                    <div class="block-title">任务金额分布</div>
                    <PieComponent v-if="chartData.countByEvaluation.length" style="height: 320px;" :rows="chartData.countByEvaluation"
                        :colors="[ '#0082ea', '#c69d0b', '#71bbfe', '#e0618e', '#14d5bb', '#75bb39', '#da7af2' ]"></PieComponent>
                    <PieComponent v-else style="height: 320px;" :rows="[]"
                        :colors="[ '#0082ea', '#c69d0b', '#71bbfe', '#e0618e', '#14d5bb', '#75bb39', '#da7af2' ]"></PieComponent>
                </el-card>
            </el-col>
        </el-row>

        <el-card shadow="never" class="table_card mt20">
            <div class="block-title">项目执行进度</div>
            <el-row v-if="chartData.purchaseProjectExecute && chartData.purchaseProjectExecute.length >= 3" class=" mt20" :gutter="24">
                <el-col :span="8">
                    <div class="row-item" style="height: 130px;">
                        <div class="row-item-img">
                            <img src="/images/stat/icon-success.png" >
                        </div>
                        <div class="row-item-infos" style="">
                            <div class="info">
                                <div class="info-title">今年以来累计完成采购任务数量</div>
                                <div class="info-num">{{ chartData.purchaseProjectExecute[0].num || 0 }}</div>
                            </div>
                            <div class="info">
                                <div class="info-title">预算金额（万元）</div>
                                <div class="info-num">{{ chartData.purchaseProjectExecute[0].evaluationTotal || 0 }}</div>
                            </div>
                            <div class="info">
                                <div class="info-title">合同金额（万元）</div>
                                <div class="info-num">{{ chartData.purchaseProjectExecute[0].evaluationContractTotal || 0 }}</div>
                            </div>
                        </div>
                    </div>
                </el-col>
                <el-col :span="8">
                    <div class="row-item" style="height: 130px;">
                        <div class="row-item-img">
                            <img src="/images/stat/icon-right-arrow.png" >
                        </div>
                        <div class="row-item-infos" >
                            <div class="info">
                                <div class="info-title">正常推进采购任务数量</div>
                                <div class="info-num">{{ chartData.purchaseProjectExecute[1].num || 0 }}</div>
                            </div>
                            <div class="info">
                                <div class="info-title">预算金额（万元）</div>
                                <div class="info-num">{{ chartData.purchaseProjectExecute[1].evaluationTotal || 0 }}</div>
                            </div>
                        </div>
                    </div>
                </el-col>
                <el-col :span="8">
                    <div class="row-item" style="height: 130px;">
                        <div class="row-item-img">
                            <img src="/images/stat/icon-warning.png" >
                        </div>
                        <div class="row-item-infos">
                            <div class="info">
                                <div class="info-title">未完成采购任务(含正常推进和滞后任务)</div>
                                <div class="info-num">{{ chartData.purchaseProjectExecute[2].num || 0 }}</div>
                            </div>
                            <div class="info">
                                <div class="info-title">预算金额（万元）</div>
                                <div class="info-num">{{ chartData.purchaseProjectExecute[2].evaluationTotal || 0 }}</div>
                            </div>
                        </div>
                    </div>
                </el-col>
            </el-row>
            

            <el-row class=" mt20" :gutter="24">
                <el-col :span="12" style="position: relative;">
                    <div class="echart-title">
                        <div class="title">新完成采购任务数量</div>
                        <div class="filter-items">
                            <div class="filter-item" :class="{ 'actived': filterSelected.filter3 === 'month' }" @click="handleFilterItemClick('filter3', 'month')">月</div>
                            <div class="filter-item" :class="{ 'actived': filterSelected.filter3 === 'quarter' }" @click="handleFilterItemClick('filter3', 'quarter')">季</div>
                            <div class="filter-item" :class="{ 'actived': filterSelected.filter3 === 'year' }" @click="handleFilterItemClick('filter3', 'year')">年</div>
                        </div>
                    </div>
                    <BarLine2Component v-if="chartData.purchaseTaskFinish.xAxis && chartData.purchaseTaskFinish.xAxis.length" style="height: 320px;" 
                        :x-axis="chartData.purchaseTaskFinish.xAxis" :rows="chartData.purchaseTaskFinish.rows"></BarLine2Component>
                </el-col>
                <el-col :span="12" style="position: relative;">
                    <div class="echart-title">
                        <div class="title">各阶段采购任务数量分布</div>
                    </div>
                    <Pie2Component v-if="chartData.countEveryStatusNum" style="height: 320px;" :rows="chartData.countEveryStatusNum"></Pie2Component>
                </el-col>
            </el-row>
        </el-card>


        

        <el-card shadow="never" class="table_card mt20">
            <div class="block-title">滞后任务情况</div>
            <el-row v-if="chartData.countProjectExceed && chartData.countProjectExceed.length >= 2" class=" mt20" :gutter="24">
                <el-col :span="12">
                    <div class="row-item">
                        <div class="row-item-img">
                            <img src="/images/stat/icon-warning.png" >
                        </div>
                        <div class="row-item-infos" style="flex-direction: row; justify-content: space-around;">
                            <div class="info">
                                <div class="info-title" style="width: initial;">累计积压的执行滞后采购任务</div>
                                <div class="info-num">{{ chartData.countProjectExceed[0].num || 0 }}</div>
                            </div>
                            <div class="info">
                                <div class="info-title">预算金额（万元）</div>
                                <div class="info-num">{{ chartData.countProjectExceed[0].evaluationTotal || 0 }}</div>
                            </div>
                        </div>
                    </div>
                </el-col>
                <el-col :span="12">
                    <div class="row-item">
                        <div class="row-item-img">
                            <img src="/images/stat/icon-warning.png" >
                        </div>
                        <div class="row-item-infos" style="flex-direction: row; justify-content: space-around;">
                            <div class="info">
                                <div class="info-title" style="width: initial;">执行停滞采购任务数量</div>
                                <div class="info-num">{{ chartData.countProjectExceed[1].num || 0 }}</div>
                            </div>
                            <div class="info">
                                <div class="info-title">预算金额（万元）</div>
                                <div class="info-num">{{ chartData.countProjectExceed[0].evaluationTotal || 0 }}</div>
                            </div>
                        </div>
                    </div>
                </el-col>
            </el-row>
            

            <el-row class=" mt20" :gutter="24">
                <el-col :span="12" style="position: relative;">
                    <div class="echart-title">
                        <div class="title">新增执行滞后采购任务数量分析</div>
                        <div class="filter-items">
                            <div class="filter-item" :class="{ 'actived': filterSelected.filter4 === 'month' }" @click="handleFilterItemClick('filter4', 'month')">月</div>
                            <div class="filter-item" :class="{ 'actived': filterSelected.filter4 === 'quarter' }" @click="handleFilterItemClick('filter4', 'quarter')">季</div>
                            <div class="filter-item" :class="{ 'actived': filterSelected.filter4 === 'year' }" @click="handleFilterItemClick('filter4', 'year')">年</div>
                        </div>
                    </div>
                    <BarLine5Component v-if="chartData.countMajorProjectNumCGB.xAxis && chartData.countMajorProjectNumCGB.xAxis.length" style="height: 320px;" 
                        :x-axis="chartData.countMajorProjectNumCGB.xAxis" :rows="chartData.countMajorProjectNumCGB.rows"></BarLine5Component>
                </el-col>
                <el-col :span="12" style="position: relative;">
                    <div class="echart-title">
                        <div class="title">滞后采购任务时长分析</div>
                    </div>
                    <Pie2Component v-if="chartData.exceedMarketAnalysis" style="height: 320px;" :rows="chartData.exceedMarketAnalysis"></Pie2Component>
                </el-col>
            </el-row>
            <el-row class=" mt20" :gutter="24">
                <el-col :span="12" style="position: relative;">
                    <div class="echart-title">
                        <div class="title">滞留环节</div>
                    </div>
                    <Pie2Component v-if="chartData.exceedLink" style="height: 320px;" :rows="chartData.exceedLink"></Pie2Component>
                </el-col>
                <el-col :span="12" style="position: relative;">
                    <div class="echart-title">
                        <div class="title">滞后直接原因</div>
                    </div>
                    <Pie2Component v-if="chartData.exceedReasonZj" style="height: 320px;" :rows="chartData.exceedReasonZj"></Pie2Component>
                </el-col>
            </el-row>
        </el-card>



        <el-card shadow="never" class="table_card">
            <div class="block-title">重大规划采购任务专项情况计划管理情况</div>
            
            <el-row v-if="chartData.countMajorProjectCGB?.length >= 3" class=" mt20" :gutter="24">
                <el-col v-for="(item, index) in chartData.countMajorProjectCGB.slice(0, 3)" :key="index" :span="8">
                    <div class="row-item row-item-1">
                        <div class="row-item-img">
                            <img src="/images/stat/icon-add.png" >
                        </div>
                        <div class="row-item-infos">
                            <div class="info">
                                <div class="info-title actived">{{ item.columnName || '' }}</div>
                            </div>
                            <div class="info">
                                <div class="info-title" :title="item.columnNamePlus || ''">{{ item.columnNamePlus || '' }}</div>
                                <div class="info-num">{{ item.num || 0 }}</div>
                            </div>
                            <div class="info">
                                <div class="info-title">预算金额（万元）</div>
                                <div class="info-num">{{ item.evaluationTotal || 0 }}</div>
                            </div>
                        </div>
                    </div>
                </el-col>
            </el-row>
            <el-row v-if="chartData.countMajorProjectCGB?.length >= 6" class=" mt20" :gutter="24">
                <el-col v-for="(item, index) in chartData.countMajorProjectCGB.slice(3, 6)" :key="index" :span="8">
                    <div class="row-item row-item-1">
                        <div class="row-item-img">
                            <img src="/images/stat/icon-right-arrow.png" >
                        </div>
                        <div class="row-item-infos">
                            <div class="info">
                                <div class="info-title actived">{{ item.columnName || '' }}</div>
                            </div>
                            <div class="info">
                                <div class="info-title" :title="item.columnNamePlus || ''">{{ item.columnNamePlus || '' }}</div>
                                <div class="info-num">{{ item.num || 0 }}</div>
                            </div>
                            <div class="info">
                                <div class="info-title">预算金额（万元）</div>
                                <div class="info-num">{{ item.evaluationTotal || 0 }}</div>
                            </div>
                        </div>
                    </div>
                </el-col>
            </el-row>
            <el-row v-if="chartData.countMajorProjectCGB?.length >= 9" class=" mt20" :gutter="24">
                <el-col v-for="(item, index) in chartData.countMajorProjectCGB.slice(6, 9)" :key="index" :span="8">
                    <div class="row-item row-item-1">
                        <div class="row-item-img">
                            <img src="/images/stat/icon-success.png" >
                        </div>
                        <div class="row-item-infos">
                            <div class="info">
                                <div class="info-title actived">{{ item.columnName || '' }}</div>
                            </div>
                            <div class="info">
                                <div class="info-title" :title="item.columnNamePlus || ''">{{ item.columnNamePlus || '' }}</div>
                                <div class="info-num">{{ item.num || 0 }}</div>
                            </div>
                            <div class="info">
                                <div class="info-title">预算金额（万元）</div>
                                <div class="info-num">{{ item.evaluationTotal || 0 }}</div>
                            </div>
                        </div>
                    </div>
                </el-col>
            </el-row>
            <el-row v-if="chartData.countMajorProjectCGB?.length >= 12" class=" mt20" :gutter="24">
                <el-col v-for="(item, index) in chartData.countMajorProjectCGB.slice(9, 12)" :key="index" :span="8">
                    <div class="row-item row-item-1">
                        <div v-if="index === 0" class="row-item-img">
                            <img src="/images/stat/icon-success.png" >
                        </div>
                        <div v-if="index === 1" class="row-item-img">
                            <img src="/images/stat/icon-right-arrow.png" >
                        </div>
                        <div v-if="index === 2" class="row-item-img">
                            <img src="/images/stat/icon-warning.png" >
                        </div>
                        <div class="row-item-infos">
                            <div class="info">
                                <div class="info-title actived">{{ item.columnName || '' }}</div>
                            </div>
                            <div class="info">
                                <div class="info-title" :title="item.columnNamePlus || ''">{{ item.columnNamePlus || '' }}</div>
                                <div class="info-num">{{ item.num || 0 }}</div>
                            </div>
                            <div class="info">
                                <div class="info-title">预算金额（万元）</div>
                                <div class="info-num">{{ item.evaluationTotal || 0 }}</div>
                            </div>
                        </div>
                    </div>
                </el-col>
            </el-row>
        </el-card>








    </div>
</template>

<script setup name="stat">
import { listDeptByUser } from "@/api/system/dept";
// import { queryPurchaseProjectStatistical, queryPurchaseProjectExecute, queryCountProjectExceed, queryCountMajorProject, 
//     queryPurchaseProjectCompleteNumber, queryCountEveryStatusNum, queryCountByProjectAttr, queryCountByEvaluation,
//     queryExceedMarketAnalysis, queryCountByPurchaseMouth, queryTaskQuantityAnalysis, queryExceedAnalysis, queryPurchaseProjectDistribution,
//     queryPurchaseProjectNumberAnalysis } from "@/api/stat/index";


import { queryCountPurTask, queryPurchaseProjectNumberAnalysis, queryCountByProjectAttrPurchase, queryCountByPurchaseMode, queryCountMajorProject, 
    queryCountByEvaluation, queryPurchaseProjectExecute, queryPurchaseTaskFinish, queryCountEveryStatusNum, 
    queryCountProjectExceed, queryCountMajorProjectNumCGB, queryExceedMarketAnalysis, queryExceedLink,
    queryExceedReasonZj, queryCountMajorProjectCGB } from '@/api/stat/index'


import BarLineComponent from "./components/bar-line/index";
import BarLine2Component from "./components/bar-line/index2";
import BarLine3Component from "./components/bar-line/index3";
import BarLine4Component from "./components/bar-line/index4";
import BarLine5Component from "./components/bar-line/index5";
import LineComponent from "./components/line/index";
import PieComponent from "./components/pie/index";
import Pie1Component from "./components/pie/index1";
import Pie2Component from "./components/pie/index2";

const data = ref({
    purchaseProjectStatistical: {},
    purchaseProjectExecute: {},
    countProjectExceed: {},
    countMajorProject: [
        {}, {}, {}, {}
    ]
})

const chartData = ref({
    purchaseProjectNumberAnalysis: { xAxis: undefined, rows: [] },
    purchaseProjectNumberAnalysis1: { xAxis: undefined, rows: [] },
    countByPurchaseMode: [],
    countByEvaluation: [],
    countByProjectAttr: [],
    countMajorProjectNumCGB: { xAxis: undefined, rows: [] },
    purchaseTaskFinish: { xAxis: undefined, rows: [] },
})
function init() {

    queryCountPurTask({ purchaseDeptId: unitSelected.value }).then((resp) => {
        chartData.value.demandSubCount = resp.data || {}
    });
    queryPurchaseProjectNumberAnalysis({ purchaseDeptId: unitSelected.value, timeType: 3 }).then((resp) => {
        chartData.value.purchaseProjectNumberAnalysis = {
            xAxis: resp.data.map(m => m.yy),
            rows: [
                resp.data.map(m => m.countNum || 0) || [],
                resp.data.map(m => m.yearonyearNum || 0) || [],
                resp.data.map(m => m.monthonmonthNum || 0) || [],
            ],
        }
        chartData.value.purchaseProjectNumberAnalysis1 = {
            xAxis: resp.data.map(m => m.yy),
            rows: [
                resp.data.map(m => m.evaluationSum || 0) || [],
                resp.data.map(m => m.yearonyearAmount || 0) || [],
                resp.data.map(m => m.monthonmonthAmount || 0) || [],
            ],
        }
    });
    queryCountProjectExceed({ purchaseDeptId: unitSelected.value }).then((resp) => {
        chartData.value.countProjectExceed = resp.data || {}
    });
    queryCountMajorProject({ purchaseDeptId: unitSelected.value }).then((resp) => {
        chartData.value.countMajorProject = resp.data || {}
    });
    
    queryCountByProjectAttrPurchase({ purchaseDeptId: unitSelected.value }).then((resp) => {
        chartData.value.countByProjectAttr = resp?.data?.map(m => {
            return {
                name: m.columnName,
                value: m.num || 0,
                money: m.evaluationTotal
            }
        })
        console.log('chartData.value.countByProjectAttr', chartData.value.countByProjectAttr)
    });
    
    queryCountByPurchaseMode({ purchaseDeptId: unitSelected.value }).then((resp) => {
        chartData.value.countByPurchaseMode = resp?.data?.map(m => {
            return {
                name: m.columnName,
                value: m.num || 0,
                money: m.evaluationTotal
            }
        })
    });
    
    queryCountByEvaluation({ purchaseDeptId: unitSelected.value }).then((resp) => {
        chartData.value.countByEvaluation = resp?.data?.map(m => {
            return {
                name: m.columnName,
                value: m.num || 0,
                money: m.evaluationTotal
            }
        })
    });
    
    // queryCountByProjectAttr({ purchaseDeptId: unitSelected.value }).then((resp) => {
    //     chartData.value.countByProjectAttr = {
    //         xAxis: resp.data.map(m => m.columnName),
    //         rows: [
    //             resp.data.map(m => m.evaluation || 0) || [],
    //             resp.data.map(m => m.num || 0) || [],
    //         ],
    //     }
    // });

    queryPurchaseProjectExecute({ purchaseDeptId: unitSelected.value }).then((resp) => {
        chartData.value.purchaseProjectExecute = resp.data || {}
    });

    queryPurchaseTaskFinish({ purchaseDeptId: unitSelected.value, timeType: 3 }).then((resp) => {
        chartData.value.purchaseTaskFinish = {
            xAxis: resp.data.map(m => m.yy),
            rows: [
                resp.data.map(m => m.countNum || 0) || [],
                resp.data.map(m => m.evaluation || 0) || [],,
                resp.data.map(m => m.contractAmount || 0) || [],
            ],
        }
    });

    queryCountEveryStatusNum({ purchaseDeptId: unitSelected.value }).then((resp) => {
        chartData.value.countEveryStatusNum = resp?.data?.map(m => {
            return {
                name: m.columnName,
                value: m.num || 0,
                money: m.evaluationTotal
            }
        })
    });

    

    queryCountProjectExceed({ purchaseDeptId: unitSelected.value }).then((resp) => {
        chartData.value.countProjectExceed = resp.data || {}
    });

    queryCountMajorProjectNumCGB({ purchaseDeptId: unitSelected.value, timeType: 3 }).then((resp) => {
        resp.data = resp.data['月']
        chartData.value.countMajorProjectNumCGB = {
            xAxis: resp.data.map(m => m.columnName),
            rows: [
                resp.data.map(m => m.num || 0) || [],
            ],
        }
    });

    

    queryExceedMarketAnalysis({ purchaseDeptId: unitSelected.value }).then((resp) => {
        chartData.value.exceedMarketAnalysis = Object.entries(resp?.data || {}).map(m => {
            return {
                name: m[0],
                value: m[1] || 0
            }
        })
    });

    queryExceedLink({ purchaseDeptId: unitSelected.value }).then((resp) => {
        chartData.value.exceedLink = resp?.data?.map(m => {
            return {
                name: m.columnName,
                value: m.num || 0,
                money: m.evaluationTotal
            }
        })
    });

    queryExceedReasonZj({ purchaseDeptId: unitSelected.value }).then((resp) => {
        chartData.value.exceedReasonZj = resp?.data?.map(m => {
            return {
                name: m.columnName,
                value: m.num || 0,
                money: m.evaluationTotal
            }
        })
    });

    queryCountMajorProjectCGB({ purchaseDeptId: unitSelected.value }).then((resp) => {
        chartData.value.countMajorProjectCGB = resp.data || {}
    });
    
}

const unitSelected = ref()
const dropdown = ref({})
function queryDropDown() {
    listDeptByUser().then((response) => {
        // .filter(f => f.parentId !== 0)
        if (!response.data) response.data = [];
        response.data = [ { deptId: -1, deptName: '全部' }, ...response.data ];
        dropdown.value.unitList = response.data;
        if (dropdown.value.unitList && dropdown.value.unitList.length) {
            unitSelected.value = dropdown.value.unitList[0].deptId;
            init();
        }
    });
}
queryDropDown()

function handleQuery() {

    chartData.value = {
        purchaseProjectNumberAnalysis: { xAxis: undefined, rows: [] },
        purchaseProjectNumberAnalysis1: { xAxis: undefined, rows: [] },
        countByPurchaseMode: [],
        countByEvaluation: [],
        countByProjectAttr: [],
        countMajorProjectNumCGB: { xAxis: undefined, rows: [] },
        purchaseTaskFinish: { xAxis: undefined, rows: [] },
    }

    filterSelected.value = {
        filter1: 'month',
        filter2: 'month',
        filter3: 'month',
        filter4: 'month'
    }
    init();
}


function loadFilterChart1(timeType) {
    chartData.value.purchaseProjectNumberAnalysis = { xAxis: undefined, rows: [] };
    queryPurchaseProjectNumberAnalysis({ purchaseDeptId: unitSelected.value, timeType: timeType }).then((resp) => {
        chartData.value.purchaseProjectNumberAnalysis = {
            xAxis: resp.data.map(m => m.yy),
            rows: [
                resp.data.map(m => m.countNum || 0) || [],
                resp.data.map(m => m.yearonyearNum || 0) || [],
                resp.data.map(m => m.monthonmonthNum || 0) || [],
            ],
        }
    })
}
function loadFilterChart2(timeType) {
    chartData.value.purchaseProjectNumberAnalysis1 = { xAxis: undefined, rows: [] };
    queryPurchaseProjectNumberAnalysis({ purchaseDeptId: unitSelected.value, timeType: timeType }).then((resp) => {
        chartData.value.purchaseProjectNumberAnalysis1 = {
            xAxis: resp.data.map(m => m.yy),
            rows: [
                resp.data.map(m => m.evaluationSum || 0) || [],
                resp.data.map(m => m.yearonyearAmount || 0) || [],
                resp.data.map(m => m.monthonmonthAmount || 0) || [],
            ],
        }
    })
}
function loadFilterChart3(timeType) {
    chartData.value.purchaseTaskFinish = { xAxis: undefined, rows: [] };
    queryPurchaseTaskFinish({ purchaseDeptId: unitSelected.value, timeType: timeType }).then((resp) => {
        chartData.value.purchaseTaskFinish = {
            xAxis: resp.data.map(m => m.yy),
            rows: [
                resp.data.map(m => m.countNum || 0) || [],
                resp.data.map(m => m.evaluation || 0) || [],,
                resp.data.map(m => m.contractAmount || 0) || [],
            ],
        }
    });
}
function loadFilterChart4(timeType) {
    chartData.value.countMajorProjectNumCGB = { xAxis: undefined, rows: [] };
    queryCountMajorProjectNumCGB({ purchaseDeptId: unitSelected.value, timeType: timeType }).then((resp) => {
        if (timeType === 3) {
            resp.data = resp.data['月']
        } else if (timeType === 2) {
            resp.data = resp.data['季度']
        } else if (timeType === 1) {
            resp.data = resp.data['年']
        }
        chartData.value.countMajorProjectNumCGB = {
            xAxis: resp.data.map(m => m.columnName),
            rows: [
                resp.data.map(m => m.num || 0) || [],
            ],
        }
    });
}

const filterSelected = ref({
    filter1: 'month',
    filter2: 'month',
    filter3: 'month',
    filter4: 'month'
})
let _yqmMapping = {
    'month': 3,
    'quarter': 2,
    'year': 1
}
function handleFilterItemClick(key, value) {
    filterSelected.value[key] = value;
    
    if (key === 'filter1') {
        loadFilterChart1(_yqmMapping[value])
    } else if (key === 'filter2') {
        loadFilterChart2(_yqmMapping[value])
    } else if (key === 'filter3') {
        loadFilterChart3(_yqmMapping[value]);
    } else if (key === 'filter4') {
        loadFilterChart4(_yqmMapping[value]);
    }
}
</script>

<style lang="scss" scoped>
.stat-page {
    padding: 20px;
}
.el-card {
    border: none !important;
}
.row-item {
    display: flex;
    flex-direction: row;
    align-items: center;
    background: #FFF7F0;
    border-radius: 5px 5px 5px 5px;
    padding: 20px 20px;
    box-sizing: border-box;
    
    &-img {
        width: 60px;
        img {
            width: 40px;
            height: 40px;
            vertical-align: middle;
            font-size: 0;
            position: relative;
            top: 4px;
        }
    }
    &-infos {
        flex: 1;
        display: flex;
        flex-direction: column;
        justify-items: center;
        justify-content: center;
        line-height: 30px;
        font-size: 15px;
        .info {
            display: flex;
            flex-direction: row;
            align-items: center;
            .info-title {
                flex: 1;
                color: #6f6f6f;
                // overflow: hidden;
                // white-space: nowrap;
                // text-overflow: ellipsis;
                // width: 130px
                &.actived {
                    color: #333;
                    font-weight: bold;
                }
            }
            .info-num {
                text-align: right;
                width: 100px;
                color: #333;
            }
        }
    }
}
.row-item-1 {
    position: relative;
    .row-item-img {
        width: 60px;
        position: absolute;
        right: -10px;
        top: 7px;
    }
    .info {
        .actived {
            position: relative;
            top: -5px;
        }
    }
}
.block-title {
    position: relative;
    width: 100%;
    padding: 10px 10px 10px 20px;
    box-sizing: border-box;
    // background: linear-gradient(270deg, rgba(218,231,246,0) 0%, #E3EBF4 100%);
    font-weight: bold;
    margin-bottom: 20px;
    &::before {
        content: ' ';
        position: absolute;
        left: 0px;
        width: 5px;
        height: 50%;
        top: 10px;
        background: #006aea;
        border-radius: 6px;
    }
}

.echart-title {
    position: absolute;
    top: 0px;
    left: 0px;
    right: 0px;
    width: 100%;
    padding: 0px 20px;
    z-index: 1;
    box-sizing: border-box;
    font-size: 18px;
    .filter-items {
        position: absolute;
        right: 12px;
        top: 0px;
        font-size: 14px;
        .filter-item {
            display: inline-block;
            background: #fff;
            padding: 4px 20px;
            border-radius: 4px;
            border: 1px solid #006AEA;
            color: #333;
            cursor: pointer;
            &.actived {
                background: #006AEA;
                color: #fff;
            }
            &:nth-child(n + 2) {
                margin-left: 6px;
            }
        }
    }
}

</style>