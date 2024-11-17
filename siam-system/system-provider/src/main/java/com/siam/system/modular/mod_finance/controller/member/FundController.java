package com.siam.system.modular.mod_finance.controller.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicResult;
import com.siam.system.modular.mod_finance.model.param.FundParam;
import com.siam.system.modular.mod_finance.service.FundService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/rest/member/fund")
@Transactional(rollbackFor = Exception.class)
@Api(tags = "基金信息模块相关接口", description = "FundController")
public class FundController {

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