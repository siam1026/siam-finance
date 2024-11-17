<template>
	<section>
		<!--工具条-->
		<el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
			<el-form :inline="true" :model="searchMsg">
				<el-form-item label="" prop="fundName">
					<el-input v-model="searchMsg.fundName" clearable placeholder="请输入名称"></el-input>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click="getList">查询</el-button>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click="handleEdit">新增</el-button>
				</el-form-item>
			</el-form>
		</el-col>

		<!--列表-->
		<el-table :data="relatedList" highlight-current-row v-loading="relatedListLoading" style="width: 100%;" :cell-style="cellStyle" :header-cell-style="getRowClass">
			<el-table-column prop="id" label="编号" width="50"></el-table-column>
			<el-table-column prop="name" label="名称"></el-table-column>
			<el-table-column prop="type" label="操作类型" width="100" :formatter="formatType"></el-table-column>
			<el-table-column prop="amount" label="金额">
				<template scope="scope">
					<span v-if="scope.row.type==0 || scope.row.type==2 || scope.row.type==4 || scope.row.type==7" style="color: red; font-weight: bold">+{{scope.row.amount | formatMoney}}</span>
                    <span v-else-if="scope.row.type==1 || scope.row.type==3 || scope.row.type==5 || scope.row.type==6" style="color: green; font-weight: bold">-{{scope.row.amount | formatMoney}}</span>
               </template>				
			</el-table-column>
			<el-table-column prop="remarks" label="备注"></el-table-column>
			<el-table-column prop="status" label="状态" :formatter="formatStatus" sortable></el-table-column>
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
			<el-table-column prop="name" label="名称"></el-table-column>
			<el-table-column prop="type" label="操作类型" width="100" :formatter="formatType"></el-table-column>
			<el-table-column prop="amount" label="金额">
				<template scope="scope">
					<span v-if="scope.row.type==0 || scope.row.type==2 || scope.row.type==4 || scope.row.type==7" style="color: red; font-weight: bold">+{{scope.row.amount | formatMoney}}</span>
                    <span v-else-if="scope.row.type==1 || scope.row.type==3 || scope.row.type==5 || scope.row.type==6" style="color: green; font-weight: bold">-{{scope.row.amount | formatMoney}}</span>
               </template>				
			</el-table-column>
			<el-table-column prop="remarks" label="备注"></el-table-column>
			<el-table-column prop="status" label="状态" :formatter="formatStatus" sortable></el-table-column>
			<el-table-column prop="createTime" label="创建时间" :formatter="formatTime" sortable></el-table-column>
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
				fundList: [],
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
					name: [{ required: true, message: '请选择名称', trigger: 'change' }],
					amount: [{ required: true, message: '请输入买入金额', trigger: 'change' }],
					// remarks: [{ required: true, message: '请输入备注', trigger: 'blur' }],
					createTime: [{ required: true, message: '请输入产生时间', trigger: 'blur' }]
				},
				//编辑界面数据
				editForm: {
					name: '',
					amount: '',
					remarks: '',
					createTime: ''
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
			formatTime(row, column) {
				let date = new Date(row[column.property]);
				let str = this.$utils.formatDate(date, 'yyyy-MM-dd hh:mm:ss');
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
			gotoOtherPage(type, row) {
				if(type === 'detail') {
					this.$router.push({path:'editGoods', query:{id: row.id}})
				}
			},			
			handleCurrentChange(val) {
				this.searchMsg.pageNo = val;
				this.getList(this.$route.query.id);
			},
			getRelatedList(id) { // 获取商品列表
				let vue = this
				let param = JSON.parse(JSON.stringify(vue.searchMsg));
				param.id = id;

				//去掉id，上一步查询关联列表会影响这里				
				delete param.relatedRecordId;

				vue.relatedListLoading = true;
				vue.$http.post(vue, '/rest/member/capitalRecord/list', param,
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
			getList(id) { // 获取商品列表
				let vue = this
				let param = JSON.parse(JSON.stringify(vue.searchMsg));
				param.relatedRecordId = id;

				//去掉id，上一步查询关联列表会影响这里
				delete param.id

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
						name: row.name,						
						amount: row.amount,
						remarks: row.remarks,			
						createTime: new Date(row.createTime).getTime()						
					}
				}else{
					this.editForm = {
						name: '',
						amount: '',
						remarks: '',
						createTime: ''
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
							url = '/rest/member/capitalRecord/update';						
						}else{
							url = '/rest/member/capitalRecord/insert';	
							param.relatedRecordId = this.$route.query.id;		
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
			// this.getTypeList()
			let id = this.$route.query.id
			this.getRelatedList(id)			
			this.getList(id)
		}
	}

</script>

<style scoped>

</style>