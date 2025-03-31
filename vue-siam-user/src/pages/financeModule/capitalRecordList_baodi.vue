<template>
	<section>
		<div class="todayBox">
			<div class="totdayBlock blue">
				<p class="todayBlockTitle">收入</p>
				<p class="todayBlockNum">
					<span>￥{{todayList.incomeAmount | formatMoney}}</span>
				</p>
			</div>
			<div class="totdayBlock green">
				<p class="todayBlockTitle">支出</p>
				<p class="todayBlockNum">
					<span>￥{{todayList.expendAmount | formatMoney}}</span>
				</p>
			</div>	
			<div class="totdayBlock yellow">
				<p class="todayBlockTitle">结余</p>
				<p class="todayBlockNum">
					<span>￥{{todayList.leaveAmount | formatMoney}}</span>
				</p>
			</div>					
		</div>		
		<!--工具条-->
		<el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
			<el-form :inline="true" :model="searchMsg">
				<el-form-item label="" prop="name">
					<el-input v-model="searchMsg.name" clearable placeholder="请输入名称"></el-input>
				</el-form-item>
				<!-- <el-form-item label="资金分类名称" prop="capitalTypeId">
					<el-select v-model="searchMsg.capitalTypeId" clearable class="minInput">
						<el-option v-for="item in capitalTypeList" :key="item.id" :label="item.name" :value="item.id"></el-option>
					</el-select>
				</el-form-item>				 -->
				<el-form-item label="" prop="type">
					<el-select v-model="searchMsg.type" clearable placeholder="请输入操作类型">
						<el-option label="收入" :value="0"></el-option>
						<el-option label="支出" :value="1"></el-option>
						<!-- <el-option label="借入" :value="2"></el-option>
						<el-option label="借出" :value="3"></el-option>
						<el-option label="待转收入" :value="4"></el-option>
						<el-option label="待转支出" :value="5"></el-option> -->
					</el-select>
				</el-form-item>
				<el-form-item label="" prop="transactionCategoryId">
					<el-select
					v-model="searchMsg.transactionCategoryId"
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
				<el-form-item label="">
					<el-date-picker
					v-model="searchMsg.createTime"
					type="daterange"
					align="right"
					unlink-panels
					range-separator="至"
					start-placeholder="开始日期"
					end-placeholder="结束日期"
					:picker-options="pickerOptions">
					</el-date-picker>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click="search">查询</el-button>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click="handleEdit">新增</el-button>
				</el-form-item>
			</el-form>
		</el-col>
		<!--列表-->
		<el-table :data="list" highlight-current-row v-loading="listLoading" style="width: 100%;" :cell-style="cellStyle" :header-cell-style="headerCellStyle">
			<el-table-column type="index" label="序号" width="50">
				<template scope="scope">
					<span>{{(searchMsg.pageNo - 1) * searchMsg.pageSize + scope.$index + 1}}</span>
				</template>		
			</el-table-column>
			<el-table-column prop="name" label="名称"></el-table-column>
			<el-table-column prop="type" label="操作类型" width="100" :formatter="formatType"></el-table-column>
			<el-table-column prop="amount" label="金额" sortable>
				<template scope="scope">
					<span v-if="scope.row.type==0 || scope.row.type==2 || scope.row.type==4" style="color: red; font-weight: bold">+{{scope.row.amount | formatMoney}}</span>
                    <span v-else-if="scope.row.type==1 || scope.row.type==3 || scope.row.type==5" style="color: green; font-weight: bold">-{{scope.row.amount | formatMoney}}</span>
               </template>				
			</el-table-column>
			<el-table-column prop="remarks" label="备注">
				<template slot-scope="scope" v-if="scope.row.remarks != undefined">
					<el-popover
						placement="top-start"
						width="300"
						trigger="hover"
						:disabled="scope.row.remarks.length <= 20"
					>
						<div v-html="scope.row.remarks.replace(/[\n\r]/g, '<br/>')"></div>
						<span slot="reference" v-if="scope.row.remarks.length <= 20">{{scope.row.remarks}}</span>
						<span slot="reference" v-if="scope.row.remarks.length > 20">{{scope.row.remarks.substr(0, 20) + "..."}}</span>
					</el-popover>
				</template>
			</el-table-column>
			<el-table-column prop="status" label="状态" :formatter="formatStatus" sortable></el-table-column>
			<el-table-column prop="transactionCategoryName" label="交易分类" width="100" sortable></el-table-column>
			<el-table-column prop="createTime" label="创建时间" sortable :formatter="formatTime"></el-table-column>
			<!-- <el-table-column prop="updateTime" label="修改时间" :formatter="formatTime"></el-table-column> -->
			<el-table-column label="操作" fixed="right" width="150">
				<template slot-scope="scope">
					<el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
					<span v-if="scope.row.type==2 || scope.row.type==3 || scope.row.type==4 || scope.row.type==5">
						<el-button size="small" @click="gotoOtherPage('view', scope.row)">详情</el-button>
					</span>					
					<!-- <el-button type="danger" size="small" @click="handleDel(scope.row.id)">删除</el-button> -->
				</template>
				<!-- scope.$index, scope.row -->
			</el-table-column>
		</el-table>

		<!--工具条-->
		<el-col :span="24" class="toolbar">
			<!-- <el-button type="danger" @click="batchRemove" :type="this.sels.length===0">批量删除</el-button> -->
			<!-- <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="searchMsg.pageSize" :total="total" style="float:right;">
			</el-pagination> -->
			<el-pagination
				@size-change="handleSizeChange"
				@current-change="handleCurrentChange"
				:page-sizes="[20, 100, 200, 300, 400]"
				:page-size="searchMsg.pageSize"
				layout="total, sizes, prev, pager, next, jumper"
				:total="total"
				style="float:right;">
			</el-pagination>
		</el-col>

		<!--编辑界面-->
		<el-dialog title="编辑" :visible.sync="editFormVisible" @close="closeDialog" :close-on-click-modal="false">
			<el-form :model="editForm" label-width="150px" style="width: 80%;" :rules="editFormRules" ref="editForm">
				<!-- <el-form-item label="资金分类名称" prop="capitalTypeId">
					<el-select v-model="editForm.capitalTypeId" class="minInput">
						<el-option v-for="item in capitalTypeList" :key="item.id" :label="item.name" :value="item.id"></el-option>
					</el-select>
				</el-form-item> -->
				<el-form-item label="操作类型" prop="type">
					<el-select v-model="editForm.type" placeholder="请选择">
						<el-option label="收入" :value="0"></el-option>
						<el-option label="支出" :value="1"></el-option>
						<!-- <el-option label="借入" :value="2"></el-option>
						<el-option label="借出" :value="3"></el-option>
						<el-option label="待转收入" :value="4"></el-option>
						<el-option label="待转支出" :value="5"></el-option>						 -->
					</el-select>
				</el-form-item>
				<el-form-item label="名称" prop="name">
					<el-input v-model="editForm.name"></el-input>
				</el-form-item>				
				<el-form-item label="金额(元)" prop="amount">
					<el-input v-model="editForm.amount"></el-input>
				</el-form-item>
				<el-form-item label="备注" prop="remarks">
					<el-input type="textarea" v-model="editForm.remarks" :rows="5" ></el-input>
				</el-form-item>
				<el-form-item label="产生时间" prop="createTime">
					<el-date-picker
						v-model="editForm.createTime"
						value-format="timestamp"
						format="yyyy/MM/dd hh:mm:ss"
						type="datetime"
						placeholder="选择产生时间">
					</el-date-picker>
				</el-form-item>

				<el-form-item label="交易分类" prop="transactionCategoryId">
					<el-select
						v-model="editForm.transactionCategoryId"
						filterable
						clearable
						remote
						reserve-keyword
						placeholder="请选择"
					>
						<el-option
						v-for="item in transactionCategoryData"
						:key="item.id"
						:label="item.name"
						:value="item.id"
						></el-option>
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
				pickerOptions: {
					shortcuts: [{
						text: '最近一周',
							onClick(picker) {
							const end = new Date();
							let start = new Date();
							start.setTime(end.getTime() - 1000 * 60 * 60 * 24 * 7);
							picker.$emit('pick', [start, end]);
						}
					}, {
						text: '最近两周',
							onClick(picker) {
							const end = new Date();
							let start = new Date();
							start.setTime(end.getTime() - 1000 * 60 * 60 * 24 * 14);
							picker.$emit('pick', [start, end]);
						}
					}, {
						text: '最近一月',
						onClick(picker) {
							const end = new Date();
							let start = new Date();
							start.setTime(end.getTime() - 1000 * 60 * 60 * 24 * 30);
							picker.$emit('pick', [start, end]);
						}
					}, {
						text: '最近三月',
						onClick(picker) {
							const end = new Date();
							let start = new Date();
							start.setTime(end.getTime() - 1000 * 60 * 60 * 24 * 30 * 3);
							picker.$emit('pick', [start, end]);
						}
					}, {
						text: '最近半年',
						onClick(picker) {
							const end = new Date();
							let start = new Date();
							start.setTime(end.getTime() - 1000 * 60 * 60 * 24 * 180);
							picker.$emit('pick', [start, end]);
						}
					}, {
						text: '最近一年',
						onClick(picker) {
							const end = new Date();
							let start = new Date();
							start.setTime(end.getTime() - 1000 * 60 * 60 * 24 * 30 * 12);
							picker.$emit('pick', [start, end]);
						}
					}, {
						text: '最近两年',
						onClick(picker) {
							const end = new Date();
							let start = new Date();
							start.setTime(end.getTime() - 1000 * 60 * 60 * 24 * 30 * 24);
							picker.$emit('pick', [start, end]);
						}
					}, {
						text: '最近三年',
						onClick(picker) {
							const end = new Date();
							let start = new Date();
							start.setTime(end.getTime() - 1000 * 60 * 60 * 24 * 30 * 36);
							picker.$emit('pick', [start, end]);
						}
					}]
				},
				capitalTypeList: [],
				searchMsg: {
					pageNo: 1,
					pageSize: 20
				},
				
				list: [],
				total: 0,
				listLoading: false,
				sels: [],//列表选中列

				todayList:{}, //今日数据

				//本页面对应的资金分类id
				capitalTypeId: '',

	  			transactionCategoryData: [],
				transactionCategoryOptions: [], //分类候选框
				
				editFormVisible: false,//编辑界面是否显示
				editLoading: false,
				editFormRules: {
					// capitalTypeId: [{ required: true, message: '请选择资金分类名称', trigger: 'change' }],
					type: [{ required: true, message: '请选择操作类型', trigger: 'change' }],
					name: [{ required: true, message: '请选择名称', trigger: 'change' }],
					amount: [{ required: true, message: '请输入买入金额', trigger: 'change' }],
					// remarks: [{ required: true, message: '请输入备注', trigger: 'blur' }],
					transactionCategoryId: [{ required: true, message: '请选择交易分类', trigger: 'change' }],
					createTime: [{ required: true, message: '请输入产生时间', trigger: 'blur' }]
				},
				//编辑界面数据
				editForm: {
					// capitalTypeId: '',
					type: '',
					name: '',
					amount: '',
					remarks: '',
					createTime: '',
					transactionCategoryId: ''
				}
			}
		},
		methods: {
			cellStyle({row, column, rowIndex, columnIndex}){
				if(columnIndex == 4){
					return "text-align:left";
				}
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
				// 处理数据库中时间为NULL场景
				if(str == "1970-01-01 08:00:00"){
					str = "-";
				}
        		return str;
			},
			formatType (row, column) { // 0=正常/启用  1=禁用
				let str = '';
				switch(row.type){
					case 0:
						str = '收入';
						break;
					case 1:
						str = '支出';
						break;
					case 2:
						str = '借入';
						break;
					case 3:
						str = '借出';
						break;
					case 4:
						str = '待转收入';
						break;
					case 5:
						str = '待转支出';
						break;		
					case 6:
						str = '借入还款';
						break;	
					case 7:
						str = '借出归还';
						break;																																					
				}
				return str;
			},		
			formatStatus (row, column) { // 0=正常/启用  1=禁用
				let str = '';
				switch(row.status){
					case 0:
						str = '-';
						break;
					case 1:
						str = '部分还款';
						break;
					case 2:
						str = '部分归还';
						break;
					case 3:
						str = '部分收入';
						break;
					case 4:
						str = '部分支出';
						break;
					case 5:
						str = '已还款';
						break;			
					case 6:
						str = '已归还';
						break;	
					case 7:
						str = '已收入';
						break;		
					case 8:
						str = '已支出';
						break;																																													
				}
				return str;
			},					
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
			search() {
				this.getList();
				this.statistics();
			},			
			handleSizeChange(val) {
				this.searchMsg.pageSize = val;
				this.getList();
			},
			handleCurrentChange(val) {
				this.searchMsg.pageNo = val;
				this.getList();
			},
			getList() { // 获取商品列表
				let vue = this
				let param = JSON.parse(JSON.stringify(vue.searchMsg));

				//处理开始日期、结束日期
				if(vue.searchMsg.createTime){
					let startDate = vue.searchMsg.createTime[0];
					let endDate = vue.searchMsg.createTime[1];
					param.startDate = this.$utils.formatDate(new Date(startDate), 'yyyy/MM/dd');
					param.endDate = this.$utils.formatDate(new Date(endDate), 'yyyy/MM/dd');
					delete param.createTime;
				}

				param.relatedRecordId = 0;
				param.capitalTypeId = vue.capitalTypeId;
				vue.listLoading = true;
				vue.$http.post(vue, '/rest/member/capitalRecord/list', param,
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
			statistics() { // 统计资金收入与支出金额
				let vue = this
				let param = JSON.parse(JSON.stringify(vue.searchMsg));

				//处理开始日期、结束日期
				if(vue.searchMsg.createTime){
					let startDate = vue.searchMsg.createTime[0];
					let endDate = vue.searchMsg.createTime[1];
					param.startDate = this.$utils.formatDate(new Date(startDate), 'yyyy/MM/dd');
					param.endDate = this.$utils.formatDate(new Date(endDate), 'yyyy/MM/dd');
					delete param.createTime;
				}

				param.relatedRecordId = 0;
				param.capitalTypeId = vue.capitalTypeId;
				vue.listLoading = true;
				vue.$http.post(vue, '/rest/member/capitalRecord/statistics', param,
					(vue, data) => {
						vue.todayList.incomeAmount = data.data.incomeAmount;
						vue.todayList.expendAmount = data.data.expendAmount;
						vue.todayList.leaveAmount = data.data.leaveAmount;
						this.$forceUpdate();
					},(error, data)=> {
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
					pageSize: 10000,
					name: '保底资金'
				}
				vue.$http.post(vue, '/rest/member/capitalType/list', param,
					(vue, data) => {
						//vue.capitalTypeList = data.data.records
						vue.capitalTypeId = data.data.records[0].id;
						this.getList();
						this.statistics();
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
						vue.transactionCategoryData = data.data.records;
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
			handleDel (id) { // 删除
				this.$confirm('确认删除该记录吗?', '提示', {
					type: 'warning'
				}).then(() => {
					// this.listLoading = true;
				let vue = this;
				
				vue.$http.delete(vue, '/rest/member/capitalRecord/delete', {"ids" : id},
					function(vue, data) {
						vue.$message({
							showClose: true,
							message: data.message,
							type: 'success'
						});
						vue.getList();
						vue.statistics();
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
						// capitalTypeId: row.capitalTypeId,
						type: row.type,
						name: row.name,
						amount: row.amount,
						remarks: row.remarks,			
						createTime: new Date(row.createTime).getTime(),
						transactionCategoryId: row.transactionCategoryId,
					}
				}else{
					this.editForm = {
						// capitalTypeId: '',
						type: '',		
						name: '',			
						amount: '',
						remarks: '',
						createTime: '',
						transactionCategoryId: ''
					}
        		}
			},
			editSubmit: function () { // 编辑
				this.$refs.editForm.validate((valid) => {
					if (valid) {
						this.editLoading = true;
						let vue = this
						let param = Object.assign({}, this.editForm);
						param.capitalTypeId = vue.capitalTypeId;

						//处理下时间格式(本来传递一个时间戳就可以了的，但是后台配置有问题，所以先用字符串传参)
						let createTime = new Date(param.createTime);
						let str = this.$utils.formatDate(createTime, 'yyyy/MM/dd hh:mm:ss');
						param.createTime = str;

						let url = '';
						if(param.id){
							url = '/rest/member/capitalRecord/update';						
						}else{
							url = '/rest/member/capitalRecord/insert';	
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
								vue.statistics();
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
			this.getTypeList()
			// 获取交易分类
    		this.transactionCategoryList()
		}
	}

</script>

<style scoped>

</style>