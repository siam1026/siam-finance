package com.siam.system.modular.mod_finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.siam.system.modular.mod_finance.entity.FundFixedInvestment;
import com.siam.system.modular.mod_finance.model.param.FundFixedInvestmentParam;
import com.siam.system.modular.mod_finance.model.result.FundFixedInvestmentResult;

import java.util.Map;

public interface FundFixedInvestmentService extends IService<FundFixedInvestment> {
//    int countByExample(FundFixedInvestmentExample example);

    void delete(FundFixedInvestmentParam param);

    void insert(FundFixedInvestmentParam param);

//    List<FundFixedInvestment> selectByExample(FundFixedInvestmentExample example);
//
//    FundFixedInvestment selectByPrimaryKey(Integer id);

//    void updateByExampleSelective(FundFixedInvestment record, FundFixedInvestmentExample example);

    void update(FundFixedInvestmentParam param);

    Page<FundFixedInvestment> getListByPage(int pageNo, int pageSize, FundFixedInvestment fundFixedInvestment);

    Page<Map<String, Object>> getMapListByPageJoinFund(FundFixedInvestmentParam param);

    /**
     * 绑定卖出记录与买入记录的关系
     *
     * @author JiangP
     */
    void bindRelatedTrading(FundFixedInvestmentParam param);

    /**
     * 基金定投交易记录详情
     *
     * @author JiangP
     */
    FundFixedInvestmentResult detail(FundFixedInvestmentParam param);
}