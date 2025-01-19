// const Login = () => import( './pages/Login.vue')
const LoginMain = () => import('./pages/LoginMain.vue')
const NotFound = () => import( './pages/404.vue')
const Home = () => import( './pages/Home.vue')
const Main = () => import( './pages/Main.vue')

const login = () => import('./pages/login/login.vue')
const register = () => import('./pages/login/register.vue')
const userAgreement = () => import('./pages/login/userAgreement.vue')
const QuickLogin = () => import('./pages/login/QuickLogin.vue')
const setPassword = () => import('./pages/login/setPassword.vue')
const fillInformation = () => import('./pages/login/fillInformation.vue')

//空页面
const emptyList = () => import( './pages/basicModule/emptyList.vue')
//const CoreImage = () => import( './pages/basicModule/CoreImage.vue')


//商品管理
const menuList = () => import( './pages/goodsManage/menuList.vue')
const goodsList = () => import( './pages/goodsManage/goodsList.vue')
const addGoods = () => import( './pages/goodsManage/addGoods.vue')
const editGoods = () => import( './pages/goodsManage/editGoods.vue')
const goodsAccessoriesList = () => import( './pages/goodsManage/goodsAccessoriesList.vue')
const rawmaterialList = () => import( './pages/goodsManage/rawmaterialList.vue')
const goodsRawmaterialRelationList = () => import( './pages/goodsManage/goodsRawmaterialRelationList.vue')
// const specificationList = () => import( './pages/goodsManage/specificationList.vue')
// const stockList = () => import( './pages/goodsManage/stockList.vue')
const recommendGoods = () => import( './pages/goodsManage/recommendGoods.vue')

//订单管理
const waitHandleOrderList = () => import( './pages/orderManage/waitHandleOrderList.vue')
const forHereTabsChild1 = () => import( './pages/orderManage/forHereTabsChild1.vue')
const takeOutTabsChild1 = () => import( './pages/orderManage/takeOutTabsChild1.vue')
const takeOutTabsChild2 = () => import( './pages/orderManage/takeOutTabsChild2.vue')
const forHereTabsChild2 = () => import( './pages/orderManage/forHereTabsChild2.vue')
const refundOrderList = () => import( './pages/orderManage/refundOrderList.vue')
const forHereTabsChild3 = () => import( './pages/orderManage/forHereTabsChild3.vue')
const orderDetail = () => import( './pages/orderManage/orderDetail.vue')
//const unpaidOrderList = () => import( './pages/MallManage/unpaidOrderList.vue')
const forHereOrderList = () => import( './pages/orderManage/forHereOrderList.vue')
const takeOutOrderList = () => import( './pages/orderManage/takeOutOrderList.vue')
const todayOrderList = () => import( './pages/orderManage/todayOrderList.vue')
const appraiseList = () => import( './pages/orderManage/appraiseList.vue')

//促销管理
const fullReductionRuleList = () => import( './pages/promotionManage/fullReductionRuleList.vue')
const couponsList = () => import( './pages/promotionManage/couponsList.vue')

//门店管理
const shopInfo = () => import( './pages/shopManage/shopInfo.vue')
const shopInfoImportant = () => import( './pages/shopManage/shopInfoImportant.vue')
const shopChangeRecord = () => import( './pages/shopManage/shopChangeRecord.vue')
const courierList = () => import( './pages/shopManage/internal/courierList.vue')
const ticketPrinterList = () => import( './pages/shopManage/internal/ticketPrinterList.vue')
const labelPrinterList = () => import( './pages/shopManage/internal/labelPrinterList.vue')

//轮播图管理
const advertisementList = () => import( './pages/shopDecoration/advertisementList.vue')

//数据中心
const statisticGraph = () => import( './pages/basicModule/statisticGraph.vue')

//财务报表
const accountInfo = () => import( './pages/accountModule/accountInfo.vue')
const merchantWithdrawRecord = () => import( './pages/accountModule/merchantWithdrawRecord.vue')
const merchantBillingRecord = () => import( './pages/accountModule/merchantBillingRecord.vue')







