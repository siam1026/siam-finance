<template>
<uni-shadow-root class="dist-vant-picker-toolbar"><view v-if="showToolbar" class="van-picker__toolbar toolbar-class">
  <view class="van-picker__cancel" hover-class="van-picker__cancel--hover" hover-stay-time="70" data-type="cancel" @click="emit">
    {{ cancelButtonText }}
  </view>
  <view v-if="title" class="van-picker__title van-ellipsis">{{
    title
  }}</view>
  <view class="van-picker__confirm" hover-class="van-picker__confirm--hover" hover-stay-time="70" data-type="confirm" @click="emit">
    {{ confirmButtonText }}
  </view>
</view></uni-shadow-root>
</template>

<script>

global['__wxRoute'] = 'dist/vant/picker/toolbar'

Component({})

export default global['__wxComponents']['dist/vant/picker/toolbar']
</script>
<style platform="mp-weixin">

</style>