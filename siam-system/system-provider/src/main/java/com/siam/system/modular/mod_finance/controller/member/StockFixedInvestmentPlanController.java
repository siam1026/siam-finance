package com.siam.system.modular.mod_finance.controller.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicResult;
import com.siam.system.modular.mod_finance.model.param.StockFixedInvestmentPlanParam;
import com.siam.system.modular.mod_finance.service.StockFixedInvestmentPlanService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/rest/member/stockFixedInvestmentPlan")
@Transactional(rollbackFor = Exception.class)
@Api(tags = "基金定投计划模块相关接口", description = "StockFixedInvestmentPlanController")
public class StockFixedInvestmentPlanController {

    @Autowired
    private StockFixedInvestmentPlanService stockFixedInvestmentPlanService;

    /**
     * 基金定投计划列表
     *
     * @author JiangP
     */
    @PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) StockFixedInvestmentPlanParam param) {
        Page<Map<String, Object>> page = stockFixedInvestmentPlanService.getMapListByPage(param.getPageNo(), param.getPageSize(), param);
        return BasicResult.success(page);
    }

    /**
     * 添加基金定投计划
     *
     * @author JiangP
     */
    @PostMapping(value = "/insert")
    public BasicResult insertStockFixedInvestmentPlan(@RequestBody @Validated(value = {}) StockFixedInvestmentPlanParam param) {
        stockFixedInvestmentPlanService.insert(param);
        return BasicResult.success();
    }

    /**
     * 修改基金定投计划
     *
     * @author JiangP
     */
    @PostMapping(value = "/update")
    public BasicResult updateStockFixedInvestmentPlan(@RequestBody @Validated(value = {}) StockFixedInvestmentPlanParam param) {
        stockFixedInvestmentPlanService.update(param);
        return BasicResult.success();
    }

    /**
     * 删除基金定投计划
     *
     * @author JiangP
     */
    @PostMapping(value = "/delete")
    public BasicResult deleteStockFixedInvestmentPlan(@RequestBody @Validated(value = {}) StockFixedInvestmentPlanParam param) {
        stockFixedInvestmentPlanService.delete(param);
        return BasicResult.success();
    }
}