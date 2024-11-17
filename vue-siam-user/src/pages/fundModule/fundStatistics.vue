<template>
	<section>
		<!--工具条-->
		<el-col :span="24" class="toolbar" style="padding-bottom: 0px;">

      <!-- <div style="font-size:18px; font-weight:bold; color:darkslategrey; margin-bottom:20px;">
        计算规则：<br/>
        1、可用金额 = 除反哺资金外，其它所有分类相加<br/>
        2、操作类型只计算了收入、支出两种分类，没有包含(借入 借出 借入还款 借出归还)<br/>
      </div> -->

			<!-- <el-form :inline="true" :model="searchMsg">
				<el-form-item label="" prop="type">
					<el-date-picker
						v-model="searchMsg.startDate_tmp"
						value-format="timestamp"
						format="yyyy/MM/dd"
						type="datetime"
						placeholder="选择开始时间">
					</el-date-picker>
					<el-date-picker
						v-model="searchMsg.endDate_tmp"
						value-format="timestamp"
						format="yyyy/MM/dd"
						type="datetime"
						placeholder="选择结束时间">
					</el-date-picker>          
				</el-form-item>    
				<el-form-item>
					<el-button type="primary" @click="getList">查询</el-button>
				</el-form-item>
			</el-form> -->
		</el-col>
    <!-- 图表 -->
    <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
      <div class="hello">
        <div class="mychart" id="chart1"></div>
        <div class="mychart" id="chart2"></div>
        <!-- <div class="mychart" id="chart3"></div> -->
      </div>      
    </el-col>
	</section>
</template>

<script>
	export default {
		data() {
			return {
				capitalTypeList: [],
				searchMsg: {
					pageNo: 1,
					pageSize: 20
				},
				
				list: [],
				total: 0,
				listLoading: false,
				sels: [],//列表选中列

			}
		},
		methods: {
			changeDecimal (val, scale) { // 小数点后保留2位小数，不足自动补0
				let vue = this;
				val = vue.$utils.changeDecimal(val, scale);
				return val;
			},	
			gotoOtherPage(type, row) {
				if(type === 'view') {
					this.$router.push({path:'viewCapitalRecord', query:{id: row.id}})
				}
			},
			handleCurrentChange(val) {
				this.searchMsg.pageNo = val;
				this.getList();
			},
			getList() { // 获取商品列表
				let vue = this
				let param = JSON.parse(JSON.stringify(vue.searchMsg));

				if(param.startDate_tmp == undefined){
					param.startDate = "1970/01/01";
				}else{
					param.startDate = this.$utils.formatDate(new Date(param.startDate_tmp), 'yyyy/MM/dd');
				}
				if(param.endDate_tmp == undefined){
					var curDate = new Date();
					// var preDate = new Date(curDate.getTime() - 24*60*60*1000); //前一天
					var nextDate = new Date(curDate.getTime() + 24*60*60*1000); //后一天          
					param.endDate = this.$utils.formatDate(nextDate, 'yyyy/MM/dd');
				}else{
					param.endDate = this.$utils.formatDate(new Date(param.endDate_tmp), 'yyyy/MM/dd');
				}

				// alert(param.startDate + " " + param.endDate)
			
				vue.listLoading = true;
				vue.$http.post(vue, '/rest/member/fund/statistics', param,
					(vue, data) => {
					vue.listLoading = false;
					var result = data.data;

					//现存资金饼图
					var shortTermList = new Array();
					for(var i = 0; i < result.shortTermList.length; i++){
						var map = {};
						map.name = result.shortTermList[i].fundName + "(" + result.shortTermList[i].ratio + "%)";
						map.value = result.shortTermList[i].amount;
						shortTermList[i] = map;
					}
					var availableHalfTotalAmount = (result.shortTermAmount / 2) - result.usedShortTermAmount;
					var statisticsData = {
						text : "【短线仓】\n\n总资产(元)：" + this.$options.filters["formatMoney"](result.shortTermAmount) + 
							"\n\n总持仓：" + result.usedShortTermRatio + "%" + "(" + this.$options.filters["formatMoney"](result.usedShortTermAmount) + ")" + 
							"\n\n剩余仓位：" + result.availableShortTermRatio + "%" + "(" + this.$options.filters["formatMoney"](result.availableShortTermAmount) + ")" + 
							"\n\n可用金额：" + this.$options.filters["formatMoney"](availableHalfTotalAmount),
						// subtext : param.startDate + " ~ " + param.endDate,
						data : shortTermList
					}
					this.$chart.pie2('chart1', statisticsData);

					//收入情况饼图
					var longTermList = new Array();
					for(var i = 0; i < result.longTermList.length; i++){
						var map = {};
						map.name = result.longTermList[i].fundName + "(" + result.longTermList[i].ratio + "%)";
						map.value = result.longTermList[i].amount;
						longTermList[i] = map;
					}
					availableHalfTotalAmount = (result.longTermAmount / 2) - result.usedLongTermAmount;
					var statisticsData = {
						text : "【中长线仓】\n\n总资产(元)：" + this.$options.filters["formatMoney"](result.longTermAmount) + 
							"\n\n总持仓：" + result.usedLongTermRatio + "%" + "(" + this.$options.filters["formatMoney"](result.usedLongTermAmount) + ")" + 
							"\n\n剩余仓位：" + result.availableLongTermRatio + "%" + "(" + this.$options.filters["formatMoney"](result.availableLongTermAmount) + ")" + 
							"\n\n可用金额：" + this.$options.filters["formatMoney"](availableHalfTotalAmount),
						// subtext : param.startDate + " ~ " + param.endDate,
						data : longTermList
					}
					this.$chart.pie2('chart2', statisticsData);

					},(error, data)=> {
						alert(data);
						vue.listLoading = false;
						vue.$message({
							showClose: true,
							message: data.message,
							type: 'error'
						});
					}
				)
			},
		},
		mounted() {
      this.getList();
		}
	}

</script>

<style scoped>
  /* #chart1 {
    width: 1080px;
    height: 456px;
  }   */
  #chart1, #chart2, #chart3 {
    /* width: 360px;
    height: 152px; */
    /* width: 1080px;
    height: 456px; */
	width: 100%;
    height: 680px;
  }
  .mychart {
    margin-bottom: 20px;
  }
</style>