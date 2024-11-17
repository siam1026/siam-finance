<template>
	<section>
		<!--工具条-->
		<el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
			<el-form :inline="true" :model="searchMsg">
				<el-form-item label="" prop="name">
					<el-input v-model="searchMsg.name" clearable placeholder="请输入可转债名称"></el-input>
				</el-form-item>
				<el-form-item label="" prop="code">
					<el-input v-model="searchMsg.code" clearable placeholder="请输入可转债代码"></el-input>
				</el-form-item>									
				<!-- <el-form-item label="可转债状态" prop="disabled">
							<el-select v-model="searchMsg.disabled" clearable>
					<el-option label="正常" :value="0"></el-option>
					<el-option label="禁用" :value="1"></el-option>
				</el-select> 
				</el-form-item>-->
				<el-form-item>
					<el-button type="primary" @click="getList">查询</el-button>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click="handleEdit">新增</el-button>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" plain @click="syncPrice">同步最新价格</el-button>
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
			<el-table-column prop="name" label="可转债名称"></el-table-column>
			<!-- <el-table-column prop="disabled" label="状态" width="100" :formatter="formatType"></el-table-column> -->
			<el-table-column prop="code" label="可转债代码"></el-table-column>
			<el-table-column prop="lastestPrice" label="最新价格" :formatter="formatLastestPrice"></el-table-column>
			<el-table-column prop="increaseRt" label="最新涨跌幅">
				<template scope="scope">
					<span v-if="scope.row.increaseRt > 0" style="color: red; font-weight: bold">+{{changeDecimal(scope.row.increaseRt, 2)}}%</span>
                    <span v-else style="color: green; font-weight: bold">{{changeDecimal(scope.row.increaseRt, 2)}}%</span>
               </template>						
			</el-table-column>
			<el-table-column prop="bondPy" label="拼音"></el-table-column>
			<el-table-column prop="stockId" label="正股代码"></el-table-column>
			<el-table-column prop="stockNm" label="正股名称"></el-table-column>
			<el-table-column prop="convertPrice" label="转股价"></el-table-column>
			<el-table-column prop="convertValue" label="转股价值"></el-table-column>
			<el-table-column prop="premiumRt" label="溢价率"></el-table-column>
			<el-table-column prop="ratingCd" label="债券评级"></el-table-column>
			<el-table-column prop="putConvertPrice" label="回售触发价"></el-table-column>
			<el-table-column prop="forceRedeemPrice" label="强赎触发价"></el-table-column>
			<el-table-column prop="convertAmtRatio" label="转债占比(%)"></el-table-column>
			<el-table-column prop="fundRt" label="基金持仓"></el-table-column>
			<el-table-column prop="shortMaturityDt" label="到期时间"></el-table-column>
			<el-table-column prop="yearLeft" label="剩余年限"></el-table-column>
			<el-table-column prop="currIssAmt" label="剩余规模(亿元)"></el-table-column>
			<el-table-column prop="volume" label="成交额(万元)"></el-table-column>
			<el-table-column prop="turnoverRt" label="换手率(%)"></el-table-column>
			<el-table-column prop="ytmRt" label="到期税前收益(%)"></el-table-column>
			<el-table-column prop="convertPriceTips" label="转股提示"></el-table-column>			
			<el-table-column prop="remarks" label="备注"></el-table-column>
			<el-table-column prop="createTime" label="创建时间" :formatter="formatTime" sortable></el-table-column>
			<el-table-column prop="updateTime" label="修改时间" :formatter="formatTime"></el-table-column>
			<el-table-column label="操作" fixed="right" width="150">
				<template slot-scope="scope">
					<el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
					<el-button type="danger" size="small" @click="handleDel(scope.row.id)">删除</el-button>
				</template>
				<!-- scope.$index, scope.row -->
			</el-table-column>
		</el-table>

		<!--工具条-->
		<el-col :span="24" class="toolbar">
			<!-- <el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button> -->
			<el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="searchMsg.pageSize" :total="total" style="float:right;">
			</el-pagination>
		</el-col>

		<!--编辑界面-->
		<el-dialog title="编辑" :visible.sync="editFormVisible" @close="closeDialog" :close-on-click-modal="false">
			<el-form :model="editForm" label-width="150px" style="width: 80%;" :rules="editFormRules" ref="editForm">
				<el-form-item label="可转债名称" prop="name">
					<el-input v-model="editForm.name"></el-input>
				</el-form-item>
				<el-form-item label="可转债代码" prop="code">
					<el-input v-model="editForm.code"></el-input>
				</el-form-item>				
				<el-form-item label="最新价格" prop="lastestPrice">
					<el-input v-model="editForm.lastestPrice"></el-input>
				</el-form-item>		
				<el-form-item label="最新价格对应的日期" prop="lastestPriceDate">
					<el-date-picker
						v-model="editForm.lastestPriceDate"
						value-format="timestamp"
						format="yyyy-MM-dd"
						type="datetime"
						placeholder="选择最新价格对应的日期">
					</el-date-picker>
				</el-form-item>	
				<el-form-item label="备注" prop="remarks">
					<el-input type="textarea" v-model="editForm.remarks" :rows="5" ></el-input>
				</el-form-item>
				<el-form-item label="是否为自选" prop="isOptional">
					<el-select v-model="editForm.isOptional" placeholder="请选择">
						<el-option label="否" :value="false"></el-option>
						<el-option label="是" :value="true"></el-option>
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
					// disabled: [{ required: true, message: '请选择可转债状态', trigger: 'change' }],
					name: [{ required: true, message: '请输入可转债名称', trigger: 'blur' }],
					code: [{ required: true, message: '请输入可转债代码', trigger: 'blur' }],
					lastestPrice: [{ required: true, message: '请输入最新价格', trigger: 'blur' }],
					lastestPriceDate: [{ required: true, message: '请输入最新价格对应的日期', trigger: 'blur' }],
				},
				//编辑界面数据
				editForm: {
					isOptional: false,
					name: '',
					code: '',
					lastestPrice: '',
					lastestPriceDate: '',
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
				return row.disabled ? '禁用' : '正常';
			},	
			formatLastestPrice (row, column) { // 小数点后要保留4位小数，不足自动补0
				//处理最新价格
				let vue = this;
				let lastestPrice = vue.$utils.changeDecimal(row.lastestPrice, 4);

				//处理最新价格对应的日期
				let date = new Date(row.lastestPriceDate);
				let str = this.$utils.formatDate(date, 'MM-dd');
				return lastestPrice+"（"+ str +"）";
			},
			changeDecimal (val, scale) { // 小数点后保留2位小数，不足自动补0
				let vue = this;
				val = vue.$utils.changeDecimal(val, scale);
				return val;
			},					
			handleCurrentChange(val) {
				this.searchMsg.pageNo = val;
				this.getList();
			},
			getList() { // 获取商品可转债列表
				let vue = this
				let param = JSON.parse(JSON.stringify(vue.searchMsg));
				param.isOptional = true;
				vue.listLoading = true;
				vue.$http.post(vue, '/rest/member/convertibleBond/list', param,
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
			syncPrice: function (index, row) { // 同步最新价格
				let vue = this;
				vue.$http.post(vue, '/rest/member/convertibleBond/syncPrice', {"isOptional" : true},
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
			},
			handleDel (id) { // 删除
				this.$confirm('确认删除该记录吗?', '提示', {
					type: 'warning'
				}).then(() => {
					// this.listLoading = true;
				let vue = this;
				
				vue.$http.post(vue, '/rest/member/convertibleBond/delete', {"id" : id},
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
						isOptional: row.isOptional,
						name: row.name,
						code: row.code,
						lastestPrice: row.lastestPrice,
						lastestPriceDate: new Date(row.lastestPriceDate).getTime(),
						remarks: row.remarks
					}
				}else{
					this.editForm = {
						isOptional: false,
						name: '',
						code: '',
						lastestPrice: '',
						lastestPriceDate: '',
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
						let lastestPriceDate = new Date(param.lastestPriceDate);
						let str = this.$utils.formatDate(lastestPriceDate, 'yyyy/MM/dd hh:mm:ss');
						param.lastestPriceDate = str;

						let url = '';
						if(param.id){
							url = '/rest/member/convertibleBond/update';					
						}else{
							url = '/rest/member/convertibleBond/insert';						
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
		}
	}

</script>

<style scoped>

</style>