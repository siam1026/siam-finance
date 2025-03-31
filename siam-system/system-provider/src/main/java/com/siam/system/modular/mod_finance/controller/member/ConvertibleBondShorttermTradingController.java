package com.siam.system.modular.mod_finance.controller.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicResult;
import com.siam.system.modular.mod_finance.model.param.ConvertibleBondShorttermTradingParam;
import com.siam.system.modular.mod_finance.service.ConvertibleBondShorttermTradingService;
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
@RequestMapping(value = "/rest/member/convertibleBondShorttermTrading")
@Transactional(rollbackFor = Exception.class)
@Api(tags = "基金短期交易/做T交易记录模块相关接口", description = "ConvertibleBondShorttermTradingController")
public class ConvertibleBondShorttermTradingController {

    @Autowired
    private ConvertibleBondShorttermTradingService fundShorttermTradingService;

    /**
     * 基金短期交易/做T交易记录列表
     *
     * @author JiangP
     */
    @PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) ConvertibleBondShorttermTradingParam param) {
        Page<Map<String, Object>> page = fundShorttermTradingService.getListByPageJoinConvertibleBond(param.getPageNo(), param.getPageSize(), param);
        return BasicResult.success(page);
    }

    /**
     * 添加基金短期交易/做T交易记录
     *
     * @author JiangP
     */
    @PostMapping(value = "/insert")
    public BasicResult insertConvertibleBondShorttermTrading(@RequestBody @Validated(value = {}) ConvertibleBondShorttermTradingParam param) {
        fundShorttermTradingService.insert(param);
        return BasicResult.success();
    }

    /**
     * 修改基金短期交易/做T交易记录
     *
     * @author JiangP
     */
    @PostMapping(value = "/update")
    public BasicResult updateConvertibleBondShorttermTrading(@RequestBody @Validated(value = {}) ConvertibleBondShorttermTradingParam param) {
        fundShorttermTradingService.update(param);
        return BasicResult.success();
    }

    /*@ApiOperation(value = "删除基金短期交易/做T交易记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "基金短期交易/做T交易记录表主键id", required = true, paramType = "query", dataType = "int"),
    })
    @PostMapping(value = "/delete")
    public BasicResult deleteConvertibleBondShorttermTrading(@RequestBody @Validated(value = {}) ConvertibleBondShorttermTradingParam param){
        fundShorttermTradingService.delete(param);
        return BasicResult.success();
    }*/
}