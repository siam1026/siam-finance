package com.siam.system.modular.mod_finance.controller.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicResult;
import com.siam.system.modular.mod_finance.model.param.StockFixedInvestmentParam;
import com.siam.system.modular.mod_finance.model.result.StockFixedInvestmentResult;
import com.siam.system.modular.mod_finance.service.StockFixedInvestmentService;
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
@RequestMapping(value = "/rest/member/stockFixedInvestment")
@Transactional(rollbackFor = Exception.class)
@Api(tags = "基金定投交易记录模块相关接口", description = "StockFixedInvestmentController")
public class StockFixedInvestmentController {

    @Autowired
    private StockFixedInvestmentService stockFixedInvestmentService;

    /**
     * 基金定投交易记录列表
     *
     * @author JiangP
     */
    @PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) StockFixedInvestmentParam param) {
        Page<Map<String, Object>> page = stockFixedInvestmentService.getMapListByPageJoinStock(param);
        return BasicResult.success(page);
    }

    /**
     * 添加基金定投交易记录
     *
     * @author JiangP
     */
    @PostMapping(value = "/insert")
    public BasicResult insertStockFixedInvestment(@RequestBody @Validated(value = {}) StockFixedInvestmentParam param) {
        stockFixedInvestmentService.insert(param);
        return BasicResult.success();
    }

    /**
     * 修改基金定投交易记录
     *
     * @author JiangP
     */
    @PostMapping(value = "/update")
    public BasicResult updateStockFixedInvestment(@RequestBody @Validated(value = {}) StockFixedInvestmentParam param) {
        stockFixedInvestmentService.update(param);
        return BasicResult.success();
    }

    /**
     * 删除基金定投交易记录
     *
     * @author JiangP
     */
    @PostMapping(value = "/delete")
    public BasicResult deleteStockFixedInvestment(@RequestBody @Validated(value = {}) StockFixedInvestmentParam param) {
        stockFixedInvestmentService.delete(param);
        return BasicResult.success();
    }

    /**
     * 绑定卖出记录与买入记录的关系
     *
     * @author JiangP
     */
    @PostMapping(value = "/bindRelatedTrading")
    public BasicResult bindRelatedTrading(@RequestBody @Validated(value = {}) StockFixedInvestmentParam param) {
        stockFixedInvestmentService.bindRelatedTrading(param);
        return BasicResult.success();
    }

    /**
     * 基金定投交易记录详情
     *
     * @author JiangP
     */
    @PostMapping(value = "/detail")
    public BasicResult detail(@RequestBody @Validated(value = {}) StockFixedInvestmentParam param) {
        StockFixedInvestmentResult result = stockFixedInvestmentService.detail(param);
        return BasicResult.success(result);
    }
}