<template>
  <div class="app-container">    
    <!--工具条-->
    <el-form :inline="true">
      <el-form-item label="" prop="">
        <el-select v-model="dateType" clearable placeholder="请选择时间维度" style="width:160px;" @change="handleDateChange">
          <el-option label="日" :value="0"></el-option>
          <el-option label="月" :value="1"></el-option>
          <el-option label="年" :value="2"></el-option>
        </el-select>

        <el-date-picker
          style=""
          v-if="dateType==0"
          v-model="orderCountDate"
          type="daterange"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="handleDateChange"
          :picker-options="pickerOptions">
        </el-date-picker>

        <el-date-picker
          style=""
          v-if="dateType==1"
          v-model="orderCountMonth"
          type="monthrange"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="handleDateChange">
        </el-date-picker>
      </el-form-item>

      <el-form-item v-if="dateType==2">
        <el-date-picker
          v-model="yeartsStart"
          :picker-options="startDatePicker"
          value-format="yyyy"
          type="year"
          @change="handleDateChange"
          placeholder="开始时间"
        >
        </el-date-picker>
        ~
        <el-date-picker
          v-model="yeartsEnd"
          value-format="yyyy"
          @change="handleDateChange"
          :picker-options="endDatePicker"
          type="year"
          placeholder="结束时间"
        >
        </el-date-picker>
      </el-form-item>

      <el-form-item label="" prop="">
        <el-select v-model="capitalTypeId" clearable placeholder="请输入分类" @change="handleCapitalTypeIdChange">
					<el-option
						v-for="item in capitalTypeOptions"
						:key="item.id"
						:label="item.name"
						:value="item.id"
					></el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="" prop="">
        <el-select
        v-model="transactionCategoryId"
        @change="handleCapitalTypeIdChange"
        filterable
        clearable
        remote
        reserve-keyword
        placeholder="请输入交易分类名"
        >
          <el-option
            v-for="item in transactionCategoryOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>

      <!-- <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
      </el-form-item> -->
    </el-form>

    <div class="statistics-layout">
      <div class="layout-title-income">资金统计-收入</div>
      <el-row>
        <el-col :span="4">
          <div style="padding: 20px">
            <div>
              <div style="color: #909399;font-size: 14px">总交易数（笔）</div>
              <div style="color: #606266;font-size: 24px;padding: 10px 0">{{todayList.totalOrderCountOfIncome || 0}}</div>
            </div>
            <div>
              <div style="color: #909399;font-size: 14px">总收入（元）</div>
              <div style="color: red;font-size: 24px;padding: 10px 0">+{{todayList.totalOrderAmountOfIncome | formatMoney}}</div>
            </div>
            <!-- <div>
              <div style="color: #909399;font-size: 14px">本月交易总数</div>
              <div style="color: #606266;font-size: 24px;padding: 10px 0">{{todayList.thisMonthCountPaid || 0}}</div>
              <div>
                <span class="color-success" style="font-size: 14px">{{todayList.thisMonthCountPaidCompare || 0}}</span>
                <span style="color: #C0C4CC;font-size: 14px">同比上月</span>
              </div>
            </div>
            <div style="margin-top: 20px;">
              <div style="color: #909399;font-size: 14px">本周交易总数</div>
              <div style="color: #606266;font-size: 24px;padding: 10px 0">{{todayList.thisWeekCountPaid || 0}}</div>
              <div>
                <span class="color-danger" style="font-size: 14px">{{todayList.thisWeekCountPaidCompare || 0}}</span>
                <span style="color: #C0C4CC;font-size: 14px">同比上周</span>
              </div>
            </div>
            <div style="margin-top: 20px;">
              <div style="color: #909399;font-size: 14px">本月销售总额</div>
              <div style="color: #606266;font-size: 24px;padding: 10px 0">{{todayList.thisMonthSumActualPrice || 0}}</div>
              <div>
                <span class="color-success" style="font-size: 14px">{{todayList.thisMonthSumActualPriceCompare || 0}}</span>
                <span style="color: #C0C4CC;font-size: 14px">同比上月</span>
              </div>
            </div>
            <div style="margin-top: 20px;">
              <div style="color: #909399;font-size: 14px">本周销售总额</div>
              <div style="color: #606266;font-size: 24px;padding: 10px 0">{{todayList.thisWeekSumActualPrice || 0}}</div>
              <div>
                <span class="color-danger" style="font-size: 14px">{{todayList.thisWeekSumActualPriceCompare || 0}}</span>
                <span style="color: #C0C4CC;font-size: 14px">同比上周</span>
              </div>
            </div> -->
          </div>
        </el-col>
        <el-col :span="20">
          <div style="padding: 10px;border-left:1px solid #DCDFE6">
            <div>
              <ve-line
                :data="chartDataOfIncome"
                :legend-visible="true"
                :loading="loadingOfIncome"
                :data-empty="dataEmptyOfIncome"
                :settings="chartSettings"></ve-line>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <div class="statistics-layout">
      <div class="layout-title-expend">资金统计-支出</div>
      <el-row>
        <el-col :span="4">
          <div style="padding: 20px">
            <div>
              <div style="color: #909399;font-size: 14px">总交易数（笔）</div>
              <div style="color: #606266;font-size: 24px;padding: 10px 0">{{todayList.totalOrderCountOfExpend || 0}}</div>
            </div>
            <div>
              <div style="color: #909399;font-size: 14px">总支出（元）</div>
              <div style="color: green;font-size: 24px;padding: 10px 0">-{{todayList.totalOrderAmountOfExpend | formatMoney}}</div>
            </div>
          </div>
        </el-col>
        <el-col :span="20">
          <div style="padding: 10px;border-left:1px solid #DCDFE6">
            <div>
              <ve-line
                :data="chartDataOfExpend"
                :legend-visible="true"
                :loading="loadingOfExpend"
                :data-empty="dataEmptyOfExpend"
                :settings="chartSettings"></ve-line>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

  </div>
