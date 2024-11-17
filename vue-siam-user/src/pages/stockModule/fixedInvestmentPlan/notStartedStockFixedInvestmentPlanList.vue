<template>
	<section>
		<!--工具条-->
		<el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
			<el-form :inline="true" :model="searchMsg">
				<el-form-item>
					<el-input v-model="searchMsg.stockName" clearable placeholder="请输入股票名称"></el-input>
				</el-form-item>
				<el-form-item>
					<el-select v-model="searchMsg.type" placeholder="请选择操作类型搜索" clearable>
						<el-option label="多股票混合定投" :value="0"></el-option>
						<el-option label="单股票定投" :value="1"></el-option>
					</el-select>
				</el-form-item>             
				<el-form-item>
					<el-button type="primary" @click="getList">查询</el-button>
				</el-form-item>
				<!-- <el-form-item>
					<el-button type="primary" @click="handleEdit">新增</el-button>
				</el-form-item> -->
			</el-form>
		</el-col>

		<!--列表-->
		<el-table :data="list" highlight-current-row v-loading="listLoading" style="width: 100%;" :cell-style="cellStyle" :header-cell-style="headerCellStyle">
			<el-table-column type="index" label="序号" width="50">
				<template scope="scope">
					<span>{{(searchMsg.pageNo - 1) * searchMsg.pageSize + scope.$index + 1}}</span>
				</template>		
			</el-table-column>
			<el-table-column prop="stockName" label="股票名称"></el-table-column>
			<el-table-column prop="type" label="操作类型">
				<template scope="scope">
					<span v-if="scope.row.type == 0">多股票混合定投</span>
                    <span v-if="scope.row.type == 1">单股票定投</span>
               </template>				
			</el-table-column>			
			<el-table-column prop="amount" label="定投金额" sortable>
				<template scope="scope">
					<span>{{scope.row.amount | formatMoney}}元</span>			
               </template>						
			</el-table-column>
			<el-table-column prop="cycle" label="定投周期" sortable>
				<template scope="scope">
					<span>{{scope.row.cycle}}个月</span>
               </template>					
			</el-table-column>
			<el-table-column prop="startTime" label="开始时间" :formatter="formatTime" sortable></el-table-column>
			<el-table-column prop="endTime" label="结束时间" :formatter="formatTime" sortable></el-table-column>
			<el-table-column prop="expectedReturnRate" label="期望收益率">
				<template scope="scope">
					<span v-if="scope.row.expectedReturnRate >= 0" style="color: red; font-weight: bold">{{changeDecimal(scope.row.expectedReturnRate, 2)}}%</span>
                    <span v-else style="color: green; font-weight: bold">{{changeDecimal(scope.row.expectedReturnRate, 2)}}%</span>
               </template>
			</el-table-column>
			<el-table-column prop="status" label="状态" sortable>
				<template scope="scope">
					<span v-if="scope.row.status == 0" style="color: red; font-weight: bold">未开始</span>
					<span v-if="scope.row.status == 1" style="color: red; font-weight: bold">进行中</span>
					<span v-if="scope.row.status == 2" style="color: red; font-weight: bold">已清仓</span>
					<span v-if="scope.row.status == 3" style="color: red; font-weight: bold">已完成</span>
               </template>				
			</el-table-column>
			<el-table-column prop="income" label="收益(已扣除手续费)" sortable>
				<template scope="scope">
					<span v-if="scope.row.income == 0.00">-</span>
					<span v-else-if="scope.row.income > 0" style="color: red; font-weight: bold">+{{changeDecimal(scope.row.income, 2)}}</span>
                    <span v-else style="color: green; font-weight: bold">{{changeDecimal(scope.row.income, 2)}}</span>
               </template>				
			</el-table-column>	
			<el-table-column prop="actualReturnRate" label="实际收益率">
				<template scope="scope">
					<span v-if="scope.row.actualReturnRate == 0">-</span>
					<span v-else-if="scope.row.actualReturnRate > 0" style="color: red; font-weight: bold">+{{changeDecimal(scope.row.actualReturnRate, 2)}}%</span>
                    <span v-else style="color: green; font-weight: bold">{{changeDecimal(scope.row.actualReturnRate, 2)}}%</span>
               </template>
			</el-table-column>
			<el-table-column prop="remarks" label="备注"></el-table-column>
			<el-table-column prop="createTime" label="创建时间" :formatter="formatTime" sortable></el-table-column>
			<el-table-column label="操作" fixed="right" width="260">
				<template slot-scope="scope">
					<el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
					<span v-if="scope.row.remainingSharesRate != 1100">
						<el-button size="small" @click="gotoOtherPage('view', scope.row)">买入详情</el-button>
						<el-button size="small" @click="gotoOtherPage('viewSell', scope.row)">卖出详情</el-button>
					</span>
					<!-- <el-button type="danger" size="small" @click="handleDel(scope.row.id)">删除</el-button> -->
				</template>
				<!-- scope.$index, scope.row -->
			</el-table-column>
		</el-table>

		<!--工具条-->
		<el-col :span="24" class="toolbar">
			<!-- <el-button type="danger" @click="batchRemove" :type="this.sels.length===0">批量删除</el-button> -->
			<el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="searchMsg.pageSize" :total="total" style="float:right;">
			</el-pagination>
		</el-col>

		<!--编辑界面-->
		<el-dialog title="编辑" :visible.sync="editFormVisible" @close="closeDialog" :close-on-click-modal="false">
			<el-form :model="editForm" label-width="150px" style="width: 80%;" :rules="editFormRules" ref="editForm">
				<el-form-item label="操作类型" prop="type">
					<el-select v-model="editForm.type" placeholder="请选择">
						<!-- <el-option label="多股票混合定投" :value="0"></el-option> -->
						<el-option label="单股票定投" :value="1"></el-option>
					</el-select>
				</el-form-item>				
				<el-form-item label="股票名称" prop="stockIdComma">
					<el-select v-model="editForm.stockIdComma" class="minInput" filterable>
						<el-option v-for="item in stockList" :key="item.id" :label="item.name" :value="item.id"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="定投金额(元)" prop="amount">
					<el-input v-model="editForm.amount"></el-input>
				</el-form-item>
				<el-form-item label="定投周期(月)" prop="cycle">
					<el-input v-model="editForm.cycle"></el-input>
				</el-form-item>
				<el-form-item label="开始时间" prop="startTime">
					<el-date-picker
						v-model="editForm.startTime"
						value-format="timestamp"
						format="yyyy/MM/dd"
						type="datetime"
						placeholder="选择开始时间">
					</el-date-picker>
				</el-form-item>		
				<el-form-item label="结束时间" prop="endTime">
					<el-date-picker
						v-model="editForm.endTime"
						value-format="timestamp"
						format="yyyy/MM/dd"
						type="datetime"
						placeholder="选择结束时间">
					</el-date-picker>
				</el-form-item>										
				<el-form-item label="期望收益率(%)" prop="expectedReturnRate">
					<el-input v-model="editForm.expectedReturnRate"></el-input>
				</el-form-item>
				<el-form-item label="备注" prop="remarks">
					<el-input type="textarea" v-model="editForm.remarks" :rows="5" ></el-input>
				</el-form-item>
				<el-form-item label="状态" prop="status">
					<el-select v-model="editForm.status" placeholder="请选择">
						<el-option label="未开始" :value="0"></el-option>
						<el-option label="进行中" :value="1"></el-option>
						<el-option label="已清仓" :value="2"></el-option>
						<el-option label="已完成" :value="3"></el-option>
					</el-select>
				</el-form-item>				
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="editFormVisible = false">取消</el-button>
				<el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
			</div>
		</el-dialog>
	</section>
