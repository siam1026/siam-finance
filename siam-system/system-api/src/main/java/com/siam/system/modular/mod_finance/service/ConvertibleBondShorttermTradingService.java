package com.siam.system.modular.mod_finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.siam.system.modular.mod_finance.entity.ConvertibleBondShorttermTrading;
import com.siam.system.modular.mod_finance.model.param.ConvertibleBondShorttermTradingParam;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 可转债短期交易/做T交易记录表(一个做T买入记录可能对应多个做T卖出操作)业务层
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
public interface ConvertibleBondShorttermTradingService extends IService<ConvertibleBondShorttermTrading> {

    Page<ConvertibleBondShorttermTrading> getListByPage(int pageNo, int pageSize, ConvertibleBondShorttermTrading fundShorttermTrading);

    Page<Map<String, Object>> getListByPageJoinConvertibleBond(int pageNo, int pageSize, ConvertibleBondShorttermTradingParam param);

    BigDecimal selectSumSellSharesByRelatedTradingId(Integer relatedTradingId);

    BigDecimal selectSumSellAmountByRelatedTradingId(Integer relatedTradingId);

    /**
     * 新增接口
     *
     * @author JiangP
     */
    void insert(ConvertibleBondShorttermTradingParam param);

    /**
     * 修改接口
     *
     * @author JiangP
     */
    void update(ConvertibleBondShorttermTradingParam param);

    /**
     * 删除接口
     *
     * @author JiangP
     */
    void delete(ConvertibleBondShorttermTradingParam param);
}