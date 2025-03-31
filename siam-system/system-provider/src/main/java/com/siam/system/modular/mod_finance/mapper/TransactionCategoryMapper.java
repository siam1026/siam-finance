package com.siam.system.modular.mod_finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.system.modular.mod_finance.entity.TransactionCategory;
import com.siam.system.modular.mod_finance.model.param.TransactionCategoryParam;
import com.siam.system.modular.mod_finance.model.result.TransactionCategoryResult;
import org.apache.ibatis.annotations.Param;

public interface TransactionCategoryMapper extends BaseMapper<TransactionCategory> {

    Page<TransactionCategoryResult> list(@Param("page") Page page, @Param("paramCondition") TransactionCategoryParam paramCondition);

}