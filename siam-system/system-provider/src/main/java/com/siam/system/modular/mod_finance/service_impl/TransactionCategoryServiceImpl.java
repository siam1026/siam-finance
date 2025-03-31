package com.siam.system.modular.mod_finance.service_impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siam.package_common.exception.StoneCustomerException;
import com.siam.system.modular.mod_finance.api.StockApiService;
import com.siam.system.modular.mod_finance.entity.TransactionCategory;
import com.siam.system.modular.mod_finance.mapper.TransactionCategoryMapper;
import com.siam.system.modular.mod_finance.model.param.TransactionCategoryParam;
import com.siam.system.modular.mod_finance.model.result.TransactionCategoryResult;
import com.siam.system.modular.mod_finance.service.TransactionCategoryService;
import com.siam.system.modular.mod_user.auth.cache.MemberSessionManager;
import com.siam.system.modular.mod_user.entity.Member;
import com.siam.system.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionCategoryServiceImpl extends ServiceImpl<TransactionCategoryMapper, TransactionCategory> implements TransactionCategoryService {

    @Autowired
    private TransactionCategoryMapper transactionCategoryMapper;

    @Autowired
    private StockApiService stockApiService;

    @Autowired
    private MemberSessionManager memberSessionManager;

    public void insert(TransactionCategoryParam param){
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        param.setMemberId(loginMember.getId());
        transactionCategoryMapper.insert(param);
    }

    public void deleteBatch(TransactionCategoryParam param){
        //检验
        param.getIdList().forEach(id -> {
            TransactionCategory dbTransactionCategory = this.getById(id);
            if(dbTransactionCategory == null){
                throw new StoneCustomerException(dbTransactionCategory.getName() + "，该分类不存在，删除失败");
            }
        });
        //批量删除
        transactionCategoryMapper.deleteBatchIds(param.getIdList());
    }

    public void update(TransactionCategoryParam param){
        TransactionCategory dbTransactionCategory = this.getById(param.getId());
        if(dbTransactionCategory == null){
            throw new StoneCustomerException("该分类不存在，修改失败");
        }
        //修改文章
        this.updateById(param);
    }

    @Override
    public Page list(TransactionCategoryParam param) {
        //查询分类列表
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        param.setMemberId(loginMember.getId());
        Page<TransactionCategoryResult> page = transactionCategoryMapper.list(new Page(param.getPageNo(), param.getPageSize()), param);
        return page;
    }
}