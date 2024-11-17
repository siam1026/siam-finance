package com.siam.system.modular.mod_finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.siam.system.modular.mod_finance.entity.ConvertibleBondFixedInvestment;
import com.siam.system.modular.mod_finance.model.param.ConvertibleBondFixedInvestmentParam;
import com.siam.system.modular.mod_finance.model.result.ConvertibleBondFixedInvestmentResult;

import java.util.Map;

/**
 * 可转债定投交易记录表业务层
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
public interface ConvertibleBondFixedInvestmentService extends IService<ConvertibleBondFixedInvestment> {

    void delete(ConvertibleBondFixedInvestmentParam param);

    void insert(ConvertibleBondFixedInvestmentParam param);

//    List<ConvertibleBondFixedInvestment> selectByExample(ConvertibleBondFixedInvestmentExample example);
//
//    ConvertibleBondFixedInvestment selectByPrimaryKey(Integer id);

//    void updateByExampleSelective(ConvertibleBondFixedInvestment record, ConvertibleBondFixedInvestmentExample example);

    void update(ConvertibleBondFixedInvestmentParam param);

    Page<ConvertibleBondFixedInvestment> getListByPage(int pageNo, int pageSize, ConvertibleBondFixedInvestment fundFixedInvestment);

    Page<Map<String, Object>> getMapListByPageJoinConvertibleBond(ConvertibleBondFixedInvestmentParam param);

    /**
     * 绑定卖出记录与买入记录的关系
     *
     * @author JiangP
     */
    void bindRelatedTrading(ConvertibleBondFixedInvestmentParam param);

    /**
     * 基金定投交易记录详情
     *
     * @author JiangP
     */
    ConvertibleBondFixedInvestmentResult detail(ConvertibleBondFixedInvestmentParam param);
}