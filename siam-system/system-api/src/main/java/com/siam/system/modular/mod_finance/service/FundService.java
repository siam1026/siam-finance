package com.siam.system.modular.mod_finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.siam.system.modular.mod_finance.entity.Fund;
import com.siam.system.modular.mod_finance.model.example.FundExample;
import com.siam.system.modular.mod_finance.model.param.FundParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface FundService extends IService<Fund> {

    int countByExample(FundExample example);

    void deleteByPrimaryKey(Integer id);

    List<Fund> selectByExample(FundExample example);

    Fund selectByPrimaryKey(Integer id);

    void updateByExampleSelective(Fund record, FundExample example);

    void updateByPrimaryKeySelective(Fund record);

    Page getListByPage(int pageNo, int pageSize, Fund fund);

    /**
     * 同步基金列表
     *
     */
    void syncFundList();

    /**
     * 更新基金净值
     *
     */
    void syncNetValue(Boolean isOptional);

    /**
     * 新增接口
     *
     * @author JiangP
     */
    void insert(FundParam param);

    /**
     * 修改接口
     *
     * @author JiangP
     */
    void update(FundParam param);

    /**
     * 删除接口
     *
     * @author JiangP
     */
    void delete(FundParam param);

    /**
     * 基金-统计分析接口
     *
     * @author JiangP
     */
    Map<String, Object> statistics(FundParam param);

    /**
     * 批量插入
     *
     */
    Integer insertBatchSomeColumn(Collection<Fund> entityList);
}