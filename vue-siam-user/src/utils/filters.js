import Vue from 'vue'
/**
 * 金额格式化，三位数逗号分隔，小数点后保留两位
 */
Vue.filter("formatMoney", (data) => {
	if (!data) return '0.00'
	//防止数据不规范，可能为字符串类型，统一给他转成浮点数
	data = parseFloat(data)
	// 将数据分割，保留两位小数
	data= data.toFixed(2)
	// 获取整数部分
	const intPart = Math.trunc(data)
	// 整数部分处理，增加,
	const intPartFormat = intPart.toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,')
	// 预定义小数部分
	let floatPart = '.00'
	// 将数据分割为小数部分和整数部分
	const newArr = data.toString().split('.')
	if (newArr.length === 2) { // 有小数部分
	floatPart = newArr[1].toString() // 取得小数部分
	return intPartFormat + '.' + floatPart
	}
	return intPartFormat + floatPart
})
