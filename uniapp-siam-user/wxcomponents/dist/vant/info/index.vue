<template>
<uni-shadow-root class="dist-vant-info-index"><view v-if="info !== null && info !== '' || dot" :class="'van-info '+(utils.bem('info', { dot }))+' custom-class'" :style="customStyle">{{ dot ? '' : info }}</view></uni-shadow-root>
</template>
<wxs src="../wxs/utils.wxs" module="utils"></wxs>
<script>

global['__wxRoute'] = 'dist/vant/info/index'
import { VantComponent } from '../common/component';
VantComponent({
    props: {
        dot: Boolean,
        info: null,
        customStyle: String,
    },
});
export default global['__wxComponents']['dist/vant/info/index']
</script>
<style platform="mp-weixin">
@import '../common/index.css';.van-info{align-items:center;background-color:var(--info-background-color,#ee0a24);border:var(--info-border-width,1px) solid #fff;border-radius:var(--info-size,16px);box-sizing:border-box;color:var(--info-color,#fff);display:inline-flex;font-family:var(--info-font-family,-apple-system-font,Helvetica Neue,Arial,sans-serif);font-size:var(--info-font-size,12px);font-weight:var(--info-font-weight,500);height:var(--info-size,16px);justify-content:center;min-width:var(--info-size,16px);padding:var(--info-padding,0 3px);position:absolute;right:0;top:0;transform:translate(50%,-50%);transform-origin:100%;white-space:nowrap}.van-info--dot{background-color:var(--info-dot-color,#ee0a24);border-radius:100%;height:var(--info-dot-size,8px);min-width:0;width:var(--info-dot-size,8px)}
</style>