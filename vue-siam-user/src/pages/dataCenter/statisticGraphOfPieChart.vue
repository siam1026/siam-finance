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
          <el-option label="展示所有交易类别" :value="-1"></el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="" prop="">
        <el-select v-model="sortType" clearable placeholder="请选择排序方式" @change="handleCapitalTypeIdChange">
          <el-option label="金额从高到低" :value="0"></el-option>
          <el-option label="金额从低到高" :value="1"></el-option>
        </el-select>
      </el-form-item>

      <!-- <el-form-item label="" prop="">
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
      </el-form-item> -->

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
              <div style="color: #909399;font-size: 14px">总收入（元）</div>
              <div style="color: red;font-size: 24px;padding: 10px 0">+{{todayList.totalOrderAmountOfIncome | formatMoney}}</div>
            </div>
          </div>
        </el-col>
        <el-col :span="20">
          <div style="padding: 10px;border-left:1px solid #DCDFE6">
            <div>
              <ve-pie
                :data="chartDataOfIncome"
                :legend-visible="true"
                :loading="loadingOfIncome"
                :data-empty="dataEmptyOfIncome"
                :extend="chartExtend"
                :settings="chartSettings"></ve-pie>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <div class="statistics-layout">
      <div class="layout-title-income">资金统计-支出</div>
      <el-row>
        <el-col :span="4">
          <div style="padding: 20px">
            <div>
              <div style="color: #909399;font-size: 14px">总支出（元）</div>
              <div style="color: green;font-size: 24px;padding: 10px 0">-{{todayList.totalOrderAmountOfExpend | formatMoney}}</div>
            </div>
          </div>
        </el-col>
        <el-col :span="20">
          <div style="padding: 10px;border-left:1px solid #DCDFE6">
            <div>
              <ve-pie
                :data="chartDataOfExpend"
                :legend-visible="true"
                :loading="loadingOfExpend"
                :data-empty="dataEmptyOfExpend"
                :extend="chartExtend"
                :settings="chartSettings"></ve-pie>
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
        yeartsStart: '',
        yeartsEnd: '',
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
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '本月',
            onClick(picker) {
              let start = new Date();
              start.setDate(1);
              const end = new Date();
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
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '下半年',
            onClick(picker) {
              let start = new Date();
              start.setMonth(6);
              start.setDate(1);
              const end = new Date();
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '本年度',
            onClick(picker) {
              let start = new Date();
              start.setMonth(0);
              start.setDate(1);
              const end = new Date();
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
              picker.$emit('pick', [start, end]);
            }
          }]
        },
        //收入区域
        capitalTypeId: '',
        sortType: 0,
        showType: '',
        transactionCategoryId: '',
        dateType:2,
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
          // axisSite: { right: ['orderCount']},
          labelMap: {'type': '类别', 'amount': '金额(元)'},   
        },
        chartExtend: {
          // x轴的文字倾斜(范围：-90~90)
          "xAxis.0.axisLabel.rotate": 45,
          yAxis: {
            //是否显示y轴线条
            axisLine: {
              show: true,
            },
            // 纵坐标网格线设置，同理横坐标
            splitLine: {
              show: false,
            },
            // 线条位置
            position: "left",
          },
          xAxis: {
            axisLine: {
              show: true,
            },
            // 纵坐标网格线设置，同理横坐标
            splitLine: {
              show: false,
            },            
          },
          series: {
            label: { show: false, position: "top" },
            // barMinWidth: 10, //最小宽度
            // barmaxWidth: 60, //最大宽度
            // barWidth : 60, //自适应宽度(只有这个参数有用，最小宽度、最大宽度都没用，但是这个参数不能设置百分比，编译会报错)
          },
          //数据过多时出现横向滚动条
          // dataZoom: [
          //   {
          //     show: true,
          //     realtime: true,
          //     start: 0,
          //     end: 50,
          //   },
          //   {
          //     type: "inside",
          //     realtime: true,
          //     start: 0,
          //     end: 50,
          //   },
          // ],
        },
        chartDataOfIncome: {
          columns: ['type', 'amount'],
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

        todayList:{
          totalOrderAmountOfIncome:0,
          totalOrderAmountOfExpend:0,
        }, //今日数据
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

      //查询资金统计数据-收入
      this.getData(0);
      this.getData(1);
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
		this.getData(0);
		this.getData(1);
      },
      initOrderCountDate(){
        //按日选择：默认展示本月的交易数据
        let start = new Date();
        start.setDate(1);
        const end = new Date();
        this.orderCountDate = [start, end];

        //按月选择：默认展示本年的交易数据
        let start2 = new Date();
        start2.setMonth(0);
        const end2 = new Date();
        this.orderCountMonth = [start2, end2];

        //按年选择：默认展示本年的交易数据
        this.yeartsStart = new Date().getFullYear().toString();
        this.yeartsEnd = new Date().getFullYear().toString();
      },
      getData(type){
		  if(type == 0){
			//收入
			setTimeout(() => {
				this.chartDataOfIncome = {
				columns: ['type', 'amount'],
				rows: []
				};

				let vue = this
				var params = {};

				if(this.dateType == 0){
					//日
					let start=this.orderCountDate[0];
					let end=this.orderCountDate[1];
					params.startDate = this.$utils.formatDate(start, 'yyyy/MM/dd');
					params.endDate = this.$utils.formatDate(end, 'yyyy/MM/dd');
				}else if(this.dateType == 1){
					//月
					let start=this.orderCountMonth[0];
					let end=this.orderCountMonth[1];
					// 获取选定月最后一日
					end.setMonth(end.getMonth() + 1);
					end.setDate(0);
					params.startDate = this.$utils.formatDate(start, 'yyyy/MM/dd');
					params.endDate = this.$utils.formatDate(end, 'yyyy/MM/dd');
				}else if(this.dateType == 2){
					//年
					let start = this.yeartsStart + "/01/01";
					let end = this.yeartsEnd + "/12/31";
					params.startDate = start;
					params.endDate = end;
				}

				//统计接口
				params.capitalTypeId = this.capitalTypeId;
				params.type = type;
				if(this.sortType == 0){
					params.sortKey = "amount";
					params.sortType = "desc";
				}else if(this.sortType == 1){
					params.sortKey = "amount";
					params.sortType = "asc";
				}

				vue.$http.post(vue, '/rest/member/capitalRecord/statisticsOfPieChart', params,
				(vue, data) => {
					//加载折线图数据
					this.chartDataOfIncome.rows = data.data.list;
					//渲染交易总数、交易总额
					this.todayList.totalOrderAmountOfIncome = data.data.totalAmount;
					// this.todayList.totalOrderAmountOfExpend = data.data.totalExpend;

					this.dataEmptyOfIncome = false;
					this.loadingOfIncome = false
					
				},(error, data)=> {
					vue.$message({
					showClose: true,
					message: data.message,
					type: 'error'
					});
				}
				)
			}, 1000)
		  }else if(type == 1){
			//支出
			setTimeout(() => {
				this.chartDataOfExpend = {
				columns: ['type', 'amount'],
				rows: []
				};

				let vue = this
				var params = {};

				if(this.dateType == 0){
					//日
					let start=this.orderCountDate[0];
					let end=this.orderCountDate[1];
					params.startDate = this.$utils.formatDate(start, 'yyyy/MM/dd');
					params.endDate = this.$utils.formatDate(end, 'yyyy/MM/dd');
				}else if(this.dateType == 1){
					//月
					let start=this.orderCountMonth[0];
					let end=this.orderCountMonth[1];
					// 获取选定月最后一日
					end.setMonth(end.getMonth() + 1);
					end.setDate(0);
					params.startDate = this.$utils.formatDate(start, 'yyyy/MM/dd');
					params.endDate = this.$utils.formatDate(end, 'yyyy/MM/dd');
				}else if(this.dateType == 2){
					//年
					let start = this.yeartsStart + "/01/01";
					let end = this.yeartsEnd + "/12/31";
					params.startDate = start;
					params.endDate = end;
				}

				//统计接口
				params.capitalTypeId = this.capitalTypeId;
				params.type = type;
				if(this.sortType == 0){
					params.sortKey = "amount";
					params.sortType = "desc";
				}else if(this.sortType == 1){
					params.sortKey = "amount";
					params.sortType = "asc";
				}

				vue.$http.post(vue, '/rest/member/capitalRecord/statisticsOfPieChart', params,
				(vue, data) => {
					//加载折线图数据
					this.chartDataOfExpend.rows = data.data.list;
					//渲染交易总数、交易总额
					this.todayList.totalOrderAmountOfExpend = data.data.totalAmount;

					this.dataEmptyOfExpend = false;
					this.loadingOfExpend = false
					
				},(error, data)=> {
					vue.$message({
					showClose: true,
					message: data.message,
					type: 'error'
					});
				}
				)
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
