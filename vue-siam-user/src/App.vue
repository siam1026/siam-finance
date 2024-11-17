<template>
	<div id="app">
		<transition name="fade"
		            mode="out-in">
			<router-view></router-view>
		</transition>
	</div>
</template>

<script>
export default {
	name: 'app',
	components: {
	}
}

</script>

<style lang="scss">
body {
	margin: 0px;
	padding: 0px;
	/*background: url(assets/bg1.jpg) center !important;
		background-size: cover;*/
	// background: #1F2D3D;
	font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, SimSun, sans-serif;
	font-size: 14px;
	-webkit-font-smoothing: antialiased;
}

#app {
	position: absolute;
	top: 0px;
	bottom: 0px;
	width: 100%;
}

.el-submenu [class^=fa] {
	vertical-align: baseline;
	margin-right: 10px;
}

.el-menu-item [class^=fa] {
	vertical-align: baseline;
	margin-right: 10px;
}
// .content-wrapper .el-input {
//   width: 300px;
// }
.toolbar {
	// background: #f2f2f2;
	padding: 10px;
	//border:1px solid #dfe6ec;
	margin: 10px 0px;
	.el-form-item {
		margin-bottom: 10px;
	}
}

.fade-enter-active,
.fade-leave-active {
	transition: all .2s ease;
}

.fade-enter,
.fade-leave-active {
	opacity: 0;
}

// 自定义样式
// 左侧菜单栏
.el-menu {
    background-color: #090723
}
.el-menu-item {
	color: #fff;
}
.el-menu-item:hover {
	color: #fff;
	// background-color: #16162f
  // background-color: dimgrey;
  background-color: slategray;
}
.el-menu-item.is-active {
    color: #fff;
    // background-color: #16162f
    // background-color: dimgrey;
    background-color: slategray;
}
.el-submenu__title {
	color: #fff;
	// font-weight: bold;
}
.el-submenu__title:hover {
	color: #fff;
	// background-color: #16162f
  // background-color: dimgrey;
  background-color: slategray;
}
// 右侧区域
.container .main .content-container[data-v-5a90ec03] {
    // background: aliceblue;
	// background: #eceff1;
	// background: #f9f7ff;
	background: white;
}
.container .main .content-container .content-wrapper[data-v-5a90ec03] {
    background-color: unset!important;
}
.container .header .userinfo .userinfo-inner[data-v-5a90ec03] {
    color: black!important;
}
// 图标
.el-icon-wallet:before, .el-icon-coin:before, .el-icon-star-off:before {
    color: #fff;
}
// 按钮
.el-button--primary {
    color: #FFF;
    // background-color: #409EFF;
    // border-color: #409EFF;
	background-color: #5facb4;
    border-color: #5facb4;
}
.el-button--primary:focus, .el-button--primary:hover {
    // background: #66b1ff;
    // border-color: #66b1ff;
    color: #FFF;
	background: #5ca2aa;
    border-color: #5ca2aa;
}
// 网站标题
.container .header .logo-width[data-v-5a90ec03] {
    background: #22253b;
    color: #fff;
	font-weight: bold;
	width: 229px!important;
}
.container .header .logo-collapse-width[data-v-5a90ec03] {
    background: #22253b;
}
// 用户头像
.container .header .userinfo .userinfo-inner img[data-v-5a90ec03] {
	margin-top: 5px!important;
}

/* Start 金额信息卡片 */
  .content{
    width: 100%;
    height: 100%;
    padding-top: 30px;
    overflow: hidden;
    /* background-color: #E7F1FA; */
  }
  .todayTitle{
    font-size:20px;
    font-weight:bold;
    color:rgba(51,51,51,1);
    margin-right: 16px;
  }
  .todayBox{
    width: 100%;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin-top: 24px;
    margin-bottom: 12px;
    display: -webkit-inline-box;    
  }
  .totdayBlock{
    width: 30%;
    color: #fff;
    padding-top: 16px;
    padding-bottom: 30px;
    border-radius:4px;
    cursor: pointer;
    margin-right: 20px;
  }
  .orange{
    background-color: #F78A71;
  }
  .yellow{
    background-color: #F8B548;
  }
  .green{
    background-color: #3FD7E4;
  }
  .blue{
    background-color: #05ACEC;
  }
  .todayBlockTitle{
    margin-left: 18px;
    /* color: rgba(0, 0, 0, 0.45); */
    font-weight: 700;
    font-size: 14px;
  }
  .todayBlockNum{
    /* text-align: center; */
    font-size: 34px;
    line-height: 34px;
    margin-top: 20px;
    margin-left: 18px;
  }
  .searchBox{
    margin-top: 10px;
  }
  /* End 金额信息卡片 */

</style>