//金融模块-基金投资
const fundList = () => import( './pages/fundModule/fundList.vue')
const optionalFundList = () => import( './pages/fundModule/optionalFundList.vue')
const fundTotalList = () => import( './pages/fundModule/fundTotalList.vue')
const fundShorttermTradingTotalList = () => import( './pages/fundModule/shorttermTrading/fundShorttermTradingTotalList.vue')
const fundFixedInvestmentPlanTotalList = () => import( './pages/fundModule/fixedInvestmentPlan/fundFixedInvestmentPlanTotalList.vue')
const runningFundShorttermTradingList = () => import( './pages/fundModule/shorttermTrading/runningFundShorttermTradingList.vue')
const completedFundShorttermTradingList = () => import( './pages/fundModule/shorttermTrading/completedFundShorttermTradingList.vue')
const viewFundShorttermTrading = () => import( './pages/fundModule/shorttermTrading/viewFundShorttermTrading.vue')
//中长线
const notStartedFundFixedInvestmentPlanList = () => import( './pages/fundModule/fixedInvestmentPlan/notStartedFundFixedInvestmentPlanList.vue')
const runningFundFixedInvestmentPlanList = () => import( './pages/fundModule/fixedInvestmentPlan/runningFundFixedInvestmentPlanList.vue')
const liquidatedFundFixedInvestmentPlanList = () => import( './pages/fundModule/fixedInvestmentPlan/liquidatedFundFixedInvestmentPlanList.vue')
const completedFundFixedInvestmentPlanList = () => import( './pages/fundModule/fixedInvestmentPlan/completedFundFixedInvestmentPlanList.vue')
const viewFundFixedInvestmentPlan = () => import( './pages/fundModule/fixedInvestmentPlan/viewFundFixedInvestmentPlan.vue')
const viewFundFixedInvestmentPlanSell = () => import( './pages/fundModule/fixedInvestmentPlan/viewFundFixedInvestmentPlanSell.vue')
const fundStatistics = () => import( './pages/fundModule/fundStatistics.vue')

//金融模块-股票投资
const stockList = () => import( './pages/stockModule/stockList.vue')
const optionalStockList = () => import( './pages/stockModule/optionalStockList.vue')
const stockTotalList = () => import( './pages/stockModule/stockTotalList.vue')
const stockShorttermTradingTotalList = () => import( './pages/stockModule/shorttermTrading/stockShorttermTradingTotalList.vue')
const stockFixedInvestmentPlanTotalList = () => import( './pages/stockModule/fixedInvestmentPlan/stockFixedInvestmentPlanTotalList.vue')
const runningStockShorttermTradingList = () => import( './pages/stockModule/shorttermTrading/runningStockShorttermTradingList.vue')
const completedStockShorttermTradingList = () => import( './pages/stockModule/shorttermTrading/completedStockShorttermTradingList.vue')
const viewStockShorttermTrading = () => import( './pages/stockModule/shorttermTrading/viewStockShorttermTrading.vue')
//中长线
const notStartedStockFixedInvestmentPlanList = () => import( './pages/stockModule/fixedInvestmentPlan/notStartedStockFixedInvestmentPlanList.vue')
const runningStockFixedInvestmentPlanList = () => import( './pages/stockModule/fixedInvestmentPlan/runningStockFixedInvestmentPlanList.vue')
const liquidatedStockFixedInvestmentPlanList = () => import( './pages/stockModule/fixedInvestmentPlan/liquidatedStockFixedInvestmentPlanList.vue')
const completedStockFixedInvestmentPlanList = () => import( './pages/stockModule/fixedInvestmentPlan/completedStockFixedInvestmentPlanList.vue')
const viewStockFixedInvestmentPlan = () => import( './pages/stockModule/fixedInvestmentPlan/viewStockFixedInvestmentPlan.vue')
const viewStockFixedInvestmentPlanSell = () => import( './pages/stockModule/fixedInvestmentPlan/viewStockFixedInvestmentPlanSell.vue')
const stockStatistics = () => import( './pages/stockModule/stockStatistics.vue')

