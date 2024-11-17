<template>
	<section>
		<!--工具条-->
		<el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
			<el-form :inline="true" :model="searchMsg">
				<el-form-item>
					<el-button type="primary" @click="handleEdit">新增</el-button>
				</el-form-item>
				<el-form-item>
					<el-button @click="backHistory">返回</el-button>
				</el-form-item>						
			</el-form>
		</el-col>

		<!--列表-->
		<el-table :data="relatedList" highlight-current-row v-loading="relatedListLoading" style="width: 100%;" :cell-style="cellStyle" :header-cell-style="getRowClass">
			<el-table-column prop="id" label="编号" width="50"></el-table-column>
			<el-table-column prop="convertibleBondName" label="可转债名称"></el-table-column>
			<el-table-column prop="type" label="操作类型">
				<template scope="scope">
					<span v-if="scope.row.type == 0">多可转债混合定投</span>
                    <span v-if="scope.row.type == 1">单可转债定投</span>
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
					<span v-if="scope.row.status == 2" style="color: red; font-weight: bold">已结束待买入</span>
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
		</el-table>

		<br/><br/>

		<!--列表-->
		<el-table :data="list" highlight-current-row v-loading="listLoading" style="width: 100%;" :cell-style="cellStyle" :header-cell-style="headerCellStyle">
			<el-table-column type="index" label="序号" width="50">
				<template scope="scope">
					<span>{{(searchMsg.pageNo - 1) * searchMsg.pageSize + scope.$index + 1}}</span>
				</template>		
			</el-table-column>
			<el-table-column prop="convertibleBondName" label="可转债名称"></el-table-column>
			<!-- <el-table-column prop="type" label="操作类型" width="100" :formatter="formatType"></el-table-column> -->
			<el-table-column prop="amount" label="买入金额(元)" sortable>
				<template scope="scope">
					<span>{{scope.row.amount | formatMoney}}</span>
               </template>						
			</el-table-column>
			<el-table-column prop="quantity" label="买入数量(份)">
				<template scope="scope">
					<span>{{changeDecimal(scope.row.quantity, 2)}}</span>
               </template>					
			</el-table-column>
			<el-table-column prop="price" label="买入价格">
				<template scope="scope">
					<span>{{changeDecimal(scope.row.price, 4)}}</span>
               </template>					
			</el-table-column>
			<!-- <el-table-column prop="income" label="收益(已扣除手续费)" sortable>
				<template scope="scope">
					<span v-if="scope.row.returnRate >= 0" style="color: red; font-weight: bold">+{{changeDecimal(scope.row.income, 2)}}</span>
                    <span v-else style="color: green; font-weight: bold">{{changeDecimal(scope.row.income, 2)}}</span>
               </template>				
			</el-table-column> -->
			<el-table-column prop="serviceCharge" label="买入手续费">
				<template scope="scope">
					<span>{{changeDecimal(scope.row.serviceCharge, 2)}}</span>
               </template>					
			</el-table-column>
			<!-- <el-table-column prop="returnRate" label="收益率" sortable>
				<template scope="scope">
					<span v-if="scope.row.returnRate >= 0" style="color: red; font-weight: bold">+{{changeDecimal(scope.row.returnRate, 2)}}%</span>
                    <span v-else style="color: green; font-weight: bold">{{changeDecimal(scope.row.returnRate, 2)}}%</span>
               </template>
			</el-table-column> -->
			<el-table-column prop="status" label="状态" sortable>
				<template scope="scope">
					<span v-if="scope.row.status == 0" style="color: red; font-weight: bold">未开始</span>
					<span v-if="scope.row.status == 1" style="color: red; font-weight: bold">进行中</span>
					<span v-if="scope.row.status == 2" style="color: red; font-weight: bold">已完成</span>
               </template>				
			</el-table-column>
			<el-table-column prop="sellServiceCharge" label="今日卖出手续费">
				<template scope="scope">
					<span v-if="scope.row.status == 2"> - </span>
					<span v-else >{{changeDecimal(scope.row.sellServiceCharge, 2)}}</span>
               </template>					
			</el-table-column>			
			<el-table-column prop="sellIncome" label="今日卖出可获收益(已扣除手续费)" sortable>
				<template scope="scope">
					<span v-if="scope.row.status == 2"> - </span>
					<span v-else-if="scope.row.sellIncome >= 0" style="color: red; font-weight: bold">+{{changeDecimal(scope.row.sellIncome, 2)}}</span>
                    <span v-else style="color: green; font-weight: bold">{{changeDecimal(scope.row.sellIncome, 2)}}</span>
               </template>				
			</el-table-column>	
			<el-table-column prop="sellReturnRate" label="今日卖出收益率" sortable>
				<template scope="scope">
					<span v-if="scope.row.status == 2"> - </span>
					<span v-else-if="scope.row.sellReturnRate >= 0" style="color: red; font-weight: bold">+{{changeDecimal(scope.row.sellReturnRate, 2)}}%</span>
                    <span v-else style="color: green; font-weight: bold">{{changeDecimal(scope.row.sellReturnRate, 2)}}%</span>
               </template>
			</el-table-column>						
			<el-table-column prop="remarks" label="备注"></el-table-column>						
			<el-table-column prop="createTime" label="买入时间" :formatter="formatTime" sortable></el-table-column>
			<!-- <el-table-column prop="updateTime" label="修改时间" :formatter="formatTime"></el-table-column> -->
			<el-table-column label="操作" fixed="right" width="150">
				<template slot-scope="scope">
					<el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
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
				<el-form-item label="可转债名称" prop="convertibleBondId">
					<el-select v-model="editForm.convertibleBondId" class="minInput" filterable>
						<el-option v-for="item in convertibleBondList" :key="item.id" :label="item.name" :value="item.id"></el-option>
					</el-select>
				</el-form-item>

				<!-- <el-form-item label="操作类型" prop="type">
					<el-select v-model="editForm.type" placeholder="请选择">
						<el-option label="买入" :value="0"></el-option>
						<el-option label="买入" :value="1"></el-option>
					</el-select>
				</el-form-item> -->
				<el-form-item label="买入金额(元)" prop="amount">
					<el-input v-model="editForm.amount"></el-input>
				</el-form-item>
				<el-form-item label="买入数量(份)" prop="quantity">
					<el-input v-model="editForm.quantity"></el-input>
				</el-form-item>
				<el-form-item label="买入价格" prop="price">
					<el-input v-model="editForm.price"></el-input>
				</el-form-item>
				<el-form-item label="买入手续费" prop="serviceCharge">
					<el-input v-model="editForm.serviceCharge"></el-input>
				</el-form-item>		
				<el-form-item label="买入时间" prop="createTime">
					<el-date-picker
						v-model="editForm.createTime"
						value-format="timestamp"
						format="yyyy/MM/dd hh:mm:ss"
						type="datetime"
						placeholder="选择买入时间">
					</el-date-picker>
				</el-form-item>						
				<el-form-item label="备注" prop="remarks">
					<el-input type="textarea" v-model="editForm.remarks" :rows="5" ></el-input>
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
				convertibleBondList: [],
				searchMsg: {
					pageNo: 1,
					pageSize: 20
				},

				relatedList: [],
				relatedListLoading: false,				
				
				list: [],
				total: 0,
				listLoading: false,
				sels: [],//列表选中列

				editFormVisible: false,//编辑界面是否显示
				editLoading: false,
				editFormRules: {
					// convertibleBondId: [{ required: true, message: '请选择可转债名称', trigger: 'change' }],
					amount: [{ required: true, message: '请输入买入金额', trigger: 'change' }],
					quantity: [{ required: true, message: '请输入买入数量', trigger: 'blur' }],
					price: [{ required: true, message: '请输入买入价格', trigger: 'blur' }],
					createTime: [{ required: true, message: '请输入买入时间', trigger: 'blur' }]
				},
				//编辑界面数据
				editForm: {
					// convertibleBondId: '',
					amount: '',
					quantity: '',
					price: '',
					serviceCharge: '',
					createTime: '',
					remarks: ''
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
			getRowClass({ row, column, rowIndex, columnIndex }) { //设置表格第一行的颜色
				if (rowIndex == 0) {
					return "text-align:center;background:#F5F7FA";
				} else {
					return "";
				}
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
			changeDecimal (val, scale) { // 小数点后保留2位小数，不足自动补0
				let vue = this;
				val = vue.$utils.changeDecimal(val, scale);
				return val;
			},	
			backHistory() {
				this.$router.go(-1);//返回上一层
			},					
			gotoOtherPage(type, row) {
				if(type === 'detail') {
					this.$router.push({path:'editGoods', query:{id: row.id}})
				}
			},			
			handleCurrentChange(val) {
				this.searchMsg.pageNo = val;
				this.getList(this.$route.query.id);
			},
			getRelatedList(id) { // 获取商品可转债列表
				let vue = this
				let param = JSON.parse(JSON.stringify(vue.searchMsg));
				param.id = id;

				//去掉id，上一步查询关联列表会影响这里				
				delete param.planId;
				delete param.type;

				vue.relatedListLoading = true;
				vue.$http.post(vue, '/rest/member/convertibleBondFixedInvestmentPlan/list', param,
					(vue, data) => {
						vue.relatedList = data.data.records
						vue.relatedListLoading = false;
					},(error, data)=> {
						vue.relatedListLoading = false;
						vue.$message({
							showClose: true,
							message: data.message,
							type: 'error'
						});
					}
				)
			},					
			getList(id) { // 获取商品可转债列表
				let vue = this
				let param = JSON.parse(JSON.stringify(vue.searchMsg));
				param.type = 0;
				param.planId = id;

				//去掉id，上一步查询关联列表会影响这里
				delete param.id

				vue.listLoading = true;
				vue.$http.post(vue, '/rest/member/convertibleBondFixedInvestment/list', param,
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
					pageSize: 10000,
					isOptional: 1
				}
				vue.$http.post(vue, '/rest/member/convertibleBond/list', param,
					(vue, data) => {
						vue.convertibleBondList = data.data.records
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
				
				vue.$http.delete(vue, '/rest/member/convertibleBondFixedInvestment/delete', {"ids" : id},
					function(vue, data) {
						vue.$message({
							showClose: true,
							message: data.message,
							type: 'success'
						});
            			vue.getList(this.$route.query.id)
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
						// convertibleBondId: row.convertibleBondId,
						amount: row.amount,
						quantity: row.quantity,
						price: row.price,			
						serviceCharge: row.serviceCharge,			
						createTime: new Date(row.createTime).getTime(),
						remarks: row.remarks,					
					}
				}else{
					this.editForm = {
						// convertibleBondId: '',
						amount: '',
						quantity: '',
						price: '',
						serviceCharge: '',
						createTime: '', 
						remarks: ''
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
						let createTime = new Date(param.createTime);
						let str = this.$utils.formatDate(createTime, 'yyyy/MM/dd hh:mm:ss');
						param.createTime = str;

						let url = '';
						if(param.id){
							url = '/rest/member/convertibleBondFixedInvestment/update';						
						}else{
							url = '/rest/member/convertibleBondFixedInvestment/insert';	
							param.type = 0;
							param.planId = this.$route.query.id;		
						}
						vue.$http.post(vue, url, param,
							(vue, data) => {
								this.editLoading = false;
								vue.$message({
									showClose: true,
									message: data.message,
									type: 'success'
								});
								vue.getRelatedList(this.$route.query.id)
								vue.getList(this.$route.query.id)
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
		created() {
			this.getTypeList()
			let id = this.$route.query.id
			this.getRelatedList(id)			
			this.getList(id)
		}
	}

</script>

<style scoped>

</style>