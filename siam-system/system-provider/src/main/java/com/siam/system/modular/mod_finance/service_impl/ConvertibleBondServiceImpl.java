package com.siam.system.modular.mod_finance.service_impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siam.package_common.constant.BusinessType;
import com.siam.package_common.exception.StoneCustomerException;
import com.siam.package_common.service.EmailService;
import com.siam.package_common.util.JsonUtils;
import com.siam.package_common.util.RedisUtils;
import com.siam.package_common.util.ToolUtil;
import com.siam.system.modular.mod_finance.api.ConvertibleBondApiService;
import com.siam.system.modular.mod_finance.api.StockApiService;
import com.siam.system.modular.mod_finance.vo.SinaStock;
import com.siam.system.modular.mod_finance.entity.*;
import com.siam.system.modular.mod_finance.mapper.ConvertibleBondMapper;
import com.siam.system.modular.mod_finance.model.param.ConvertibleBondParam;
import com.siam.system.modular.mod_finance.model.vo.ConvertibleBondStatisticsVo;
import com.siam.system.modular.mod_finance.service.*;
import com.siam.system.modular.mod_user.auth.cache.MemberSessionManager;
import com.siam.system.modular.mod_user.entity.Member;
import com.siam.system.util.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 可转债信息表业务实现层
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
@Service
public class ConvertibleBondServiceImpl extends ServiceImpl<ConvertibleBondMapper, ConvertibleBond> implements ConvertibleBondService {

    @Autowired
    private ConvertibleBondMapper convertibleBondMapper;

    @Autowired
    private StockApiService stockApiService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ConvertibleBondShorttermTradingService convertibleBondShorttermTradingService;

    @Autowired
    private ConvertibleBondFixedInvestmentPlanService convertibleBondFixedInvestmentPlanService;

    @Autowired
    private ConvertibleBondFixedInvestmentService convertibleBondFixedInvestmentService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private ConvertibleBondApiService convertibleBondApiService;

    @Autowired
    private MemberSessionManager memberSessionManager;

    @Override
    public Page getListByPage(int pageNo, int pageSize, ConvertibleBond convertibleBond) {
        Page<Map<String, Object>> page = convertibleBondMapper.getListByPage(new Page(pageNo, pageSize), convertibleBond);
        return page;
    }

    @Override
    public void syncConvertibleBondList() {
        //一次性查出所有数据，再在内存中进行比对
        Map<String, ConvertibleBond> filterMap = new HashMap<>();
        List<ConvertibleBond> dbList = this.list();
        dbList.forEach(convertibleBond -> {
            filterMap.put(convertibleBond.getCode(), convertibleBond);
        });

        int count = 1;
        List insertList = new ArrayList();
        List<ConvertibleBond> convertibleBondList = convertibleBondApiService.getConvertibleBondList();
        for (ConvertibleBond convertibleBond : convertibleBondList) {
            ConvertibleBond dbConvertibleBond = filterMap.get(convertibleBond.getCode());
            if(dbConvertibleBond == null){
                //新增基金信息
                ConvertibleBond insertConvertibleBond = new ConvertibleBond();
                BeanUtils.copyProperties(convertibleBond, insertConvertibleBond);
                insertConvertibleBond.setId(null);
                insertList.add(insertConvertibleBond);
                //达到5000条，进行数据插入
                if(count%5000==0 || convertibleBondList.indexOf(convertibleBond)==convertibleBondList.size()-1){
                    this.insertBatchSomeColumn(insertList);
                    count = 0;
                    insertList.clear();
                }
                count++;
            }else{
                //如果名称、债券评级、成交额不一致，则需要进行同步
                if(!convertibleBond.getName().equals(dbConvertibleBond.getName()) ||
                        !convertibleBond.getRatingCd().equals(dbConvertibleBond.getRatingCd()) ||
                        convertibleBond.getVolume().compareTo(dbConvertibleBond.getVolume())!=0){
                    ConvertibleBond updateConvertibleBond = new ConvertibleBond();
                    BeanUtils.copyProperties(convertibleBond, updateConvertibleBond);
                    updateConvertibleBond.setId(dbConvertibleBond.getId());
                    this.updateById(updateConvertibleBond);
                }
            }
        }
    }

