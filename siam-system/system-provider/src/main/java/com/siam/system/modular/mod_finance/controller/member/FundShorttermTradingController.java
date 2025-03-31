package com.siam.system.modular.mod_finance.controller.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.constant.BaseCode;
import com.siam.package_common.constant.Quantity;
import com.siam.system.modular.mod_finance.model.param.FundShorttermTradingParam;
import com.siam.system.modular.mod_finance.service.FundShorttermTradingService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/rest/member/fundShorttermTrading")
@Transactional(rollbackFor = Exception.class)
@Api(tags = "基金短期交易/做T交易记录模块相关接口", description = "FundShorttermTradingController")
public class FundShorttermTradingController {

    @Autowired
    private FundShorttermTradingService fundShorttermTradingService;

    /**
     * 基金短期交易/做T交易记录列表
     *
     * @author JiangP
     */
    @PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) FundShorttermTradingParam param) {
        Page<Map<String, Object>> page = fundShorttermTradingService.getListByPageJoinFund(param.getPageNo(), param.getPageSize(), param);
        return BasicResult.success(page);
    }

    /**
     * 添加基金短期交易/做T交易记录
     *
     * @author JiangP
     */
    @PostMapping(value = "/insert")
    public BasicResult insertFundShorttermTrading(@RequestBody @Validated(value = {}) FundShorttermTradingParam param) {
        fundShorttermTradingService.insert(param);
        return BasicResult.success();
    }

    /**
     * 修改基金短期交易/做T交易记录
     *
     * @author JiangP
     */
    @PostMapping(value = "/update")
    public BasicResult updateFundShorttermTrading(@RequestBody @Validated(value = {}) FundShorttermTradingParam param) {
        fundShorttermTradingService.update(param);
        return BasicResult.success();
    }

    /*@ApiOperation(value = "删除基金短期交易/做T交易记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "基金短期交易/做T交易记录表主键id", required = true, paramType = "query", dataType = "int"),
    })
    @PostMapping(value = "/delete")
    public BasicResult deleteFundShorttermTrading(@RequestBody @Validated(value = {}) FundShorttermTradingParam param){
        fundShorttermTradingService.delete(param);
        return BasicResult.success();
    }*/
}