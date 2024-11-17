<template>
	<section>
    <el-tabs v-model="activeName" @tab-click="handleClick" style="margin-top:10px;margin-left:10px;" class="forHereOrderTabs">
      <el-tab-pane label="进行中" name="zeroth">
        <child0 v-if="isChildUpdate0"></child0>
      </el-tab-pane>
      <el-tab-pane label="已完成" name="first">
        <child1 v-if="isChildUpdate1"></child1>
      </el-tab-pane>
    </el-tabs>
  </section>
</template>
<script>
import tabZujianChild0 from './runningGoldShorttermTradingList.vue'
import tabZujianChild1 from './completedGoldShorttermTradingList.vue'
  export default {
    components:{
        child0: tabZujianChild0,
        child1: tabZujianChild1,       
    },    
    data() {
      return {
        activeName: 'zeroth',
        isChildUpdate0:true,
        isChildUpdate1:false,
      };
    },
    methods: {
      handleClick(tab, event) {
        if(tab.name == "zeroth") {
            this.isChildUpdate0 = true;
            this.isChildUpdate1 = false;
            this.isChildUpdate2 = false;
            this.isChildUpdate3 = false;
            this.isChildUpdate4 = false;
        }else if(tab.name == "first") {
            this.isChildUpdate0 = false;
            this.isChildUpdate1 = true;
            this.isChildUpdate2 = false;
            this.isChildUpdate3 = false;
            this.isChildUpdate4 = false;
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