    @Override
    public void syncPrice(Boolean isOptional) {
        long start = System.currentTimeMillis();
        List<String> syncPriceFailedConvertibleBondList = new ArrayList<>();
        List<ConvertibleBond> updateConvertibleBondList = new ArrayList();

        //new QueryWrapper<ConvertibleBond>().last("limit 5")
        QueryWrapper queryWrapper = new QueryWrapper<>();
        if(isOptional != null){
            queryWrapper.eq("is_optional", isOptional);
        }
        List<ConvertibleBond> convertibleBondList = this.list(queryWrapper);
        for (ConvertibleBond convertibleBond : convertibleBondList) {
            SinaStock sinaStock = stockApiService.getSingleInfo(convertibleBond.getCode(), convertibleBond.getName(), null);
            if (sinaStock == null){
                log.debug("\n\n》》》 syncPrice - " + convertibleBond.getCode() + " " + convertibleBond.getName() + " is not found!");
                syncPriceFailedConvertibleBondList.add(convertibleBond.getCode() + " " + convertibleBond.getName());
                continue;
            }

            //计算涨跌幅
            BigDecimal value = sinaStock.getPrice().subtract(sinaStock.getYesterdayPrice());
            BigDecimal lastestIncreaseRate = sinaStock.getYesterdayPrice().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : value.divide(sinaStock.getYesterdayPrice(), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);

            //组装需要修改的对象
            ConvertibleBond updateConvertibleBond = new ConvertibleBond();
            updateConvertibleBond.setId(convertibleBond.getId());
            updateConvertibleBond.setLastestPrice(sinaStock.getPrice());
            updateConvertibleBond.setIncreaseRt(lastestIncreaseRate);
            updateConvertibleBond.setLastestPriceDate(sinaStock.getDate());
            updateConvertibleBond.setUpdateTime(new Date());
            updateConvertibleBondList.add(updateConvertibleBond);
        }

        //同步失败的数据存入redis
        redisUtils.set("syncPriceFailedConvertibleBondList", JsonUtils.toJson(syncPriceFailedConvertibleBondList));

        //批量修改
        this.updateBatchById(updateConvertibleBondList);

        //该方法执行的时长存入redis
        redisUtils.set("syncConvertibleBondPriceCostTime", (System.currentTimeMillis() - start) + "ms");
    }

