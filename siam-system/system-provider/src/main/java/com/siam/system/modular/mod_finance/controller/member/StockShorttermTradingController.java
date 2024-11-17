package com.siam.system.modular.mod_finance.controller.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicResult;
import com.siam.system.modular.mod_finance.model.param.StockShorttermTradingParam;
import com.siam.system.modular.mod_finance.service.StockShorttermTradingService;
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
@RequestMapping(value = "/rest/member/stockShorttermTrading")
@Transactional(rollbackFor = Exception.class)
@Api(tags = "基金短期交易/做T交易记录模块相关接口", description = "StockShorttermTradingController")
public class StockShorttermTradingController {

    @Autowired
    private StockShorttermTradingService stockShorttermTradingService;

    /**
     * 基金短期交易/做T交易记录列表
     *
     * @author JiangP
     */
    @PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) StockShorttermTradingParam param) {
        Page<Map<String, Object>> page = stockShorttermTradingService.getListByPageJoinStock(param.getPageNo(), param.getPageSize(), param);
        return BasicResult.success(page);
    }

    /**
     * 添加基金短期交易/做T交易记录
     *
     * @author JiangP
     */
    @PostMapping(value = "/insert")
    public BasicResult insertStockShorttermTrading(@RequestBody @Validated(value = {}) StockShorttermTradingParam param) {
        stockShorttermTradingService.insert(param);
        return BasicResult.success();
    }

    /**
     * 修改基金短期交易/做T交易记录
     *
     * @author JiangP
     */
    @PostMapping(value = "/update")
    public BasicResult updateStockShorttermTrading(@RequestBody @Validated(value = {}) StockShorttermTradingParam param) {
        stockShorttermTradingService.update(param);
        return BasicResult.success();
    }

    /*@ApiOperation(value = "删除基金短期交易/做T交易记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "基金短期交易/做T交易记录表主键id", required = true, paramType = "query", dataType = "int"),
    })
    @PostMapping(value = "/delete")
    public BasicResult deleteStockShorttermTrading(@RequestBody @Validated(value = {}) StockShorttermTradingParam param){
        stockShorttermTradingService.delete(param);
        return BasicResult.success();
    }*/
}