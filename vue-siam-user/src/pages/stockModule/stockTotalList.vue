<template>
	<section>
    <el-tabs v-model="activeName" @tab-click="handleClick" style="margin-top:10px;margin-left:10px;" class="forHereOrderTabs">
      <el-tab-pane label="自选" name="zeroth">
        <child0 v-if="isChildUpdate0"></child0>
      </el-tab-pane>
      <el-tab-pane label="所有" name="first">
        <child1 v-if="isChildUpdate1"></child1>
      </el-tab-pane>
      <!-- <el-tab-pane label="已清仓" name="second">
        <child2 v-if="isChildUpdate2"></child2>
      </el-tab-pane>            
      <el-tab-pane label="已完成" name="third">
        <child3 v-if="isChildUpdate3"></child3>
      </el-tab-pane> -->
    </el-tabs>
  </section>
</template>
<script>
import tabZujianChild0 from './optionalStockList.vue'
import tabZujianChild1 from './stockList.vue'
// import tabZujianChild2 from './liquidatedFundFixedInvestmentPlanList.vue'
// import tabZujianChild3 from './completedFundFixedInvestmentPlanList.vue'
  export default {
    components:{
        child0: tabZujianChild0,
        child1: tabZujianChild1,
        // child2: tabZujianChild2,
        // child3: tabZujianChild3,       
    },    
    data() {
      return {
        activeName: 'zeroth',
        isChildUpdate0:true,
        isChildUpdate1:false,
        // isChildUpdate2:false,
        // isChildUpdate3:false,
      };
    },
    methods: {
      handleClick(tab, event) {
        if(tab.name == "zeroth") {
            this.isChildUpdate0 = true;
            this.isChildUpdate1 = false;
            // this.isChildUpdate2 = false;
            // this.isChildUpdate3 = false;
        }else if(tab.name == "first") {
            this.isChildUpdate0 = false;
            this.isChildUpdate1 = true;
            // this.isChildUpdate2 = false;
            // this.isChildUpdate3 = false;
        }
      },
      getList(pageNoParam) { // 获取列表
			}      
    },
		mounted() {
      this.getList();
		},
    beforeDestroy(){
    }    
  };
</script>

<style scoped>
/* #tab-zeroth, #tab-first {
  color: red;
} */
</style>