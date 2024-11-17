<template>
	<section>
		<el-form :model="editForm" label-width="300px" style="width: 70%;" :rules="editFormRules" ref="editForm">

			<!-- 商家数据相关区域 -->
			<el-form-item label="基金-短线仓金额" prop="fundShortTermAmount">
				<el-input v-model="editForm.fundShortTermAmount" class="input-width">
					<template slot="append">元</template>
				</el-input>
			</el-form-item>		
						<el-form-item label="基金-中长线仓金额" prop="fundLongTermAmount">
				<el-input v-model="editForm.fundLongTermAmount" class="input-width">
					<template slot="append">元</template>
				</el-input>
			</el-form-item>		
			<el-form-item label="股票-短线仓金额" prop="stockShortTermAmount">
				<el-input v-model="editForm.stockShortTermAmount" class="input-width">
					<template slot="append">元</template>
				</el-input>
			</el-form-item>
			<el-form-item label="股票-中长线仓金额" prop="stockLongTermAmount">
				<el-input v-model="editForm.stockLongTermAmount" class="input-width">
					<template slot="append">元</template>
				</el-input>
			</el-form-item>		
			<el-form-item label="可转债-短线仓金额" prop="convertibleBondShortTermAmount">
				<el-input v-model="editForm.convertibleBondShortTermAmount" class="input-width">
					<template slot="append">元</template>
				</el-input>
			</el-form-item>		
			<el-form-item label="可转债-中长线仓金额" prop="convertibleBondLongTermAmount">
				<el-input v-model="editForm.convertibleBondLongTermAmount" class="input-width">
					<template slot="append">元</template>
				</el-input>
			</el-form-item>
			<el-form-item label="需推送股价信息的邮箱地址(多个以逗号分隔)" prop="pushStockPriceEmailAddress">
				<el-input v-model="editForm.pushStockPriceEmailAddress" class="input-width"></el-input>
			</el-form-item>
			<!-- <el-form-item label="公积金余额" prop="accumulationFundBalance">
				<el-input v-model="editForm.accumulationFundBalance" class="input-width">
					<template slot="append">元</template>
				</el-input>
			</el-form-item>
			<el-form-item label="医保余额" prop="medicalInsuranceBalance">
				<el-input v-model="editForm.medicalInsuranceBalance" class="input-width">
					<template slot="append">元</template>
				</el-input>
			</el-form-item>									 -->
			<div class="el-dialog__footer">
				<el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>																				
			</div>
		</el-form>
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
					fundShortTermAmount: [{ required: true, message: '请输入基金-短线仓金额', trigger: 'blur' }],					
					fundLongTermAmount: [{ required: true, message: '请输入基金-中长线仓金额', trigger: 'blur' }],					
					stockShortTermAmount: [{ required: true, message: '请输入股票-短线仓金额', trigger: 'blur' }],					
					stockLongTermAmount: [{ required: true, message: '请输入股票-中长线仓金额', trigger: 'blur' }],					
					convertibleBondShortTermAmount: [{ required: true, message: '请输入可转债-短线仓金额', trigger: 'blur' }],					
					convertibleBondLongTermAmount: [{ required: true, message: '请输入可转债-中长线仓金额', trigger: 'blur' }],					
					pushStockPriceEmailAddress: [{ required: true, message: '请输入需推送股价信息的邮箱地址', trigger: 'blur' }],
				},
				//编辑界面数据
				editForm: {
        			// pushStockPriceEmailAddressQrcodeFile: [], // 主图
					fundShortTermAmount: '',
					fundLongTermAmount: '',			
					stockShortTermAmount: '',								
					stockLongTermAmount: '',		
					convertibleBondShortTermAmount: '',		
					convertibleBondLongTermAmount: '',		
					pushStockPriceEmailAddress: '',				
				}
			}
		},
		methods: {
			resetImg(row, imgType) {
				let data = row
				console.log(row)
				if(row[imgType]) {
					let arr = row[imgType].split(',');
					let newArr = []
					let index = 0;
					console.log(arr)
					arr.forEach(ele => {
					let obj = {
						url: this.$http.publicUrl(ele),
						// oldUrl: ele,
						upUrl: ele,
						name: index +1
					}
					console.log(obj)
					newArr.push(obj)
					index++
					});
					return newArr
				} else {
					return []
				}
			},			
			getIdByArr(data) {
				let arr = data || []
				let url = arr.map((item) => {
					return item.upUrl
				});
				return url.join(',')
			},			
			handleRemoveCustomerServiceWechatQrcode(file, filelist) {
				let arr = this.editForm.pushStockPriceEmailAddressQrcodeFile
				this.editForm.pushStockPriceEmailAddressQrcodeFile = arr.filter(function(item) {
					return item.uid !== file.uid
				});
			},
			beforeAvatarUpload(file) {
				const isLt2M = file.size / 1024 / 1024 < 6;
				if (!isLt2M) {
					this.$message({
						showClose: true,
						message: '上传图片大小不能超过 6MB!',
						type: 'error'
					});
					}
					return isLt2M;
				},
				upload(option){ // 图片上传
				console.log(option)
				let vue = this;
				let value = option.file;
				let formData = new FormData();
				formData.append('file', value);    

				vue.$http.postupload(vue, '/rest/member/uploadSingleImage', formData,
					function (vue, data) {
						option.onSuccess();
						let obj = {
							url: vue.$http.publicUrl(data.data),
							uid: option.file.uid,
							upUrl: data.data
						}
						if (option.filename === 'subImagesUpload') {
							vue.editForm.subImagesFile.push(obj)
						} else if(option.filename === 'detailImagesUpload') {
							vue.editForm.detailImagesFile.push(obj)
						} else {
							vue.editForm.pushStockPriceEmailAddressQrcodeFile.push(obj)
						}
						},
					function (error) {
						option.onError();
				})
			},			
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
				if(row[column.property]==undefined || str=="1970-01-01 08:00:00"){
					str = "-";
				}
        		return str;
			},
			formatType (row, column) { // 0=正常/启用  1=禁用
				return row.stock == 1 ? '有货' : '无货';
			},
			addPointsUnit(row, column) { // 添加单位
				return (row[column.property] || 0) + '个'
			},			
			handleCurrentChange(val) {
				this.searchMsg.pageNo = val;
				this.getList();
			},
						getList(pageNoParam) { // 获取列表
				if(pageNoParam){
				this.searchMsg.pageNo = pageNoParam;
				} // 获取商品基础数据设置列表
				let vue = this
				let param = Object.assign(vue.searchMsg);
				vue.listLoading = true;

				vue.$http.post(vue, '/rest/member/setting/list', param,
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
				
					vue.$http.delete(vue, '/rest/member/setting/delete', {"id" : id},
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
						purchaseRewardPoints: row.purchaseRewardPoints,
						registrationRewardPoints: row.registrationRewardPoints,
						merchantWithdrawFee: row.merchantWithdrawFee,						
						convertibleBondShortTermAmount: row.convertibleBondShortTermAmount,		
						deliveryStartingDistance: row.deliveryStartingDistance,		
						deliveryStartingPrice: row.deliveryStartingPrice,		
						deliveryKilometerPrice: row.deliveryKilometerPrice,		
						deliveryDistanceLimit: row.deliveryDistanceLimit,		
					}
				}else{
					this.editForm = {
						purchaseRewardPoints: '',
						registrationRewardPoints: '',		
						merchantWithdrawFee: '',	
						convertibleBondShortTermAmount: '',		
						deliveryStartingDistance: '',		
						deliveryStartingPrice: '',		
						deliveryKilometerPrice: '',		
						deliveryDistanceLimit: '',																		
					}
				}		
			},
			getDetail() { // 获取商品详情
				let vue = this
				vue.$http.post(vue, '/rest/member/setting/selectCurrent', {},
					(vue, data) => {
					let obj = data.data
					obj.pushStockPriceEmailAddressQrcodeFile = vue.resetImg(obj, 'pushStockPriceEmailAddressQrcode')
					vue.editForm = Object.assign({}, obj)
					},(error, data)=> {
					vue.$message({
						showClose: true,
						message: data.message,
						type: 'error'
					});
					}
				)
			},			
			editSubmit: function () { // 编辑
				this.$refs.editForm.validate((valid) => {
					if (valid) {
						// this.editLoading = true;
						let vue = this
						let param = Object.assign({}, this.editForm);
						delete param.createTime;
						delete param.updateTime;

						// if(!vue.editForm.pushStockPriceEmailAddressQrcodeFile.length) {
						// 	vue.$message({
						// 	showClose: true,
						// 	message: '请上传客服微信二维码',
						// 	type: 'error'
						// 	});
						// 	return false
						// }else if(vue.editForm.pushStockPriceEmailAddressQrcodeFile.length > 1) {
						// 	vue.$message({
						// 	showClose: true,
						// 	message: '只能上传一张客服微信二维码',
						// 	type: 'error'
						// 	});
						// 	return false
						// }
						
						// param.pushStockPriceEmailAddressQrcode = vue.getIdByArr(param.pushStockPriceEmailAddressQrcodeFile)
						// delete param.pushStockPriceEmailAddressQrcodeFile

						let url = '';
						
						if(param.id){
							url = '/rest/member/setting/update';
							vue.$http.put(vue, url, param,
								(vue, data) => {
									// this.editLoading = false;
									vue.$message({
										showClose: true,
										message: data.message,
										type: 'success'
									});
								}, (error, data) => {
									vue.$message({
										showClose: true,
										message: data.message,
										type: 'error'
									});
								}
							)							
						}else{
							url = '/rest/member/setting/insert';
							vue.$http.post(vue, url, param,
								(vue, data) => {
									// this.editLoading = false;
									vue.$message({
										showClose: true,
										message: data.message,
										type: 'success'
									});
									
									vue.getList()
									vue.editFormVisible = false;
								}, (error, data) => {
									vue.editFormVisible = false;
									vue.$message({
										showClose: true,
										message: data.message,
										type: 'error'
									});
								}
							)							
						}
					}
				});
			}
		},
		mounted() {
			// this.getList();
			this.getDetail();
            //开启订单自动打印定时器
            this.$orderPrint.init();			
		}
	}

</script>

<style scoped>
  .input-width {
    width: 50%;
	min-width: 200px;
  }
  .note-margin {
    margin-left: 15px;
  }
</style>