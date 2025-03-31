package com.siam.system.modular.mod_finance.controller.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicResult;
import com.siam.system.modular.mod_finance.model.param.TransactionCategoryParam;
import com.siam.system.modular.mod_finance.service.TransactionCategoryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/member/transactionCategory")
@Transactional(rollbackFor = Exception.class)
@Api(tags = "交易分类模块相关接口", description = "TransactionCategoryController")
public class TransactionCategoryController {

    @Autowired
    private TransactionCategoryService transactionCategoryService;

    /**
     * 交易分类列表
     *
     * @author JiangP
     */
    @PostMapping(value = "/list")
    public BasicResult listTransactionCategory(@RequestBody @Validated(value = {}) TransactionCategoryParam param) {
        Page page = transactionCategoryService.list(param);
        return BasicResult.success(page);
    }

    /**
     * 添加交易分类
     *
     * @author JiangP
     */
    @PostMapping(value = "/insert")
    public BasicResult insertTransactionCategory(@RequestBody @Validated(value = {}) TransactionCategoryParam param) {
        transactionCategoryService.insert(param);
        return BasicResult.success();
    }

    /**
     * 修改交易分类
     *
     * @author JiangP
     */
    @PostMapping(value = "/update")
    public BasicResult updateTransactionCategory(@RequestBody @Validated(value = {}) TransactionCategoryParam param) {
        transactionCategoryService.update(param);
        return BasicResult.success();
    }

    /**
     * 批量删除交易分类
     *
     * @author JiangP
     */
    @PostMapping(value = "/deleteBatch")
    public BasicResult deleteBatch(@RequestBody @Validated(value = {}) TransactionCategoryParam param) {
        transactionCategoryService.deleteBatch(param);
        return BasicResult.success();
    }
}