package com.siam.system.modular.mod_finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.siam.system.modular.mod_finance.entity.StockShorttermTrading;
import com.siam.system.modular.mod_finance.model.param.StockShorttermTradingParam;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 股票短期交易/做T交易记录表(一个做T买入记录可能对应多个做T卖出操作)业务层
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
public interface StockShorttermTradingService extends IService<StockShorttermTrading> {

    Page<StockShorttermTrading> getListByPage(int pageNo, int pageSize, StockShorttermTrading stockShorttermTrading);

    Page<Map<String, Object>> getListByPageJoinStock(int pageNo, int pageSize, StockShorttermTradingParam param);

    BigDecimal selectSumSellSharesByRelatedTradingId(Integer relatedTradingId);

    BigDecimal selectSumSellAmountByRelatedTradingId(Integer relatedTradingId);

    /**
     * 新增接口
     *
     * @author JiangP
     */
    void insert(StockShorttermTradingParam param);

    /**
     * 修改接口
     *
     * @author JiangP
     */
    void update(StockShorttermTradingParam param);

    /**
     * 删除接口
     *
     * @author JiangP
     */
    void delete(StockShorttermTradingParam param);
}