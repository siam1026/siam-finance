package com.siam.system.modular.mod_finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.siam.system.modular.mod_finance.entity.FundShorttermTrading;
import com.siam.system.modular.mod_finance.model.example.FundShorttermTradingExample;
import com.siam.system.modular.mod_finance.model.param.FundShorttermTradingParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface FundShorttermTradingService extends IService<FundShorttermTrading> {
    int countByExample(FundShorttermTradingExample example);

    void deleteByPrimaryKey(Integer id);

    void insertSelective(FundShorttermTrading record);

    List<FundShorttermTrading> selectByExample(FundShorttermTradingExample example);

    FundShorttermTrading selectByPrimaryKey(Integer id);

    void updateByExampleSelective(FundShorttermTrading record, FundShorttermTradingExample example);

    void updateByPrimaryKeySelective(FundShorttermTrading record);

    Page<FundShorttermTrading> getListByPage(int pageNo, int pageSize, FundShorttermTrading fundShorttermTrading);

    Page<Map<String, Object>> getListByPageJoinFund(int pageNo, int pageSize, FundShorttermTradingParam param);

    BigDecimal selectSumSellSharesByRelatedTradingId(Integer relatedTradingId);

    BigDecimal selectSumSellAmountByRelatedTradingId(Integer relatedTradingId);

    /**
     * 新增接口
     *
     * @author JiangP
     */
    void insert(FundShorttermTradingParam param);

    /**
     * 修改接口
     *
     * @author JiangP
     */
    void update(FundShorttermTradingParam param);

    /**
     * 删除接口
     *
     * @author JiangP
     */
    void delete(FundShorttermTradingParam param);
}