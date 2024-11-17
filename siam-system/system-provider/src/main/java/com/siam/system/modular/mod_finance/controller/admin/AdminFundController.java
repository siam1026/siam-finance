package com.siam.system.modular.mod_finance.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.constant.BaseCode;
import com.siam.package_common.util.DateUtilsPlus;
import com.siam.system.modular.mod_finance.model.param.FundParam;
import com.siam.system.modular.mod_finance.service.FundService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/rest/admin/fund")
@Transactional(rollbackFor = Exception.class)
@Api(tags = "基金信息模块相关接口", description = "FundController")
public class AdminFundController {

    @Autowired
    private FundService fundService;

    /**
     * 基金信息列表
     *
     * @author JiangP
     */
    @PostMapping(value = "/list")
    public BasicResult listFund(@RequestBody @Validated(value = {}) FundParam param) {
//        log.debug("\n\n》》》 listFund - param.isOptional = " + param.getIsOptional());
        Page page = fundService.getListByPage(param.getPageNo(), param.getPageSize(), param);
        return BasicResult.success(page);
    }

    /**
     * 添加基金信息
     *
     * @author JiangP
     */
    @PostMapping(value = "/insert")
    public BasicResult insertFund(@RequestBody @Validated(value = {}) FundParam param) {
        fundService.insert(param);
        return BasicResult.success();
    }

    /**
     * 修改基金信息
     *
     * @author JiangP
     */
    @PostMapping(value = "/update")
    public BasicResult updateFund(@RequestBody @Validated(value = {}) FundParam param) {
        fundService.update(param);
        return BasicResult.success();
    }

    /**
     * 删除基金信息
     *
     * @author JiangP
     */
    @PostMapping(value = "/delete")
    public BasicResult deleteFund(@RequestBody @Validated(value = {}) FundParam param) {
        fundService.delete(param);
        return BasicResult.success();
    }

    /**
     * 更新基金净值/股票价格
     *
     * @author JiangP
     */
    @PostMapping(value = "/syncPrice")
    public BasicResult syncPrice(@RequestBody @Validated(value = {}) FundParam param) {
        fundService.syncNetValue(param.getIsOptional());
        return BasicResult.success();
    }

    /**
     * 基金-统计分析接口
     *
     * @author JiangP
     */
    @PostMapping(value = "/statistics")
    public BasicResult statisticsCapitalRecord(@RequestBody @Validated(value = {}) FundParam param) {
        Map<String, Object> map = fundService.statistics(param);
        return BasicResult.success(map);
    }
}