package com.siam.system.modular.mod_finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.siam.system.modular.mod_finance.entity.ConvertibleBondFixedInvestmentPlan;
import com.siam.system.modular.mod_finance.model.param.ConvertibleBondFixedInvestmentPlanParam;

import java.util.Map;

/**
 * 可转债定投计划表业务层
 *
 * @author JiangP
 * @date 2022/01/23 21:48
 */
public interface ConvertibleBondFixedInvestmentPlanService extends IService<ConvertibleBondFixedInvestmentPlan> {

    Page<ConvertibleBondFixedInvestmentPlan> getListByPage(int pageNo, int pageSize, ConvertibleBondFixedInvestmentPlan fundFixedInvestmentPlan);

    Page<Map<String, Object>> getMapListByPage(int pageNo, int pageSize, ConvertibleBondFixedInvestmentPlan fundFixedInvestmentPlan);

    /**
     * 新增接口
     *
     * @author JiangP
     */
    void insert(ConvertibleBondFixedInvestmentPlanParam param);

    /**
     * 修改接口
     *
     * @author JiangP
     */
    void update(ConvertibleBondFixedInvestmentPlanParam param);

    /**
     * 删除接口
     *
     * @author JiangP
     */
    void delete(ConvertibleBondFixedInvestmentPlanParam param);

    /**
     * 更新定投计划状态
     *
     * @author JiangP
     */
    void updateStatus(int id);
}