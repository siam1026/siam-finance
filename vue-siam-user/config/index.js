// see http://vuejs-templates.github.io/webpack for documentation.
var path = require('path')

module.exports = {
  build: {
    env: require('./prod.env'),
    index: path.resolve(__dirname, '../dist/index.html'),
    assetsRoot: path.resolve(__dirname, '../dist'),
    assetsSubDirectory: 'static',
    assetsPublicPath: './',
    productionSourceMap: false,
    // Gzip off by default as many popular static hosts such as
    // Surge or Netlify already gzip all static assets for you.
    // Before setting to `true`, make sure to:
    // npm install --save-dev compression-webpack-plugin
    productionGzip: false,
    productionGzipExtensions: ['js', 'css'],
    // Run the build command with an extra argument to
    // View the bundle analyzer report after build finishes:
    // `npm run build --report`
    // Set to `true` or `false` to always turn it on or off
    bundleAnalyzerReport: process.env.npm_config_report
  },
  appConfig: {
    rootPath: '',
    name: 'siam-shop',
    http:{
      // baseUrl: 'https://api.siamit.cn/siam-finance/', // production
      // baseUrl4Test: 'https://api.siamit.cn/siam-finance/', // production_test
      // developmentBaseUrl: 'https://api.siamit.cn/siam-finance', // development
      // cashierBaseUrl: 'https://spa.show.siamit.cn/server-cashier' // cashier

      baseUrl: 'http://127.0.0.1:9200/siam-finance', // production
      baseUrl4Test: 'http://127.0.0.1:9200/siam-finance', // production_test
      developmentBaseUrl: 'http://127.0.0.1:9200/siam-finance', // development
      cashierBaseUrl: 'http://127.0.0.1:5174'
    },
    oss:{
      domain: 'https://siam-hangzhou.oss-cn-hangzhou.aliyuncs.com/'
    }
  }
}