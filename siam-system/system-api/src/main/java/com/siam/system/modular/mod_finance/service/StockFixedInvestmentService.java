package com.siam.system.modular.mod_finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.siam.system.modular.mod_finance.entity.StockFixedInvestment;
import com.siam.system.modular.mod_finance.model.param.StockFixedInvestmentParam;
import com.siam.system.modular.mod_finance.model.result.StockFixedInvestmentResult;

import java.util.Map;

/**
 * 股票定投交易记录表业务层
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
public interface StockFixedInvestmentService extends IService<StockFixedInvestment> {

    void delete(StockFixedInvestmentParam param);

    void insert(StockFixedInvestmentParam param);

//    List<StockFixedInvestment> selectByExample(StockFixedInvestmentExample example);
//
//    StockFixedInvestment selectByPrimaryKey(Integer id);

//    void updateByExampleSelective(StockFixedInvestment record, StockFixedInvestmentExample example);

    void update(StockFixedInvestmentParam param);

    Page<StockFixedInvestment> getListByPage(int pageNo, int pageSize, StockFixedInvestment stockFixedInvestment);

    Page<Map<String, Object>> getMapListByPageJoinStock(StockFixedInvestmentParam param);

    /**
     * 绑定卖出记录与买入记录的关系
     *
     * @author JiangP
     */
    void bindRelatedTrading(StockFixedInvestmentParam param);

    /**
     * 基金定投交易记录详情
     *
     * @author JiangP
     */
    StockFixedInvestmentResult detail(StockFixedInvestmentParam param);
}