package com.siam.system.modular.mod_finance.service_impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siam.package_common.constant.Quantity;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.enums.TimeTypeEnum;
import com.siam.package_common.exception.StoneCustomerException;
import com.siam.package_common.util.DateUtilsPlus;
import com.siam.package_common.util.SortUtil;
import com.siam.system.modular.mod_finance.entity.CapitalRecord;
import com.siam.system.modular.mod_finance.entity.CapitalType;
import com.siam.system.modular.mod_finance.entity.Setting;
import com.siam.system.modular.mod_finance.entity.TransactionCategory;
import com.siam.system.modular.mod_finance.mapper.CapitalRecordMapper;
import com.siam.system.modular.mod_finance.model.example.CapitalRecordExample;
import com.siam.system.modular.mod_finance.model.example.CapitalTypeExample;
import com.siam.system.modular.mod_finance.model.param.CapitalRecordParam;
import com.siam.system.modular.mod_finance.model.result.CapitalRecordResult;
import com.siam.system.modular.mod_finance.model.vo.CapitalStatisticsVo;
import com.siam.system.modular.mod_finance.service.CapitalRecordService;
import com.siam.system.modular.mod_finance.service.CapitalTypeService;
import com.siam.system.modular.mod_finance.service.SettingService;
import com.siam.system.modular.mod_finance.service.TransactionCategoryService;
import com.siam.system.modular.mod_user.auth.cache.MemberSessionManager;
import com.siam.system.modular.mod_user.entity.Member;
import com.siam.system.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_DATE;

@Service
public class CapitalRecordServiceImpl extends ServiceImpl<CapitalRecordMapper, CapitalRecord> implements CapitalRecordService {

    @Autowired
    private CapitalTypeService capitalTypeService;

    @Autowired
    private TransactionCategoryService transactionCategoryService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private CapitalRecordMapper capitalRecordMapper;

    @Autowired
    private MemberSessionManager memberSessionManager;

    public List<CapitalRecord> selectByExample(CapitalRecordExample example){
        return capitalRecordMapper.selectByExample(example);
    }

    @Override
    public Page getListByPage(CapitalRecordParam param) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        param.setMemberId(loginMember.getId());
        Page<CapitalRecordResult> page = capitalRecordMapper.getListByPage(new Page(param.getPageNo(), param.getPageSize()), param);
        return page;
    }

//    @Override
//    public BigDecimal selectSumAmountByRelatedRecordId(Integer relatedRecordId) {
//        return capitalRecordMapper.selectSumAmountByRelatedRecordId(relatedRecordId);
//    }

