package com.siam.system.modular.mod_finance.controller.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicResult;
import com.siam.system.modular.mod_finance.model.param.ConvertibleBondParam;
import com.siam.system.modular.mod_finance.service.ConvertibleBondService;
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
@RequestMapping(value = "/rest/member/convertibleBond")
@Transactional(rollbackFor = Exception.class)
@Api(tags = "基金信息模块相关接口", description = "ConvertibleBondController")
public class ConvertibleBondController {

    @Autowired
    private ConvertibleBondService fundService;

    /**
     * 基金信息列表
     *
     * @author JiangP
     */
    @PostMapping(value = "/list")
    public BasicResult listConvertibleBond(@RequestBody @Validated(value = {}) ConvertibleBondParam param) {
        Page page = fundService.getListByPage(param.getPageNo(), param.getPageSize(), param);
        return BasicResult.success(page);
    }

    /**
     * 添加基金信息
     *
     * @author JiangP
     */
    @PostMapping(value = "/insert")
    public BasicResult insertConvertibleBond(@RequestBody @Validated(value = {}) ConvertibleBondParam param) {
        fundService.insert(param);
        return BasicResult.success();
    }

    /**
     * 修改基金信息
     *
     * @author JiangP
     */
    @PostMapping(value = "/update")
    public BasicResult updateConvertibleBond(@RequestBody @Validated(value = {}) ConvertibleBondParam param) {
        fundService.update(param);
        return BasicResult.success();
    }

    /**
     * 删除基金信息
     *
     * @author JiangP
     */
    @PostMapping(value = "/delete")
    public BasicResult deleteConvertibleBond(@RequestBody @Validated(value = {}) ConvertibleBondParam param) {
        fundService.delete(param);
        return BasicResult.success();
    }

    /**
     * 基金-统计分析接口
     *
     * @author JiangP
     */
    @PostMapping(value = "/statistics")
    public BasicResult statisticsCapitalRecord(@RequestBody @Validated(value = {}) ConvertibleBondParam param) {
        Map<String, Object> map = fundService.statistics(param);
        return BasicResult.success(map);
    }
}