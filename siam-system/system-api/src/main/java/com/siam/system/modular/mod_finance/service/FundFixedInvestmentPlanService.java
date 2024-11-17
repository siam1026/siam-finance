package com.siam.system.modular.mod_finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.siam.system.modular.mod_finance.entity.FundFixedInvestmentPlan;
import com.siam.system.modular.mod_finance.model.example.FundFixedInvestmentPlanExample;
import com.siam.system.modular.mod_finance.model.param.FundFixedInvestmentPlanParam;

import java.util.List;
import java.util.Map;

public interface FundFixedInvestmentPlanService extends IService<FundFixedInvestmentPlan> {
    int countByExample(FundFixedInvestmentPlanExample example);

    void deleteByPrimaryKey(Integer id);

    void insertSelective(FundFixedInvestmentPlan record);

    List<FundFixedInvestmentPlan> selectByExample(FundFixedInvestmentPlanExample example);

    FundFixedInvestmentPlan selectByPrimaryKey(Integer id);

    void updateByExampleSelective(FundFixedInvestmentPlan record, FundFixedInvestmentPlanExample example);

    void updateByPrimaryKeySelective(FundFixedInvestmentPlan record);

    Page<FundFixedInvestmentPlan> getListByPage(int pageNo, int pageSize, FundFixedInvestmentPlan fundFixedInvestmentPlan);

    Page<Map<String, Object>> getMapListByPage(int pageNo, int pageSize, FundFixedInvestmentPlan fundFixedInvestmentPlan);

    /**
     * 新增接口
     *
     * @author JiangP
     */
    void insert(FundFixedInvestmentPlanParam param);

    /**
     * 修改接口
     *
     * @author JiangP
     */
    void update(FundFixedInvestmentPlanParam param);

    /**
     * 删除接口
     *
     * @author JiangP
     */
    void delete(FundFixedInvestmentPlanParam param);

    /**
     * 更新定投计划状态
     *
     * @author JiangP
     */
    void updateStatus(int id);
}