</template>

<script>
	export default {
		data() {
			return {
				stockList: [],
				searchMsg: {
					pageNo: 1,
					pageSize: 20
				},
				
				list: [],
				total: 0,
				listLoading: false,
				sels: [],//列表选中列

				editFormVisible: false,//编辑界面是否显示
				editLoading: false,
				editFormRules: {
					type: [{ required: true, message: '请选择操作类型', trigger: 'change' }],
					stockIdComma: [{ required: true, message: '请选择股票名称', trigger: 'change' }],
					amount: [{ required: true, message: '请输入定投金额', trigger: 'change' }],
					cycle: [{ required: true, message: '请输入定投周期', trigger: 'blur' }],
					startTime: [{ required: true, message: '请输入开始时间', trigger: 'change' }],
					endTime: [{ required: true, message: '请输入结束时间', trigger: 'change' }],					
					expectedReturnRate: [{ required: true, message: '请输入期望收益率', trigger: 'blur' }]
				},
				//编辑界面数据
				editForm: {
					type: '',					
					stockIdComma: '',
					amount: '',
					cycle: '',
					startTime: '',
					endTime: '',										
					expectedReturnRate: ''
				}
			}
		},
		methods: {
			cellStyle({row, column, rowIndex, columnIndex}){
				return "text-align:center";
			},
			headerCellStyle({row, rowIndex}){
				return "text-align:center";
			},			
			closeDialog() {
				this.$refs['editForm'].resetFields();
			},
			formatTime(row, column) {
				let date = new Date(row[column.property]);
				let str = this.$utils.formatDate(date, 'yyyy-MM-dd hh:mm:ss');
				//去除时分秒
				if(column.property=="startTime" || column.property=="endTime"){
					str = str.substring(0, str.length-9);
				}	
				// 处理数据库中时间为NULL场景
				if(str == "1970-01-01 08:00:00"){
					str = "-";
				}
        		return str;
			},
			formatType (row, column) { // 0=正常/启用  1=禁用
				return row.type ? '禁用' : '正常';
			},			
			changeDecimal (val, scale) { // 小数点后保留2位小数，不足自动补0
				let vue = this;
				val = vue.$utils.changeDecimal(val, scale);
				return val;
			},	
			gotoOtherPage(type, row) {
				if(type === 'view') {
					this.$router.push({path:'viewStockFixedInvestmentPlan', query:{id: row.id}})
				}else if(type === 'viewSell') {
					this.$router.push({path:'viewStockFixedInvestmentPlanSell', query:{id: row.id}})
				}
			},
			handleCurrentChange(val) {
				this.searchMsg.pageNo = val;
				this.getList();
			},
			getList() { // 获取商品股票列表
				let vue = this
				let param = JSON.parse(JSON.stringify(vue.searchMsg));
				param.status = 0;
				vue.listLoading = true;
				vue.$http.post(vue, '/rest/member/stockFixedInvestmentPlan/list', param,
					(vue, data) => {
						vue.list = data.data.records
						vue.total = data.data.total
						vue.listLoading = false;
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
			getTypeList() { // 获取商品类别列表
				let vue = this
				let param = {
					pageNo: 1, 
					pageSize: 10000
				}
				vue.$http.post(vue, '/rest/member/stock/list', param,
					(vue, data) => {
						vue.stockList = data.data.records
					},(error, data)=> {
						vue.$message({
							showClose: true,
							message: data.message,
							type: 'error'
						});
					}
				)
			},			
			handleDel (id) { // 删除
				this.$confirm('确认删除该记录吗?', '提示', {
					type: 'warning'
				}).then(() => {
					// this.listLoading = true;
				let vue = this;
				
				vue.$http.delete(vue, '/rest/member/stockFixedInvestmentPlan/delete', {"ids" : id},
					function(vue, data) {
						vue.$message({
							showClose: true,
							message: data.message,
							type: 'success'
						});
            			vue.getList()
					}, function(error, data) {
						vue.$message({
							showClose: true,
							message: data.message,
							type: 'error'
						});
					}
				)
				}).catch(() => {});
			},
			handleEdit: function (index, row) { // 显示编辑界面
				this.editFormVisible = true;
				if(row){
					this.editForm = {
						id: row.id,
						type: row.type,
						stockIdComma: row.stockIdComma,
						amount: row.amount,
						cycle: row.cycle,
						startTime: new Date(row.startTime).getTime(),
						endTime: new Date(row.endTime).getTime(),
						expectedReturnRate: row.expectedReturnRate,
						status: row.status,
						remarks: row.remarks,
					}
				}else{
					this.editForm = {
						type: '',					
						stockIdComma: '',
						amount: '',
						cycle: '',
						startTime: '',
						endTime: '',										
						expectedReturnRate: ''
					}
        		}
			},
			editSubmit: function () { // 编辑
				this.$refs.editForm.validate((valid) => {
					if (valid) {
						this.editLoading = true;
						let vue = this
						let param = Object.assign({}, this.editForm);

						//处理下时间格式(本来传递一个时间戳就可以了的，但是后台配置有问题，所以先用字符串传参)
						let startTime = new Date(param.startTime);
						let startTimeStr = this.$utils.formatDate(startTime, 'yyyy/MM/dd hh:mm:ss');
						param.startTime = startTimeStr;
						let endTime = new Date(param.endTime);
						let endTimeStr = this.$utils.formatDate(endTime, 'yyyy/MM/dd hh:mm:ss');
						param.endTime = endTimeStr;						

						let url = '';
						if(param.id){
							url = '/rest/member/stockFixedInvestmentPlan/update';						
						}else{
							url = '/rest/member/stockFixedInvestmentPlan/insert';						
						}
						vue.$http.post(vue, url, param,
							(vue, data) => {
								this.editLoading = false;
								vue.$message({
									showClose: true,
									message: data.message,
									type: 'success'
								});
								
								vue.getList()
								vue.editFormVisible = false;
							}, (error, data) => {
								this.editLoading = false;
								vue.$message({
									showClose: true,
									message: data.message,
									type: 'error'
								});
							}
						)							
					}
				});
			}
		},
		mounted() {
			this.getList();
		},
		created() {
			this.getTypeList()
		}
	}
</script>

<style scoped>

</style>