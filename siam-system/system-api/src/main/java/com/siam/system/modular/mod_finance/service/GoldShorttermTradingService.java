package com.siam.system.modular.mod_finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.system.modular.mod_finance.entity.GoldShorttermTrading;
import com.siam.system.modular.mod_finance.model.example.GoldShorttermTradingExample;
import com.siam.system.modular.mod_finance.model.result.GoldShorttermTradingResult;

import java.math.BigDecimal;
import java.util.List;

public interface GoldShorttermTradingService {
    int countByExample(GoldShorttermTradingExample example);

    void deleteByPrimaryKey(Integer id);

    void insertSelective(GoldShorttermTrading record);

    List<GoldShorttermTrading> selectByExample(GoldShorttermTradingExample example);

    GoldShorttermTrading selectByPrimaryKey(Integer id);

    void updateByExampleSelective(GoldShorttermTrading record, GoldShorttermTradingExample example);

    void updateByPrimaryKeySelective(GoldShorttermTrading record);

    Page<GoldShorttermTradingResult> getListByPage(int pageNo, int pageSize, GoldShorttermTrading goldShorttermTrading);

    BigDecimal selectSumGramsByRelatedTradingId(Integer relatedTradingId);

    BigDecimal selectSumAmountByRelatedTradingId(Integer relatedTradingId);

    BigDecimal selectSumSellSharesByRelatedTradingId(Integer relatedTradingId);
}