//金融模块-可转债投资
const convertibleBondList = () => import( './pages/convertibleBondModule/convertibleBondList.vue')
const optionalConvertibleBondList = () => import( './pages/convertibleBondModule/optionalConvertibleBondList.vue')
const convertibleBondTotalList = () => import( './pages/convertibleBondModule/convertibleBondTotalList.vue')
const convertibleBondShorttermTradingTotalList = () => import( './pages/convertibleBondModule/shorttermTrading/convertibleBondShorttermTradingTotalList.vue')
const convertibleBondFixedInvestmentPlanTotalList = () => import( './pages/convertibleBondModule/fixedInvestmentPlan/convertibleBondFixedInvestmentPlanTotalList.vue')
const runningConvertibleBondShorttermTradingList = () => import( './pages/convertibleBondModule/shorttermTrading/runningConvertibleBondShorttermTradingList.vue')
const completedConvertibleBondShorttermTradingList = () => import( './pages/convertibleBondModule/shorttermTrading/completedConvertibleBondShorttermTradingList.vue')
const viewConvertibleBondShorttermTrading = () => import( './pages/convertibleBondModule/shorttermTrading/viewConvertibleBondShorttermTrading.vue')
//中长线
const notStartedConvertibleBondFixedInvestmentPlanList = () => import( './pages/convertibleBondModule/fixedInvestmentPlan/notStartedConvertibleBondFixedInvestmentPlanList.vue')
const runningConvertibleBondFixedInvestmentPlanList = () => import( './pages/convertibleBondModule/fixedInvestmentPlan/runningConvertibleBondFixedInvestmentPlanList.vue')
const liquidatedConvertibleBondFixedInvestmentPlanList = () => import( './pages/convertibleBondModule/fixedInvestmentPlan/liquidatedConvertibleBondFixedInvestmentPlanList.vue')
const completedConvertibleBondFixedInvestmentPlanList = () => import( './pages/convertibleBondModule/fixedInvestmentPlan/completedConvertibleBondFixedInvestmentPlanList.vue')
const viewConvertibleBondFixedInvestmentPlan = () => import( './pages/convertibleBondModule/fixedInvestmentPlan/viewConvertibleBondFixedInvestmentPlan.vue')
const viewConvertibleBondFixedInvestmentPlanSell = () => import( './pages/convertibleBondModule/fixedInvestmentPlan/viewConvertibleBondFixedInvestmentPlanSell.vue')
const convertibleBondStatistics = () => import( './pages/convertibleBondModule/convertibleBondStatistics.vue')

//金融模块-黄金投资
const goldList = () => import( './pages/goldModule/goldList.vue')
const goldShorttermTradingTotalList = () => import( './pages/goldModule/goldShorttermTradingTotalList.vue')
const runningGoldShorttermTradingList = () => import( './pages/goldModule/runningGoldShorttermTradingList.vue')
const completedGoldShorttermTradingList = () => import( './pages/goldModule/completedGoldShorttermTradingList.vue')
const viewGoldShorttermTrading = () => import( './pages/goldModule/viewGoldShorttermTrading.vue')

//金融模块-资金管理
const capitalTypeList = () => import( './pages/financeModule/capitalTypeList.vue')
const capitalRecordList = () => import( './pages/financeModule/capitalRecordList.vue')
const capitalRecordList_chuxu = () => import( './pages/financeModule/capitalRecordList_chuxu.vue')
const viewCapitalRecord = () => import( './pages/financeModule/viewCapitalRecord.vue')

//系统设置
const settingList = () => import('./pages/systemSetting/settingList.vue')