</template>

<script>
  // import VCharts from 'v-charts'
  import {str2Date} from '../../utils/date';
//   import img_home_order from '/assets/home_order.png';
//   import img_home_today_amount from '/assets/home_today_amount.png';
//   import img_home_yesterday_amount from '/assets/home_yesterday_amount.png';
  const DATA_FROM_BACKEND_OF_INCOME = {
    // columns: ['date', 'orderCount','orderAmount'],
    columns: ['date','orderAmount'],
    rows: [
      // {date: '2020-11-01', orderCount: 10, orderAmount: 1093},
    ]
  };
  const DATA_FROM_BACKEND_OF_INCOME_MONTH = {
    columns: ['date','orderAmount'],
    rows: []
  };
  const DATA_FROM_BACKEND_OF_INCOME_YEAR = {
    columns: ['date','orderAmount'],
    rows: []
  };  
  const DATA_FROM_BACKEND_OF_EXPEND = {
    columns: ['date','orderAmount'],
    rows: [
      // {date: '2020-11-01', orderCount: 10, orderAmount: 1093},
      // {date: '2020-11-30', orderCount: 40, orderAmount: 4293}
    ]
  };
  const DATA_FROM_BACKEND_OF_EXPEND_MONTH = {
    columns: ['date','orderAmount'],
    rows: []
  };
  const DATA_FROM_BACKEND_OF_EXPEND_YEAR = {
    columns: ['date','orderAmount'],
    rows: []
  };  
  export default {
    name: 'home',
    data() {
      return {
        startDatePicker: this.beginDate(),
        endDatePicker: this.processDate(),   
        yeartsStart:'',
        yeartsEnd:'',        
        pickerOptions: {
          shortcuts: [{
            text: '本周',
            onClick(picker) {
              //获取本周一
              const now = new Date()
              const nowTime = now.getTime()
              // getDay()返回0-6，其中0表示周日，需特殊处理
              const day = now.getDay() > 0 ? now.getDay() : 7 // 表示当前是周几
              const oneDayTime = 24 * 60 * 60 * 1000 // 一天的总ms
              // 本周一时间戳
              const MondayTime = nowTime - (day - 1) * oneDayTime
              // 格式化时间
              const monday = new Date(MondayTime)

              let start = new Date();
              start.setDate(monday.getDate());
              const end = new Date();
              //时分秒置零
              start.setHours(0);
              start.setMinutes(0);
              start.setSeconds(0);
              start.setMilliseconds(0);
              end.setHours(0);
              end.setMinutes(0);
              end.setSeconds(0);
              end.setMilliseconds(0);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '本月',
            onClick(picker) {
              let start = new Date();
              start.setDate(1);
              const end = new Date();
              //时分秒置零
              start.setHours(0);
              start.setMinutes(0);
              start.setSeconds(0);
              start.setMilliseconds(0);
              end.setHours(0);
              end.setMinutes(0);
              end.setSeconds(0);
              end.setMilliseconds(0);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '本季度',
            onClick(picker) {
              let start = new Date();
              var startMonth = start.getMonth() <= 2 ? 0 : (start.getMonth() <= 5 ? 3 : (start.getMonth() <= 8 ? 6 : 9)) 
              start.setMonth(startMonth);
              start.setDate(1);
              const end = new Date();
              //时分秒置零
              start.setHours(0);
              start.setMinutes(0);
              start.setSeconds(0);
              start.setMilliseconds(0);
              end.setHours(0);
              end.setMinutes(0);
              end.setSeconds(0);
              end.setMilliseconds(0);              
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '上半年',
            onClick(picker) {
              let start = new Date();
              start.setMonth(0);
              start.setDate(1);
              const end = new Date();
              if(end.getMonth() > 5){
                end.setMonth(5);
                end.setDate(31);
              }
              //时分秒置零
              start.setHours(0);
              start.setMinutes(0);
              start.setSeconds(0);
              start.setMilliseconds(0);
              end.setHours(0);
              end.setMinutes(0);
              end.setSeconds(0);
              end.setMilliseconds(0);              
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '下半年',
            onClick(picker) {
              let start = new Date();
              start.setMonth(6);
              start.setDate(1);
              const end = new Date();
              //时分秒置零
              start.setHours(0);
              start.setMinutes(0);
              start.setSeconds(0);
              start.setMilliseconds(0);
              end.setHours(0);
              end.setMinutes(0);
              end.setSeconds(0);
              end.setMilliseconds(0);              
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '本年度',
            onClick(picker) {
              let start = new Date();
              start.setMonth(0);
              start.setDate(1);
              const end = new Date();
              //时分秒置零
              start.setHours(0);
              start.setMinutes(0);
              start.setSeconds(0);
              start.setMilliseconds(0);
              end.setHours(0);
              end.setMinutes(0);
              end.setSeconds(0);
              end.setMilliseconds(0);              
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '全部',
            onClick(picker) {
              let start = new Date();
              start.setFullYear(2018);
              start.setMonth(0);
              start.setDate(1);
              const end = new Date();
              //时分秒置零
              start.setHours(0);
              start.setMinutes(0);
              start.setSeconds(0);
              start.setMilliseconds(0);
              end.setHours(0);
              end.setMinutes(0);
              end.setSeconds(0);
              end.setMilliseconds(0);              
              picker.$emit('pick', [start, end]);
            }
          }]
        },
        //收入区域
        // capitalTypeId: 5,
        transactionCategoryId: '',
        dateType:1,
        orderCountDate: '',
        orderCountMonth: '',
        // grid: {
        //   left: '5%',
        //   right: '5%',
        //   bottom: '3%',
        //   top: '10%',
        //   containLabel: true
        // },
        chartSettings: {
          // xAxisType: 'time',
          // area:true, //是否填充区域
          axisSite: { right: ['orderCount']},
          // labelMap: {'orderAmount': '金额(元)'},
          labelMap: {'orderCount': '交易数(笔)', 'orderAmount': '金额(元)'}
          // xAxis: {
          //   axisLabel: {
          //     margin: 15, // 刻度标签与轴线之间的距离
          //     interval: 0, // 坐标轴刻度标签的显示间隔
          //     rotate: 10, // 刻度标签旋转的角度
          //     align: 'center' // 文字水平对齐方式
          //   }
          // }
        },
        chartDataOfIncome: {
          columns: [],
          rows: []
        },
        loadingOfIncome: false,
        dataEmptyOfIncome: false,

        //支出区域
        chartDataOfExpend: {
          columns: [],
          rows: []
        },
        loadingOfExpend: false,
        dataEmptyOfExpend: false,

        todayList:{}, //今日数据
        transactionCategoryOptions: [], //分类候选框
        capitalTypeOptions: [], //分类候选框
      }
    },
    created(){
      this.initOrderCountDate();

      // 获取交易分类
      this.transactionCategoryList()
            
      this.capitalTypeList()

      let vue = this

      //查询基础统计数据
      // vue.$http.post(vue, '/rest/member/statistics/todayStatistic', {},
      //   (vue, data) => {
      //     vue.todayList.dayCountPaid = data.data.dayCountPaid;
      //     vue.todayList.daySumActualPrice = data.data.daySumActualPrice;
      //     vue.todayList.handleShopCount = data.data.handleShopCount;
      //     vue.todayList.handleMerchantWithdrawCount = data.data.handleMerchantWithdrawCount;
      //     vue.todayList.handleShopChangeCount = data.data.handleShopChangeCount;
      //     vue.todayList.handleOrderRefundCount = data.data.handleOrderRefundCount;
      //     vue.todayList.underShelfShopCount = data.data.underShelfShopCount;
      //     vue.todayList.onShelfShopCount = data.data.onShelfShopCount;
      //     vue.todayList.allShopCount = data.data.allShopCount;
      //     vue.todayList.dayMemberCount = data.data.dayMemberCount;
      //     vue.todayList.yesterdayMemberCount = data.data.yesterdayMemberCount;
      //     vue.todayList.thisMonthMemberCount = data.data.thisMonthMemberCount;
      //     vue.todayList.allMemberCount = data.data.allMemberCount;
      //     vue.todayList.totalActualPrice = data.data.totalActualPrice;
      //     vue.todayList.totalDeliveryFee = data.data.totalDeliveryFee;
      //     vue.todayList.totalRefundAmount = data.data.totalRefundAmount;
      //     vue.todayList.totalRefundDeliveryFee = data.data.totalRefundDeliveryFee;
      //     vue.todayList.unCompletedNum = data.data.unCompletedNum;
      //     vue.todayList.waitHandleRefundNum = data.data.waitHandleRefundNum;
      //     vue.todayList.balance = data.data.balance;
      //     vue.todayList.daySumIncome = data.data.daySumIncome;
      //     vue.todayList.daySumExpense = data.data.daySumExpense;
      //   },(error, data)=> {
      //     vue.$message({
      //       showClose: true,
      //       message: data.message,
      //       type: 'error'
      //     });
      //   }
      // )

      //查询资金统计数据-收入
      this.initStatisticOfLineChart(0);
      //查询资金统计数据-支出
      this.initStatisticOfLineChart(1);
    },
    methods:{
			capitalTypeList: function() {
				let vue = this
				var params = {};
				params.pageNo = 1;
				params.pageSize = 500;
				vue.$http.post(vue, '/rest/member/capitalType/list', params,
					(vue, data) => {
						vue.capitalTypeOptions = data.data.records;
					},(error, data)=> {
						vue.$message({
							showClose: true,
							message: data.message,
							type: 'error'
						});
					}
				)
			},
			transactionCategoryList: function() {
				let vue = this
				var params = {};
				params.pageNo = 1;
				params.pageSize = 500;
				vue.$http.post(vue, '/rest/member/transactionCategory/list', params,
					(vue, data) => {
						vue.transactionCategoryOptions = data.data.records;
					},(error, data)=> {
						vue.$message({
							showClose: true,
							message: data.message,
							type: 'error'
						});
					}
				)
			},	      
      // 选择年份范围选择时开始时间不能大于结束时间，结束时间不能小于开始时间
      // 提出开始时间必须小于提出结束时间
      beginDate() {
        let self = this
        return {
          disabledDate(time) {
            if (self.yeartsEnd !== '') {
              let fixedTime = new Date(time)
              return fixedTime.getFullYear() > self.yeartsEnd
            }
          }
        }
      },
      // 提出结束时间必须大于提出开始时间
      processDate() {
        let self = this
        return {
          disabledDate(time) {
            // let fixedTime = new Date(self.oldTime).getTime()
            // return time.getTime() < fixedTime
            let fixedTime = new Date(time)
            return fixedTime.getFullYear() < self.yeartsStart
          }
        }
      },
      handleDateChange(){
        if(this.dateType == 0){
          if(this.orderCountDate){
            this.getData(0);
            this.getData(1);
          }
        }else if(this.dateType == 1){
          if(this.orderCountMonth){
            this.getData(0);
            this.getData(1);
          }
        }else if(this.dateType == 2){
          if(this.yeartsStart != '' && this.yeartsEnd != ''){
            this.getData(0);
            this.getData(1);
          }
        }
      },
      handleCapitalTypeIdChange(){
        this.initStatisticOfLineChart(0);
        this.initStatisticOfLineChart(1);
      },
      initOrderCountDate(){
        //按日选择：默认展示本月的交易数据
        let start = new Date();
        start.setDate(1);
        const end = new Date();
        //时分秒置零
        start.setHours(0);
        start.setMinutes(0);
        start.setSeconds(0);
        start.setMilliseconds(0);
        end.setHours(0);
        end.setMinutes(0);
        end.setSeconds(0);
        end.setMilliseconds(0);            
        this.orderCountDate = [start, end];

        //按月选择：默认展示本年的交易数据
        let start2 = new Date();
        start2.setMonth(0);
        start2.setDate(1);
        const end2 = new Date();
        //时分秒置零
        start2.setHours(0);
        start2.setMinutes(0);
        start2.setSeconds(0);
        start2.setMilliseconds(0);
        end2.setHours(0);
        end2.setMinutes(0);
        end2.setSeconds(0);
        end2.setMilliseconds(0);            
        this.orderCountMonth = [start2, end2];

        //按年选择：默认展示本年的交易数据
        this.yeartsStart = new Date().getFullYear().toString();
        this.yeartsEnd = new Date().getFullYear().toString();        
      },
      initStatisticOfLineChart(type){
        //查询资金统计数据
        let vue = this
        if(type == 0){
          //收入
          //1、按照日来统计
          var params = {};
          params.dateType = 0;
          params.capitalTypeId = this.capitalTypeId;
          params.transactionCategoryId = this.transactionCategoryId;
          params.type = type;
          vue.$http.post(vue, '/rest/member/capitalRecord/statisticOfLineChart', params,
            (vue, data) => {
              //加载折线图数据
              DATA_FROM_BACKEND_OF_INCOME.rows = data.data.resultList;

              // vue.todayList.thisMonthCountPaid = data.data.thisMonthCountPaid;
              // vue.todayList.lastMonthCountPaid = data.data.lastMonthCountPaid;
              // vue.todayList.thisWeekCountPaid = data.data.thisWeekCountPaid;
              // vue.todayList.lastWeekCountPaid = data.data.lastWeekCountPaid;
              // vue.todayList.thisMonthSumActualPrice = data.data.thisMonthSumActualPrice;
              // vue.todayList.lastMonthSumActualPrice = data.data.lastMonthSumActualPrice;
              // vue.todayList.thisWeekSumActualPrice = data.data.thisWeekSumActualPrice;
              // vue.todayList.lastWeekSumActualPrice = data.data.lastWeekSumActualPrice;

              // //计算同比情况
              // if(vue.todayList.lastMonthCountPaid == 0){
              //   vue.todayList.thisMonthCountPaidCompare = "无";
              // }else if(vue.todayList.thisMonthCountPaid > vue.todayList.lastMonthCountPaid){
              //   let compareNum = (vue.todayList.thisMonthCountPaid - vue.todayList.lastMonthCountPaid) / vue.todayList.lastMonthCountPaid;
              //   vue.todayList.thisMonthCountPaidCompare = "+" + Number(compareNum * 100).toFixed(0) + "%";
              // }else{
              //   let compareNum = (vue.todayList.lastMonthCountPaid - vue.todayList.thisMonthCountPaid) / vue.todayList.lastMonthCountPaid;
              //   vue.todayList.thisMonthCountPaidCompare = "-" + Number(compareNum * 100).toFixed(0) + "%";            
              // }

              // if(vue.todayList.lastWeekCountPaid == 0){
              //   vue.todayList.thisWeekCountPaidCompare = "无";
              // }else if(vue.todayList.thisWeekCountPaid > vue.todayList.lastWeekCountPaid){
              //   let compareNum = (vue.todayList.thisWeekCountPaid - vue.todayList.lastWeekCountPaid) / vue.todayList.lastWeekCountPaid;
              //   vue.todayList.thisWeekCountPaidCompare = "+" + Number(compareNum * 100).toFixed(0) + "%";
              // }else{
              //   let compareNum = (vue.todayList.lastWeekCountPaid - vue.todayList.thisWeekCountPaid) / vue.todayList.lastWeekCountPaid;
              //   vue.todayList.thisWeekCountPaidCompare = "-" + Number(compareNum * 100).toFixed(0) + "%";            
              // }

              // if(vue.todayList.lastMonthSumActualPrice == 0){
              //   vue.todayList.thisMonthSumActualPriceCompare = "无";
              // }else if(vue.todayList.thisMonthSumActualPrice > vue.todayList.lastMonthSumActualPrice){
              //   let compareNum = (vue.todayList.thisMonthSumActualPrice - vue.todayList.lastMonthSumActualPrice) / vue.todayList.lastMonthSumActualPrice;
              //   vue.todayList.thisMonthSumActualPriceCompare = "+" + Number(compareNum * 100).toFixed(0) + "%";
              // }else{
              //   let compareNum = (vue.todayList.lastMonthSumActualPrice - vue.todayList.thisMonthSumActualPrice) / vue.todayList.lastMonthSumActualPrice;
              //   vue.todayList.thisMonthSumActualPriceCompare = "-" + Number(compareNum * 100).toFixed(0) + "%";            
              // }

              // if(vue.todayList.lastWeekSumActualPrice == 0){
              //   vue.todayList.thisWeekSumActualPriceCompare = "无";
              // }else if(vue.todayList.thisWeekSumActualPrice > vue.todayList.lastWeekSumActualPrice){
              //   let compareNum = (vue.todayList.thisWeekSumActualPrice - vue.todayList.lastWeekSumActualPrice) / vue.todayList.lastWeekSumActualPrice;
              //   vue.todayList.thisWeekSumActualPriceCompare = "+" + Number(compareNum * 100).toFixed(0) + "%";
              // }else{
              //   let compareNum = (vue.todayList.lastWeekSumActualPrice - vue.todayList.thisWeekSumActualPrice) / vue.todayList.lastWeekSumActualPrice;
              //   vue.todayList.thisWeekSumActualPriceCompare = "-" + Number(compareNum * 100).toFixed(0) + "%";            
              // }
            },(error, data)=> {
              vue.$message({
                showClose: true,
                message: data.message,
                type: 'error'
              });
            }
          )

          //2、按照月来统计
          var params2 = {};
          params2.dateType = 1;
          params2.capitalTypeId = this.capitalTypeId;
          params2.transactionCategoryId = this.transactionCategoryId;
          params2.type = type;
          vue.$http.post(vue, '/rest/member/capitalRecord/statisticOfLineChart', params2,
            (vue, data) => {
              //加载折线图数据
              DATA_FROM_BACKEND_OF_INCOME_MONTH.rows = data.data.resultList;
            },(error, data)=> {
              vue.$message({
                showClose: true,
                message: data.message,
                type: 'error'
              });
            }
          )

          //3、按照年来统计
          var params3 = {};
          params3.dateType = 2;
          params3.capitalTypeId = this.capitalTypeId;
          params3.transactionCategoryId = this.transactionCategoryId;
          params3.type = type;
          vue.$http.post(vue, '/rest/member/capitalRecord/statisticOfLineChart', params3,
            (vue, data) => {
              //加载折线图数据
              DATA_FROM_BACKEND_OF_INCOME_YEAR.rows = data.data.resultList;
            },(error, data)=> {
              vue.$message({
                showClose: true,
                message: data.message,
                type: 'error'
              });
            }
          )

          //加载图形数据
          this.getData(0);

        }else if(type == 1){
          //支出
          var params = {};
          params.dateType = 0;
          params.capitalTypeId = this.capitalTypeId;
          params.transactionCategoryId = this.transactionCategoryId;
          params.type = type;
          vue.$http.post(vue, '/rest/member/capitalRecord/statisticOfLineChart', params,
            (vue, data) => {
              //加载折线图数据
              DATA_FROM_BACKEND_OF_EXPEND.rows = data.data.resultList;

              // vue.todayList.thisMonthCountPaid = data.data.thisMonthCountPaid;
              // vue.todayList.lastMonthCountPaid = data.data.lastMonthCountPaid;
              // vue.todayList.thisWeekCountPaid = data.data.thisWeekCountPaid;
              // vue.todayList.lastWeekCountPaid = data.data.lastWeekCountPaid;
              // vue.todayList.thisMonthSumActualPrice = data.data.thisMonthSumActualPrice;
              // vue.todayList.lastMonthSumActualPrice = data.data.lastMonthSumActualPrice;
              // vue.todayList.thisWeekSumActualPrice = data.data.thisWeekSumActualPrice;
              // vue.todayList.lastWeekSumActualPrice = data.data.lastWeekSumActualPrice;

              // //计算同比情况
              // if(vue.todayList.lastMonthCountPaid == 0){
              //   vue.todayList.thisMonthCountPaidCompare = "无";
              // }else if(vue.todayList.thisMonthCountPaid > vue.todayList.lastMonthCountPaid){
              //   let compareNum = (vue.todayList.thisMonthCountPaid - vue.todayList.lastMonthCountPaid) / vue.todayList.lastMonthCountPaid;
              //   vue.todayList.thisMonthCountPaidCompare = "+" + Number(compareNum * 100).toFixed(0) + "%";
              // }else{
              //   let compareNum = (vue.todayList.lastMonthCountPaid - vue.todayList.thisMonthCountPaid) / vue.todayList.lastMonthCountPaid;
              //   vue.todayList.thisMonthCountPaidCompare = "-" + Number(compareNum * 100).toFixed(0) + "%";            
              // }

              // if(vue.todayList.lastWeekCountPaid == 0){
              //   vue.todayList.thisWeekCountPaidCompare = "无";
              // }else if(vue.todayList.thisWeekCountPaid > vue.todayList.lastWeekCountPaid){
              //   let compareNum = (vue.todayList.thisWeekCountPaid - vue.todayList.lastWeekCountPaid) / vue.todayList.lastWeekCountPaid;
              //   vue.todayList.thisWeekCountPaidCompare = "+" + Number(compareNum * 100).toFixed(0) + "%";
              // }else{
              //   let compareNum = (vue.todayList.lastWeekCountPaid - vue.todayList.thisWeekCountPaid) / vue.todayList.lastWeekCountPaid;
              //   vue.todayList.thisWeekCountPaidCompare = "-" + Number(compareNum * 100).toFixed(0) + "%";            
              // }

              // if(vue.todayList.lastMonthSumActualPrice == 0){
              //   vue.todayList.thisMonthSumActualPriceCompare = "无";
              // }else if(vue.todayList.thisMonthSumActualPrice > vue.todayList.lastMonthSumActualPrice){
              //   let compareNum = (vue.todayList.thisMonthSumActualPrice - vue.todayList.lastMonthSumActualPrice) / vue.todayList.lastMonthSumActualPrice;
              //   vue.todayList.thisMonthSumActualPriceCompare = "+" + Number(compareNum * 100).toFixed(0) + "%";
              // }else{
              //   let compareNum = (vue.todayList.lastMonthSumActualPrice - vue.todayList.thisMonthSumActualPrice) / vue.todayList.lastMonthSumActualPrice;
              //   vue.todayList.thisMonthSumActualPriceCompare = "-" + Number(compareNum * 100).toFixed(0) + "%";            
              // }

              // if(vue.todayList.lastWeekSumActualPrice == 0){
              //   vue.todayList.thisWeekSumActualPriceCompare = "无";
              // }else if(vue.todayList.thisWeekSumActualPrice > vue.todayList.lastWeekSumActualPrice){
              //   let compareNum = (vue.todayList.thisWeekSumActualPrice - vue.todayList.lastWeekSumActualPrice) / vue.todayList.lastWeekSumActualPrice;
              //   vue.todayList.thisWeekSumActualPriceCompare = "+" + Number(compareNum * 100).toFixed(0) + "%";
              // }else{
              //   let compareNum = (vue.todayList.lastWeekSumActualPrice - vue.todayList.thisWeekSumActualPrice) / vue.todayList.lastWeekSumActualPrice;
              //   vue.todayList.thisWeekSumActualPriceCompare = "-" + Number(compareNum * 100).toFixed(0) + "%";            
              // }
            },(error, data)=> {
              vue.$message({
                showClose: true,
                message: data.message,
                type: 'error'
              });
            }
          )

          //2、按照月来统计
          var params2 = {};
          params2.dateType = 1;
          params2.capitalTypeId = this.capitalTypeId;
          params2.transactionCategoryId = this.transactionCategoryId;
          params2.type = type;
          vue.$http.post(vue, '/rest/member/capitalRecord/statisticOfLineChart', params2,
            (vue, data) => {
              //加载折线图数据
              DATA_FROM_BACKEND_OF_EXPEND_MONTH.rows = data.data.resultList;
            },(error, data)=> {
              vue.$message({
                showClose: true,
                message: data.message,
                type: 'error'
              });
            }
          )

          //3、按照年来统计
          var params3 = {};
          params3.dateType = 2;
          params3.capitalTypeId = this.capitalTypeId;
          params3.transactionCategoryId = this.transactionCategoryId;
          params3.type = type;
          vue.$http.post(vue, '/rest/member/capitalRecord/statisticOfLineChart', params3,
            (vue, data) => {
              //加载折线图数据
              DATA_FROM_BACKEND_OF_EXPEND_YEAR.rows = data.data.resultList;
            },(error, data)=> {
              vue.$message({
                showClose: true,
                message: data.message,
                type: 'error'
              });
            }
          )

          //加载图形数据
          this.getData(1);
        }
      },      
      getData(type){
        if(type == 0){
          //收入
          setTimeout(() => {
            this.chartDataOfIncome = {
              columns: ['date', 'orderCount','orderAmount'],
              // columns: ['date','orderAmount'],
              rows: []
            };
            var totalOrderCountOfIncome = Number(0);
            var totalOrderAmountOfIncome = Number(0);

            if(this.dateType == 0){
              //日
              for(let i=0;i<DATA_FROM_BACKEND_OF_INCOME.rows.length;i++){
                let item=DATA_FROM_BACKEND_OF_INCOME.rows[i];
                let currDate=str2Date(item.date);
                let start=this.orderCountDate[0];
                let end=this.orderCountDate[1];
                if(currDate.getTime() >= start.getTime() && currDate.getTime() <= end.getTime()){
                  this.chartDataOfIncome.rows.push(item);
                  totalOrderCountOfIncome = this.$utils.NumberAdd(totalOrderCountOfIncome, item.orderCount);
                  totalOrderAmountOfIncome = this.$utils.NumberAdd(totalOrderAmountOfIncome, item.orderAmount);
                }
              }
            }else if(this.dateType == 1){
              //月
              for(let i=0;i<DATA_FROM_BACKEND_OF_INCOME_MONTH.rows.length;i++){
                let item=DATA_FROM_BACKEND_OF_INCOME_MONTH.rows[i];
                let currDate=str2Date(item.date + "-01");
                let start=this.orderCountMonth[0];
                let end=this.orderCountMonth[1];
                if(currDate.getTime() >= start.getTime() && currDate.getTime() <= end.getTime()){
                  this.chartDataOfIncome.rows.push(item);
                  totalOrderCountOfIncome = this.$utils.NumberAdd(totalOrderCountOfIncome, item.orderCount);
                  totalOrderAmountOfIncome = this.$utils.NumberAdd(totalOrderAmountOfIncome, item.orderAmount);
                }
              }              
            }else if(this.dateType == 2){
              //年
              for(let i=0;i<DATA_FROM_BACKEND_OF_INCOME_YEAR.rows.length;i++){
                let item=DATA_FROM_BACKEND_OF_INCOME_YEAR.rows[i];
                let currDate=item.date;
                let start=this.yeartsStart;
                let end=this.yeartsEnd;
                if(currDate >= start && currDate <= end){
                  item.date = item.date;
                  this.chartDataOfIncome.rows.push(item);
                  totalOrderCountOfIncome = this.$utils.NumberAdd(totalOrderCountOfIncome, item.orderCount);
                  totalOrderAmountOfIncome = this.$utils.NumberAdd(totalOrderAmountOfIncome, item.orderAmount);
                }
              }
            }

            this.dataEmptyOfIncome = false;
            this.loadingOfIncome = false

            //渲染交易总数、交易总额
            this.todayList.totalOrderCountOfIncome = totalOrderCountOfIncome;
            this.todayList.totalOrderAmountOfIncome = totalOrderAmountOfIncome;

          }, 1000)          
        }else if(type == 1){
          //支出
          setTimeout(() => {
            this.chartDataOfExpend = {
              columns: ['date', 'orderCount','orderAmount'],
              // columns: ['date','orderAmount'],
              rows: []
            };
            var totalOrderCountOfExpend = Number(0);
            var totalOrderAmountOfExpend = Number(0);            

            if(this.dateType == 0){
              //日
              for(let i=0;i<DATA_FROM_BACKEND_OF_EXPEND.rows.length;i++){
                let item=DATA_FROM_BACKEND_OF_EXPEND.rows[i];
                let currDate=str2Date(item.date);
                let start=this.orderCountDate[0];
                let end=this.orderCountDate[1];
                if(currDate.getTime()>=start.getTime()&&currDate.getTime()<=end.getTime()){
                  this.chartDataOfExpend.rows.push(item);
                  totalOrderCountOfExpend = this.$utils.NumberAdd(totalOrderCountOfExpend, item.orderCount);
                  totalOrderAmountOfExpend = this.$utils.NumberAdd(totalOrderAmountOfExpend, item.orderAmount);                
                }
              }
            }else if(this.dateType == 1){
              //月
              for(let i=0;i<DATA_FROM_BACKEND_OF_EXPEND_MONTH.rows.length;i++){
                let item=DATA_FROM_BACKEND_OF_EXPEND_MONTH.rows[i];
                let currDate=str2Date(item.date + "-01");
                let start=this.orderCountMonth[0];
                let end=this.orderCountMonth[1];
                if(currDate.getTime()>=start.getTime()&&currDate.getTime()<=end.getTime()){
                  this.chartDataOfExpend.rows.push(item);
                  totalOrderCountOfExpend = this.$utils.NumberAdd(totalOrderCountOfExpend, item.orderCount);
                  totalOrderAmountOfExpend = this.$utils.NumberAdd(totalOrderAmountOfExpend, item.orderAmount);                
                }
              }              
            }else if(this.dateType == 2){
              //年
              for(let i=0;i<DATA_FROM_BACKEND_OF_EXPEND_YEAR.rows.length;i++){
                let item=DATA_FROM_BACKEND_OF_EXPEND_YEAR.rows[i];
                let currDate=item.date;
                let start=this.yeartsStart;
                let end=this.yeartsEnd;
                if(currDate >= start && currDate <= end){
                  item.date = item.date;
                  this.chartDataOfExpend.rows.push(item);
                  totalOrderCountOfExpend = this.$utils.NumberAdd(totalOrderCountOfExpend, item.orderCount);
                  totalOrderAmountOfExpend = this.$utils.NumberAdd(totalOrderAmountOfExpend, item.orderAmount);                
                }
              }
            }

            // for(let i=0;i<DATA_FROM_BACKEND_OF_EXPEND.rows.length;i++){
            //   let item=DATA_FROM_BACKEND_OF_EXPEND.rows[i];
            //   let currDate=str2Date(item.date);
            //   let start=this.orderCountDate[0];
            //   let end=this.orderCountDate[1];
            //   if(currDate.getTime()>=start.getTime()&&currDate.getTime()<=end.getTime()){
            //     this.chartDataOfExpend.rows.push(item);
            //     totalOrderCountOfExpend = this.$utils.NumberAdd(totalOrderCountOfExpend, item.orderCount);
            //     totalOrderAmountOfExpend = this.$utils.NumberAdd(totalOrderAmountOfExpend, item.orderAmount);                
            //   }
            // }

            this.dataEmptyOfExpend = false;
            this.loadingOfExpend = false
            
            //渲染交易总数、交易总额
            this.todayList.totalOrderCountOfExpend = totalOrderCountOfExpend;
            this.todayList.totalOrderAmountOfExpend = totalOrderAmountOfExpend;

          }, 1000)          
        }
      }
    }
  }
</script>

<style scoped>
  .app-container {
    margin-top: 40px;
    /* margin-left: 120px; */
    /* margin-right: 120px; */
  }

  .address-layout {
  }

  .total-layout {
    margin-top: 20px;
  }

  .total-frame {
    border: 1px solid #DCDFE6;
    padding: 20px;
    height: 100px;
  }

  .total-icon {
    color: #409EFF;
    width: 60px;
    height: 60px;
  }

  .total-title {
    position: relative;
    font-size: 16px;
    color: #909399;
    left: 70px;
    top: -50px;
  }

  .total-value {
    position: relative;
    font-size: 18px;
    color: #606266;
    left: 70px;
    top: -40px;
  }

  .un-handle-layout {
    margin-top: 20px;
    border: 1px solid #DCDFE6;
  }

  .layout-title-income {
    /* color: #606266; */
    color: red;
    padding: 15px 20px;
    background: #F2F6FC;
    font-weight: bold;
  }
  
  .layout-title-expend {
    /* color: #606266; */
    color: green;
    padding: 15px 20px;
    background: #F2F6FC;
    font-weight: bold;
  }

  .un-handle-content {
    padding: 20px 40px;
  }

  .un-handle-item {
    border-bottom: 1px solid #EBEEF5;
    padding: 10px;
    font-size:20px;
  }

  .overview-layout {
    margin-top: 20px;
  }

  .overview-item-value {
    font-size: 24px;
    text-align: center;
  }

  .overview-item-title {
    margin-top: 10px;
    text-align: center;
  }

  .out-border {
    border: 1px solid #DCDFE6;
  }

  .statistics-layout {
    margin-top: 20px;
    border: 1px solid #DCDFE6;
  }
  .mine-layout {
    position: absolute;
    right: 140px;
    top: 107px;
    width: 250px;
    height: 235px;
  }
  .address-content{
    padding: 20px;
    font-size: 18px
  }
 .font-tag {
    color: #f56c6c;
    font-size: 18px;
}
  .color-danger {
    color: #f56c6c;
}

.color-success {
    color: #67c23a;
}
</style>