    @Override
    public void monitorPrice(Boolean isOptional) {
        String text = "";
        String responseMessage = "";
        QueryWrapper queryWrapper = new QueryWrapper<>();
        if(isOptional != null){
            queryWrapper.eq("is_optional", isOptional);
        }
        List<ConvertibleBond> convertibleBondList = this.list(queryWrapper);
        for (ConvertibleBond convertibleBond : convertibleBondList) {
            SinaStock sinaStock = stockApiService.getSingleInfo(convertibleBond.getCode(), convertibleBond.getName(), null);
            if (sinaStock == null){
                log.debug("\n\n》》》 monitorPrice - " + convertibleBond.getCode() + " " + convertibleBond.getName() + " is not found!");
                continue;
            }
            //股票价格涨跌幅达到设定比例(2~20%)则推送通知，如果当日已经推送过同样的信息，则无需再推送
            Double[] array = new Double[19];
            for (int i = 0; i < array.length; i++) {
                array[i] = new Double(i + 2);
            }
            BigDecimal value = sinaStock.getPrice().subtract(sinaStock.getYesterdayPrice());
            BigDecimal valueAbs = value.abs();
            BigDecimal rate = sinaStock.getYesterdayPrice().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : valueAbs.divide(sinaStock.getYesterdayPrice(), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
            for (int i = array.length-1; i >= 0; i--) {
                if(rate.compareTo(BigDecimal.valueOf(array[i])) >= 0){
                    //符合条件
                    String mark = value.compareTo(BigDecimal.ZERO)>=0 ? "涨幅" : "跌幅";
                    String str = convertibleBond.getName() + " " + mark + rate + "%" + "，价格为" + sinaStock.getPrice() +"元";
                    String tempStr = convertibleBond.getName() + " " + mark + array[i] + "%";
                    Map<String, Object> stockPriceNoticeMap = redisUtils.hasKey("stockPriceNoticeMap") ? (Map) JsonUtils.toObject(redisUtils.get("stockPriceNoticeMap").toString(), Map.class) : new HashMap();
                    if(stockPriceNoticeMap.containsKey(tempStr)){
                        break;
                    }

                    //把文本存入redis
                    stockPriceNoticeMap.put(tempStr, null);
                    redisUtils.set("stockPriceNoticeMap", JsonUtils.toJson(stockPriceNoticeMap), ToolUtil.getSecondsNextEarlyMorning());

                    text += str + "\n";
                    responseMessage += sinaStock.getResponseMessage() + "\n";
                    break;
                }
            }
        }

        //进行通知推送
        if(!text.isEmpty()){
            text = text.substring(0, text.length()-1);
            text = text + "\n\n\n" + responseMessage;
            emailService.sendMail(BusinessType.SYSTEM_EMAIL_ADDRESS, "cbp notice", text);
        }
    }

    @Override
    public void insert(ConvertibleBondParam param) {
        //添加基金信息
        param.setCreateTime(new Date());
        param.setUpdateTime(new Date());
        convertibleBondMapper.insert(param);
    }

    @Override
    public void update(ConvertibleBondParam param) {
        ConvertibleBond dbConvertibleBond = convertibleBondMapper.selectById(param.getId());
        if(dbConvertibleBond == null){
            throw new StoneCustomerException("该基金信息不存在，修改失败");
        }
        //修改基金信息
        param.setUpdateTime(new Date());
        convertibleBondMapper.updateById(param);
    }

    @Override
    public void delete(ConvertibleBondParam param) {
        ConvertibleBond dbConvertibleBond = convertibleBondMapper.selectById(param.getId());
        if(dbConvertibleBond == null){
            throw new StoneCustomerException("该基金信息不存在，删除失败");
        }
        //删除基金信息
        param.setUpdateTime(new Date());
        convertibleBondMapper.deleteById(param.getId());
    }

    @Override
    public Map<String, Object> statistics(ConvertibleBondParam param) {
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        param.setMemberId(loginMember.getId());

        //查询基础数据设置
        Setting dbSetting = settingService.selectCurrent();

        //1、查询基金-短期仓配置情况
        BigDecimal shortTermAmount = dbSetting.getConvertibleBondShortTermAmount();
        BigDecimal usedShortTermAmount = BigDecimal.ZERO;
        BigDecimal availableShortTermAmount, availableShortTermRatio;
        Map<Integer, BigDecimal> filterMap = new HashMap();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("type", ConvertibleBondShorttermTrading.TYPE_BUY);
        queryWrapper.eq("status", ConvertibleBondShorttermTrading.STATUS_IN_PROGRESS);
        queryWrapper.eq("member_id", loginMember.getId());
        List<ConvertibleBondShorttermTrading> tradingList = convertibleBondShorttermTradingService.list(queryWrapper);
        for (ConvertibleBondShorttermTrading trading : tradingList) {
            int key = trading.getConvertibleBondId();
            if(filterMap.containsKey(key)){
                BigDecimal amount = filterMap.get(key);
                filterMap.put(key, amount.add(trading.getAmount()));
            }else{
                filterMap.put(key, trading.getAmount());
            }
            usedShortTermAmount = usedShortTermAmount.add(trading.getAmount());
        }
        //2、查询基金-长期仓配置情况【定投】
        queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", ConvertibleBondFixedInvestment.STATUS_IN_PROGRESS);
        queryWrapper.eq("member_id", loginMember.getId());
        List<ConvertibleBondFixedInvestment> investmentTempList = convertibleBondFixedInvestmentService.list(queryWrapper);
        for (ConvertibleBondFixedInvestment investment : investmentTempList) {
            ConvertibleBondFixedInvestmentPlan investmentPlan = convertibleBondFixedInvestmentPlanService.getById(investment.getPlanId());
            if(StringUtils.isNotBlank(investmentPlan.getRemarks()) && (investmentPlan.getRemarks().contains("【短线仓】"))){
                int key = investment.getConvertibleBondId();
                if(filterMap.containsKey(key)){
                    BigDecimal amount = filterMap.get(key);
                    filterMap.put(key, amount.add(investment.getAmount()));
                }else{
                    filterMap.put(key, investment.getAmount());
                }
                usedShortTermAmount = usedShortTermAmount.add(investment.getAmount());
            }
        }
        //封装返回结果
        List<ConvertibleBondStatisticsVo> shortTermList = new ArrayList<>();
        for (Integer key : filterMap.keySet()) {
            ConvertibleBond dbConvertibleBond = convertibleBondMapper.selectById(key);
            ConvertibleBondStatisticsVo statisticsVo = new ConvertibleBondStatisticsVo();
            statisticsVo.setConvertibleBondId(dbConvertibleBond.getId());
            statisticsVo.setConvertibleBondName(dbConvertibleBond.getName());
            statisticsVo.setAmount(filterMap.get(key));
            statisticsVo.setRatio(shortTermAmount.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : filterMap.get(key).divide(shortTermAmount, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
            shortTermList.add(statisticsVo);
        }
        //按照占比金额升序排列
        Collections.sort(shortTermList, (o1, o2) -> {
            return o1.getAmount().compareTo(o2.getAmount());
        });
        //计算可用资金
        availableShortTermAmount = shortTermAmount.subtract(usedShortTermAmount);
        availableShortTermRatio = shortTermAmount.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : availableShortTermAmount.divide(shortTermAmount, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
        //添加空闲资金项
        ConvertibleBondStatisticsVo statisticsVoByAvailableShortTerm = new ConvertibleBondStatisticsVo();
        statisticsVoByAvailableShortTerm.setConvertibleBondId(null);
        statisticsVoByAvailableShortTerm.setConvertibleBondName("空闲资金");
        statisticsVoByAvailableShortTerm.setAmount(availableShortTermAmount);
        statisticsVoByAvailableShortTerm.setRatio(availableShortTermRatio);
        shortTermList.add(statisticsVoByAvailableShortTerm);

        //2、查询基金-长期仓配置情况
        BigDecimal longTermAmount = dbSetting.getConvertibleBondLongTermAmount();
        BigDecimal usedLongTermAmount = BigDecimal.ZERO;
        BigDecimal availableLongTermAmount, availableLongTermRatio;
        filterMap = new HashMap();
        queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", ConvertibleBondFixedInvestment.STATUS_IN_PROGRESS);
        queryWrapper.eq("member_id", loginMember.getId());
        List<ConvertibleBondFixedInvestment> investmentList = convertibleBondFixedInvestmentService.list(queryWrapper);
        for (ConvertibleBondFixedInvestment investment : investmentList) {
            ConvertibleBondFixedInvestmentPlan investmentPlan = convertibleBondFixedInvestmentPlanService.getById(investment.getPlanId());
            if(StringUtils.isNotBlank(investmentPlan.getRemarks()) && (investmentPlan.getRemarks().contains("【初始阶段】") || investmentPlan.getRemarks().contains("【零散资金】") || investmentPlan.getRemarks().contains("【短线仓】"))){
                continue;
            }
            int key = investment.getConvertibleBondId();
            if(filterMap.containsKey(key)){
                BigDecimal amount = filterMap.get(key);
                filterMap.put(key, amount.add(investment.getAmount()));
            }else{
                filterMap.put(key, investment.getAmount());
            }
            usedLongTermAmount = usedLongTermAmount.add(investment.getAmount());
        }
        //封装返回结果
        List<ConvertibleBondStatisticsVo> longTermList = new ArrayList<>();
        for (Integer key : filterMap.keySet()) {
            ConvertibleBond dbConvertibleBond = convertibleBondMapper.selectById(key);
            ConvertibleBondStatisticsVo statisticsVo = new ConvertibleBondStatisticsVo();
            statisticsVo.setConvertibleBondId(dbConvertibleBond.getId());
            statisticsVo.setConvertibleBondName(dbConvertibleBond.getName());
            statisticsVo.setAmount(filterMap.get(key));
            statisticsVo.setRatio(longTermAmount.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : filterMap.get(key).divide(longTermAmount, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
            longTermList.add(statisticsVo);
        }
        //按照占比金额升序排列
        Collections.sort(longTermList, (o1, o2) -> {
            return o1.getAmount().compareTo(o2.getAmount());
        });
        //计算可用资金
        availableLongTermAmount = longTermAmount.subtract(usedLongTermAmount);
        availableLongTermRatio = longTermAmount.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : availableLongTermAmount.divide(longTermAmount, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
        //添加空闲资金项
        ConvertibleBondStatisticsVo statisticsVoByAvailableLongTerm = new ConvertibleBondStatisticsVo();
        statisticsVoByAvailableLongTerm.setConvertibleBondId(null);
        statisticsVoByAvailableLongTerm.setConvertibleBondName("空闲资金");
        statisticsVoByAvailableLongTerm.setAmount(availableLongTermAmount);
        statisticsVoByAvailableLongTerm.setRatio(availableLongTermRatio);
        longTermList.add(statisticsVoByAvailableLongTerm);

        Map<String, Object> resultMap = new HashMap();
        resultMap.put("shortTermList", shortTermList);
        resultMap.put("shortTermAmount", shortTermAmount);
        resultMap.put("usedShortTermAmount", usedShortTermAmount);
        resultMap.put("usedShortTermRatio", shortTermAmount.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : usedShortTermAmount.divide(shortTermAmount, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
        resultMap.put("availableShortTermAmount", availableShortTermAmount);
        resultMap.put("availableShortTermRatio", availableShortTermRatio);
        resultMap.put("longTermList", longTermList);
        resultMap.put("longTermAmount", longTermAmount);
        resultMap.put("usedLongTermAmount", usedLongTermAmount);
        resultMap.put("usedLongTermRatio", longTermAmount.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : usedLongTermAmount.divide(longTermAmount, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
        resultMap.put("availableLongTermAmount", availableLongTermAmount);
        resultMap.put("availableLongTermRatio", availableLongTermRatio);
        return resultMap;
    }

    @Override
    public Integer insertBatchSomeColumn(Collection<ConvertibleBond> entityList) {
        if(entityList==null || entityList.isEmpty()){
            return null;
        }
        return this.baseMapper.insertBatchSomeColumn(entityList);
    }
}