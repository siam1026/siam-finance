package com.siam.system.modular.mod_finance.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicResult;
import com.siam.system.modular.mod_finance.model.param.StockParam;
import com.siam.system.modular.mod_finance.service.StockService;
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
@RequestMapping(value = "/rest/admin/stock")
@Transactional(rollbackFor = Exception.class)
@Api(tags = "基金信息模块相关接口", description = "StockController")
public class AdminStockController {

    @Autowired
    private StockService stockService;

    /**
     * 基金信息列表
     *
     * @author JiangP
     */
    @PostMapping(value = "/list")
    public BasicResult listStock(@RequestBody @Validated(value = {}) StockParam param) {
        Page page = stockService.getListByPage(param.getPageNo(), param.getPageSize(), param);
        return BasicResult.success(page);
    }

    /**
     * 添加基金信息
     *
     * @author JiangP
     */
    @PostMapping(value = "/insert")
    public BasicResult insertStock(@RequestBody @Validated(value = {}) StockParam param) {
        stockService.insert(param);
        return BasicResult.success();
    }

    /**
     * 修改基金信息
     *
     * @author JiangP
     */
    @PostMapping(value = "/update")
    public BasicResult updateStock(@RequestBody @Validated(value = {}) StockParam param) {
        stockService.update(param);
        return BasicResult.success();
    }

    /**
     * 删除基金信息
     *
     * @author JiangP
     */
    @PostMapping(value = "/delete")
    public BasicResult deleteStock(@RequestBody @Validated(value = {}) StockParam param) {
        stockService.delete(param);
        return BasicResult.success();
    }

    /**
     * 更新基金净值/股票价格
     *
     * @author JiangP
     */
    @PostMapping(value = "/syncPrice")
    public BasicResult syncPrice(@RequestBody @Validated(value = {}) StockParam param) {
        stockService.syncPrice(param.getIsOptional());
        return BasicResult.success();
    }

    /**
     * 基金-统计分析接口
     *
     * @author JiangP
     */
    @PostMapping(value = "/statistics")
    public BasicResult statisticsCapitalRecord(@RequestBody @Validated(value = {}) StockParam param) {
        Map<String, Object> map = stockService.statistics(param);
        return BasicResult.success(map);
    }
}