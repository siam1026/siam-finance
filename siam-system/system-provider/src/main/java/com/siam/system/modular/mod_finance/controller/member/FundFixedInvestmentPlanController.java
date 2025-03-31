package com.siam.system.modular.mod_finance.controller.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.constant.BaseCode;
import com.siam.package_common.util.OSSUtils;
import com.siam.system.modular.mod_finance.model.param.FundFixedInvestmentPlanParam;
import com.siam.system.modular.mod_finance.service.FundFixedInvestmentPlanService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/rest/member/fundFixedInvestmentPlan")
@Transactional(rollbackFor = Exception.class)
@Api(tags = "基金定投计划模块相关接口", description = "FundFixedInvestmentPlanController")
public class FundFixedInvestmentPlanController {

    @Autowired
    private FundFixedInvestmentPlanService fundFixedInvestmentPlanService;

    /**
     * 基金定投计划列表
     *
     * @author JiangP
     */
    @PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) FundFixedInvestmentPlanParam param) {
        Page<Map<String, Object>> page = fundFixedInvestmentPlanService.getMapListByPage(param.getPageNo(), param.getPageSize(), param);
        return BasicResult.success(page);
    }

    /**
     * 添加基金定投计划
     *
     * @author JiangP
     */
    @PostMapping(value = "/insert")
    public BasicResult insertFundFixedInvestmentPlan(@RequestBody @Validated(value = {}) FundFixedInvestmentPlanParam param) {
        fundFixedInvestmentPlanService.insert(param);
        return BasicResult.success();
    }

    /**
     * 修改基金定投计划
     *
     * @author JiangP
     */
    @PostMapping(value = "/update")
    public BasicResult updateFundFixedInvestmentPlan(@RequestBody @Validated(value = {}) FundFixedInvestmentPlanParam param) {
        fundFixedInvestmentPlanService.update(param);
        return BasicResult.success();
    }

    /**
     * 删除基金定投计划
     *
     * @author JiangP
     */
    @PostMapping(value = "/delete")
    public BasicResult deleteFundFixedInvestmentPlan(@RequestBody @Validated(value = {}) FundFixedInvestmentPlanParam param) {
        fundFixedInvestmentPlanService.delete(param);
        return BasicResult.success();
    }
}