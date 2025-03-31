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
import com.siam.system.modular.mod_finance.api.StockApiService;
import com.siam.system.modular.mod_finance.entity.*;
import com.siam.system.modular.mod_finance.mapper.StockMapper;
import com.siam.system.modular.mod_finance.model.param.StockParam;
import com.siam.system.modular.mod_finance.model.vo.StockStatisticsVo;
import com.siam.system.modular.mod_finance.service.*;
import com.siam.system.modular.mod_finance.vo.SinaStock;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 股票信息表业务实现层
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private StockApiService stockApiService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private StockShorttermTradingService stockShorttermTradingService;

    @Autowired
    private StockFixedInvestmentPlanService stockFixedInvestmentPlanService;

    @Autowired
    private StockFixedInvestmentService stockFixedInvestmentService;

    @Autowired
    private SettingService settingService;

    @Override
    public Page getListByPage(int pageNo, int pageSize, Stock stock) {
        Page<Map<String, Object>> page = stockMapper.getListByPage(new Page(pageNo, pageSize), stock);
        return page;
    }

    @Override
    public void syncStockList() {
        //一次性查出所有数据，再在内存中进行比对
        Map<String, Stock> filterMap = new HashMap<>();
        List<Stock> dbList = this.list();
        dbList.forEach(stock -> {
            filterMap.put(stock.getCode(), stock);
        });

        int count = 1;
        List insertList = new ArrayList();
        List<Stock> stockList = stockApiService.getStockList();
        for (Stock stock : stockList) {
            Stock dbStock = filterMap.get(stock.getCode());
            if(dbStock == null){
                //新增股票信息
                Stock insertStock = new Stock();
                BeanUtils.copyProperties(stock, insertStock);
                insertStock.setId(null);
                insertList.add(insertStock);
                //达到5000条，进行数据插入
                if(count%5000==0 || stockList.indexOf(stock)==stockList.size()-1){
                    this.insertBatchSomeColumn(insertList);
                    count = 0;
                    insertList.clear();
                }
                count++;
            }else{
                //如果名称、市场简写、上市状态不一致，则需要进行同步
                if(!stock.getName().equals(dbStock.getName()) ||
                        !stock.getMarket().equals(dbStock.getMarket()) ||
                        !stock.getState().equals(dbStock.getState())){
                    Stock updateStock = new Stock();
                    BeanUtils.copyProperties(stock, updateStock);
                    updateStock.setId(dbStock.getId());
                    //如果同步过来的备注不为空，则附加在原备注的后面
                    if(StringUtils.isNotBlank(stock.getRemarks())){
                        updateStock.setRemarks(dbStock.getRemarks() + "\n" + stock.getRemarks());
                    }
                    this.updateById(updateStock);
                }
            }
        }
    }

    @Override
    public void syncPrice(Boolean isOptional) {
        long start = System.currentTimeMillis();
        List<String> syncPriceFailedStockList = new ArrayList<>();
        List<Stock> updateStockList = new ArrayList();

        //new QueryWrapper<Stock>().last("limit 5")
        QueryWrapper queryWrapper = new QueryWrapper<>();
        if(isOptional != null){
            queryWrapper.eq("is_optional", isOptional);
        }
        List<Stock> stockList = this.list(queryWrapper);
        for (Stock stock : stockList) {
            SinaStock sinaStock = stockApiService.getSingleInfo(stock.getCode(), stock.getName(), stock.getMarket());
            if (sinaStock == null){
                log.debug("\n\n》》》 syncPrice - " + stock.getCode() + " " + stock.getName() + " is not found!");
                syncPriceFailedStockList.add(stock.getCode() + " " + stock.getName());
                continue;
            }

            //计算涨跌幅
            BigDecimal value = sinaStock.getPrice().subtract(sinaStock.getYesterdayPrice());
            BigDecimal lastestIncreaseRate = sinaStock.getYesterdayPrice().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : value.divide(sinaStock.getYesterdayPrice(), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);

            //组装需要修改的对象
            Stock updateStock = new Stock();
            updateStock.setId(stock.getId());
            updateStock.setLastestPrice(sinaStock.getPrice());
            updateStock.setLastestIncreaseRate(lastestIncreaseRate);
            updateStock.setLastestPriceDate(sinaStock.getDate());
            updateStock.setUpdateTime(new Date());
            updateStockList.add(updateStock);
        }

        //同步失败的数据存入redis
        redisUtils.set("syncPriceFailedStockList", JsonUtils.toJson(syncPriceFailedStockList));

        //批量修改
        this.updateBatchById(updateStockList);

        //该方法执行的时长存入redis
        redisUtils.set("syncStockPriceCostTime", (System.currentTimeMillis() - start) + "ms");
    }

    @Override
    public void monitorPrice(Boolean isOptional) {
        String text = "";
        String responseMessage = "";
        QueryWrapper queryWrapper = new QueryWrapper<>();
        if(isOptional != null){
            queryWrapper.eq("is_optional", isOptional);
        }
        List<Stock> stockList = this.list(queryWrapper);
        for (Stock stock : stockList) {
            SinaStock sinaStock = stockApiService.getSingleInfo(stock.getCode(), stock.getName(), stock.getMarket());
            if (sinaStock == null){
                log.debug("\n\n》》》 monitorPrice - " + stock.getCode() + " " + stock.getName() + " is not found!");
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
                    String str = stock.getName() + " " + mark + rate + "%" + "，价格为" + sinaStock.getPrice() +"元";
                    String tempStr = stock.getName() + " " + mark + array[i] + "%";
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
            emailService.sendMail(BusinessType.SYSTEM_EMAIL_ADDRESS, "sp notice", text);
        }
    }

    @Override
    public void insert(StockParam param) {
        //添加基金信息
        param.setCreateTime(new Date());
        param.setUpdateTime(new Date());
        stockMapper.insert(param);
    }

    @Override
    public void update(StockParam param) {
        Stock dbStock = stockMapper.selectById(param.getId());
        if(dbStock == null){
            throw new StoneCustomerException("该基金信息不存在，修改失败");
        }
        //修改基金信息
        param.setUpdateTime(new Date());
        stockMapper.updateById(param);
    }

    @Override
    public void delete(StockParam param) {
        Stock dbStock = stockMapper.selectById(param.getId());
        if(dbStock == null){
            throw new StoneCustomerException("该基金信息不存在，删除失败");
        }
        //删除基金信息
        param.setUpdateTime(new Date());
        stockMapper.deleteById(param.getId());
    }

    @Override
    public Map<String, Object> statistics(StockParam param) {
        //查询基础数据设置
        Setting dbSetting = settingService.selectCurrent();

        //1、查询基金-短期仓配置情况
        BigDecimal shortTermAmount = dbSetting.getStockShortTermAmount();
        BigDecimal usedShortTermAmount = BigDecimal.ZERO;
        BigDecimal availableShortTermAmount, availableShortTermRatio;
        Map<Integer, BigDecimal> filterMap = new HashMap();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("type", StockShorttermTrading.TYPE_BUY);
        queryWrapper.eq("status", StockShorttermTrading.STATUS_IN_PROGRESS);
        List<StockShorttermTrading> tradingList = stockShorttermTradingService.list(queryWrapper);
        for (StockShorttermTrading trading : tradingList) {
            int key = trading.getStockId();
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
        queryWrapper.eq("status", StockFixedInvestment.STATUS_IN_PROGRESS);
        List<StockFixedInvestment> investmentTempList = stockFixedInvestmentService.list(queryWrapper);
        for (StockFixedInvestment investment : investmentTempList) {
            StockFixedInvestmentPlan investmentPlan = stockFixedInvestmentPlanService.getById(investment.getPlanId());
            if(StringUtils.isNotBlank(investmentPlan.getRemarks()) && (investmentPlan.getRemarks().contains("【短线仓】"))){
                int key = investment.getStockId();
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
        List<StockStatisticsVo> shortTermList = new ArrayList<>();
        for (Integer key : filterMap.keySet()) {
            Stock dbStock = stockMapper.selectById(key);
            StockStatisticsVo statisticsVo = new StockStatisticsVo();
            statisticsVo.setStockId(dbStock.getId());
            statisticsVo.setStockName(dbStock.getName());
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
        StockStatisticsVo statisticsVoByAvailableShortTerm = new StockStatisticsVo();
        statisticsVoByAvailableShortTerm.setStockId(null);
        statisticsVoByAvailableShortTerm.setStockName("空闲资金");
        statisticsVoByAvailableShortTerm.setAmount(availableShortTermAmount);
        statisticsVoByAvailableShortTerm.setRatio(availableShortTermRatio);
        shortTermList.add(statisticsVoByAvailableShortTerm);

        //2、查询基金-长期仓配置情况
        BigDecimal longTermAmount = dbSetting.getStockLongTermAmount();
        BigDecimal usedLongTermAmount = BigDecimal.ZERO;
        BigDecimal availableLongTermAmount, availableLongTermRatio;
        filterMap = new HashMap();
        queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", StockFixedInvestment.STATUS_IN_PROGRESS);
        List<StockFixedInvestment> investmentList = stockFixedInvestmentService.list(queryWrapper);
        for (StockFixedInvestment investment : investmentList) {
            StockFixedInvestmentPlan investmentPlan = stockFixedInvestmentPlanService.getById(investment.getPlanId());
            if(StringUtils.isNotBlank(investmentPlan.getRemarks()) && (investmentPlan.getRemarks().contains("【初始阶段】") || investmentPlan.getRemarks().contains("【零散资金】") || investmentPlan.getRemarks().contains("【短线仓】"))){
                continue;
            }
            int key = investment.getStockId();
            if(filterMap.containsKey(key)){
                BigDecimal amount = filterMap.get(key);
                filterMap.put(key, amount.add(investment.getAmount()));
            }else{
                filterMap.put(key, investment.getAmount());
            }
            usedLongTermAmount = usedLongTermAmount.add(investment.getAmount());
        }
        //封装返回结果
        List<StockStatisticsVo> longTermList = new ArrayList<>();
        for (Integer key : filterMap.keySet()) {
            Stock dbStock = stockMapper.selectById(key);
            StockStatisticsVo statisticsVo = new StockStatisticsVo();
            statisticsVo.setStockId(dbStock.getId());
            statisticsVo.setStockName(dbStock.getName());
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
        StockStatisticsVo statisticsVoByAvailableLongTerm = new StockStatisticsVo();
        statisticsVoByAvailableLongTerm.setStockId(null);
        statisticsVoByAvailableLongTerm.setStockName("空闲资金");
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
    public Integer insertBatchSomeColumn(Collection<Stock> entityList) {
        if(entityList==null || entityList.isEmpty()){
            return null;
        }
        return this.baseMapper.insertBatchSomeColumn(entityList);
    }

    @Override
    public Integer updateBatchSomeColumn(List<Stock> entityList) {
        List<Integer> idList = new ArrayList<>();
        Map entityMap = new HashMap();
        return this.baseMapper.updateBatchSomeColumn(idList, entityMap);
    }
}