//    @Override
//    public BigDecimal sumAmount(int capitalTypeId, Date startDate, Date endDate) {
//        return capitalRecordMapper.sumAmount(capitalTypeId, startDate, endDate);
//    }
//
//    @Override
//    public BigDecimal sumExpendAmount(int capitalTypeId, Date startDate, Date endDate) {
//        return capitalRecordMapper.sumExpendAmount(capitalTypeId, startDate, endDate);
//    }

    @Override
    public void insert(CapitalRecordParam param) {
        //如果操作关联的收支记录id不为空，则需要改变父记录的状态
        if(param.getRelatedRecordId() != null){
            //状态 0=无 1=部分还款 2=部分归还 3=部分收入 4=部分支出 5=已还款 6=已归还 7=已收入 8=已支出
            CapitalRecord relatedCapitalRecord = this.getById(param.getRelatedRecordId());
            if(relatedCapitalRecord.getStatus()==Quantity.INT_5 || relatedCapitalRecord.getStatus()==Quantity.INT_6 || relatedCapitalRecord.getStatus()==Quantity.INT_7 || relatedCapitalRecord.getStatus()==Quantity.INT_8){
                throw new StoneCustomerException("操作关联的收支记录已完成，不允许新增子记录");
            }

            //查询该关联记录已累积的金额
            BigDecimal sumAmount = capitalRecordMapper.selectSumAmountByRelatedRecordId(relatedCapitalRecord.getId());
            //该关联记录的状态
            int status = 0;
            //资金收支记录的操作类型
            int type = 0;
            if(relatedCapitalRecord.getAmount().compareTo(sumAmount.add(param.getAmount())) == Quantity.INT_0){
                //已经全部结算完
                switch (relatedCapitalRecord.getType()){
                    case 2:
                        status = Quantity.INT_5;
                        type = Quantity.INT_6;
                        break;
                    case 3:
                        status = Quantity.INT_6;
                        type = Quantity.INT_7;
                        break;
                    case 4:
                        status = Quantity.INT_7;
                        type = Quantity.INT_0;
                        break;
                    case 5:
                        status = Quantity.INT_8;
                        type = Quantity.INT_1;
                        break;
                }

            }else if(relatedCapitalRecord.getAmount().compareTo(sumAmount.add(param.getAmount())) > Quantity.INT_0){
                //部分结算完
                switch (relatedCapitalRecord.getType()){
                    case 2:
                        status = Quantity.INT_1;
                        type = Quantity.INT_6;
                        break;
                    case 3:
                        status = Quantity.INT_2;
                        type = Quantity.INT_7;
                        break;
                    case 4:
                        status = Quantity.INT_3;
                        type = Quantity.INT_0;
                        break;
                    case 5:
                        status = Quantity.INT_4;
                        type = Quantity.INT_1;
                        break;
                }

            }else{
                //数据异常
                throw new StoneCustomerException("数据异常，累积金额已经超过操作关联的收支记录金额");
            }

            //修改关联收支记录的状态
            CapitalRecord updateCapitalRecord = new CapitalRecord();
            updateCapitalRecord.setId(relatedCapitalRecord.getId());
            updateCapitalRecord.setStatus(status);
            this.updateById(updateCapitalRecord);

            //自动为资金收支记录赋值 资金分类id、操作类型
            param.setCapitalTypeId(relatedCapitalRecord.getCapitalTypeId());
            param.setType(type);
        }
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        param.setMemberId(loginMember.getId());

        //添加资金收入与支出记录
        //capitalRecord.setCreateTime(new Date());
        param.setUpdateTime(new Date());
        this.save(param);
    }

    @Override
    public void update(CapitalRecordParam param) {
        BasicResult basicResult = new BasicResult();

        CapitalRecord dbCapitalRecord = this.getById(param.getId());
        if(dbCapitalRecord == null){
            throw new StoneCustomerException("该资金收入与支出记录不存在，修改失败");
        }

        //修改资金收入与支出记录
        param.setUpdateTime(new Date());
        this.updateById(param);

        //如果操作关联的收支记录id不为空，则需要改变父记录的状态
        if(dbCapitalRecord.getRelatedRecordId() != Quantity.INT_0){
            //状态 0=无 1=部分还款 2=部分归还 3=部分收入 4=部分支出 5=已还款 6=已归还 7=已收入 8=已支出
            CapitalRecord relatedCapitalRecord = this.getById(dbCapitalRecord.getRelatedRecordId());
            if(relatedCapitalRecord.getStatus()==Quantity.INT_5 || relatedCapitalRecord.getStatus()==Quantity.INT_6 || relatedCapitalRecord.getStatus()==Quantity.INT_7 || relatedCapitalRecord.getStatus()==Quantity.INT_8){
                throw new StoneCustomerException("操作关联的收支记录已完成，不允许修改子记录");
            }

            //查询该关联记录已累积的金额
            BigDecimal sumAmount = capitalRecordMapper.selectSumAmountByRelatedRecordId(relatedCapitalRecord.getId());
            int status = 0;
            if(relatedCapitalRecord.getAmount().compareTo(sumAmount.add(param.getAmount())) == Quantity.INT_0){
                //已经全部结算完
                switch (relatedCapitalRecord.getType()){
                    case 2:
                        status = Quantity.INT_5;
                        break;
                    case 3:
                        status = Quantity.INT_6;
                        break;
                    case 4:
                        status = Quantity.INT_7;
                        break;
                    case 5:
                        status = Quantity.INT_8;
                        break;
                }

            }else if(relatedCapitalRecord.getAmount().compareTo(sumAmount.add(param.getAmount())) > Quantity.INT_0){
                //部分结算完
                switch (relatedCapitalRecord.getType()){
                    case 2:
                        status = Quantity.INT_1;
                        break;
                    case 3:
                        status = Quantity.INT_2;
                        break;
                    case 4:
                        status = Quantity.INT_3;
                        break;
                    case 5:
                        status = Quantity.INT_4;
                        break;
                }

            }else{
                //数据异常
                throw new StoneCustomerException("数据异常，累积金额已经超过操作关联的收支记录金额");
            }

            //修改关联收支记录的状态
            CapitalRecord updateCapitalRecord = new CapitalRecord();
            updateCapitalRecord.setId(relatedCapitalRecord.getId());
            updateCapitalRecord.setStatus(status);
            this.updateById(updateCapitalRecord);
        }
    }

    @Override
    public CapitalStatisticsVo statisticsOfHistogramChart(CapitalRecordParam param) {
        CapitalStatisticsVo statisticsVo = new CapitalStatisticsVo();

        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        param.setMemberId(loginMember.getId());

        //返回内容：类别、交易金额
        List<String> typeList = null;
        List<Map<String, Object>> incomeStatisticList = null;
        List<Map<String, Object>> expendStatisticList = null;
        if(param.getCapitalTypeId() == null){
            //按照大类别统计
            CapitalTypeExample example = new CapitalTypeExample();
            example.createCriteria().andNameNotLike("【已废弃】%").andNameNotLike("【不统计】%").andMemberIdEqualTo(loginMember.getId());
            List<CapitalType> capitalTypeList = capitalTypeService.selectByExample(example);
            typeList = capitalTypeList.stream().map(CapitalType::getName).distinct().collect(Collectors.toList());

            //查询类别、交易金额
            param.setType(0);
            incomeStatisticList = capitalRecordMapper.sumAmountByCapitalType(param);

            param.setType(1);
            expendStatisticList = capitalRecordMapper.sumAmountByCapitalType(param);
        }else{
            //按照小类别统计
            LambdaQueryWrapper<TransactionCategory> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.notLike(TransactionCategory::getName, "【已废弃】%").notLike(TransactionCategory::getName, "【不统计】%").eq(TransactionCategory::getMemberId, loginMember.getId());
            List<TransactionCategory> transactionCategoryList = transactionCategoryService.list(queryWrapper);
            typeList = transactionCategoryList.stream().map(TransactionCategory::getName).distinct().collect(Collectors.toList());

            //查询类别、交易金额
            param.setType(0);
            incomeStatisticList = capitalRecordMapper.sumAmountByTransactionCategory(param);

            param.setType(1);
            expendStatisticList = capitalRecordMapper.sumAmountByTransactionCategory(param);
        }

        //2、构造分类列表
        Map<String, Map> filterMap = new HashMap();
        List<Map<String, Object>> resultList = new ArrayList();
        for (String dateStr : typeList) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", dateStr); //前端的变量名为date
            map.put("income", 0);
            map.put("expend", 0);
            map.put("leave", 0);
            resultList.add(map);
            //记录日期对应的集合元素，便于后续赋值
            filterMap.put(dateStr, map);
        }

        //3、组装返回数据
        incomeStatisticList.forEach(statisticMap -> {
            if(filterMap.containsKey(statisticMap.get("typeName"))){
                //修改订单数据
                Map map = filterMap.get(statisticMap.get("typeName"));
                map.put("income", statisticMap.get("amount"));
            }
        });
        expendStatisticList.forEach(statisticMap -> {
            if(filterMap.containsKey(statisticMap.get("typeName"))){
                //修改订单数据
                Map map = filterMap.get(statisticMap.get("typeName"));
                map.put("expend", statisticMap.get("amount"));
            }
        });
        statisticsVo.setList(resultList);

        //计算总金额
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpend = BigDecimal.ZERO;
        for (Map<String, Object> map : resultList) {
            BigDecimal leave = BigDecimal.valueOf(Double.valueOf(map.get("income").toString())).subtract(BigDecimal.valueOf(Double.valueOf(map.get("expend").toString())));
            map.put("leave", leave);
            totalIncome = totalIncome.add(BigDecimal.valueOf(Double.valueOf(map.get("income").toString())));
            totalExpend = totalExpend.add(BigDecimal.valueOf(Double.valueOf(map.get("expend").toString())));
        }
        BigDecimal totalLeave = totalIncome.subtract(totalExpend);
        statisticsVo.setTotalIncome(totalIncome);
        statisticsVo.setTotalExpend(totalExpend);
        statisticsVo.setTotalLeave(totalLeave);

        //如果是按照交易分类来统计的话，需要过滤掉统计金额为0的数据
        if(param.getCapitalTypeId() != null){
            List<Map<String, Object>> finalList = new ArrayList();
            for (Map<String, Object> map : resultList) {
                //判断展示方式
                if(param.getShowType() == null){
                    if(Double.valueOf(map.get("income").toString()) != 0 || Double.valueOf(map.get("expend").toString()) != 0){
                        finalList.add(map);
                    }
                }else if(param.getShowType() == 0){
                    if(Double.valueOf(map.get("income").toString()) != 0){
                        finalList.add(map);
                    }
                }else if(param.getShowType() == 1){
                    if(Double.valueOf(map.get("expend").toString()) != 0){
                        finalList.add(map);
                    }
                }
            }
            statisticsVo.setList(finalList);
        }

        //排序
        if(param.getSortKey() != null){
            List<Map<String, Object>> list = SortUtil.sortDoubleMap(statisticsVo.getList(), param.getSortType(), param.getSortKey());
            statisticsVo.setList(list);
        }

        return statisticsVo;
    }

    @Override
    public CapitalStatisticsVo statisticsOfPieChart(CapitalRecordParam param) {
        CapitalStatisticsVo statisticsVo = new CapitalStatisticsVo();

        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        param.setMemberId(loginMember.getId());

        //返回内容：类别、交易金额
        List<String> typeList = null;
        List<Map<String, Object>> statisticList = null;
        if(param.getCapitalTypeId() == null){
            //按照大类别统计
            CapitalTypeExample example = new CapitalTypeExample();
            example.createCriteria().andNameNotLike("【已废弃】%").andNameNotLike("【不统计】%").andMemberIdEqualTo(loginMember.getId());
            List<CapitalType> capitalTypeList = capitalTypeService.selectByExample(example);
            typeList = capitalTypeList.stream().map(CapitalType::getName).distinct().collect(Collectors.toList());

            //查询类别、交易金额
            statisticList = capitalRecordMapper.sumAmountByCapitalType(param);

        }else{
            //按照小类别统计
            LambdaQueryWrapper<TransactionCategory> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.notLike(TransactionCategory::getName, "【已废弃】%").notLike(TransactionCategory::getName, "【不统计】%").eq(TransactionCategory::getMemberId, loginMember.getId());
            List<TransactionCategory> transactionCategoryList = transactionCategoryService.list(queryWrapper);
            typeList = transactionCategoryList.stream().map(TransactionCategory::getName).distinct().collect(Collectors.toList());

            //查询类别、交易金额
            statisticList = capitalRecordMapper.sumAmountByTransactionCategory(param);
        }

        //2、构造分类列表
        Map<String, Map> filterMap = new HashMap();
        List<Map<String, Object>> resultList = new ArrayList();
        for (String dateStr : typeList) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", dateStr); //前端的变量名为date
            map.put("amount", 0);
            resultList.add(map);
            //记录日期对应的集合元素，便于后续赋值
            filterMap.put(dateStr, map);
        }

        //3、组装返回数据
        statisticList.forEach(statisticMap -> {
            if(filterMap.containsKey(statisticMap.get("typeName"))){
                //修改订单数据
                Map map = filterMap.get(statisticMap.get("typeName"));
                map.put("amount", statisticMap.get("amount"));
            }
        });
        statisticsVo.setList(resultList);

        //计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Map<String, Object> map : resultList) {
            totalAmount = totalAmount.add(BigDecimal.valueOf(Double.valueOf(map.get("amount").toString())));
        }
        statisticsVo.setTotalAmount(totalAmount);

        //如果是按照交易分类来统计的话，需要过滤掉统计金额为0的数据
        if(param.getCapitalTypeId() != null){
            List<Map<String, Object>> finalList = new ArrayList();
            for (Map<String, Object> map : resultList) {
                if(Double.valueOf(map.get("amount").toString()) != 0){
                    finalList.add(map);
                }
            }
            statisticsVo.setList(finalList);
        }

        //排序
        if(param.getSortKey() != null){
            List<Map<String, Object>> list = SortUtil.sortDoubleMap(statisticsVo.getList(), param.getSortType(), param.getSortKey());
            statisticsVo.setList(list);
        }

        return statisticsVo;
    }

    @Override
    public Map statisticOfLineChart(CapitalRecordParam param) {
        Map resultMap = new HashMap();

        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        param.setMemberId(loginMember.getId());

        //统计分类为[保底资金、反哺资金、储蓄资金、业余收入、博弈资金、博弈资金B、储蓄资金(含保底资金、反哺资金)]的交易数据
        //值为-1代表查询储蓄资金(含保底资金、反哺资金)分类

        //返回内容：日期、交易数量、交易金额

        //1、查询开始日期(系统/订单服务第一次上线日期) - 今天
        /*String startDate = "2020-05-10";*/
        String startDate = capitalRecordMapper.selectStartDateOrder(param);
        if(startDate == null){
            startDate = DateUtilsPlus.formatDate(new Date(), "YYYY-MM-dd");
        }
        String endDate = DateUtilsPlus.formatDate(new Date(), "YYYY-MM-dd");

        List<String> betweenDays = null;
        List<Map<String, Object>> statisticList = null;
        if(param.getDateType() == 0){
            //按日进行统计
            betweenDays = DateUtilsPlus.getBetweenDays(startDate, endDate);

            //查询每一个日期对应的 交易数量、交易金额
            statisticList = capitalRecordMapper.selectStatisticOrder(param);

        } else if(param.getDateType() == 1){
            //按月进行统计
            betweenDays = DateUtilsPlus.getMonthBetweenDate(startDate, endDate);

            //查询每一个日期对应的 交易数量、交易金额
            statisticList = capitalRecordMapper.selectStatisticOrderByMonth(param);

        } else if(param.getDateType() == 2){
            //按年进行统计
            LocalDate start = LocalDate.parse(startDate, ISO_DATE);
            LocalDate end = LocalDate.parse(endDate, ISO_DATE);
            long timstrap = start.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
            long endtime = end.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
            betweenDays = DateUtilsPlus.getTimePeriodFromTwoTime(timstrap, endtime, TimeTypeEnum.YEAR);

            //查询每一个日期对应的 交易数量、交易金额
            statisticList = capitalRecordMapper.selectStatisticOrderByYear(param);
        }

        //2、构造日期列表
        Map<String, Map> filterMap = new HashMap();
        List<Map<String, Object>> resultList = new ArrayList();
        for (String dateStr : betweenDays) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", dateStr); //前端的变量名为date
            map.put("orderCount", 0);
            map.put("orderAmount", 0);
            resultList.add(map);
            //记录日期对应的集合元素，便于后续赋值
            filterMap.put(dateStr, map);
        }

        //3、组装返回数据
        statisticList.forEach(statisticMap -> {
            if(filterMap.containsKey(statisticMap.get("orderDate"))){
                //修改订单数据
                Map map = filterMap.get(statisticMap.get("orderDate"));
                map.put("orderCount", statisticMap.get("orderCount"));
                map.put("orderAmount", statisticMap.get("orderAmount"));
            }
        });
        resultMap.put("resultList", resultList);

        return resultMap;
    }

    @Override
    public Map statistics(CapitalRecordParam param) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        param.setMemberId(loginMember.getId());

        //计算收入金额
        param.setType(0);
        BigDecimal incomeAmount = capitalRecordMapper.sumAmount(param);

        //计算支出金额
        param.setType(1);
        BigDecimal expendAmount = capitalRecordMapper.sumAmount(param);

        //计算结余金额
        BigDecimal leaveAmount = incomeAmount.subtract(expendAmount);

        Map map = new HashMap();
        map.put("incomeAmount", incomeAmount);
        map.put("expendAmount", expendAmount);
        map.put("leaveAmount", leaveAmount);
        return map;
    }

    @Override
    public Map totalCapitalStatistic(CapitalRecordParam param) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        param.setMemberId(loginMember.getId());

        //获取资金分类id
        int capitalTypeId_chuxu = capitalTypeService.selectByName("储蓄资金").getId();
        int capitalTypeId_boyi = capitalTypeService.selectByName("博弈资金").getId();
        int capitalTypeId_yeyu = capitalTypeService.selectByName("业余收入").getId();
        int capitalTypeId_other = capitalTypeService.selectByName("【不统计】其它资产").getId();

        //计算各个资金分类的收入情况
        param.setType(0);
        param.setCapitalTypeId(capitalTypeId_chuxu);
        BigDecimal income_chuxu = capitalRecordMapper.sumAmount(param);

        param.setCapitalTypeId(capitalTypeId_yeyu);
        BigDecimal income_yeyu = capitalRecordMapper.sumAmount(param);

        param.setCapitalTypeId(capitalTypeId_boyi);
        BigDecimal income_boyi = capitalRecordMapper.sumAmount(param);

        param.setCapitalTypeId(capitalTypeId_other);
        BigDecimal income_other = capitalRecordMapper.sumAmount(param);

        //计算各个资金分类的支出情况
        param.setType(1);
        param.setCapitalTypeId(capitalTypeId_chuxu);
        BigDecimal expenditure_chuxu = capitalRecordMapper.sumAmount(param);

        param.setCapitalTypeId(capitalTypeId_yeyu);
        BigDecimal expenditure_yeyu = capitalRecordMapper.sumAmount(param);

        param.setCapitalTypeId(capitalTypeId_boyi);
        BigDecimal expenditure_boyi = capitalRecordMapper.sumAmount(param);

        param.setCapitalTypeId(capitalTypeId_other);
        BigDecimal expenditure_other = capitalRecordMapper.sumAmount(param);

        //计算各个资金分类的现存资金情况
        BigDecimal leave_chuxu = income_chuxu.subtract(expenditure_chuxu);
        BigDecimal leave_boyi = income_boyi.subtract(expenditure_boyi);
        BigDecimal leave_yeyu = income_yeyu.subtract(expenditure_yeyu);
        BigDecimal leave_other = income_other.subtract(expenditure_other);

        //总共待转收入的资金
        param = new CapitalRecordParam();
        param.setType(4);
        BigDecimal totalDeferredIncome = capitalRecordMapper.sumAmountAllowRelatedRecordId(param);
        //待转收入已入账的资金
        param = new CapitalRecordParam();
        param.setType(0);
        param.setRelatedRecordId(-1);
        BigDecimal returnedDeferredIncome = capitalRecordMapper.sumAmountAllowRelatedRecordId(param);
        //待转收入未入账的资金
        BigDecimal leaveDeferredIncome = totalDeferredIncome.subtract(returnedDeferredIncome);

        //总共借出的资金
        param = new CapitalRecordParam();
        param.setType(3);
        BigDecimal totalLendAmount = capitalRecordMapper.sumAmountAllowRelatedRecordId(param);
        //借出已归还的资金
        param = new CapitalRecordParam();
        param.setType(7);
        BigDecimal returnedLendAmount = capitalRecordMapper.sumAmountAllowRelatedRecordId(param);
        //借出未归还的资金
        BigDecimal leaveLendAmount = totalLendAmount.subtract(returnedLendAmount);

        //反哺资金
        BigDecimal fanbuAmount = BigDecimal.ZERO;
        LambdaQueryWrapper<TransactionCategory> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(TransactionCategory::getName, "反哺资金");
        TransactionCategory transactionCategory = transactionCategoryService.getOne(queryWrapper);
        if(transactionCategory != null){
            param = new CapitalRecordParam();
            param.setTransactionCategoryId(transactionCategory.getId());
            fanbuAmount = capitalRecordMapper.sumAmount(param);
        }

        Setting setting = settingService.selectCurrent();

        //公积金余额
        BigDecimal accumulationFundBalance = setting.getAccumulationFundBalance();

        //医保余额
        BigDecimal medicalInsuranceBalance = setting.getMedicalInsuranceBalance();

        //总资金(金融资产)
        BigDecimal totalAmount = leave_chuxu.add(leave_boyi).add(leave_yeyu).add(leaveDeferredIncome).add(fanbuAmount);

        //总资金(含其它资产)
        BigDecimal totalAmountWithOther = leave_chuxu.add(leave_boyi).add(leave_yeyu).add(leaveDeferredIncome).add(fanbuAmount).add(leave_other);

        //可用资金
        BigDecimal availableAmount = leave_chuxu.add(leave_boyi).add(leave_yeyu).subtract(accumulationFundBalance);

        //可用资金(含封存资金)
        BigDecimal availableAmountWithBlocked = leave_chuxu.add(leave_boyi).add(leave_yeyu);

        //已落袋资金
        BigDecimal receivedAmount = leave_chuxu.add(leave_boyi).add(leave_yeyu).add(fanbuAmount);

        //不可用资金
        BigDecimal unavailableAmount = accumulationFundBalance.add(leaveDeferredIncome).add(fanbuAmount).add(leave_other);

        Map map = new HashMap();
        map.put("totalAmount", totalAmount);
        map.put("totalAmountWithOther", totalAmountWithOther);
        map.put("availableAmount", availableAmount);
        map.put("availableAmountWithBlocked", availableAmountWithBlocked);
        map.put("receivedAmount", receivedAmount);
        map.put("unavailableAmount", unavailableAmount);
        map.put("leave_chuxu", leave_chuxu);
        map.put("leave_boyi", leave_boyi);
        map.put("leave_yeyu", leave_yeyu);
        map.put("leave_other", leave_other);
        map.put("leaveDeferredIncome", leaveDeferredIncome);
        map.put("leaveLendAmount", leaveLendAmount);
        map.put("fanbuAmount", fanbuAmount);
        map.put("accumulationFundBalance", accumulationFundBalance);
        map.put("medicalInsuranceBalance", medicalInsuranceBalance);
        return map;
    }
}