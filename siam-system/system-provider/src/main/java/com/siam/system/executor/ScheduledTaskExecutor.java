package com.siam.system.executor;

import com.siam.system.modular.mod_finance.service.ConvertibleBondService;
import com.siam.system.modular.mod_finance.service.FundService;
import com.siam.system.modular.mod_finance.service.GoldService;
import com.siam.system.modular.mod_finance.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 定时任务执行器
 */
@Slf4j
@Component
@Transactional(rollbackFor = Exception.class)
public class ScheduledTaskExecutor {

    @Autowired
    private FundService fundService;

    @Autowired
    private StockService stockService;

    @Autowired
    private ConvertibleBondService convertibleBondService;

    @Autowired
    private GoldService goldService;

    /**
     * 周1~周5 11:30 更新基金净值/股票价格/黄金价格
     */
    @Scheduled(cron = "0 30 11 ? * MON,TUE,WED,THU,FRI")
    public void task1(){
        fundService.syncNetValue(true);
        stockService.syncPrice(true);
        convertibleBondService.syncPrice(true);
        goldService.syncPrice();
    }

    /**
     * 周1~周5 14:46 更新基金净值/股票价格/黄金价格
     */
    @Scheduled(cron = "0 46 14 ? * MON,TUE,WED,THU,FRI")
    public void task2(){
        fundService.syncNetValue(true);
        stockService.syncPrice(true);
        convertibleBondService.syncPrice(true);
        goldService.syncPrice();
    }

    /**
     * 周1~周5 21:30 更新基金净值/股票价格/黄金价格
     */
    @Scheduled(cron = "0 30 21 ? * MON,TUE,WED,THU,FRI")
    public void task3(){
        fundService.syncNetValue(null);
        stockService.syncPrice(null);
        convertibleBondService.syncPrice(null);
        goldService.syncPrice();
    }

    /**
     * 周1~周5 每1分钟执行一次 监控股价(依靠程序进行盯盘)，股票价格涨跌幅达到设定比例则推送通知
     */
    @Scheduled(cron = "0 */1 9-15 * * MON,TUE,WED,THU,FRI")
    public void task4(){
        stockService.monitorPrice(true);
        convertibleBondService.monitorPrice(true);
    }
}