let routes = [
    // {
    //     path: '/login',
    //     component: Login,
    //     name: '',
    //     hidden: true
    // },
    {
      path: '/login',
      component: LoginMain,
      name: '',
      hidden: true,
      children: [
          { path: '/', component: login, pagename: '登录', icon: 'el-icon-search', hidden: true },
          { path: '/register', component: register, pagename: '注册', icon: 'el-icon-search', hidden: true },
          { path: '/QuickLogin', component: QuickLogin, pagename: '手机验证登录', icon: 'el-icon-search', hidden: true },
          { path: '/setPassword', component: setPassword, pagename: '忘记密码', icon: 'el-icon-search', hidden: true },
          
      ]
    },    
    {
      path: '/userAgreement',
      component: userAgreement,
      name: '',
      hidden: true
    },    
    {
      path: '/fillInformation',
      component: fillInformation,
      name: '',
      hidden: true
    },            
    {
        path: '/404',
        component: NotFound,
        name: '',
        hidden: true
    },

    {
      path: '/',
      component: Home,
      name: '资金管理',
      iconCls: 'el-icon-wallet',
      children: [
          { path: '/capitalTypeList', component: capitalTypeList, name: '资金分类' },
          { path: '/capitalRecordList', component: capitalRecordList, name: '资金记录列表', hidden: true },
          // { path: '/capitalRecordList_chuxu', component: capitalRecordList_chuxu, name: '储蓄资金' },
          { path: '/viewCapitalRecord', component: viewCapitalRecord, name: '查看资金收支记录', hidden: true },
      ]
    },
    {
      path: '/',
      component: Home,
      name: '基金投资',
      iconCls: 'el-icon-coin',
      children: [
        { path: '/fundStatistics', component: fundStatistics, name: '统计分析' },
          { path: '/fundTotalList', component: fundTotalList, name: '基金列表' },
          { path: '/fundShorttermTradingTotalList', component: fundShorttermTradingTotalList, name: '短线' },
          { path: '/fundFixedInvestmentPlanTotalList', component: fundFixedInvestmentPlanTotalList, name: '中长线' },
        //   { path: '/runningFundShorttermTradingList', component: runningFundShorttermTradingList, name: '短线-进行中' },
        //   { path: '/completedFundShorttermTradingList', component: completedFundShorttermTradingList, name: '短线-已完成' },
        //   { path: '/runningFundFixedInvestmentPlanList', component: runningFundFixedInvestmentPlanList, name: '中长线-进行中' },
        //   { path: '/completedFundFixedInvestmentPlanList', component: completedFundFixedInvestmentPlanList, name: '中长线-已完成' },          
          { path: '/viewFundShorttermTrading', component: viewFundShorttermTrading, name: '查看短线卖出记录', hidden: true },
          { path: '/viewFundFixedInvestmentPlan', component: viewFundFixedInvestmentPlan, name: '查看中长线买入记录', hidden: true },
          { path: '/viewFundFixedInvestmentPlanSell', component: viewFundFixedInvestmentPlanSell, name: '查看中长线卖出记录', hidden: true },
      ]
    },      
    {
      path: '/',
      component: Home,
      name: '股票投资',
      iconCls: 'el-icon-coin',
      children: [
        { path: '/stockStatistics', component: stockStatistics, name: '统计分析' },
          { path: '/stockTotalList', component: stockTotalList, name: '股票列表' },
          { path: '/stockShorttermTradingTotalList', component: stockShorttermTradingTotalList, name: '短线' },
          { path: '/stockFixedInvestmentPlanTotalList', component: stockFixedInvestmentPlanTotalList, name: '中长线' },
        //   { path: '/runningStockShorttermTradingList', component: runningStockShorttermTradingList, name: '短线-进行中' },
        //   { path: '/completedStockShorttermTradingList', component: completedStockShorttermTradingList, name: '短线-已完成' },
        //   { path: '/runningStockFixedInvestmentPlanList', component: runningStockFixedInvestmentPlanList, name: '中长线-进行中' },
        //   { path: '/completedStockFixedInvestmentPlanList', component: completedStockFixedInvestmentPlanList, name: '中长线-已完成' },          
          { path: '/viewStockShorttermTrading', component: viewStockShorttermTrading, name: '查看短线卖出记录', hidden: true },
          { path: '/viewStockFixedInvestmentPlan', component: viewStockFixedInvestmentPlan, name: '查看中长线买入记录', hidden: true },
          { path: '/viewStockFixedInvestmentPlanSell', component: viewStockFixedInvestmentPlanSell, name: '查看中长线卖出记录', hidden: true },
      ]
    },   
    {
      path: '/',
      component: Home,
      name: '可转债投资',
      iconCls: 'el-icon-coin',
      children: [
        { path: '/convertibleBondStatistics', component: convertibleBondStatistics, name: '统计分析' },
          { path: '/convertibleBondTotalList', component: convertibleBondTotalList, name: '可转债列表' },
          { path: '/convertibleBondShorttermTradingTotalList', component: convertibleBondShorttermTradingTotalList, name: '短线' },
          { path: '/convertibleBondFixedInvestmentPlanTotalList', component: convertibleBondFixedInvestmentPlanTotalList, name: '中长线' },
        //   { path: '/runningConvertibleBondShorttermTradingList', component: runningConvertibleBondShorttermTradingList, name: '短线-进行中' },
        //   { path: '/completedConvertibleBondShorttermTradingList', component: completedConvertibleBondShorttermTradingList, name: '短线-已完成' },
        //   { path: '/runningConvertibleBondFixedInvestmentPlanList', component: runningConvertibleBondFixedInvestmentPlanList, name: '中长线-进行中' },
        //   { path: '/completedConvertibleBondFixedInvestmentPlanList', component: completedConvertibleBondFixedInvestmentPlanList, name: '中长线-已完成' },          
          { path: '/viewConvertibleBondShorttermTrading', component: viewConvertibleBondShorttermTrading, name: '查看短线卖出记录', hidden: true },
          { path: '/viewConvertibleBondFixedInvestmentPlan', component: viewConvertibleBondFixedInvestmentPlan, name: '查看中长线买入记录', hidden: true },
          { path: '/viewConvertibleBondFixedInvestmentPlanSell', component: viewConvertibleBondFixedInvestmentPlanSell, name: '查看中长线卖出记录', hidden: true },
      ]
    },           
    {
      path: '/',
      component: Home,
      name: '黄金投资',
      iconCls: 'el-icon-coin',
      children: [
          { path: '/goldList', component: goldList, name: '黄金列表' },
          { path: '/goldShorttermTradingTotalList', component: goldShorttermTradingTotalList, name: '短线' },
        //   { path: '/runningGoldShorttermTradingList', component: runningGoldShorttermTradingList, name: '短线-进行中' },
        //   { path: '/completedGoldShorttermTradingList', component: completedGoldShorttermTradingList, name: '短线-已完成' },
          { path: '/viewGoldShorttermTrading', component: viewGoldShorttermTrading, name: '查看短线卖出记录', hidden: true },
      ]
    },

    {
      path: '/',
      component: Home,
      name: '系统配置',
      iconCls: 'el-icon-setting',
      children: [
        {
          path: '/settingList', component: settingList, name: '基础数据配置',
          leaf: true,//只有一个节点
          children: [
            { path: '/settingList', component: settingList, name: '基础数据配置' },
          ]
        },
        // { path: '/emptyList', component: emptyList, name: '支付设置' },
      ]
    },





    // {
    //   path: '/',
    //   component: Home,
    //   name: '数据中心',
    //   iconCls: 'el-icon-user',
    //   leaf: true,//只有一个节点
    //   children: [
    //       // { path: '/emptyList', component: emptyList, name: '每日数据' },
    //       { path: '/statisticGraph', component: statisticGraph, name: '数据中心' },
    //   ]
    // },
    // {
    //   path: '/',
    //   component: Home,
    //   name: '订单管理',
    //   iconCls: 'el-icon-s-order',
    //   children: [
    //       { path: '/todayOrderList', component: todayOrderList, name: '今日订单' },
    //       { path: '/forHereOrderList', component: forHereOrderList, name: '自取订单' },
    //       { path: '/takeOutOrderList', component: takeOutOrderList, name: '外卖订单' },          
    //       // { path: '/unpaidOrderList', component: unpaidOrderList, name: '未付款' },
    //       // { path: '/waitHandleOrderList', component: waitHandleOrderList, name: '待处理订单' },
    //       // { path: '/forHereTabsChild1', component: forHereTabsChild1, name: '待自取订单' },
    //       // { path: '/takeOutTabsChild1', component: takeOutTabsChild1, name: '待配送订单' },
    //       // { path: '/takeOutTabsChild2', component: takeOutTabsChild2, name: '已配送订单' },
    //       // { path: '/forHereTabsChild2', component: forHereTabsChild2, name: '已完成订单' },       
    //       { path: '/refundOrderList', component: refundOrderList, name: '售后处理' },          
    //       { path: '/appraiseList', component: appraiseList, name: '评价管理' },     
    //       // { path: '/forHereTabsChild3', component: forHereTabsChild3, name: '已取消订单' },          
    //       { path: '/orderDetail', component: orderDetail, name: '查看订单详情', hidden: true },   
    //      // { path: '/CoreImage', component: CoreImage, name: '裁剪图片' },        
    //   ]
    // }, 
    // {
    //   path: '/',
    //   component: Home,
    //   name: '商品管理',
    //   iconCls: 'el-icon-milk-tea',
    //   children: [
    //       { path: '/menuList', component: menuList, name: '分类管理' },
    //       { path: '/goodsList', component: goodsList, name: '商品列表' },
    //       { path: '/addGoods', component: addGoods, name: '新增商品', hidden: true },
    //       { path: '/editGoods', component: editGoods, name: '编辑商品', hidden: true },
    //       { path: '/recommendGoods', component: recommendGoods, name: '用户推荐' },
    //       // { path: '/goodsAccessoriesList', component: goodsAccessoriesList, name: '商品辅料' },          
    //       // { path: '/rawmaterialList', component: rawmaterialList, name: '商品原料' },  
    //       // { path: '/goodsRawmaterialRelationList', component: goodsRawmaterialRelationList, name: '原料配比' },  
    //       // { path: '/specificationList', component: specificationList, name: '商品规格' },
    //       // { path: '/stockList', component: stockList, name: '商品库存' },          
    //   ]
    // },        
    // {
    //   path: '/',
    //   component: Home,
    //   name: '活动中心',
    //   iconCls: 'el-icon-s-order',
    //   children: [
    //       // { path: '/unpaidOrderList', component: unpaidOrderList, name: '未付款' },
    //       { path: '/fullReductionRuleList', component: fullReductionRuleList, name: '满减' },
    //       { path: '/couponsList', component: couponsList, name: '优惠券' },
    //   ]
    // },     
    // {
    //   path: '/',
    //   component: Home,
    //   name: '门店管理',
    //   iconCls: 'el-icon-house',
    //   children: [
    //       { path: '/shopInfo', component: shopInfo, name: '门店基本信息' },
    //       { path: '/shopInfoImportant', component: shopInfoImportant, name: '门店重要信息' },
    //       { path: '/shopChangeRecord', component: shopChangeRecord, name: '门店信息变更记录' },          
    //       { path: '/advertisementList', component: advertisementList, name: '门店装修' },
    //       { path: '/courierList', component: courierList, name: '骑手信息' },
    //   ]
    // },
    // {
    //   path: '/',
    //   component: Home,
    //   name: '打印机管理',
    //   iconCls: 'el-icon-user',
    //   children: [
    //       { path: '/ticketPrinterList', component: ticketPrinterList, name: '小票打印机列表' },
    //       { path: '/labelPrinterList', component: labelPrinterList, name: '标签打印机列表' },
    //   ]
    // },
    // {
    //   path: '/',
    //   component: Home,
    //   name: '财务报表',
    //   iconCls: 'el-icon-user',
    //   children: [
    //       { path: '/accountInfo', component: accountInfo, name: '账户信息' },          
    //       { path: '/merchantBillingRecord', component: merchantBillingRecord, name: '账单记录' },//交易明细          
    //       { path: '/merchantWithdrawRecord', component: merchantWithdrawRecord, name: '提现记录' },
    //   ]
    // },
    
    {
        path: '*',
        hidden: true,
        redirect: { path: '/404' }
    }
];

export default routes;