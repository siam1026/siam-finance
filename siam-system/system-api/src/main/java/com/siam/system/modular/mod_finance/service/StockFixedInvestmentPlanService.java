package com.siam.system.modular.mod_finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.siam.system.modular.mod_finance.entity.StockFixedInvestmentPlan;
import com.siam.system.modular.mod_finance.model.param.StockFixedInvestmentPlanParam;

import java.util.Map;

/**
 * 股票定投计划表业务层
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
public interface StockFixedInvestmentPlanService extends IService<StockFixedInvestmentPlan> {

    Page<StockFixedInvestmentPlan> getListByPage(int pageNo, int pageSize, StockFixedInvestmentPlan stockFixedInvestmentPlan);

    Page<Map<String, Object>> getMapListByPage(int pageNo, int pageSize, StockFixedInvestmentPlan stockFixedInvestmentPlan);

    /**
     * 新增接口
     *
     * @author JiangP
     */
    void insert(StockFixedInvestmentPlanParam param);

    /**
     * 修改接口
     *
     * @author JiangP
     */
    void update(StockFixedInvestmentPlanParam param);

    /**
     * 删除接口
     *
     * @author JiangP
     */
    void delete(StockFixedInvestmentPlanParam param);

    /**
     * 更新定投计划状态
     *
     * @author JiangP
     */
    void updateStatus(int id);
}