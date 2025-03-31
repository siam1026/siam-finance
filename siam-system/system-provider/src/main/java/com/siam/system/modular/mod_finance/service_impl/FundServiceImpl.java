package com.siam.system.modular.mod_finance.service_impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siam.package_common.constant.BaseCode;
import com.siam.package_common.constant.BusinessType;
import com.siam.package_common.exception.StoneCustomerException;
import com.siam.package_common.service.EmailService;
import com.siam.package_common.util.JsonUtils;
import com.siam.package_common.util.RedisUtils;
import com.siam.package_common.util.ToolUtil;
import com.siam.system.modular.mod_finance.api.FundApiService;
import com.siam.system.modular.mod_finance.vo.TTFund;
import com.siam.system.modular.mod_finance.entity.*;
import com.siam.system.modular.mod_finance.model.example.FundExample;
import com.siam.system.modular.mod_finance.mapper.FundMapper;
import com.siam.system.modular.mod_finance.model.param.FundParam;
import com.siam.system.modular.mod_finance.model.vo.FundStatisticsVo;
import com.siam.system.modular.mod_finance.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class FundServiceImpl extends ServiceImpl<FundMapper, Fund> implements FundService {

    @Autowired
    private FundMapper fundMapper;

    @Autowired
    private FundApiService fundApiService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private FundShorttermTradingService fundShorttermTradingService;

    @Autowired
    private FundFixedInvestmentPlanService fundFixedInvestmentPlanService;

    @Autowired
    private FundFixedInvestmentService fundFixedInvestmentService;

    @Autowired
    private SettingService settingService;

    public int countByExample(FundExample example){
        return fundMapper.countByExample(example);
    }

    public void deleteByPrimaryKey(Integer id){
        fundMapper.deleteByPrimaryKey(id);
    }

    public List<Fund> selectByExample(FundExample example){
        return fundMapper.selectByExample(example);
    }

    public Fund selectByPrimaryKey(Integer id){
        return fundMapper.selectByPrimaryKey(id);
    }

    public void updateByExampleSelective(Fund record, FundExample example){
        fundMapper.updateByExampleSelective(record, example);
    }

    public void updateByPrimaryKeySelective(Fund record){
        fundMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Page getListByPage(int pageNo, int pageSize, Fund fund) {
        Page<Map<String, Object>> page = fundMapper.getListByPage(new Page(pageNo, pageSize), fund);
        return page;
    }

    @Override
    public void syncFundList() {
        //一次性查出所有数据，再在内存中进行比对
        Map<String, Fund> filterMap = new HashMap<>();
        List<Fund> dbList = this.list();
        dbList.forEach(fund -> {
            filterMap.put(fund.getCode(), fund);
        });

        int count = 1;
        List insertList = new ArrayList();
        List<Fund> fundList = fundApiService.getFundList();
        for (Fund fund : fundList) {
            Fund dbFund = filterMap.get(fund.getCode());
            if(dbFund == null){
                //新增基金信息
                Fund insertFund = new Fund();
                BeanUtils.copyProperties(fund, insertFund);
                insertFund.setId(null);
                insertList.add(insertFund);
                //达到5000条，进行数据插入
                if(count%5000==0 || fundList.indexOf(fund)==fundList.size()-1){
                    this.insertBatchSomeColumn(insertList);
                    count = 0;
                    insertList.clear();
                }
                count++;
            }else{
                //如果名称不一致，则需要进行同步
                if(!fund.getName().equals(dbFund.getName())){
                    Fund updateFund = new Fund();
                    BeanUtils.copyProperties(fund, updateFund);
                    updateFund.setId(dbFund.getId());
                    this.updateById(updateFund);
                }
            }
        }
    }

    @Override
    public void syncNetValue(Boolean isOptional) {
        long start = System.currentTimeMillis();
        List<String> syncPriceFailedFundList = new ArrayList<>();
        List<Fund> updateFundList = new ArrayList();

        //new QueryWrapper<Fund>().last("limit 5")
        QueryWrapper queryWrapper = new QueryWrapper<>();
        if(isOptional != null){
            queryWrapper.eq("is_optional", isOptional);
        }
        List<Fund> fundList = this.list(queryWrapper);
        for (Fund fund : fundList) {
            TTFund ttFund = fundApiService.getSingleInfo(fund.getCode());
            if (ttFund == null){
                log.debug("\n\n》》》 syncPrice - " + fund.getCode() + " " + fund.getName() + " is not found!");
                syncPriceFailedFundList.add(fund.getCode() + " " + fund.getName());
                continue;
            }
            //组装需要修改的对象
            Fund updateFund = new Fund();
            updateFund.setId(fund.getId());
            updateFund.setLastestNetValue(ttFund.getGsz());
            updateFund.setLastestIncreaseRate(ttFund.getGszzl());
            updateFund.setLastestNetValueDate(ttFund.getGztimeDate());
            updateFund.setUpdateTime(new Date());
            updateFundList.add(updateFund);
        }

        //同步失败的数据存入redis
        redisUtils.set("syncPriceFailedFundList", JsonUtils.toJson(syncPriceFailedFundList));

        //批量修改
        this.updateBatchById(updateFundList);

        //该方法执行的时长存入redis
        redisUtils.set("syncFundPriceCostTime", (System.currentTimeMillis() - start) + "ms");
    }

    @Override
    public void insert(FundParam param) {
        //添加基金信息
        param.setCreateTime(new Date());
        param.setUpdateTime(new Date());
        fundMapper.insert(param);
    }

    @Override
    public void update(FundParam param) {
        Fund dbFund = fundMapper.selectByPrimaryKey(param.getId());
        if(dbFund == null){
            throw new StoneCustomerException("该基金信息不存在，修改失败");
        }
        //修改基金信息
        param.setUpdateTime(new Date());
        fundMapper.updateById(param);
    }

    @Override
    public void delete(FundParam param) {
        Fund dbFund = fundMapper.selectByPrimaryKey(param.getId());
        if(dbFund == null){
            throw new StoneCustomerException("该基金信息不存在，删除失败");
        }
        //删除基金信息
        param.setUpdateTime(new Date());
        fundMapper.deleteByPrimaryKey(param.getId());
    }

    @Override
    public Map<String, Object> statistics(FundParam param) {
        //查询基础数据设置
        Setting dbSetting = settingService.selectCurrent();

        //1、查询基金-短期仓配置情况
        BigDecimal shortTermAmount = dbSetting.getFundShortTermAmount();
        BigDecimal usedShortTermAmount = BigDecimal.ZERO;
        BigDecimal availableShortTermAmount, availableShortTermRatio;
        Map<Integer, BigDecimal> filterMap = new HashMap();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("type", FundShorttermTrading.TYPE_BUY);
        queryWrapper.eq("status", FundShorttermTrading.STATUS_IN_PROGRESS);
        List<FundShorttermTrading> tradingList = fundShorttermTradingService.list(queryWrapper);
        for (FundShorttermTrading trading : tradingList) {
            int key = trading.getFundId();
            if(filterMap.containsKey(key)){
                BigDecimal amount = filterMap.get(key);
                filterMap.put(key, amount.add(trading.getAmount()));
            }else{
                filterMap.put(key, trading.getAmount());
            }
            usedShortTermAmount = usedShortTermAmount.add(trading.getAmount());
        }
        //2、查询基金-短期仓配置情况【定投】
        queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", FundFixedInvestment.STATUS_IN_PROGRESS);
        List<FundFixedInvestment> investmentTempList = fundFixedInvestmentService.list(queryWrapper);
        for (FundFixedInvestment investment : investmentTempList) {
            FundFixedInvestmentPlan investmentPlan = fundFixedInvestmentPlanService.getById(investment.getPlanId());
            if(StringUtils.isNotBlank(investmentPlan.getRemarks()) && (investmentPlan.getRemarks().contains("【短线仓】"))){
                int key = investment.getFundId();
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
        List<FundStatisticsVo> shortTermList = new ArrayList<>();
        for (Integer key : filterMap.keySet()) {
            Fund dbFund = fundMapper.selectById(key);
            FundStatisticsVo statisticsVo = new FundStatisticsVo();
            statisticsVo.setFundId(dbFund.getId());
            statisticsVo.setFundName(dbFund.getName());
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
        FundStatisticsVo statisticsVoByAvailableShortTerm = new FundStatisticsVo();
        statisticsVoByAvailableShortTerm.setFundId(null);
        statisticsVoByAvailableShortTerm.setFundName("空闲资金");
        statisticsVoByAvailableShortTerm.setAmount(availableShortTermAmount);
        statisticsVoByAvailableShortTerm.setRatio(availableShortTermRatio);
        shortTermList.add(statisticsVoByAvailableShortTerm);

        //2、查询基金-长期仓配置情况
        BigDecimal longTermAmount = dbSetting.getFundLongTermAmount();
        BigDecimal usedLongTermAmount = BigDecimal.ZERO;
        BigDecimal availableLongTermAmount, availableLongTermRatio;
        filterMap = new HashMap();
        queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", FundFixedInvestment.STATUS_IN_PROGRESS);
        List<FundFixedInvestment> investmentList = fundFixedInvestmentService.list(queryWrapper);
        for (FundFixedInvestment investment : investmentList) {
            FundFixedInvestmentPlan investmentPlan = fundFixedInvestmentPlanService.getById(investment.getPlanId());
            if(StringUtils.isNotBlank(investmentPlan.getRemarks()) && (investmentPlan.getRemarks().contains("【初始阶段】") || investmentPlan.getRemarks().contains("【零散资金】") || investmentPlan.getRemarks().contains("【短线仓】"))){
                continue;
            }
            int key = investment.getFundId();
            if(filterMap.containsKey(key)){
                BigDecimal amount = filterMap.get(key);
                filterMap.put(key, amount.add(investment.getAmount()));
            }else{
                filterMap.put(key, investment.getAmount());
            }
            usedLongTermAmount = usedLongTermAmount.add(investment.getAmount());
        }
        //封装返回结果
        List<FundStatisticsVo> longTermList = new ArrayList<>();
        for (Integer key : filterMap.keySet()) {
            Fund dbFund = fundMapper.selectById(key);
            FundStatisticsVo statisticsVo = new FundStatisticsVo();
            statisticsVo.setFundId(dbFund.getId());
            statisticsVo.setFundName(dbFund.getName());
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
        FundStatisticsVo statisticsVoByAvailableLongTerm = new FundStatisticsVo();
        statisticsVoByAvailableLongTerm.setFundId(null);
        statisticsVoByAvailableLongTerm.setFundName("空闲资金");
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
    public Integer insertBatchSomeColumn(Collection<Fund> entityList) {
        if(entityList==null || entityList.isEmpty()){
            return null;
        }
        return this.baseMapper.insertBatchSomeColumn(entityList);
    }
}