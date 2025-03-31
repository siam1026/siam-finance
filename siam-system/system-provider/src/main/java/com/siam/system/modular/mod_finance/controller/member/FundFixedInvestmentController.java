package com.siam.system.modular.mod_finance.controller.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicResult;
import com.siam.system.modular.mod_finance.model.param.FundFixedInvestmentParam;
import com.siam.system.modular.mod_finance.model.result.FundFixedInvestmentResult;
import com.siam.system.modular.mod_finance.service.FundFixedInvestmentService;
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
@RequestMapping(value = "/rest/member/fundFixedInvestment")
@Transactional(rollbackFor = Exception.class)
@Api(tags = "基金定投交易记录模块相关接口", description = "FundFixedInvestmentController")
public class FundFixedInvestmentController {

    @Autowired
    private FundFixedInvestmentService fundFixedInvestmentService;

    /**
     * 基金定投交易记录列表
     *
     * @author JiangP
     */
    @PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) FundFixedInvestmentParam param) {
        Page<Map<String, Object>> page = fundFixedInvestmentService.getMapListByPageJoinFund(param);
        return BasicResult.success(page);
    }

    /**
     * 添加基金定投交易记录
     *
     * @author JiangP
     */
    @PostMapping(value = "/insert")
    public BasicResult insertFundFixedInvestment(@RequestBody @Validated(value = {}) FundFixedInvestmentParam param) {
        fundFixedInvestmentService.insert(param);
        return BasicResult.success();
    }

    /**
     * 修改基金定投交易记录
     *
     * @author JiangP
     */
    @PostMapping(value = "/update")
    public BasicResult updateFundFixedInvestment(@RequestBody @Validated(value = {}) FundFixedInvestmentParam param) {
        fundFixedInvestmentService.update(param);
        return BasicResult.success();
    }

    /**
     * 删除基金定投交易记录
     *
     * @author JiangP
     */
    @PostMapping(value = "/delete")
    public BasicResult deleteFundFixedInvestment(@RequestBody @Validated(value = {}) FundFixedInvestmentParam param) {
        fundFixedInvestmentService.delete(param);
        return BasicResult.success();
    }

    /**
     * 绑定卖出记录与买入记录的关系
     *
     * @author JiangP
     */
    @PostMapping(value = "/bindRelatedTrading")
    public BasicResult bindRelatedTrading(@RequestBody @Validated(value = {}) FundFixedInvestmentParam param) {
        fundFixedInvestmentService.bindRelatedTrading(param);
        return BasicResult.success();
    }

    /**
     * 基金定投交易记录详情
     *
     * @author JiangP
     */
    @PostMapping(value = "/detail")
    public BasicResult detail(@RequestBody @Validated(value = {}) FundFixedInvestmentParam param) {
        FundFixedInvestmentResult result = fundFixedInvestmentService.detail(param);
        return BasicResult.success(result);
    }
}