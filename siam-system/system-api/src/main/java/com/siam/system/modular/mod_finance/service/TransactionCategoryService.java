package com.siam.system.modular.mod_finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.siam.system.modular.mod_finance.entity.TransactionCategory;
import com.siam.system.modular.mod_finance.model.param.TransactionCategoryParam;

public interface TransactionCategoryService extends IService<TransactionCategory> {

    void insert(TransactionCategoryParam param);

    void deleteBatch(TransactionCategoryParam param);

    void update(TransactionCategoryParam param);

    Page list(TransactionCategoryParam param);
}