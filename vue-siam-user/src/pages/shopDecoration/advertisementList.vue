<template>
  <section>
    <el-form :model="editForm" label-width="150px" class="editForm" style="width: 80%;" :rules="editFormRules" ref="editForm">
        <el-form-item label="店铺详情页第一栏海报（宽450px * 高185px）" prop="firstPosterFile" class="firstPosterFileItem">
          <el-upload
              ref="multipleUpload"
              :file-list="editForm.firstPosterFile"
              class="avatar-uploader" action="" accept=".image, .png, .jpg" multiple name="firstPosterUpload"
              :on-remove="handleRemoveShopWithinImg"
              :before-upload="beforeAvatarUpload" :http-request="upload"
              list-type="picture-card" >
              <i class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
		</el-form-item>
	</el-form>

	<div slot="footer" class="el-dialog__footer" style="text-align:center;">
		<el-button type="primary" @click="saveShopMsg">保存</el-button>
	</div>
  </section>
</template>
<script>
  import cityData from '../../../static/citys.json';
  export default {
    data() {
      return {
        options: cityData,
        props: {
            value: 'label',
            children: 'children'
        },
        menuList: [],
        editForm: {
          name:'',
          selectedOptions: [],
          street:'',
          managePrimary: '',
          shopImg: '',
          firstPoster:'',
          shopLogoImg:'',
          businessLicense:'',
          idCardFrontSide:'',
          idCardBackSide:'',          
          takeOutPhone:'',
          contactRealname:'',
          contactPhone:'',             
          shopLogoImgFile: [], // 主图
          firstPosterFile: [], // 介绍图          
          detailImagesFile: [], // 详情图
          businessLicenseFile: [], // 介绍图
          idCardFrontSideFile: [], // 介绍图
          idCardBackSideFile: [], // 介绍图                             
					isOperating: '正常营业',
					startTime: '8:00',
          endTime: '19:30',              
          announcement:'',          
          briefIntroduction:'',                    
        },
        editFormRules: {
          name: [{ required: true, message: '请输入门店名称', trigger: 'blur' }],
					selectedOptions:[{ type: 'array', required: true, message: '请选择所在城市', trigger: 'change' }],          
          street: [{ required: true, message: '请输入门店地址', trigger: 'blur' }],
          managePrimary: [{ required: true, message: '请选择主营类目', trigger: 'blur' }],
          takeOutPhone: [{ required: true, message: '请输入外卖电话', trigger: 'blur' }],
          announcement: [{ required: true, message: '请输入门店公告', trigger: 'blur' }],                
          briefIntroduction: [{ required: true, message: '请输入门店简介', trigger: 'blur' }],                
					isOperating: [{ required: true, message: '请选择店铺是否营业', trigger: 'blur' }],
					startTime: [{ required: true, message: '请输入店铺营业开始时间', trigger: 'blur' }],
					endTime: [{ required: true, message: '请输入店铺营业结束时间', trigger: 'blur' }],          
          shopLogoImgFile: [{ type: 'array', required: true, message: '请上传主图', trigger: 'change' }],
          firstPosterFile: [{type: 'array', required: true, message: '请上传店铺详情页第一栏海报', trigger: 'change' }],
          detailImagesFile: [{type: 'array', required: true, message: '请上传详情图片', trigger: 'change' }],
          businessLicenseFile: [{type: 'array', required: true, message: '请上传营业执照', trigger: 'change' }],
          idCardFrontSideFile: [{type: 'array', required: true, message: '请上传身份证正面照片', trigger: 'change' }],
          idCardBackSideFile: [{type: 'array', required: true, message: '请上传身份证反面照片', trigger: 'change' }]                    
        }
      }
    },
    methods: {
      getIdByArr(data) {
        let arr = data || []
        let url = arr.map((item) => {
          return item.oldUrl
        });
        return url.join(',')
      },      
      formmatAddress(row) { 
        let data = [...row]
        let [ province, city, area] = data
        this.searchMsg.province = province
        this.searchMsg.city = city
        // this.searchMsg.area = area
      },    
      saveShopMsg() {
        this.$refs.editForm.validate((valid) => {
          if (valid) {
            let vue = this
            if(!vue.editForm.firstPosterFile.length) {
              vue.$message({
                showClose: true,
                message: '请上传店铺详情页第一栏海报',
                type: 'error'
              });
              return false
            }else if(vue.editForm.firstPosterFile.length > 1) {
              vue.$message({
                showClose: true,
                message: '只能上传一张店铺详情页第一栏海报',
                type: 'error'
              });
              return false
            }
            let param = Object.assign({}, this.editForm);
            delete param.createTime
            delete param.updateTime

            param.firstPoster =  vue.getIdByArr(param.firstPosterFile)      
            delete param.firstPosterFile

            delete param.auditTime;

            let url = ''
            param.id ? url = '/rest/member/shop/update' : url = '/rest/member/shop/insert'
            vue.$http.post( vue, url, param,
              (vue, data) => {
                vue.$message({
                  showClose: true,
                  message: data.message,
                  type: 'success'
                });
                // vue.goBack()
              }, (error, data) => {
                vue.$message({
                  showClose: true,
                  message: data.message,
                  type: 'error'
                });
              }
            )
          }
        });
      },
      getDetail(id) { // 获取商品详情
          let vue = this
          vue.$http.post(vue, '/rest/member/shop/getLoginMemberShopInfo', {id},
            (vue, data) => {
              let obj = data.data
              obj.firstPosterFile = vue.resetImg(obj, 'firstPoster')                 
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
      resetImg(row, imgType) {
        let data = row
        if(row[imgType]) {
          let arr = row[imgType].split(',');
          let newArr = []
          let index = 0
          arr.forEach(ele => {
            let obj = {
              url: this.$http.publicUrl(ele),
              oldUrl: ele,
              name: index +1
            }
            newArr.push(obj)
            index++
          });
          return newArr
        } else {
          return []
        }
        
      },
      getTypeList() { // 获取商品类别列表
          let vue = this
          let param = {
            pageNo: -1, 
            pageSize: 10,
            typestatus: 0
          }
          vue.$http.post(vue, '/rest/member/menu/list', param,
            (vue, data) => {
              vue.menuList = data.data.records
            },(error, data)=> {
              vue.$message({
                showClose: true,
                message: data.message,
                type: 'error'
              });
            }
          )
      },
      handleRemoveShopLogoImg(file, filelist) {
        let arr = this.editForm.shopLogoImgFile
        this.editForm.shopLogoImgFile = arr.filter(function(item) {
          return item.uid !== file.uid
        });
      },
      handleRemoveShopWithinImg(file, filelist) {
        let arr = this.editForm.firstPosterFile
        this.editForm.firstPosterFile = arr.filter(function(item) {
          return item.uid !== file.uid
        });
      },
      handleRemoveBusinessLicense(file, filelist) {
        let arr = this.editForm.businessLicenseFile
        this.editForm.businessLicenseFile = arr.filter(function(item) {
          return item.uid !== file.uid
        });
      },
      handleRemoveIdCardFrontSide(file, filelist) {
        let arr = this.editForm.idCardFrontSideFile
        this.editForm.idCardFrontSideFile = arr.filter(function(item) {
          return item.uid !== file.uid
        });
      },
      handleRemoveIdCardBackSide(file, filelist) {
        let arr = this.editForm.idCardBackSideFile
        this.editForm.idCardBackSideFile = arr.filter(function(item) {
          return item.uid !== file.uid
        });
      },                  
      beforeAvatarUpload(file) {
          const isLt2M = file.size / 1024 / 1024 < 2;
          if (!isLt2M) {
          this.$message({
              showClose: true,
              message: '上传图片大小不能超过 2MB!',
              type: 'error'
          });
          }
          return isLt2M;
      },
      upload(option){ // 图片上传
        let vue = this;
        let value = option.file;
        let formData = new FormData();
        formData.append('file', value);
          vue.$http.postupload(
            vue, '/rest/member/uploadSingleImage', formData,
            function (vue, data) {
              option.onSuccess();
              let obj = {
                oldUrl: data.data,
                url: vue.$http.publicUrl(data.data),
                uid: option.file.uid,
              }
              if (option.filename === 'firstPosterUpload') {
                vue.editForm.firstPosterFile.push(obj)
              } else if(option.filename === 'detailImagesUpload') {
                vue.editForm.detailImagesFile.push(obj)
              } else if(option.filename === 'businessLicenseUpload') {
                vue.editForm.businessLicenseFile.push(obj)
              } else if(option.filename === 'idCardFrontSideUpload') {
                vue.editForm.idCardFrontSideFile.push(obj)
              } else if(option.filename === 'idCardBackSideUpload') {
                vue.editForm.idCardBackSideFile.push(obj)
              } else if(option.filename === 'shopLogoImgUpload') {
                vue.editForm.shopLogoImgFile.push(obj)
              }
            },
            function (error) {
                option.onError();
          })
      },
    },
    created() {
		this.getTypeList()
		let id = this.$route.query.id
		this.getDetail(id)
		//开启订单自动打印定时器
		this.$orderPrint.init();           
    }
  }
</script>

<style scoped>
.editForm .el-input {
  width: 420px;
}
.standForm .el-input {
  width: 200px;
}
.avatar-uploader .el-upload {
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    width: 148px;
    height: 148px;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 148px;
    height: 148px;
    line-height: 148px;
    text-align: center;
  }
  .avatar {
    width: 148px;
    height: 148px;
    display: block;
  }
  .editForm .minInput {
    width: 240px;
  }
</style>

