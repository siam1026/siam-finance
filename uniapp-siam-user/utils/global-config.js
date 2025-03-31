export default class GlabalConfig {
    // static baseUrl = 'https://api.test.siamit.cn/siam-finance';
	// #ifdef H5
	static baseUrl = 'siam-finance';
	// #endif
	// #ifdef APP-PLUS||MP-WEIXIN||MP-ALIPAY
	static baseUrl = 'https://api.show.siamit.cn/siam-finance';
	// #endif
    
    // static baseUrl = 'http://localhost:9020';
    static ossUrl = 'https://siam-hangzhou.oss-cn-hangzhou.aliyuncs.com/';
}
